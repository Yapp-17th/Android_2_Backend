FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/base-api-0.0.1-SNAPSHOT.jar base-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "base-api.jar"]
