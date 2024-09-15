package ua.com.telegramweatherbot.model.dto;

import lombok.Value;

import java.io.Serializable;

@Value
public class CityDto implements Serializable {

    long id;

    String name;

}
