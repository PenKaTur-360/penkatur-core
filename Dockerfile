FROM maven:3-eclipse-temurin-17 AS BUILD_IMAGE

RUN mkdir -p /opt/PenKaTurCore
WORKDIR /opt/PenKaTurCore
ADD ./PenKaTurCore .

RUN mvn clean install


FROM eclipse-temurin:17-jre

RUN mkdir -p /usr/local/bin/PenKaTurCore
WORKDIR /usr/local/bin/PenKaTurCore

COPY --from=BUILD_IMAGE /opt/PenKaTurCore/target/PenKaTurCore.jar .
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/usr/local/bin/PenKaTurCore/PenKaTurCore.jar"]
