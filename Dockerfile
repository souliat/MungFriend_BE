FROM openjdk:8-jdk-slim-buster
ARG JAR_FILE=build/libs/*.jar
ONBUILD COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]