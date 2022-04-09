FROM openjdk:13-alpine
WORKDIR /app
COPY application.properties /app/application.properties
COPY sensor-server.jar /app/sensor-server.jar
ENV SERVER_PORT 9081
ENTRYPOINT ["java","-jar","sensor-server.jar"]