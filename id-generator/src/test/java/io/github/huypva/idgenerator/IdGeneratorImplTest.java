package io.github.huypva.idgenerator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.huypva.idgenerator.config.ConfigInitializer;
import io.github.huypva.idgenerator.config.ConfigInitializerImpl;
import io.github.huypva.idgenerator.worker.WorkerAssigner;
import io.github.huypva.idgenerator.worker.WorkerAssignerImpl;
import io.github.huypva.idgenerator.exception.IdGeneratorException;
import io.github.huypva.idgenerator.utils.JacksonUtils;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
@Slf4j
public class IdGeneratorImplTest {

  ConfigInitializer configInitializer;
  WorkerAssigner workerAssigner;

  @BeforeEach
  void setUp() throws Exception {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(TestContainer.MYSQL_DB.getDriverClassName());
    config.setJdbcUrl(TestContainer.MYSQL_DB.getJdbcUrl());
    config.setUsername(TestContainer.MYSQL_DB.getUsername());
    config.setPassword(TestContainer.MYSQL_DB.getPassword());
    HikariDataSource hikariDataSource = new HikariDataSource(config);

    configInitializer = new ConfigInitializerImpl(hikariDataSource);
    workerAssigner = new WorkerAssignerImpl(hikariDataSource);

  }

  @Test
  void testGetUID() throws Exception {
    String updConfigSql = "UPDATE config SET date_bits=15, worker_id_bits=18, sequence_bits=30, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);

    IdGenerator idGenerator = new IdGeneratorImpl(configInitializer, workerAssigner);
    long uid = idGenerator.genId();

    Assertions.assertTrue(uid > 0);
  }

  @Test
  void testParseUID() throws Exception {
    String updConfigSql = "UPDATE config SET date_bits=15, worker_id_bits=18, sequence_bits=30, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);

    IdGenerator idGenerator = new IdGeneratorImpl(configInitializer, workerAssigner);
    Assertions.assertEquals(
        "{\"id\":\"57702372373168128\",\"lastDate\":\"2022-07-25\",\"workerId\":\"2\",\"sequence\":\"0\"}",
        idGenerator.parseId(57702372373168128L));
  }

  @Test
  void testExeption() throws Exception {
    LocalDate now =  LocalDate.now();
    String updConfigSql = "UPDATE config SET date_bits=1, worker_id_bits=1, sequence_bits=1, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);
    Assertions.assertThrows(IllegalArgumentException.class, () -> new IdGeneratorImpl(configInitializer, workerAssigner));

    updConfigSql = "UPDATE config SET date_bits=0, worker_id_bits=62, sequence_bits=1, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);
    Assertions.assertThrows(IllegalArgumentException.class, () -> new IdGeneratorImpl(configInitializer, workerAssigner));

    updConfigSql = "UPDATE config SET date_bits=62, worker_id_bits=0, sequence_bits=1, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);
    Assertions.assertThrows(IllegalArgumentException.class, () -> new IdGeneratorImpl(configInitializer, workerAssigner));

    updConfigSql = "UPDATE config SET date_bits=62, worker_id_bits=1, sequence_bits=0, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);
    Assertions.assertThrows(IllegalArgumentException.class, () -> new IdGeneratorImpl(configInitializer, workerAssigner));

    updConfigSql = "UPDATE config SET date_bits=61, worker_id_bits=1, sequence_bits=1, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);
    Assertions.assertThrows(IllegalArgumentException.class, () -> new IdGeneratorImpl(configInitializer, workerAssigner));
  }

  @Test
  void testExceedSequence() throws Exception {
    LocalDate now = LocalDate.now();
    String updConfigSql = "UPDATE config SET date_bits=20, worker_id_bits=40, sequence_bits=3, epoch_date='2022-01-01'  WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);

    String sql = "UPDATE worker SET last_date='" + now.toString() + "', worker_id=0 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, sql);

    IdGeneratorImpl idGenerator = new IdGeneratorImpl(configInitializer, workerAssigner);

    long lastId = 0;
    for (int i = 0; i < 9; i++) {
      lastId = idGenerator.genId();
      log.info("Parse: {}", idGenerator.parseId(lastId));
    }

    Assertions.assertEquals(2, JacksonUtils.getField(idGenerator.parseId(lastId), "workerId").asLong());
    Assertions.assertEquals(0, JacksonUtils.getField(idGenerator.parseId(lastId), "sequence").asLong());
  }

  @Test
  void testExceedWorkerId() throws Exception {
    LocalDate now = LocalDate.now();
    //40 workerId bits => MaxWorkerId = 1099511627775
    String updConfigSql = "UPDATE config SET date_bits=20, worker_id_bits=40, sequence_bits=3, epoch_date='2022-01-01' WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);
    String updWorkerSql = "UPDATE worker SET last_date='" + now.toString() + "', worker_id=1099511627775 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updWorkerSql);

    IdGeneratorImpl idGenerator = new IdGeneratorImpl(configInitializer, workerAssigner);
    long id = idGenerator.genId();

    Assertions.assertEquals(now.plusDays(1).toString(), JacksonUtils.getField(idGenerator.parseId(id), "lastDate").asText());
    Assertions.assertEquals(0, JacksonUtils.getField(idGenerator.parseId(id), "workerId").asLong());
    Assertions.assertEquals(0, JacksonUtils.getField(idGenerator.parseId(id), "sequence").asLong());
  }

  @Test
  void testExceedDays() throws Exception {
    String updConfigSql = "UPDATE config SET date_bits=20, worker_id_bits=42, sequence_bits=1, epoch_date='2022-01-01' WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);

    String updateSql = "UPDATE worker SET last_date='4892-11-25'" + ", worker_id=4398046511102 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updateSql);

    IdGeneratorImpl idGenerator = new IdGeneratorImpl(configInitializer, workerAssigner);
    log.info("MaxDeltaDays {}", idGenerator.getMaxDeltaDays());
    log.info("MaxWorkerId: {}", idGenerator.getMaxWorkerId());
    log.info("MaxSequence: {}", idGenerator.getMaxSequence());

    LocalDate epochDate = LocalDate.parse("2022-01-01");
    log.info("Last Days {}", epochDate.plusDays(idGenerator.getMaxDeltaDays()));
    //MaxDeltaDays = 1048575
    //Last Days = 4892-11-25
    //MaxWorkerId = 4398046511103
    //MaxSequence = 1

    Assertions.assertDoesNotThrow(() -> log.info(idGenerator.parseId(idGenerator.genId()))); //sequence = 0
    Assertions.assertDoesNotThrow(() -> log.info(idGenerator.parseId(idGenerator.genId()))); //sequence = 1
    Assertions.assertThrows(IdGeneratorException.class, () -> log.info(idGenerator.parseId(idGenerator.genId())));
  }
}