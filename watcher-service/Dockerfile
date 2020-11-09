FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/watcher-service-0.0.1-SNAPSHOT.jar watcher-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "watcher-service.jar"]
