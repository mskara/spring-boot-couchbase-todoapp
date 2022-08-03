# Spring Boot and Couchbase Todo App

## About The Project
A simple to-do application that users can create their own lists.

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
docker run -d --name couchbase -p 8091-8093:8091-8093 -p 11210:11210 mskara/couchbase
```
```bash
docker run -p 1905:8080 --link couchbase mskara/todoapp
```

### Swagger
http://localhost:1905/swagger-ui.html#/

### Couchbase Interface
http://localhost:8091/ui/index.html