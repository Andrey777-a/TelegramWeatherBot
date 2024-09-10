package ua.com.telegramweatherbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.telegramweatherbot.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
