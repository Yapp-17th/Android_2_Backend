FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "api-gateway.jar"]
