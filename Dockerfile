#FROM openjdk:17-jdk-alpine
#
#EXPOSE 8082
#ARG JAR_FILE=target/openai-connector-demo-0.0.1-SNAPSHOT.jar
#ADD ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "/app.jar"]

FROM openjdk:17-jdk-alpine as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
# Extract the layers of the artifact
RUN java -Djarmode=layertools -jar application.jar extract


FROM openjdk:17-jdk-alpine
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]