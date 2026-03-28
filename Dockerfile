FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY target/*.jar app.jar

# COPY src/main/resources/service-account.json service-account.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]