FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} productservice.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/productservice.jar"]