FROM maven:3.9.4-eclipse-temurin-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/target/SpringProject-1.0.0.jar app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]
