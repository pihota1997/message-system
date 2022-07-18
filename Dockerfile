FROM openjdk:15-jdk-slim

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

CMD ["java","-jar","-Dspring.profiles.active=dev", "/app.jar"]