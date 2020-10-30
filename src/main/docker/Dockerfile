FROM gradle:6.7-jdk11
RUN mkdir /app
RUN mkdir /dist

COPY . /app
RUN gradle -p /app/ clean build
RUN cp /app/build/libs/log-aggregator.jar /dist

CMD ["java", "-jar", "/dist/log-aggregator.jar"]
