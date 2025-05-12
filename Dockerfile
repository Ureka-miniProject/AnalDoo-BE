FROM amazoncorretto:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} analdoo.jar

ENTRYPOINT ["java","-jar","/analdoo.jar"]

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime