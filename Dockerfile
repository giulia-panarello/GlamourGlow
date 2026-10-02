FROM tomcat:10.1-jdk17
COPY target/GlamourGlow-1.0.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
