FROM openjdk:17-ea-jdk-buster

RUN apt-get update -y; apt-get install curl -y
RUN apt-get -y install maven

WORKDIR /talan

COPY . ocr/
WORKDIR /talan/ocr

ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
