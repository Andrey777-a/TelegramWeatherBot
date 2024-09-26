package ua.com.telegramweatherbot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.com.telegramweatherbot.exception.UserAlreadyExistsException;
import ua.com.telegramweatherbot.exception.UserNotFoundException;
import ua.com.telegramweatherbot.mapper.UserMapper;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.entity.UserEntity;
import ua.com.telegramweatherbot.repository.UserRepository;
import ua.com.telegramweatherbot.service.impl.UserServiceImpl;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final long CHAT_ID = 123456L;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity userEntity;
    private Update update;
    private UserDto userDto;

    @BeforeEach
    void setup() {

        update = createMockUpdate(
                CHAT_ID,
                "John",
                "Doe",
                "johndoe",
                "en"
        );

        userEntity = UserEntity.builder()
                .chatId(CHAT_ID)
                .firstname("John")
                .lastname("Doe")
                .username("johndoe")
                .language("en")
                .build();

        userDto = UserDto.builder()
                .chatId(CHAT_ID)
                .firstname("John")
                .lastname("Doe")
                .username("johndoe")
                .language("en")
                .build();

    }


    @Test
    void createUser_shouldSaveUser_whenUserDoesNotExist() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        userService.createUser(update);

        verify(userRepository).save(userEntity);
        verify(userMapper).toDto(userEntity);

    }

    @Test
    void createUser_shouldThrowException_whenUserAlreadyExists() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));

        assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.createUser(update),
                "User with chatId " + CHAT_ID + " already exists"
        );

        verify(userRepository, never()).save(userEntity);
    }

    @Test
    void findByChatId_shouldReturnUser_whenUserExists() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));

        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        Optional<UserDto> result = userService.findByChatId(CHAT_ID);

        verify(userRepository).findByChatId(CHAT_ID);

        verify(userMapper).toDto(userEntity);

        assertTrue(result.isPresent());
        assertEquals(userDto, result.get());

    }

    @Test
    void findByChatId_shouldReturnEmpty_whenUserDoesNotExist() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.findByChatId(CHAT_ID);

        verify(userRepository).findByChatId(CHAT_ID);

        assertFalse(result.isPresent());
    }

    @Test
    void updateLastWeatherRequest_shouldReturnUser_whenUserExists() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.saveAndFlush(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        userService.updateLastWeatherRequest(CHAT_ID);

        verify(userRepository).saveAndFlush(userEntity);
        verify(userMapper).toDto(userEntity);


    }

    @Test
    void updateLastWeatherRequest_shouldReturnEmpty_whenUserDoesNotExist() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateLastWeatherRequest(CHAT_ID),
                "User with chatId " + CHAT_ID + " not found"
        );

    }

    @Test
    void findAll_shouldReturnListUser() {

        when(userRepository.findAll()).thenReturn(List.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        List<UserDto> all = userService.findAll();

        verify(userRepository).findAll();

        assertSame(userDto, all.get(0));

        assertThat(all).hasSize(1);

    }

    @Test
    void findAll_shouldReturnEmptyList() {

        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> all = userService.findAll();

        verify(userRepository).findAll();

        assertThat(all).hasSize(0);

        verifyNoInteractions(userMapper);

    }

    @Test
    void changeLanguage_shouldReturnUser_whenUserExists() {
        String newLanguage = "es";

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));

        when(userRepository.saveAndFlush(userEntity)).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            entity.setLanguage(newLanguage);
            return entity;
        });

        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        userService.changeLanguage(CHAT_ID, newLanguage);

        verify(userRepository).saveAndFlush(userEntity);
        verify(userMapper).toDto(userEntity);


    }

    @Test
    void changeLanguage_shouldReturnEmpty_whenUserDoesNotExist() {
        String newLanguage = "es";

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.changeLanguage(CHAT_ID, newLanguage),
                "User with chatId " + CHAT_ID + " not found"
        );

        verifyNoInteractions(userMapper);


    }

    @Test
    void changeUnits_shouldReturnUser_whenUserExists() {

        String newUnit = "imperial";

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.saveAndFlush(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        userService.changeUnits(CHAT_ID, newUnit);

        verify(userRepository).saveAndFlush(userEntity);
        verify(userMapper).toDto(userEntity);

    }

    @Test
    void changeUnits_shouldReturnEmpty_whenUserDoesNotExist() {

        String newUnit = "imperial";

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.changeUnits(CHAT_ID, newUnit),
                "User with chatId " + CHAT_ID + " not found"
        );

        verifyNoInteractions(userMapper);

    }

    @Test
    void changeTimeNotification_shouldReturnUser_whenUserExists() {

        LocalTime time = LocalTime.of(10, 0);

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.saveAndFlush(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        userService.changeTimeNotification(CHAT_ID, time);

        verify(userRepository).saveAndFlush(userEntity);
        verify(userMapper).toDto(userEntity);

    }

    @Test
    void changeTimeNotification_shouldReturnEmpty_whenUserDoesNotExist() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.changeTimeNotification(CHAT_ID, LocalTime.now()),
                "User with chatId " + CHAT_ID + " not found"
        );


    }

    @Test
    void changeCity_shouldReturnUser_whenUserExists() {

        String newCity = "London";

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));
        when(userRepository.saveAndFlush(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        userService.changeCity(CHAT_ID, newCity);

        verify(userRepository).saveAndFlush(userEntity);
        verify(userMapper).toDto(userEntity);

    }

    @Test
    void changeCity_shouldReturnEmpty_whenUserDoesNotExist() {

        String newCity = "London";

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.changeCity(CHAT_ID, newCity),
                "User with chatId " + CHAT_ID + " not found"
        );

    }

    @Test
    void getUserLanguage() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));

        String userLanguage = userService.getUserLanguage(CHAT_ID);

        assertThat(userLanguage).isEqualTo("en");

    }

    @Test
    void getUserUnits() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));

        String userUnits = userService.getUserUnits(CHAT_ID);

        assertThat(userUnits).isEqualTo("metric");

    }

    @Test
    void getUserUnitsText() {

        when(userRepository.findByChatId(CHAT_ID)).thenReturn(Optional.of(userEntity));

        String userUnitsText = userService.getUserUnitsText(CHAT_ID);

        assertThat(userUnitsText).isEqualTo("°C");

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
