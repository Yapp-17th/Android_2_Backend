FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/chatting-consumer-0.0.1-SNAPSHOT.jar chatting-consumer.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "chatting-consumer.jar"]
