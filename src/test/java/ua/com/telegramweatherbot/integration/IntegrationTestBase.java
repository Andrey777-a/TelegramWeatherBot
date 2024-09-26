package ua.com.telegramweatherbot.integration;

import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ua.com.telegramweatherbot.integration.annotation.IntegrationTest;

@IntegrationTest
public abstract class IntegrationTestBase {

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16.3-alpine");

    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer("redis:7.2-rc3-alpine");

    @BeforeAll
    static void runContainer() {
        POSTGRE_SQL_CONTAINER.start();
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);

        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () ->
                REDIS_CONTAINER.getMappedPort(6379));
    }

}
