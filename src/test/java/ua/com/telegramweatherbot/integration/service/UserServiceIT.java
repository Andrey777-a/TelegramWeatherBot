package ua.com.telegramweatherbot.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.com.telegramweatherbot.exception.UserAlreadyExistsException;
import ua.com.telegramweatherbot.exception.UserNotFoundException;
import ua.com.telegramweatherbot.integration.IntegrationTestBase;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.service.UserInfoService;
import ua.com.telegramweatherbot.service.UserManagementService;
import ua.com.telegramweatherbot.service.UserSettingsService;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private final static long CHAT_ID_EXISTS = 678228966L;
    private final static long CHAT_ID_NOT_EXISTS = 1234567L;

    private final UserManagementService userManagementService;
    private final UserInfoService userInfoService;
    private final UserSettingsService userSettingsService;
    private final CacheManager cacheManager;

    @Test
    public void findAll() {

        List<UserDto> all = userManagementService.findAll();

        assertThat(all).hasSize(3);

        assertThat(all.get(0).getChatId()).isIn(CHAT_ID_EXISTS);

    }

    @Test
    public void findByChatId_whenUserExists() {

        Optional<UserDto> byChatId = userManagementService.findByChatId(CHAT_ID_EXISTS);

        byChatId.ifPresent(actual -> {
            assertThat(actual.getChatId()).isEqualTo(CHAT_ID_EXISTS);
            assertThat(actual.getFirstname()).isEqualTo("James");
            assertThat(actual.getLastname()).isEqualTo("Smith");
            assertThat(actual.getUsername()).isEqualTo("fine_boy");
        });

        UserDto cachedUser = (UserDto) cacheManager
                .getCache("UserService::findByChatId")
                .get(CHAT_ID_EXISTS)
                .get();

        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getFirstname()).isEqualTo("James");
        assertThat(cachedUser.getUsername()).isEqualTo("fine_boy");

    }

    @Test
    public void findByChatId_whenUserDoesNotExist() {

        assertThat(userManagementService.findByChatId(CHAT_ID_NOT_EXISTS)).isNotPresent();

    }

    @Test
    void createUser_shouldAddUser_whenUserDoesNotExist() {

        Update update = createMockUpdate(
                123456L,
                "John",
                "Doe",
                "johndoe",
                "en"
        );

        UserDto createdUser = userManagementService.createUser(update);
        assertThat(createdUser).isInstanceOf(UserDto.class);

        Optional<UserDto> byChatId = userManagementService.findByChatId(123456L);

        byChatId.ifPresent(actual -> {
            assertThat(actual.getChatId()).isEqualTo(123456L);
            assertThat(actual.getFirstname()).isEqualTo("John");
            assertThat(actual.getUsername()).isEqualTo("johndoe");
            assertThat(actual.getLastname()).isEqualTo("Doe");
        });

        UserDto cachedUser = (UserDto) cacheManager
                .getCache("UserService::findByChatId")
                .get(123456L)
                .get();

        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getFirstname()).isEqualTo("John");
        assertThat(cachedUser.getUsername()).isEqualTo("johndoe");
    }

    @Test
    void createUser_shouldThrowException_whenUserExists() {

        Update update = createMockUpdate(
                CHAT_ID_EXISTS,
                "John",
                "Doe",
                "johndoe",
                "en"
        );

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userManagementService.createUser(update),
                "User with chatId " + CHAT_ID_EXISTS + " already exists"
        );

    }

    @Test
    void updateLastWeatherRequest_shouldUpdateTime_whenUserExists() {

        Optional<UserDto> byChatId = userManagementService.findByChatId(CHAT_ID_EXISTS);
        byChatId.ifPresent(
                actual -> assertThat(byChatId.get().getLastWeatherRequest()).isNull()
        );

        UserDto userDto = userManagementService.updateLastWeatherRequest(CHAT_ID_EXISTS);
        assertThat(userDto.getLastWeatherRequest()).isNotNull();

    }

    @Test
    void updateLastWeatherRequest_shouldThrowException_whenUserDoesNotExist() {

        assertThrows(
                UserNotFoundException.class,
                () -> userManagementService.updateLastWeatherRequest(CHAT_ID_NOT_EXISTS),
                "User with chatId " + CHAT_ID_NOT_EXISTS + " does not exist"
        );

    }

    @Test
    void changeLanguage_shouldUpdateLanguage_whenUserExists() {

        String newLanguage = "es";

        userSettingsService.changeLanguage(CHAT_ID_EXISTS, newLanguage);

        String userLanguage = userInfoService.getUserLanguage(CHAT_ID_EXISTS);

        assertThat(userLanguage).isEqualTo(newLanguage);

        UserDto cachedUser = (UserDto) cacheManager
                .getCache("UserService::findByChatId")
                .get(CHAT_ID_EXISTS)
                .get();


        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getLanguage()).isEqualTo(newLanguage);

    }

    @Test
    void changeLanguage_shouldThrowException_whenUserDoesNotExist() {

        String newLanguage = "es";

        assertThrows(
                UserNotFoundException.class,
                () -> userSettingsService.changeLanguage(CHAT_ID_NOT_EXISTS, newLanguage),
                "User with chatId " + CHAT_ID_NOT_EXISTS + " does not exist"
        );

    }

    @Test
    void changeCity_shouldUpdateCity_whenUserExists() {

        String newCity = "New York";

        userSettingsService.changeCity(CHAT_ID_EXISTS, newCity);

        Optional<UserDto> byChatId = userManagementService.findByChatId(CHAT_ID_EXISTS);
        byChatId.ifPresent(
                actual -> assertThat(byChatId.get().getCity()).isEqualTo(newCity)
        );

        UserDto cachedUser = (UserDto) cacheManager
                .getCache("UserService::findByChatId")
                .get(CHAT_ID_EXISTS)
                .get();

        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getCity()).isEqualTo(newCity);

    }

    @Test
    void changeCity_shouldThrowException_whenUserDoesNotExist() {

        String newCity = "New York";

        assertThrows(
                UserNotFoundException.class,
                () -> userSettingsService.changeCity(CHAT_ID_NOT_EXISTS, newCity),
                "User with chatId " + CHAT_ID_NOT_EXISTS + " does not exist"
        );

    }

    @Test
    void changeTimeNotification_shouldUpdateTime_whenUserExists() {

        LocalTime newTime = LocalTime.of(10, 0);

        userSettingsService.changeTimeNotification(CHAT_ID_EXISTS, newTime);

        Optional<UserDto> byChatId = userManagementService.findByChatId(CHAT_ID_EXISTS);
        byChatId.ifPresent(
                actual -> assertThat(byChatId.get().getNotificationTime()).isEqualTo(newTime)
        );

        UserDto cachedUser = (UserDto) cacheManager
                .getCache("UserService::findByChatId")
                .get(CHAT_ID_EXISTS)
                .get();

        assertThat(cachedUser).isNotNull();
        assertThat(cachedUser.getNotificationTime()).isEqualTo(newTime);
    }

    @Test
    void changeTimeNotification_shouldThrowException_whenUserDoesNotExist() {

        LocalTime newTime = LocalTime.of(10, 0);

        assertThrows(
                UserNotFoundException.class,
                () -> userSettingsService.changeTimeNotification(CHAT_ID_NOT_EXISTS, newTime),
                "User with chatId " + CHAT_ID_NOT_EXISTS + " does not exist"
        );

    }

    @Test
    void changeUnits_shouldUpdateUnit_whenUserExists() {

        String userUnitsBefore = userInfoService.getUserUnits(CHAT_ID_EXISTS);

        assertThat(userUnitsBefore).isEqualTo("metric");

        userSettingsService.changeUnits(CHAT_ID_EXISTS, "imperial");

        String userUnitsAfter = userInfoService.getUserUnits(CHAT_ID_EXISTS);

        assertThat(userUnitsAfter).isEqualTo("imperial");

    }

    @Test
    void changeUnits_shouldThrowException_whenUserDoesNotExist() {

        assertThrows(
                UserNotFoundException.class,
                () -> userSettingsService.changeUnits(CHAT_ID_NOT_EXISTS, "imperial"),
                "User with chatId " + CHAT_ID_EXISTS + " does not exist"
        );

    }

    private Update createMockUpdate(Long chatId,
                                    String firstName,
                                    String lastName,
                                    String username,
                                    String languageCode) {
        Update update = new Update();
        Message message = new Message();

        Chat chat = new Chat();
        chat.setId(chatId);
        message.setChat(chat);

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(username);
        user.setLanguageCode(languageCode);

        update.setMessage(message);
        message.setFrom(user);

        return update;
    }

}