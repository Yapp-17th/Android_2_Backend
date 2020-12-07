FROM openjdk:11-jre-slim
COPY build/libs/api-gateway-1.0.0.jar api-gateway.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENV TZ Asia/Seoul
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
