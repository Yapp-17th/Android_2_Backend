FROM openjdk:11-jre-slim
COPY build/libs/chat-bot-service-1.0.0.jar chat-bot-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "chat-bot-service.jar"]
