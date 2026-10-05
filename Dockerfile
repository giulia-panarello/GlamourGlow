
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM tomcat:10.1-jdk17
COPY --from=builder /app/target/GlamourGlow-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]