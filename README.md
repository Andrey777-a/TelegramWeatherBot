### Telegram Weather Bot

Telegram Weather Bot is a Java-based bot built using the Spring Boot framework that provides users
with weather information for specified cities. The bot supports multiple languages, weather
notifications, and caching using Redis. User data is stored in a PostgreSQL database. The bot
interacts with external APIs like OpenWeather to retrieve weather data.

### Features:

- **Weather Forecast**: Users can input a city name and receive the current weather forecast.
- **Multilingual Support**: The bot automatically detects the user’s language and displays
  information accordingly. Users can also change the language via the settings menu.
- **Caching**: Redis is used to cache user data, such as language preferences and weather requests,
  for faster processing.
- **Weather Notifications**: Users can set up daily weather notifications at a specified time.
- **Database**: User information, settings, and preferred cities are stored in a PostgreSQL
  database.

### Technologies Used:

- **Java 21**: Primary programming language.
- **Spring Boot**: Framework for building web applications and RESTful services.
- **PostgreSQL**: Relational database to store user information.
- **Redis**: Used for caching data.
- **Telegram Bot API**: For interacting with Telegram via its API.
- **OpenWeather API**: To fetch weather data. https://openweathermap.org/api
- **Docker**: Used for containerizing the application.
- **Lombok**: Reduces boilerplate code via annotations.
- **MapStruct**: For automatic mapping of entities to DTOs.
- **Liquibase**: It is a database migration management system.

### Setup and Project Launch:

#### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/telegram-weather-bot.git
cd telegram-weather-bot
```

#### Step 2: Configure Environment Variables

In the root of the project, create files `.env`, `.env.docker` and specify the following parameters:

```env
BOT_KEY=your_telegram_bot_token
BOT_NAME=your_bot_name

# PostgreSQL settings
POSTGRES_DB=telegram_weather_bot_db
POSTGRES_USER=postgres_user
POSTGRES_PASSWORD=postgres_password
POSTGRES_PORT=5432

# Redis settings
REDIS_HOST=redis
REDIS_PORT=6379

# OpenWeather API Key
WEATHER_API_KEY=your_openweather_api_key
```

#### Step 3: Start Docker Containers

The project includes a pre-configured `docker-compose.yml` that sets up PostgreSQL, Redis, and the
application itself.

To start all services, run:

```bash
docker-compose up -d
```

This command will start the following containers:

- **PostgreSQL**: Database for storing user data.
- **Redis**: Cache for user data and weather requests.
- **Telegram Bot**: The bot itself, running in a Java application.

#### Step 4: Interact with the Bot

Once the application is running, you can interact with the bot on Telegram. Search for the bot by
its name (as set in the `BOT_NAME` environment variable) and start chatting.

### Main Bot Commands:

- `/start` — Registers the user and sends a welcome message.
- `/weather` — Get the weather forecast for a city.
- `/settings` — Manage user settings, including changing the language.
- `/notifications` — Set up daily weather notifications.

### Usage Examples:

1. **User Registration**: Upon first interaction, the bot automatically registers the user in the
   database, saving their language, name, and chat ID.
2. **Weather Forecast**: After typing `/weather`, the bot will prompt the user to choose a city and
   will display the current weather for that location.
3. **Notifications**: The bot sends weather notifications to users at a specified time, which can be
   set in the settings.

### Project Structure:

- **/src/main/java/ua/com/telegramweatherbot** — Contains the core logic of the application.
    - **controller** — Handles incoming requests and interaction with the Telegram API.
    - **service** — Contains the business logic, such as handling weather and user data.
    - **repository** — Interfaces for database interactions.
    - **dto** — Data Transfer Objects (DTOs) for sending data between services and clients.
    - **config** — Configuration files for Spring (database, Redis, Telegram API).

### Additional Configurations:

- **Caching**: User and weather data is cached using Redis. To modify the caching settings, you can
  adjust the configuration in `application.yml`.
- **Database Migrations**: Liquibase is used to automatically handle database schema migrations when
  the application starts.
