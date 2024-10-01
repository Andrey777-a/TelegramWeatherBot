package ua.com.telegramweatherbot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityEntity {

    @Id
    long id;

    String name;

}
