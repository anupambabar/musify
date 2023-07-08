FROM openjdk:11
ADD ./target/musify-0.0.1-SNAPSHOT.jar musify-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/musify-0.0.1-SNAPSHOT.jar"]