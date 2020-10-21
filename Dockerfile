FROM adoptopenjdk/openjdk11-openj9
ARG JAR_FILE=build/libs/slog-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
