FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/base-service-0.0.1-SNAPSHOT.jar base-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "base-service.jar"]
