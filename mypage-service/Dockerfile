FROM openjdk:11-jre-slim
COPY build/libs/mypage-service-1.0.0.jar mypage-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENTRYPOINT ["java", "-jar", "mypage-service.jar"]
