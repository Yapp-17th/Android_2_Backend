FROM openjdk:11-jre-slim
COPY build/libs/watcher-service-1.0.0.jar watcher-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "watcher-service.jar"]
