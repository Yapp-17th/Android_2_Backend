FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/login-service-0.0.1-SNAPSHOT.jar login-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "login-service.jar"]
