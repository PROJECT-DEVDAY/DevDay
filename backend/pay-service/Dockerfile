FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/pay-service-0.0.1-SNAPSHOT.jar PayService.jar
ENTRYPOINT ["java", "-jar", "PayService.jar"]