FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/board-api-0.0.1-SNAPSHOT.jar board-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "board-api.jar"]
