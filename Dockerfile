FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/Library_Managment_System-1.0-SNAPSHOT.jar app.jar
CMD ["java","-jar","app.jar"]