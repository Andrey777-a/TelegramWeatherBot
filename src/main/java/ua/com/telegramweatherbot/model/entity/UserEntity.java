package ua.com.telegramweatherbot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "chat_id")
    long chatId;

    String firstname;

    String lastname;

    String username;

    String city;

    String language;

    String units;

    @CreatedDate
    @Column(name = "registered_at")
    LocalDateTime registeredAt;

    @Column(name = "last_weather_request")
    LocalDateTime lastWeatherRequest;

    @Column(name = "notification_time")
    LocalTime notificationTime;

    @PrePersist
    private void onUnits() {

        if (units == null) {
            units = "metric";
        }

        if (language == null) {
            language = "uk";
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        return chatId == that.chatId
                && Objects.equals(firstname, that.firstname)
                && Objects.equals(lastname, that.lastname)
                && Objects.equals(username, that.username)
                && Objects.equals(city, that.city)
                && Objects.equals(language, that.language)
                && Objects.equals(units, that.units)
                && Objects.equals(registeredAt, that.registeredAt)
                && Objects.equals(lastWeatherRequest, that.lastWeatherRequest)
                && Objects.equals(notificationTime, that.notificationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                chatId,
                firstname,
                lastname,
                username,
                city,
                language,
                units,
                registeredAt,
                lastWeatherRequest,
                notificationTime
        );
    }
}
