package ua.com.telegramweatherbot.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private String chatId;

    private String firstname;

    private String lastname;

    private String username;

    private String city;

    private String language;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "last_weather_request")
    private Timestamp lastWeatherRequest;




}
