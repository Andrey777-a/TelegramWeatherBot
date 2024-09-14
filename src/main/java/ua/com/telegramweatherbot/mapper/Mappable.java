package ua.com.telegramweatherbot.mapper;

public interface Mappable<E, D>{

    E toEntity(D d);
    D toDto(E e);

}
