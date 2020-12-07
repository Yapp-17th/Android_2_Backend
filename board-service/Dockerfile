FROM openjdk:11-jre-slim
COPY build/libs/board-service-1.0.0.jar board-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "board-service.jar"]
