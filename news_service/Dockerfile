FROM openjdk:8-jdk-alpine
ARG JAR_FILE=out/artifacts/news_service_jar/*.jar
COPY ${JAR_FILE} news_service.jar
ENTRYPOINT ["java", "-jar", "/news_service.jar"]