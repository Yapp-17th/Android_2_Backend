FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/chat-bot-service-0.0.1-SNAPSHOT.jar chat-bot-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "chat-bot-service.jar"]
