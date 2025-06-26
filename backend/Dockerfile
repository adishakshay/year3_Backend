# FROM maven:3.8.5-openjdk-17 AS build
# COPY . .
# RUN mvn clean package -DskipTests

# FROM openjdk:17.0.1-jdk-slim
# COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar demo.jar
# EXPOSE 8080
# ENTRYPOINT [ "java","-jar","demo.jar"]

# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set working directory to /app
WORKDIR /app

# Copy only the contents of backend/
COPY backend/ /app

# Run Maven to build the app
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17.0.1-jdk-slim

# Copy built jar from the build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar demo.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "demo.jar"]
