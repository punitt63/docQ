# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copy entire repo (build context is repo root from docker-compose)
COPY . /app
# Skip Flyway during image build (DB isn't reachable in build stage)
# -q  : quiet Maven output (less log noise in Docker builds)
# -pl : build only the specified module (here, health-facility) plus dependencies because of -am
RUN mvn -pl health-facility -am clean install -DskipTests -Dflyway.skip=true

# -------- Run stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/health-facility/target/health-facility.jar /app/health-facility.jar
EXPOSE 9097
ENTRYPOINT ["java","-jar","/app/health-facility.jar"]


