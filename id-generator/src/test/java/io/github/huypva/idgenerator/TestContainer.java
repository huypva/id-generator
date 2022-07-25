package io.github.huypva.idgenerator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.util.Assert;
import org.testcontainers.containers.MySQLContainer;

/**
 * @author huypva
 */
public class TestContainer {

  public static MySQLContainer<?> MYSQL_DB;

  static {
    MYSQL_DB =
        new MySQLContainer<>("mysql:5.6")
            .withDatabaseName("id_generator")
            .withUsername("user")
            .withPassword("password")
            .withInitScript("scripts/init_mysql.sql");

    MYSQL_DB.start();
  }

  public static void executeSql(MySQLContainer container, String sql) throws SQLException {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(container.getJdbcUrl());
    hikariConfig.setUsername(container.getUsername());
    hikariConfig.setPassword(container.getPassword());

    try (HikariDataSource ds = new HikariDataSource(hikariConfig);
        Connection connection = ds.getConnection();
        Statement statement = connection.createStatement()) {
      statement.execute(sql);
    }
  }

}
