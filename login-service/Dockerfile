FROM openjdk:11-jre-slim
COPY build/libs/login-service-1.0.0.jar login-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENV TZ Asia/Seoul
ENTRYPOINT ["java", "-jar", "login-service.jar"]
