FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/board-service-0.0.1-SNAPSHOT.jar board-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "board-service.jar"]
