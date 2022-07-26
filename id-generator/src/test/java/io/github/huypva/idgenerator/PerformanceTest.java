package io.github.huypva.idgenerator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.huypva.idgenerator.config.ConfigInitializerImpl;
import io.github.huypva.idgenerator.worker.WorkerAssignerImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
@Slf4j
public class PerformanceTest {
  private static final int MAX_SIZE = 100000;
  private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

  public static IdGenerator idGenerator;

  @BeforeAll
  public static void setUp() throws Exception {
    String updConfigSql = "UPDATE config SET date_bits=15, worker_id_bits=28, sequence_bits=20, epoch_date='2022-01-01' WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updConfigSql);

    String updateSql = "UPDATE worker SET last_date=now()" + ", worker_id=1 WHERE id=1;";
    TestContainer.executeSql(TestContainer.MYSQL_DB, updateSql);

    HikariConfig config = new HikariConfig();
    config.setDriverClassName(TestContainer.MYSQL_DB.getDriverClassName());
    config.setJdbcUrl(TestContainer.MYSQL_DB.getJdbcUrl());
    config.setUsername(TestContainer.MYSQL_DB.getUsername());
    config.setPassword(TestContainer.MYSQL_DB.getPassword());
    HikariDataSource hikariDataSource = new HikariDataSource(config);

    idGenerator = new IdGeneratorImpl(new ConfigInitializerImpl(hikariDataSource), new WorkerAssignerImpl(hikariDataSource));
  }

  @Test
  void testGenerate() throws Exception {
    Set<Long> idSet = new HashSet<>(MAX_SIZE);

    for (int i = 0; i < MAX_SIZE; i++) {
      genId(idSet);
    }

    // Check UIDs are all unique
    checkUnique(idSet);
  }

  @Test
  public void testParallelGenerate() throws InterruptedException {
    AtomicInteger size = new AtomicInteger(-1);
    List<Thread> threadList = new ArrayList<>(THREADS);
    Set<Long> idSet = new ConcurrentSkipListSet<>();
    for (int i = 0; i < THREADS; i++) {
      Thread thread = new Thread(() -> workerRun(idSet, size));
      thread.setName("UID-generator-" + i);

      threadList.add(thread);
      thread.start();
    }

    // Wait for worker done
    for (Thread thread : threadList) {
      thread.join();
    }

    // Check generate 10w times
    Assert.assertEquals(MAX_SIZE, size.get());

    // Check UIDs are all unique
    checkUnique(idSet);
  }

  private void workerRun(Set<Long> idSet, AtomicInteger size) {
    for (;;) {
      int next = size.updateAndGet(t -> (t == MAX_SIZE ? MAX_SIZE : t + 1));
      if (next == MAX_SIZE) {
        return;
      }

      genId(idSet);
    }

  }

  /**
   * Generate id
   */
  private void genId(Set<Long> uidSet) {
    long id = idGenerator.genId();
    uidSet.add(id);

    Assert.assertTrue(id > 0L);
  }

  /**
   * Check id are all unique
   */
  private void checkUnique(Set<Long> idSet) {
    Assert.assertEquals(MAX_SIZE, idSet.size());
  }

  @AllArgsConstructor
  public class GenIDThread implements Runnable {

    int threadId;
    IdGenerator idGenerator;

    @Override
    public void run() {
      for (int i = 0; i < 1000000; i++) {
        log.info("Thread {} gen id {}", threadId, idGenerator.parseId(this.idGenerator.genId()));
      }
    }
  }
}