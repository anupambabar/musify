FROM openjdk:17
VOLUME /tmp
EXPOSE 8081
ADD ./target/musify-0.0.1-SNAPSHOT.jar musify-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/musify-0.0.1-SNAPSHOT.jar"]