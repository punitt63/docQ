# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
# Copy entire repo (parent pom and dependencies)
COPY . /app
# Build keycloak-admin-client and copy dependencies
RUN mvn -q -pl keycloak-admin-client -am -DskipTests package

# -------- Run stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copy the jar and dependencies
COPY --from=build /app/keycloak-admin-client/target/keycloak-admin-client-1.2-SNAPSHOT.jar /app/keycloak-admin-client.jar
COPY --from=build /app/keycloak-admin-client/target/dependency /app/dependency

# Default command to run the setup
ENTRYPOINT ["java", "-cp", "/app/keycloak-admin-client.jar:/app/dependency/*", "in.docq.KeycloakSetupOrchestrator"]



