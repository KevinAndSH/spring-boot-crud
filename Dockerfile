FROM openjdk:latest

COPY "./target/demo-0.0.1-SNAPSHOT.war" "app.war"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.war"]
