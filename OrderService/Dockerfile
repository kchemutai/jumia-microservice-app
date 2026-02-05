FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} orderservice.jar

EXPOSE 8082
ENTRYPOINT ["java","-jar","/orderservice.jar"]