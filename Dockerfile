FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/Moneymager-0.0.1-SNAPSHOT.jar moneymager-v1.0.jar
EXPOSE 9090
ENTRYPOINT ["java","-jar","moneymager-v1.0.jar"]