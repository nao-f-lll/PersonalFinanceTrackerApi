FROM openjdk:21-jdk

WORKDIR /app

COPY target/personal-finance-tracker-api.jar /app/personal-finance-tracker-api.jar

ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://${DB_HOST}:5432/personal_finance_app_db
ENV SPRING_DATASOURCE_USERNAME=${DB_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/personal-finance-tracker-api.jar"]
