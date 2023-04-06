FROM openjdk:17-ea-11-jdk-slim
VOLUME /tmp
COPY build/libs/challenge-service-0.0.1-SNAPSHOT.jar ChallengeService.jar
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "ChallengeService.jar"]
