FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} tact-id.jar
ENTRYPOINT ["java","-jar","/tact-id.jar"]