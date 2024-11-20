FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /VibeVault/vibe/
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:21-jdk-slim
COPY --from=build VibeVault/vibe/target/demo-0.0.1-SNAPSHOT.jar demo.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "demo.jar"]
