package ua.com.telegramweatherbot.model.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class CityDto implements Serializable {

    long id;

    String name;

}
