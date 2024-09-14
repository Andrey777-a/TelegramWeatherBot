package ua.com.telegramweatherbot.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
