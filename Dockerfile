FROM openjdk:8-jdk-alpine
ADD target/contactlistapp.jar contactlistapp.jar
ENTRYPOINT ["java","-jar","/contactlistapp.jar"]