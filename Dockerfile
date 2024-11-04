FROM openjdk:21-oracle
RUN mkdir app
COPY "CheezlBot-2.0.0.jar" "app/CheezlBot-2.0.0.jar"
ENV DISCORD_TOKEN=''
ENV KC_CLIENT_SECRET=''
ENV WG_PASSWORD=''
WORKDIR /app
CMD ["java","-Dspring.profiles.active=prod", "-jar", "CheezlBot-2.0.0.jar"]