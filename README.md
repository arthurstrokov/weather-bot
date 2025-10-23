# Weather Bot

https://github.com/rubenlagus/TelegramBots
https://rubenlagus.github.io/TelegramBotsDocumentation/telegram-bots.html

A Spring Boot 3 application that exposes REST endpoints and a Telegram long‑polling bot to deliver current weather data and short‑term forecasts using the OpenWeather API. The app also integrates Spring AI with a local Ollama model to enrich forecast descriptions and provides Prometheus metrics via Spring Boot Actuator.

## Stack
- Language: Java 21
- Build tool / package manager: Gradle (wrapper included)
- Frameworks & libraries:
  - Spring Boot 3.4.x (Web, Actuator)
  - Spring Cloud OpenFeign (HTTP client for OpenWeather and Ollama)
  - Spring AI (MCP Server WebMVC, Ollama model client)
  - TelegramBots (long polling)
  - Micrometer + Prometheus registry
  - Lombok
  - Testing: JUnit 5, Spring Boot Test, WireMock
- Container: Dockerfile provided

## Overview
- REST API to fetch current weather and forecasts from OpenWeather.
- Telegram bot to query weather via commands and inline keyboard.
- Optional AI enrichment of forecasts using a local/remote Ollama model (via Spring AI or Feign, depending on implementation).
- Actuator endpoints and Prometheus metrics for observability.

## Requirements
- JDK 21+
- Internet access to OpenWeather API
- Telegram bot credentials (from BotFather)
- Optional: Local Ollama runtime if you want AI‑enriched responses with the "local" chat implementation (default Spring AI base URL: http://localhost:11434)
- Docker (optional, for containerized runs)

## Configuration (Environment Variables)
Values map to src/main/resources/application.yml. Do not commit secrets.

Core application
- PORT — HTTP port. Default: 8080
- IMPL — Chat implementation selector. Default: local. Allowed: local, remote
- Logging levels — configured in application.yml (override via SPRING_APPLICATION_JSON or specific envs if needed)

Telegram bot
- BOT_NAME — Public Telegram bot name. Default: WeatherForTomorrowBot
- BOT_TOKEN — Telegram bot token. Required for bot to work
- BOT_CHAT_ID — Optional chat id for diagnostics

OpenWeather
- OPEN_API_KEY — OpenWeather API key. Required
- OPEN_API_BASE_URL — Base URL for OpenWeather API. Default: https://api.openweathermap.org/data/2.5

Ollama / AI
- spring.ai.ollama.base-url — Spring AI base URL. Default: http://localhost:11434
  - Can be overridden via env var SPRING_AI_OLLAMA_BASE_URL
- OLLAMA_BASE_URL — Feign client base URL for the remote chat implementation. Default: https://ollama.com (property key: ollama.base-url)
- Models (defaults in application.yml)
  - Chat model: gpt-oss:20b
  - Embedding model: nomic-embed-text
- TODO: Externalize all Spring AI options (temperature, top-p, model names) as environment variables if deployment requires it

## Endpoints
REST (JSON)
- GET /api/weather/current?city={City}
- GET /api/weather/forecast?city={City}

Actuator/metrics
- Actuator base: /actuator (e.g., /actuator/health)
- Prometheus metrics exposed when enabled (see application.yml)

## Entry Points
- Application main class: com.gmail.arthurstrokov.weather.Application
- REST controller: com.gmail.arthurstrokov.weather.controller.WeatherController
- Telegram bot service: com.gmail.arthurstrokov.weather.service.BotService
- Chat implementations (select via IMPL env):
  - local → com.gmail.arthurstrokov.weather.service.LocalChatService (Spring AI ChatModel)
  - remote → com.gmail.arthurstrokov.weather.service.RemoteChatService (Feign to OllamaClient)

## Setup
1) Clone repository and configure environment variables (see above). At minimum, set OPEN_API_KEY and BOT_TOKEN to enable both REST and Telegram features.

2) Build
```
./gradlew clean build
```

3) Run (choose one)
- Using Gradle
```
./gradlew bootRun
```
- Using the fat jar
```
./gradlew bootJar
java -jar build/libs/weather-bot.jar
```
- Using Docker
```
docker build -t weather-bot .
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e IMPL=local \
  -e BOT_NAME=WeatherForTomorrowBot \
  -e BOT_TOKEN=your-telegram-token \
  -e OPEN_API_KEY=your-openweather-key \
  weather-bot
```
Notes
- For remote chat (IMPL=remote), you can override OLLAMA_BASE_URL (defaults to https://ollama.com). Example: -e OLLAMA_BASE_URL=http://localhost:11434
- For local chat (IMPL=local), Spring AI uses SPRING_AI_OLLAMA_BASE_URL (default http://localhost:11434)

Once running locally
- Base URL: http://localhost:8080
- Examples:
  - http://localhost:8080/api/weather/current?city=Minsk
  - http://localhost:8080/api/weather/forecast?city=London

## Scripts and Tasks
Common Gradle tasks
- ./gradlew bootRun — run the app
- ./gradlew bootJar — build executable jar (build/libs/weather-bot.jar)
- ./gradlew test — run tests
- ./gradlew jacocoTestReport — generate coverage report (build/reports/jacoco/test/html)

## Tests
- Run the test suite
```
./gradlew test
```
- Technologies: JUnit 5, Spring Boot Test, WireMock (for OpenWeather stubs).

## Project Structure (high level)
- build.gradle — dependencies and build configuration (Java toolchain 21; bootJar name = weather-bot.jar)
- Dockerfile — multi-stage build and runtime (EXPOSE/ENV PORT=8080)
- src/main/java/com/gmail/arthurstrokov/weather/
  - Application.java — Spring Boot entry point
  - configuration/ — configuration properties and beans (BotProperties, OpenWeatherProperties, ToolConfiguration, OllamaFeignConfig)
  - controller/OpenWeatherApiController.java — REST endpoints
  - gateway/ — OpenFeign clients (OpenWeatherApiClient, OllamaClient)
  - model/ — DTOs for Ollama chat
  - service/ — business services (OpenWeatherService, WeatherBotService, PromptService, ChatService impls)
  - startup/ApplicationRunnerImpl.java — application startup wiring
  - template/PromptTemplate.java — AI prompt templates
  - tool/WeatherTool.java — tool integration for AI
- src/main/resources/
  - application.yml — application configuration (server port, bot, open weather, AI, actuator/metrics)
  - bootstrap.yml — Spring bootstrap config (if used)
- src/test/java/... — tests and test utilities

## API Notes
- Default city used by bot commands: Minsk (see open.weather.parameters.city in application.yml)
- Units: metric; Language: en; Forecast count: 4 (see application.yml)

## Observability
- Actuator: /actuator/health, /actuator/metrics, etc.
- Prometheus: metrics exposed and can be scraped when enabled

## License
No license file detected in the repository.
- TODO: Add a LICENSE file (e.g., MIT, Apache‑2.0) and update this section accordingly.

## Deployment
- A general Docker-based deployment is supported via the provided Dockerfile.
- Any cloud-specific steps (e.g., Railway, Fly.io, etc.) are not maintained here.
- TODO: Provide concrete deployment instructions for your target platform once chosen.

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/874083ead14a4cc9b9fa5a359b959398)](https://app.codacy.com/gh/arthurstrokov/weather-bot/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![DeepSource](https://app.deepsource.com/gh/arthurstrokov/weather-bot.svg/?label=code+coverage&show_trend=true&token=7F9hB8-lC31CNOPWLN9EA6CE)](https://app.deepsource.com/gh/arthurstrokov/weather-bot/)
