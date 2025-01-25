FROM openjdk:21-jdk

WORKDIR /app

COPY target/personal-finance-tracker-api.jar /app/personal-finance-tracker-api.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/personal-finance-tracker-api.jar"]