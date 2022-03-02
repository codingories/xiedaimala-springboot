FROM adoptopenjdk/openjdk11:latest

RUN mkdir /app

WORKDIR /app

COPY target/gs-serving-web-content-0.1.0.jar /app

EXPOSE 8080

CMD ["java", "-jar", "gs-serving-web-content-0.1.0.jar"]

