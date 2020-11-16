FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/mypage-service-0.0.1-SNAPSHOT.jar mypage-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "mypage-service.jar"]
