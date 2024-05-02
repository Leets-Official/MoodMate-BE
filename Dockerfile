FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/MoodMate-BE-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} MoodMate-BE.jar

CMD ["java", "-jar", "-Dspring.profiles.active=dev", "-Duser.timezone=Asia/Seoul", "MoodMate-BE.jar"]