FROM java:7
LABEL maintainer="DataseekCN"
RUN apt-get update -qq && apt-get install -y maven && apt-get clean
WORKDIR /app
ADD pom.xml /app/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

ADD src /app/src
RUN ["mvn", "package"]
EXPOSE 12006

CMD ["/usr/lib/jvm/java-7-openjdk-amd64/bin/java", "-jar", "target/worker-jar-with-dependencies.jar"]
