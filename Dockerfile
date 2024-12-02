FROM openjdk:23-ea-22-jdk-slim
RUN apt-get update && apt-get install -y libfreetype6
RUN apt-get update && apt-get install -y \
    fontconfig \
    fonts-dejavu-core \
    && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY target/TelegramBot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/telegrambot.jar

ENTRYPOINT ["java", "-jar", "telegrambot.jar"]