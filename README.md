services:
postgres:
image: postgres:16
container_name: postgres
environment:
POSTGRES_DB: postgres
POSTGRES_USER: telegram
POSTGRES_PASSWORD: 1234
ports:
- "5432:5432"
volumes:
- postgres_data:/var/lib/postgresql/data

redis:
image: redis:alpine
container_name: redis
ports:
- "6379:6379"

kafka:
image: bitnami/kafka:3.9.0
container_name: kafka
environment:
    - BOOSTRAP_SERVERS=kafka:9092
    - KAFKA_CFG_PROCESS_ROLES=broker,controller
    - KAFKA_CFG_NODE_ID=1
    - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka:9093
    - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
    - KAFKA_CFG_LISTENERS=PLAINTEXT://kafka:9092,CONTROLLER://kafka:9093
    - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
    - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
    - KAFKA_CFG_LOG_DIRS=/bitnami/kafka/data
    - KAFKA_CFG_METADATA_LOG_DIR=/bitnami/kafka/meta
    - ALLOW_PLAINTEXT_LISTENER=yes
    - KAFKA_CFG_AUTO_CREATE_TOPICS_ENABLE=true
    - KAFKA_CFG_NUM_PARTITIONS=1

 volumes:
      - kafka_data:/bitnami/kafka
 ports:
      - "9092:9092"
      - "9093:9093" # для KRaft

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
BOT_TOKEN: BOT_TOKEN
ports:
- "8080:8080"

volumes:
postgres_data:
kafka_data: