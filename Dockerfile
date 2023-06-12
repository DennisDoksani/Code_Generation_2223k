FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk  # Please confirm the java version you want to use
COPY . .
RUN chmod +x mvnw
RUN mvnw clean install -U -X

EXPOSE 8080
ENTRYPOINT ["mvnw","spring-boot:run"]
