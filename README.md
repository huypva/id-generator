IdGenerator
==========================

IdGenerator is a Java library to generate unique ids
It based on a part of [Snowflake](https://github.com/twitter/snowflake) and [MySQL](https://dev.mysql.com/downloads/mysql/)

Like **Snowflake** algorithm, IdGenerator using 64 bits number(long)
+-----------+-----------+----------------+---------------+
|  sign_bit | date_bits | worker_id_bits | sequence_bits |
+-----------+-----------+----------------+---------------+
|         1 |        15 |             28 |            20 | 
+-----------+-----------+----------------+---------------+

* sign(1bit)  
  The highest bit is always 0
  
* delta days (15 bit)
  The next 15 bits, represents delta days since a customer epoch day(2022-01-01).
  The maximum time will be 89 year from epoch day 
  
* worker id (28 bits)
  The next 28 bits, represents the worker id, maximum value will be 260 million workers per day. 
  IdGenerator uses a build-in database based to assign a new worker id when startup, or a first id of next day, or use exceed sequence numbers
   
* sequence (20 bits)
  The last 20 bits, represents sequence within the worker id, maximum is one million sequence numbers per worker
  
## Prerequisites
- Make sure that you have Docker and Docker Compose installed
  - Windows or macOS:
    [Install Docker Desktop](https://www.docker.com/get-started)
  - Linux: [Install Docker](https://www.docker.com/get-started) and then
    [Docker Compose](https://github.com/docker/compose) 

- [Java8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)+

## Getting Started

### Step 1: Install & start MySQL

- Start mysql and create table in docker

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

### Step 2: Install libraries in maven repository

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

### Step 3: Using in project

- To add a dependency using Maven, use the following

```xml
  <dependency>
    <groupId>io.github.huypva</groupId>
    <artifactId>id-generator-starter</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
```
- Configure datasource in your application.yml 

```yaml
id-generator:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    jdbc-url: jdbc:mysql://127.0.0.1:3306/id_generator
    username: user
    password: password
```

- @Autowired IdGenerator bean to use 

```java
  @Autowired
  IdGenerator idGenerator;

  //Generate id
  long id = idGenerator.genId();

  //Parse a id
  log.info("Parse id: {}", idGenerator.parseId(id));
  //Parse id: {"id":"57983845202395136","lastDate":"2022-07-26","workerId":"0","sequence":"0"}
```

## Run example

```shell script
$ cd examples/id-generator-sample
$ ../../mvnw clean package
...
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.641 s
[INFO] Finished at: 2022-07-26T09:29:56+07:00
[INFO] ------------------------------------------------------------------------
$ ../../mvnw spring-boot:run
...
2022-07-26 09:32:45.797  INFO 9022 --- [           main] i.g.h.idgeneratorsample.Application      : Id: 57983845203443712
2022-07-26 09:32:45.798  INFO 9022 --- [           main] i.g.h.idgeneratorsample.Application      : Parse id: {"id":"57983845203443712","lastDate":"2022-07-26","workerId":"1","sequence":"0"}
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.363 s
[INFO] Finished at: 2022-07-26T09:32:45+07:00
[INFO] ------------------------------------------------------------------------
```

## Reference
