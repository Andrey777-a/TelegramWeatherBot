package ua.com.telegramweatherbot.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.config.SchedulerProperties;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.service.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SchedulingServiceImpl implements SchedulingService {

    MessageService messageService;
    UserManagementService userManagementService;
    UserInfoService userInfoService;
    WeatherService weatherService;
    CityService cityService;
    SchedulerProperties schedulerProperties;

    @Scheduled(cron = "#{@schedulerProperties.expression}")
    @Override
    public void sendWeatherNotification() {

        List<UserDto> all = userManagementService.findAll();

        for (UserDto user : all) {

            boolean hasCity =
                    Optional.ofNullable(user.getCity())
                            .isPresent();
            boolean hasNotificationTime =
                    Optional.ofNullable(user.getNotificationTime())
                            .isPresent();

            String userUnits = userInfoService
                    .getUserUnits(
                            user.getChatId()
                    );

            if (hasCity && hasNotificationTime) {

                boolean time = LocalTime.now().getHour()
                        == user.getNotificationTime().getHour();

                if (time) {

                    List<WeatherResponse> weatherByCity = weatherService
                            .getWeatherByCity(
                                    user.getCity(),
                                    user.getChatId()
                            );

                    List<CityResponse> localisation = cityService
                            .getCity(
                                    user.getCity()
                            );

                    String localNameCity = localisation
                            .getFirst()
                            .getLocalNameList()
                            .get(user.getLanguage());

                    messageService.sendWeatherInfo(
                            user.getChatId(),
                            localNameCity,
                            weatherByCity.getFirst(),
                            userUnits
                    );

                }
            }
        }
    }
}

