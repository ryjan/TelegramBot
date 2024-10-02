services:
  postgres:
    image: postgres:16
    container_name: postgres
    environment:
      POSTGRES_DB: DB
      POSTGRES_USER: USERNAME
      POSTGRES_PASSWORD: PASSWORD
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  redis:
    image: "redis:alpine"
    container_name: redis
    ports:
      - "6379:6379"

  app:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: telegram_bot
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
      SPRING_DATASOURCE_USERNAME: USERNAME
      SPRING_DATASOURCE_PASSWORD: PASSWORD
      BOT_TOKEN: TELEGRAM_BOT_TOKEN
    ports:
      - "8080:8080"
  pgadmin4:
    image: elestio/pgadmin:latest
    restart: always
    container_name: pgadmin
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@email.com
      PGADMIN_DEFAULT_PASSWORD: 12341234
    ports:
      - "8081:80"
    volumes:
      - ./servers.json:/pgadmin4/servers.json

volumes:
  postgres_data:
