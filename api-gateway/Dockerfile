FROM openjdk:8-jdk-alpine
# Add Maintainer Info
LABEL maintainer="duc.nguyen80@gmail.com"
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]