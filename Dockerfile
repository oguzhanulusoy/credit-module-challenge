FROM amazoncorretto:17
LABEL authors="ogulusoy"
COPY target/cmc-1-SNAPSHOT.jar /app/cmc-1-SNAPSHOT.jar
WORKDIR /app
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=stage
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl --fail http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "cmc-1-SNAPSHOT.jar"]