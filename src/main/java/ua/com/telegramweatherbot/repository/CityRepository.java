package ua.com.telegramweatherbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.telegramweatherbot.model.entity.CityEntity;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Integer> {

    Optional<CityEntity> findCitiesByName(String cityName);

    Page<CityEntity> findAllBy(Pageable pageable);

    int countAllBy();
}
