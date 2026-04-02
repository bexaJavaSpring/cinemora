FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /cinemora

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


FROM amazoncorretto:21-alpine

WORKDIR /cinemora

COPY --from=builder /cinemora/target/*.jar app.jar

EXPOSE 9999

ENTRYPOINT ["java","-jar","app.jar"]
