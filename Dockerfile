FROM openjdk:8-jdk-alpine
ADD target/contactlistapp.war contactlistapp.war
ENTRYPOINT ["java","-jar","/contactlistapp.war"]