FROM openjdk:11-jdk-slim
ARG JAR_FILE=target/onekids_project-0.0.1-SNAPSHOT.war
COPY ${JAR_FILE} app.war
ENTRYPOINT ["java","-jar","app.war"]
