FROM openjdk:17-oracle
RUN mkdir app
COPY "CheezlBot-1.0-SNAPSHOT-jar-with-dependencies.jar" "app/CheezlBot-1.0-SNAPSHOT-jar-with-dependencies.jar"
COPY settings.json app/settings.json
EXPOSE 8081
WORKDIR /app
CMD ["java", "-jar", "CheezlBot-1.0-SNAPSHOT-jar-with-dependencies.jar"]