# Use the official OpenJDK 17 image from Docker Hub
FROM openjdk:21
# Set working directory inside the container
WORKDIR /app
# Copy the compiled Java application JAR file into the container
COPY ./target/spring-customer-mgmt-jenkins-cicd.jar spring-customer-mgmt-jenkins-cicd.jar
# Expose the port the Spring Boot application will run on
EXPOSE 8080
# COPY target/spring-customer-mgmt-jenkins-cicd.jar spring-customer-mgmt-jenkins-cicd.jar
# ADD target/spring-customer-mgmt-jenkins-cicd.jar spring-customer-mgmt-jenkins-cicd.jar
# Command to run the application
# CMD ["java", "-jar", "srtechopsApp.jar"]
# ENTRYPOINT ["java","-jar","spring-customer-mgmt-jenkins-cicd.jar"]
ENTRYPOINT ["java", "-jar", "spring-customer-mgmt-jenkins-cicd.jar"]