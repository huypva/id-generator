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
|   ├── scripts/init.sql
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
$ ./mvnw clean install
...
[INFO] 
[INFO] Parent ............................................. SUCCESS [  0.698 s]
[INFO] id-generator ....................................... SUCCESS [ 30.115 s]
[INFO] id-generator-starter ............................... SUCCESS [ 21.489 s]
[INFO] id-generator-sample ................................ SUCCESS [  1.599 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  54.501 s
[INFO] Finished at: 2022-07-25T17:47:39+07:00
[INFO] ------------------------------------------------------------------------
```

## Run example

```shell script
$ cd id-generator-sample
$ ../mvnw spring-boot:run
...
2022-07-25 17:48:04.893  INFO 57525 --- [           main] i.g.h.idgeneratorsample.Application      : Parse id: {"id":"57702370226733056","lastDate":"2022-07-25","workerId":"1","sequence":"0"}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.221 s
[INFO] Finished at: 2022-07-25T17:48:04+07:00
[INFO] ------------------------------------------------------------------------
```

## Reference
