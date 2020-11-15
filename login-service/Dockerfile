FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/login-api-0.0.1-SNAPSHOT.jar login-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "login-api.jar"]
