package ua.com.telegramweatherbot.Model;

import jakarta.persistence.Column;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class WeatherRequest {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    User user;
    String city;

    String language;


    LocalDateTime requestTime;

//    @Lob
    String response;//text
    String status;


}
