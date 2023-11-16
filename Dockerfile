# ENVIRONMENT FOR JAVA
FROM openjdk:8

# COPY ./target /app/target
COPY ./start-soap.sh /app/start-soap.sh
WORKDIR /app

EXPOSE 3003

ENTRYPOINT ["bash", "start-soap.sh"]
