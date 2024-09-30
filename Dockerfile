FROM openjdk:23-ea-22-jdk-slim
WORKDIR /app
COPY target/TelegramBot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/telegrambot.jar

ENTRYPOINT ["java", "-jar", "telegrambot.jar"]