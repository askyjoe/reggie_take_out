FROM openjdk:17
LABEL authors="user"

COPY /target/reggie_take_out-1.0-SNAPSHOT.jar /reggie_take_out-1.0-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/reggie_take_out-1.0-SNAPSHOT.jar"]