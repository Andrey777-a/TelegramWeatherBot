package ua.com.telegramweatherbot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "last_weather_request")
    private LocalDateTime lastWeatherRequest;

    @Column(name = "notification_time")
    private LocalTime notificationTime;


    @PrePersist
    private void onUnits() {

        if (units == null) {
            units = "metric";
        }

        if (language == null) {
            language = "uk";
        }

    }

}
