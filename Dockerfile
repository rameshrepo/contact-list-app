FROM eclipse-temurin:17-jdk-alpine
ADD target/contactlistapp.jar contactlistapp.jar
ENTRYPOINT ["java","-jar","/contactlistapp.jar"]