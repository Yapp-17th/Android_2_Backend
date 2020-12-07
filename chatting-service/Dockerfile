FROM openjdk:11-jre-slim
COPY build/libs/chatting-service-1.0.0.jar chatting-service.jar
ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}
ENV TZ Asia/Seoul
ENTRYPOINT ["java", "-jar", "chatting-service.jar"]
