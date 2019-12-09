FROM java:8-alpine
VOLUME /tmp
EXPOSE 9092
ADD demo-0.0.1-SNAPSHOT.jar app.jar
#RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","app.jar"]