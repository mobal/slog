FROM adoptopenjdk/openjdk11-openj9:alpine-slim
ARG JAR_FILE=build/libs/slog-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.net.preferIPv4Stack=true", "-jar", "/app.jar"]
