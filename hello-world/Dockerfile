ARG SERVICE_NAME=hello-world

FROM maven:3.6.3-jdk-11 AS builder
ARG SERVICE_NAME
WORKDIR /workdir/${SERVICE_NAME}
COPY pom.xml /workdir/${SERVICE_NAME}/pom.xml
RUN mvn dependency:go-offline

COPY src /workdir/${SERVICE_NAME}/src
RUN mvn install
RUN mkdir  -p target/depency
WORKDIR /workdir/${SERVICE_NAME}/target/dependency
RUN jar -xf ../*.jar

FROM openjdk:11-jre-slim
ARG SERVICE_NAME
EXPOSE 8081
VOLUME /tmp
ARG DEPENDENCY=/workdir/${SERVICE_NAME}/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","io.codebyexample.helloworld.Application"]
