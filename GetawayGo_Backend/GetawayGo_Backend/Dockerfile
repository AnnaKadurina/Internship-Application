FROM gradle:7.5-jdk17
WORKDIR /opt/app
COPY ./build/libs/GetawayGo_Backend-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080

ENTRYPOINT ["sh", "-c","java ${JAVA_OPTS} -jar GetawayGo_Backend-0.0.1-SNAPSHOT.jar"]
