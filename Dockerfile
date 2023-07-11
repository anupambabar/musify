FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/musify-0.0.1-SNAPSHOT.jar musify-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/musify-0.0.1-SNAPSHOT.jar"]