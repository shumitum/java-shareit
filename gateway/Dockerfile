FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareit_gateway_app.jar
ENTRYPOINT ["java","-jar","/shareit_gateway_app.jar"]