package ua.com.telegramweatherbot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import ua.com.telegramweatherbot.Model.dto.UserDto;
import ua.com.telegramweatherbot.Model.entity.UserEntity;
import ua.com.telegramweatherbot.exception.UserNotFoundException;
import ua.com.telegramweatherbot.mapper.UserMapper;
import ua.com.telegramweatherbot.repository.UserRepository;
import ua.com.telegramweatherbot.service.UserService;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    //        @CacheEvict(value = "UserService::findByChatId", key = "#user.chatId")
    @Transactional
    @Override
    public void createUser(Update update) {

        long chatId = update.getMessage().getChatId();
        User userFromTg = update.getMessage().getFrom();

        boolean empty = userRepository.findByChatId(chatId).isEmpty();

        if (empty) {
            UserEntity user = UserEntity.builder()
                    .chatId(chatId)
                    .firstname(userFromTg.getFirstName())
                    .lastname(userFromTg.getLastName())
                    .username(userFromTg.getUserName())
                    .language(userFromTg.getLanguageCode())
                    .registeredAt(LocalDateTime.now())
                    .build();

            userRepository.save(user);

            log.info("User: {}, telegram id: {}, time: {} - added",
                    user.getUsername(), user.getChatId(), user.getRegisteredAt());
        }
    }

    @Cacheable(value = "UserService::findByChatId",
            key = "#chatId",
            unless = "#result==null")
    @Override
    public Optional<UserDto> findByChatId(long chatId) {
        return userRepository.findByChatId(chatId)
                .map(userMapper::toDto);

    }

    @CacheEvict(value = "UserService::findByChatId", key = "#chatId")
    @Transactional
    @Override
    public void changeLanguage(Long chatId, String language) {

        userRepository.findByChatId(chatId).ifPresentOrElse(
                e -> {
                    e.setLanguage(language);
                    userRepository.saveAndFlush(e);
                }, () -> {
                    throw new UserNotFoundException("User not found");
                }
        );
    }
}
