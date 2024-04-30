FROM amazoncorretto:17-alpine-jdk
COPY target/*.jar service.jar
ENTRYPOINT ["java", "-jar", "service.jar"]