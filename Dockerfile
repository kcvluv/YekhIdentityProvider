FROM openjdk:8-jdk-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ./ .
COPY ${JAR_FILE} YekhIdentityProvider-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/YekhIdentityProvider-1.0-SNAPSHOT.jar"]