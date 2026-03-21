FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/classes /app/classes
COPY target/libs /app/libs
COPY src/main/resources /app/classes

ENTRYPOINT ["java", "-cp", "/app/classes:/app/libs/*", "by.slava_borisov.producer.ProducerApp"]