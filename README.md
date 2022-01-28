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

##Description of test endpoints

### ```/retryable-single```
It sends message to topic **normal** . It fails on service-one and third-group ,but pass on service-two.
It uses retry mechanism in microservice. It does not require retry topics.
Failed Message is propagated to **normal-service-one-dlt** and **normal-third-group-dlt**.

### ```/retryable-single-invalid```
It sends message to topic **normal**  with invalid format. It fails on both services and propagates failed messages to all
 **normal-service-one-dlt** ,**normal-service-two-dlt** and **normal-third-group-dlt**. It uses retry mechanism in microservice.
It does not require retry topics.

### ```/retryable-default```
It sends message to topic **retryable-default**. It fails on both services and propagates failed messages to both
**retryable-default-service-one-dlt** ,**retryable-default-service-two-dlt**. It uses retry topics and retries messages before propagating to DLQ. Created by RetryTopicConfiguration
which is default configuration for retry topics. Drawback of that solution we cannot set up good prefix per consumer group if we are using
multiple consumer groups in service.

### ```/retryable-single```
It sends message to topic **retryable-single**. It fails on service-one and pass on service-two. It  propagates failed messages only to
**retryable-default-service-one-dlt** . It uses retry topics and retries messages before propagating to DLQ. It uses @RetryableTopic annotation to configure retry topics

### ```/retryable-single-invalid```
It sends message with invalid format to topic **retryable-single**. It fails both on service-one and service-two. It propagates failed messages to both
**retryable-default-service-one-dlt** both **retryable-default-service-two-dlt**. It uses retry topics and retries messages before propagating to DLQ It uses @RetryableTopic annotation to configure retry topics