FROM eclipse-temurin:17-jdk-alpine
ADD target/contactlistapp-0.0.1-SNAPSHOT.jar contactlistapp.jar
ENTRYPOINT ["java","-jar","/contactlistapp.jar"]