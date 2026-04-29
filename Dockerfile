FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/ms-ordenes-compra-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/wallet wallet

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
