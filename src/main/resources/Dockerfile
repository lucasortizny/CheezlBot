FROM openjdk:17-oracle
RUN mkdir app
COPY "CheezlBot-1.0-SNAPSHOT.jar" "app/CheezlBot-1.0-SNAPSHOT.jar"
COPY settings.json app/settings.json
EXPOSE 8081
WORKDIR /app
CMD ["java","-Dspring.profiles.active=prod", "-jar", "CheezlBot-1.0-SNAPSHOT.jar"]