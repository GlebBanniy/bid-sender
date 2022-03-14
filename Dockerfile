FROM openjdk:11
EXPOSE 9000
ADD target/*.jar app.jar
COPY Data.zip ./
ENTRYPOINT ["java","-jar","app.jar"]
