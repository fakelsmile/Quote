FROM openjdk:18-jdk-alpine
ARG JAR_FILE=target/*.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]