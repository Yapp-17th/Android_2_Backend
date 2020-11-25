FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/chatting-service-0.0.1-SNAPSHOT.jar chatting-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "chatting-service.jar"]
