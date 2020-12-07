FROM openjdk:11-jre-slim
COPY build/libs/base-service-1.0.0.jar base-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENV TZ Asia/Seoul
ENTRYPOINT ["java", "-jar", "base-service.jar"]
