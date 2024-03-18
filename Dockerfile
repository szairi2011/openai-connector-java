FROM openjdk:17-jdk-alpine

EXPOSE 8082
ARG JAR_FILE=target/openai-connector-demo-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]

#FROM openjdk:17-jdk-alpine as builder
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} application.jar
