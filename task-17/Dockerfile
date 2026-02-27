FROM maven:3.8.6-eclipse-temurin-17 as builder

WORKDIR /opt/app

COPY . .

RUN mvn clean package -DskipTests

FROM tomcat:10-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=builder /opt/app/target/*.war /usr/local/tomcat/webapps/hoteladmin.war

EXPOSE 8080

CMD ["catalina.sh", "run"]