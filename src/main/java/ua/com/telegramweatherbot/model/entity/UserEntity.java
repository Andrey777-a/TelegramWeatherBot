package ua.com.telegramweatherbot.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private long chatId;

    private String firstname;

    private String lastname;

    private String username;

    private String city;

    private String language;

    private String units;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "last_weather_request")
    private LocalDateTime lastWeatherRequest;

    @Column(name = "notification_time")
    private LocalTime notificationTime;

}
