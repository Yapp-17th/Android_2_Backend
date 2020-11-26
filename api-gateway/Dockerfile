FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar api-gateway.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
