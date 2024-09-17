package ua.com.telegramweatherbot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.com.telegramweatherbot.model.dto.CityResponse;
import ua.com.telegramweatherbot.model.dto.WeatherResponse;
import ua.com.telegramweatherbot.model.entity.UserEntity;
import ua.com.telegramweatherbot.service.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SchedulingServiceImpl implements SchedulingService {

    private final MessageService messageService;
    private final UserService userService;
    private final WeatherService weatherService;
    private final CityService cityService;

    @Scheduled(cron = "0 0 9-17 * * *")
    @Override
    public void sendWeatherNotification() {

        List<UserEntity> all = userService.findAll();

        for (UserEntity user : all) {

            boolean hasCity = Optional.ofNullable(user.getCity()).isPresent();
            boolean hasNotificationTime = Optional.ofNullable(user.getNotificationTime()).isPresent();

            if (hasCity && hasNotificationTime) {

                boolean time = LocalTime.now().getHour() == user.getNotificationTime().getHour();

                if (time) {

                    List<WeatherResponse> weatherByCity = weatherService
                            .getWeatherByCity(user.getCity(), user.getChatId());

                    List<CityResponse> localisation = cityService.getCity(user.getCity());

                    String localNameCity = localisation.getFirst()
                            .getLocalNameList()
                            .get(user.getLanguage());

                    messageService.sendWeatherInfo(
                            user.getChatId(),
                            localNameCity, weatherByCity.getFirst()
                    );

                }
            }
        }
    }
}

