# # BUILD DEPENDENCIES
# FROM maven:3.9-amazoncorretto-8

# COPY . /app
# WORKDIR /app

# RUN mvn clean package

# ENVIRONMENT FOR JAVA
FROM openjdk:8

COPY ./target /app

WORKDIR /app

EXPOSE 3003

ENTRYPOINT ["java", "-jar", "soap-service-jar-with-dependencies.jar"]
