package io.github.huypva.idgenerator.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.huypva.idgenerator.TestContainer;
import io.github.huypva.idgenerator.config.entity.ConfigEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
class ConfigInitializeImplTest {

  public static ConfigInitializerImpl configInitialize;

  @BeforeEach
  void setUp() {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(TestContainer.MYSQL_DB.getDriverClassName());
    config.setJdbcUrl(TestContainer.MYSQL_DB.getJdbcUrl());
    config.setUsername(TestContainer.MYSQL_DB.getUsername());
    config.setPassword(TestContainer.MYSQL_DB.getPassword());
    HikariDataSource hikariDataSource = new HikariDataSource(config);

    configInitialize = new ConfigInitializerImpl(hikariDataSource);
  }

  @Test
  void initializeConfig() throws Exception {
    ConfigEntity configEntity = configInitialize.initializeConfig();

    Assertions.assertTrue(configEntity != null);
    Assertions.assertEquals(15, configEntity.getDateBits());
    Assertions.assertEquals(28, configEntity.getWorkerIdBits());
    Assertions.assertEquals(20, configEntity.getSequenceBits());
    Assertions.assertEquals("2022-01-01", configEntity.getEpochDate().toString());
  }
}