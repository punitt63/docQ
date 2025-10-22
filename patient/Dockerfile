# -------- Build stage --------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY . /app
RUN mvn -q -pl patient -am -DskipTests package

# -------- Run stage --------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/patient/target/patient-service.jar /app/patient-service.jar
EXPOSE 9098
ENTRYPOINT ["java","-jar","/app/patient-service.jar"]


