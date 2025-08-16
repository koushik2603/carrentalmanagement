# Use an official OpenJDK image that matches your project's Java version (17)
FROM openjdk:21-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled .jar file from your project's 'target' folder
# into the container and rename it to app.jar
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port the application will run on (this is for documentation)
EXPOSE 8080

# The command that runs your Java application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]