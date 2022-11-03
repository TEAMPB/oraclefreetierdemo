FROM arm64v8/ibm-semeru-runtimes:open-17-jdk 

COPY --chown=1001:0 . /opt/sources

WORKDIR /opt/sources

ENV PERSONEN_SERVICE_ROOT_URL=http://freedb-service:8080

ENTRYPOINT ["./mvnw","liberty:dev"]

