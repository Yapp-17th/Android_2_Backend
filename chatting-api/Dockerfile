FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/chatting-api-0.0.1-SNAPSHOT.jar chatting-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "chatting-api.jar"]
