FROM maven:3.9-amazoncorretto-8 AS build

COPY . /app
WORKDIR /app

FROM amazoncorretto:8

COPY --from=build ./app/target /app

WORKDIR /app

EXPOSE 3003

CMD java -cp soap-service-1.0-SNAPSHOT-jar-with-dependencies.jar tubes.kicaumania.Main