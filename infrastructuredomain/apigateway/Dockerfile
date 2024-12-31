# Use official OpenJDK 21 image as the base image
FROM eclipse-temurin:21-alpine

# Add metadata for better traceability
LABEL maintainer="vegagarrido@gmail.com" \
      version="1.0" \
      description="Docker image for running Java 21 applications"

# Create a new group and non-root user for security
# Ensure the group ID does not conflict with host system IDs
RUN addgroup -g 1028 devopsc \
    && adduser -D -G devopsc admin

# Create a volume for the temporary directory
VOLUME /tmp

# Set the working directory
WORKDIR /tmp

# Copy the application JAR file into the container
ARG JAR_FILE
COPY ${JAR_FILE} /tmp/app.jar

# Set ownership of the /tmp directory to the non-root user
RUN chown -R admin:devopsc /tmp

# Switch to non-root user for security
USER admin

# Expose the port your application uses (replace 8080 with your app's port)
# EXPOSE 8080

# Use a flexible entry point to allow passing custom Java options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /tmp/app.jar"]

