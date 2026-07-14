FROM sbtscala/scala-sbt:eclipse-temurin-17.0.12_7_1.10.2_2.13.14

WORKDIR /app

COPY . .

RUN sbt update
RUN sbt compile

EXPOSE 8080

CMD ["sbt", "run"]
