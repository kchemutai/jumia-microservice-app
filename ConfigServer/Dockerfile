FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} configserver.jar

EXPOSE 9296
ENTRYPOINT ["java","-jar","/configserver.jar"]