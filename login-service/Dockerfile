FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/login-service-0.0.1-SNAPSHOT.jar login-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "login-service.jar"]
