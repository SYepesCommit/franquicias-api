
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-jammy
WORKDIR /

COPY --from=build /target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]