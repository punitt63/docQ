# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn -q -pl patient -am -DskipTests package

# -------- Run stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/patient/target/patient-1.2-SNAPSHOT.jar /app/patient-service.jar
COPY patient/entrypoint-with-secrets.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh
EXPOSE 9098
ENTRYPOINT ["/app/entrypoint.sh"]


