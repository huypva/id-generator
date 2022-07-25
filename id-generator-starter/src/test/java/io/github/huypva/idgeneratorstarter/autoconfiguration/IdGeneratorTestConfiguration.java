package io.github.huypva.idgeneratorstarter.autoconfiguration;

import com.zaxxer.hikari.HikariConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author huypva
 */
@TestConfiguration
public class IdGeneratorTestConfiguration {

  public static MySQLContainer<?> MYSQL_DB;

  static {
    MYSQL_DB = new MySQLContainer<>("mysql:5.6")
        .withDatabaseName("dbworker")
        .withUsername("user")
        .withPassword("password")
        .withInitScript("scripts/init_mysql.sql");

    MYSQL_DB.start();
  }

  @Primary
  @Bean
  public IdGeneratorProperties idGeneratorProperties() {
    IdGeneratorProperties props = new IdGeneratorProperties();

    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName(MYSQL_DB.getDriverClassName());
    hikariConfig.setJdbcUrl(MYSQL_DB.getJdbcUrl());
    hikariConfig.setUsername(MYSQL_DB.getUsername());
    hikariConfig.setPassword(MYSQL_DB.getPassword());
    props.setDatasource(hikariConfig);

    return props;
  }
}
