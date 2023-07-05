FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar shareit_server_app.jar
ENTRYPOINT ["java","-jar","/shareit_server_app.jar"]