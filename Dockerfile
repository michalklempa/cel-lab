FROM dhi.io/maven:3.9.16-jdk25-debian13-dev AS build
WORKDIR /app

COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:go-offline

COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn package

FROM eclipse-temurin:25.0.3_9-jre
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
