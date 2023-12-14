FROM openjdk:17-ea-3-jdk
WORKDIR /app
COPY target/timer-0.0.1-SNAPSHOT.jar timer-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "timer-0.0.1-SNAPSHOT.jar"]
