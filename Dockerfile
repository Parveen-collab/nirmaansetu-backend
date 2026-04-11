FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

VOLUME /tmp

# Add a non-root user (security best practice)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY target/nirmaansetu.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]