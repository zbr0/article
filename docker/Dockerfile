# Use JDK 17.0.7 as base
FROM openjdk:17.0.7

# Set the working directory
WORKDIR /app

# Copy the local file gradlew to the container
COPY gradlew .
COPY gradle ./gradle

# Make gradlew executable
RUN chmod +x ./gradlew

# Copy the current directory contents into the container at /app
COPY . .

# Clone the GitHub repository
RUN git clone https://oauth2:${GITHUB_TOKEN}@github.com/itgirlschool/itg-core.git

# Navigate to the cloned directory
WORKDIR /app/itg-core

# Checkout the specific release
RUN git checkout ${RELEASE_TAG}

# Build the project with Gradle
RUN ./gradlew build

# Run the application
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]