FROM openjdk:17-jdk

WORKDIR /api

COPY ./target/backend-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
