FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
ADD target/xe_light-1.0-SNAPSHOT.jar xe_light.jar
EXPOSE 12006
# ENTRYPOINT exec java $JAVA_OPTS -jar xe_light.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar xe_light.jar
