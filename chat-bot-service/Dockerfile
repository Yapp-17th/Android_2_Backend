FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/chat-bot-service-0.0.1-SNAPSHOT.jar chat-bot-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "chat-bot-service.jar"]
