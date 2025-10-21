# Weather Bot

A Spring Boot 3 application that exposes REST endpoints and a Telegram long-polling bot to deliver current weather data and short-term forecasts using the OpenWeather API. The app also integrates Spring AI with a local Ollama model to enrich forecast descriptions and provides Prometheus metrics via Spring Boot Actuator.

## Stack
- Language: Java 21
- Build tool: Gradle (wrapper included)
- Frameworks/Libraries:
  - Spring Boot 3.4.x (Web, Actuator)
  - Spring Cloud OpenFeign (HTTP client for OpenWeather)
  - Spring AI (MCP Server WebMVC, Ollama model client)
  - TelegramBots (long polling)
  - Micrometer + Prometheus registry
  - Lombok
  - Testing: JUnit 5, Spring Boot Test, WireMock
- Container: Dockerfile provided

## Requirements
- JDK 21+
- Internet access to OpenWeather API
- Telegram bot credentials (from BotFather)
- Optional: Local Ollama runtime if you want AI-enriched responses (default base URL: http://localhost:11434)
- Docker (optional, for containerized runs)

## Features
- REST endpoints for current conditions and forecast
  - GET /api/weather/current?city={City}
  - GET /api/weather/forecast?city={City}
- Telegram bot commands:
  - /start — shows help and inline menu
  - /current — current weather for the default city (Minsk by default)
  - /forecast — short-term forecast for the default city (Minsk by default)
  - /location — requests to share your location and returns forecast by coordinates
  - Share location — tap the keyboard button “Share location” to send coordinates
- Actuator endpoints (e.g., /actuator/health) and Prometheus metrics

## Configuration (Environment Variables)
These map to values in src/main/resources/application.yml. Do not commit secrets.

- PORT — HTTP port (default 8080)
- BOT_NAME — Public Telegram bot name (defaults to WeatherForTomorrowBot)
- BOT_TOKEN — Telegram bot token (required to enable bot)
- BOT_CHAT_ID — Optional, chat id for diagnostics
- OPEN_API_KEY — OpenWeather API key (required)
- OPEN_API_BASE_URL — Base URL for OpenWeather API (default https://api.openweathermap.org/data/2.5)
- IMPL — Chat implementation selector for the app (default: local). Values: `local`, `remote` (see ChatService implementations). 

AI/Ollama settings (defaults are in application.yml):
- spring.ai.ollama.base-url: http://localhost:11434
- Chat model: gpt-oss:20b
- Embedding model: nomic-embed-text
- TODO: Externalize these Spring AI/Ollama settings via environment variables if deployment requires it.

## Setup
1) Clone and configure environment variables (see above). At minimum, set OPEN_API_KEY and BOT_TOKEN to enable both REST and Telegram features.

2) Build:
```bash
./gradlew clean build
```

3) Run (choose one):
- Using Gradle:
```bash
./gradlew bootRun
```
- Using the fat jar:
```bash
./gradlew bootJar
java -jar build/libs/weather-bot.jar
```
- Using Docker:
```bash
docker build -t weather-bot .
docker run --rm -p 8080:8080 \
  -e BOT_NAME=WeatherForTomorrowBot \
  -e BOT_TOKEN=your-telegram-token \
  -e OPEN_API_KEY=your-openweather-key \
  weather-bot
```

Once running locally:
- REST endpoints are available at http://localhost:8080
- Examples:
  - http://localhost:8080/api/weather/current?city=Minsk
  - http://localhost:8080/api/weather/forecast?city=London

## Scripts and Tasks
Common Gradle tasks:
- ./gradlew bootRun — run the app
- ./gradlew bootJar — build executable jar (build/libs/weather-bot.jar)
- ./gradlew test — run tests
- ./gradlew jacocoTestReport — generate code coverage report (build/reports/jacoco/test/html)

## Tests
- Run the test suite:
```bash
./gradlew test
```
- Technologies: JUnit 5, Spring Boot Test, WireMock (for OpenWeather stubs).

## Project Structure (high-level)
- build.gradle — dependencies and build configuration
- Dockerfile — multi-stage Docker build and runtime
- src/main/java/com/gmail/arthurstrokov/weather/
  - Application.java — Spring Boot entry point
  - configuration/ — configuration properties and beans (BotProperties, ToolConfiguration, etc.)
  - controller/OpenWeatherApiController.java — REST endpoints
  - gateway/OpenWeatherApiClient.java — OpenFeign client to OpenWeather
  - service/ — business services (OpenWeatherService, WeatherBotService, PromptService, ChatService)
  - startup/ApplicationRunnerImpl.java — application startup wiring
  - template/PromptTemplate.java — AI prompt templates
  - tool/WeatherTool.java — tool integration for AI
- src/main/resources/
  - application.yml — application configuration (server port, bot, open weather, AI)
  - bootstrap.yml — Spring bootstrap config (if used)
- src/test/java/... — tests and test utilities

## Observability
- Actuator: /actuator/health, /actuator/metrics, etc.
- Prometheus: metrics exposed and can be scraped when enabled

## API Notes
- Default city for bot commands: Minsk (see open.weather.parameters.city in application.yml)
- Units: metric; Language: en; Forecast count: 4 (see application.yml)

## Entry Points
- Application main class: com.gmail.arthurstrokov.weather.Application
- Telegram bot service: com.gmail.arthurstrokov.weather.service.WeatherBotService

## License
No license file detected in the repository.
- TODO: Add a LICENSE file (e.g., MIT, Apache-2.0) and update this section accordingly.

## Deployment
- A general Docker-based deployment is supported via the provided Dockerfile.
- Any cloud-specific steps (e.g., Railway, Fly.io, etc.) are not maintained here.
- TODO: Provide concrete deployment instructions for your target platform once chosen.
