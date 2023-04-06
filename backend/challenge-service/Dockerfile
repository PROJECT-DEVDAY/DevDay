FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/challenge-service-0.0.1-SNAPSHOT.jar ChallengeService.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
ENTRYPOINT ["java", "-jar", "ChallengeService.jar"]
