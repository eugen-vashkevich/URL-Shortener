FROM maven:3.9.9-amazoncorretto-23 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn \
    clean package \
    dependency:copy-dependencies -DoutputDirectory=target/lib \
    -DskipTests

FROM amazoncorretto:23
WORKDIR /app

ENV JAR_EXECUTABLE_NAME="url-shortener-read-service-0.0.1.jar"
ARG LISTEN_HOST_PORT=8080

COPY --from=build /app/target/*.jar ${JAR_EXECUTABLE_NAME}

EXPOSE ${LISTEN_HOST_PORT}

SHELL ["/bin/bash", "-c"]
CMD java -jar url-shortener-read-service-0.0.1.jar
