FROM openjdk:17

MAINTAINER tactbug:tactbug@gmail.com

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} tact-id.jar

ENTRYPOINT ["java","-jar","/tact-id.jar"]
