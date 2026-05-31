FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the POM first so dependencies cache between builds
COPY pom.xml .
RUN mvn -q -B dependency:go-offline || true

# Copy sources and build (skip tests: @SpringBootTest needs a live DB)
COPY src ./src
RUN mvn -q -B -DskipTests clean package

# ---- Stage 2: slim runtime image ----
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy whatever jar Maven produced (handles the version in the name)
COPY --from=build /app/target/*.jar app.jar

# Documentation only; Railway routes via $PORT
EXPOSE 8080

# Shell form so ${PORT} is expanded at runtime.
# -Dserver.port overrides the hardcoded 8080 in application.properties.
# MaxRAMPercentage keeps the JVM within the container memory limit.
ENTRYPOINT java -XX:MaxRAMPercentage=75.0 -Dserver.port=${PORT:-8080} -jar app.jar