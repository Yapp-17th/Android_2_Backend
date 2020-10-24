FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/chatting-service-0.0.1-SNAPSHOT.jar chatting-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "chatting-service.jar"]
