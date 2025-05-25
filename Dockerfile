# Use an official OpenJDK image with JDK 17
FROM eclipse-temurin:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker cache
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (this step will be cached if pom.xml doesn't change)
RUN ./mvnw dependency:go-offline

# Copy the full source code
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# Run the app
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
