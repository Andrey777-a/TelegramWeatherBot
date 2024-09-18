package ua.com.telegramweatherbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.com.telegramweatherbot.exception.UserAlreadyExistsException;
import ua.com.telegramweatherbot.exception.UserNotFoundException;
import ua.com.telegramweatherbot.mapper.UserMapper;
import ua.com.telegramweatherbot.model.dto.UserDto;
import ua.com.telegramweatherbot.model.entity.UserEntity;
import ua.com.telegramweatherbot.repository.UserRepository;
import ua.com.telegramweatherbot.service.UserService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Caching(
            put = @CachePut(value = "UserService::findByChatId", key = "#result.chatId"),
            evict = @CacheEvict(value = "UserService::getUserLanguage", key = "#result.chatId")
    )
    @Transactional
    @Override
    public UserDto createUser(Update update) {

        long chatId = update.getMessage().getChatId();

        User userFromTg = update.getMessage().getFrom();

        String units = "metric";

        if (userRepository.findByChatId(chatId).isPresent()) {
            throw new UserAlreadyExistsException("User with chatId " + chatId + " already exists");
        }

        UserEntity user = UserEntity.builder()
                .chatId(chatId)
                .firstname(userFromTg.getFirstName())
                .lastname(userFromTg.getLastName())
                .username(userFromTg.getUserName())
                .language(userFromTg.getLanguageCode())
                .units(units)
                .registeredAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        log.info("User: {}, chat id: {}, time: {} - added",
                user.getUsername(), user.getChatId(), user.getRegisteredAt());

        return userMapper.toDto(user);
    }

    @Cacheable(value = "UserService::findByChatId", key = "#chatId", unless = "#result==null")
    @Override
    public Optional<UserDto> findByChatId(long chatId) {
        return userRepository.findByChatId(chatId)
                .map(userMapper::toDto);

    }

    @Caching(
            put = @CachePut(value = "UserService::findByChatId", key = "#chatId"),
            evict = @CacheEvict(value = "UserService::getUserLanguage", key = "#chatId")
    )
    @Transactional
    public UserDto changeLanguage(long chatId, String language) {

        return userRepository.findByChatId(chatId)
                .map(e -> {
                    e.setLanguage(language);
                    return userRepository.saveAndFlush(e);
                })
                .map(userMapper::toDto)
                .orElseThrow(
                        () -> new UserNotFoundException("User with chatId " + chatId + " not found")
                );
    }

    @Caching(
            put = @CachePut(value = "UserService::findByChatId", key = "#chatId"),
            evict = @CacheEvict(value = "UserService::getUserUnits", key = "#chatId")
    )
    @Transactional
    @Override
    public UserDto changeUnits(long chatId, String units) {

        return userRepository.findByChatId(chatId)
                .map(e -> {
                    e.setUnits(units);
                    return userRepository.saveAndFlush(e);
                })
                .map(userMapper::toDto)
                .orElseThrow(
                        () -> new UserNotFoundException("User with chatId " + chatId + " not found")
                );
    }

    @CachePut(value = "UserService::findByChatId", key = "#chatId")
    @Transactional
    @Override
    public UserDto changeTimeNotification(long chatId, LocalTime time) {

        return userRepository.findByChatId(chatId)
                .map(e -> {
                    e.setNotificationTime(time);
                    return userRepository.saveAndFlush(e);
                }).map(userMapper::toDto)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found")
                );
    }

    @CachePut(value = "UserService::findByChatId", key = "#chatId")
    @Transactional
    @Override
    public UserDto changeCity(Long chatId, String city) {
        return userRepository.findByChatId(chatId)
                .map(e -> {
                    e.setCity(city);
                    return userRepository.saveAndFlush(e);
                }).map(userMapper::toDto)
                .orElseThrow(
                        () -> new UserNotFoundException("User not found")
                );
    }

    @Cacheable(value = "UserService::getUserLanguage", key = "#chatId")
    @Override
    public String getUserLanguage(long chatId) {
        return userRepository
                .findByChatId(chatId)
                .map(UserEntity::getLanguage)
                .orElse("uk");
    }

    @Cacheable(value = "UserService::getUserUnits", key = "#chatId")
    @Override
    public String getUserUnits(long chatId) {
        return userRepository
                .findByChatId(chatId)
                .map(UserEntity::getUnits)
                .orElse("metric");
    }
}
