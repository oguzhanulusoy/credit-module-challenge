FROM openjdk:17-jdk-slim
LABEL authors="ogulusoy"
COPY target/cmc-1-SNAPSHOT /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]