FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/board-service-0.0.1-SNAPSHOT.jar board-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "board-service.jar"]
