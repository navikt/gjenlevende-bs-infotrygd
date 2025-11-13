FROM gcr.io/distroless/java21-debian12:nonroot
COPY init.sh /init-scripts/init.sh
ENV TZ="Europe/Oslo"
COPY target/gjenlevende-bs-infotrygd.jar /app/app.jar
ENV JDK_JAVA_OPTIONS="-XX:MaxRAMPercentage=75"
CMD ["-jar", "/app/app.jar"]
