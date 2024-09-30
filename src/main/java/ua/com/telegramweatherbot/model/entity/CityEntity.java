package ua.com.telegramweatherbot.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "cities")
public class CityEntity {

    @Id
    long id;

    String name;

}
