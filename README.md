The example project for StringBoot service

<div align="center">
    <img src="./assets/images/spring_boot_icon.png"/>
</div>

## Getting Started

## Project structure
```
.
├── id-generator
│   ...
├── id-generator-starter
│   ...
├── id-generator-sample
│   ...
├── infrastructure
│   ├── Dockerfile
|       init.sql
|
└── README.md
```

## Prerequisites
- Make sure that you have Docker and Docker Compose installed
  - Windows or macOS:
    [Install Docker Desktop](https://www.docker.com/get-started)
  - Linux: [Install Docker](https://www.docker.com/get-started) and then
    [Docker Compose](https://github.com/docker/compose)

## Start infrastructure

- Start mysql in docker

```shell script
$ cd infrastructure
$ docker build --tag mysqldb .
# docker rm container_id (if any)
$ docker run  -p 3306:3306 --name mysqldb -d mysqldb
$ cd ..
```

- Check connection
```shell script
$ mysql --host=127.0.0.1 --port=3306 --user=user --password=password
mysql> use worker;
...
Database changed
mysql> show tables;
+------------------+
| Tables_in_worker |
+------------------+
| id_config        |
| id_worker        |
+------------------+
2 rows in set (0.00 sec)
mysql> select * from id_config;
+----+-----------+----------------+---------------+------------+
| id | date_bits | worker_id_bits | sequence_bits | epoch_date |
+----+-----------+----------------+---------------+------------+
| 1  |        15 |             28 |            20 | 2022-01-01 |
+----+-----------+----------------+---------------+------------+
1 row in set (0.01 sec)
mysql> select * from id_worker;
+----+------------+-----------+
| id | last_date  | worker_id |
+----+------------+-----------+
| 1  | 2022-07-25 |         0 |
+----+------------+-----------+
1 row in set (0.01 sec)
```

## Build project

- Clean & build project
```shell script
$ ../mvnw clean install
...
```

### Start services in docker 

```shell script
$ docker-compose -f ./docker-compose-service.yml -p spring-boot-service up -d
```

Run multiple instances

```shell script
$ docker-compose -f ./docker-compose-service.yml -p spring-boot-service up -d --scale hello-world=2
```

## Run testing

```shell script
curl http://localhost:8081/greet?name=World
```

## Stop project

- Kill project if start in local mode
- Stop infrastructure & services in docker

```shell script
$ docker-compose -f ./docker-compose-infrastructure.yml -p spring-boot-infrastructure down
$ docker-compose -f ./docker-compose-service.yml -p spring-boot-service down
```

## Run example

## Reference
