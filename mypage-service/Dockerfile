FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/mypage-service-0.0.1-SNAPSHOT.jar mypage-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "mypage-service.jar"]
