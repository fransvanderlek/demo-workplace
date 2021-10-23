FROM amd64/alpine
RUN apk add openjdk11-jre
COPY target/demo-workplace-0.0.1-SNAPSHOT.jar app.jar
COPY target/dependency /dependency
ENTRYPOINT ["java","-cp","/app.jar", "org.intelligentindustry.demoworkplace.MainApp"]
