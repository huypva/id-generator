package io.github.huypva.idgenerator.worker;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.huypva.idgenerator.TestContainer;
import io.github.huypva.idgenerator.worker.entity.WorkerEntity;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author huypva
 */
@Slf4j
@SpringBootTest
class WorkerAssignerImplTest {

  public static WorkerAssignerImpl workerAssigner;

  @BeforeAll
  public static void setUp() {
    HikariConfig config = new HikariConfig();
    config.setDriverClassName(TestContainer.MYSQL_DB.getDriverClassName());
    config.setJdbcUrl(TestContainer.MYSQL_DB.getJdbcUrl());
    config.setUsername(TestContainer.MYSQL_DB.getUsername());
    config.setPassword(TestContainer.MYSQL_DB.getPassword());
    HikariDataSource hikariDataSource = new HikariDataSource(config);

    workerAssigner = new WorkerAssignerImpl(hikariDataSource);
  }

  @Test
  void testNextWorker() throws Exception {
    WorkerEntity worker = workerAssigner.assignWorker(10);

    Assertions.assertTrue(worker.getLastDate().compareTo(LocalDate.now()) >= 0);
    Assertions.assertTrue(worker.getWorkerId() >= 0);
  }


  @Test
  void testMaxWorker() throws Exception {
    LocalDate now = LocalDate.now();
    String sql = "UPDATE worker SET last_date='" + now.toString() +"', worker_id=10 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, sql);

    WorkerEntity worker = workerAssigner.assignWorker(10);
    log.info("Worker {}", worker);

    Assertions.assertEquals(now.plusDays(1), worker.getLastDate());
    Assertions.assertEquals(0,worker.getWorkerId());
  }

  @Test
  void testMaxWorkerWithDateInDbGreaterThanToday() throws Exception {
    LocalDate tomorrow = LocalDate.now().plusDays(1);
    String sql = "UPDATE worker SET last_date='" + tomorrow.toString() +"', worker_id=0 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, sql);

    WorkerEntity worker = workerAssigner.assignWorker(10);
    log.info("Worker {}", worker);

    Assertions.assertEquals(tomorrow, worker.getLastDate());
    Assertions.assertEquals(1,worker.getWorkerId());
  }


  @Test
  void testMaxWorkerWithDateInDbLessThanToday() throws Exception {
    LocalDate today = LocalDate.now();
    LocalDate yesterday = LocalDate.now().plusDays(-1);
    String sql = "UPDATE worker SET last_date='" + yesterday.toString() +"', worker_id=0 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, sql);

    WorkerEntity worker = workerAssigner.assignWorker(10);
    log.info("Worker {}", worker);

    Assertions.assertEquals(today, worker.getLastDate());
    Assertions.assertEquals(0,worker.getWorkerId());
  }
}