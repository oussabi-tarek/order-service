FROM openjdk:17
ADD target/order-service.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]