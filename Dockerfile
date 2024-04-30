FROM openjdk:17
VOLUME /tmp
COPY target/BankApplication-0.0.1-SNAPSHOT.jar BankApplication-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","BankApplication-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080