FROM adoptopenjdk/openjdk11
WORKDIR /app
COPY application.yml /app/application.yml
COPY sensor-server.jar /app/sensor-server.jar
ENV SERVER_PORT 9081
ENTRYPOINT ["java","-jar","sensor-server.jar"]