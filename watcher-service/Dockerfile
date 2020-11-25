FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/watcher-service-0.0.1-SNAPSHOT.jar watcher-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "watcher-service.jar"]
