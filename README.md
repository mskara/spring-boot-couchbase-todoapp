# Spring Boot and Couchbase Todo App

## About The Project
A simple to-do application that users can create their own lists.

### Technology Stack
* Java 8
* Spring Boot
* Spring Data Couchbase
* Couchbase
* Swagger
* Maven
* Docker
* JUnit and Mockito

## Getting Started
Follow the instructions to set up the project in your local environment.

### Prerequisites
* JDK 8
* Maven
* Docker

### Run the System
We can run only a single command:
```bash
docker-compose up
```

Docker will pull the Couchbase and Spring Boot images.

The services can be run on the background with command:
```bash
docker-compose up -d
```

### Stop the System
Stopping all the running containers is also simple with a single command:
```bash
docker-compose down
```


## Installation Without Download The Project

You can run both of todo application and couchbase service in your local environment. Please run the below commands.


```bash
docker run -d --name couchbase -p 8091-8093:8091-8093 -p 11210:11210 -e CLUSTER_NAME=couchbase-demo -e COUCHBASE_ADMINISTRATOR_USERNAME=mskara -e COUCHBASE_ADMINISTRATOR_PASSWORD=123456 -e COUCHBASE_BUCKET=todoapp -e COUCHBASE_BUCKET_RAMSIZE=512 -e COUCHBASE_RAM_SIZE=2048 -e COUCHBASE_INDEX_RAM_SIZE=512 mskara/couchbase
```

```bash
docker exec couchbase /opt/couchbase/init/init-cbserver.sh
```

```bash
docker run --name todoapp -p 1905:8080 -e CONFIG_COUCHBASE_HOST=couchbase -e CONFIG_COUCHBASE_USERNAME=mskara -e CONFIG_COUCHBASE_PASSWORD=123456 -e CONFIG_COUCHBASE_BUCKETNAME=todoapp --link couchbase mskara/todoapp
```



### Swagger
http://localhost:1905/swagger-ui.html#/

### Couchbase Interface
http://localhost:8091/ui/index.html

### How it works
* after running the app, you can register from swagger interface or postman
* after registiration take the jwt token
* what can you do with this app
    - display todo list of user who has token
    - add new item to todo list
    - update status of todo item with item id
