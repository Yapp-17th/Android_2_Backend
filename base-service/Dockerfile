FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/base-service-0.0.1-SNAPSHOT.jar base-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "base-service.jar"]
