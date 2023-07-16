FROM openjdk:17
VOLUME /musify
EXPOSE 8081
ARG JAR_FILE=target/musify-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} musify-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/musify-0.0.1-SNAPSHOT.jar"]