# Kafka DLQ POC

Demo is built from two microservices. Service one and Service two
emulates different consumer groups. Additionally in service one are
endpoints which are used to generate messages for different topic scenarios with error.

##Requirements
* Java 11
* Docker with Docker compose

## How to run

Start kafka in docker
```
docker compose up
```

Start service one
```
./gradlew service-one:bootRun
```
Start service two
```
./gradlew service-two:bootRun
```

##Helpful Endpoints

Swagger to generate messages
```
http://localhost:8080/swagger-ui.html
```
Kafka UI to inspect topics
```
http://localhost:9021/clusters
```

