FROM eclipse-temurin:21-jre-alpine

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} serviceregistry.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","/serviceregistry.jar"]