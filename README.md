# Number Generator

### Configuration
Change the property task-configuration.delay-in-seconds to change the delay between subsequent number generation.

### How to run the application
./mvnw clean spring-boot:run

### How to run the test cases
./mvnw clean test

### How to create docker image (Using dockerfile-maven-plugin)
./mvnw package

### How to run using docker image
docker run -p 8080:8080 number-generator/app:latest
