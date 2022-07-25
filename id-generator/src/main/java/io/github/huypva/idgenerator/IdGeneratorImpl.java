package io.github.huypva.idgenerator;

import io.github.huypva.idgenerator.config.ConfigInitializer;
import io.github.huypva.idgenerator.config.entity.ConfigEntity;
import io.github.huypva.idgenerator.worker.WorkerAssigner;
import io.github.huypva.idgenerator.exception.IdGeneratorException;
import io.github.huypva.idgenerator.utils.DateUtils;
import io.github.huypva.idgenerator.worker.entity.WorkerEntity;
import java.time.LocalDate;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * Bits for [sign-> date -> worker -> sequence]
 * 15 bits date: 89 year
 * 28 bits workers: 260 million workers per day
 * 20 bits sequence: one million sequence numbers per worker
 * @author huypva
 */
@Slf4j
@Getter
public class IdGeneratorImpl implements IdGenerator {

  private static final int TOTAL_BITS = 64;
  private static final int MAX_DATE_BITS = 20; //maximum 2^20/365=2872 years
  /**
   * Bits for [sign-> date -> workerId-> sequence]
   */
  private final LocalDate epochDate;

  private int signBits = 1;
  private final int dateBits;
  private final int workerIdBits;
  private final int sequenceBits;

  /** Initialize after construct*/
  private final WorkerAssigner workerAssigner;
  private final long maxDeltaDays;
  private final long maxWorkerId;
  private final long maxSequence;

  /** Volatile fields caused by nextId() */
  protected long workerId;
  protected LocalDate lastDate;
  protected long sequence;

  public IdGeneratorImpl(ConfigInitializer configInitializer, WorkerAssigner workerAssigner) throws Exception {
    ConfigEntity configEntity = configInitializer.initializeConfig();
    this.dateBits = configEntity.getDateBits();
    this.workerIdBits = configEntity.getWorkerIdBits();
    this.sequenceBits = configEntity.getSequenceBits();
    this.epochDate = configEntity.getEpochDate();
    Assert.isTrue(dateBits > 0, "allocate dateBits must greater than 0");
    Assert.isTrue(dateBits <= MAX_DATE_BITS, "allocate dateBits must less than " + MAX_DATE_BITS);
    Assert.isTrue(workerIdBits > 0, "allocate workerIdBits must greater than 0");
    Assert.isTrue(sequenceBits > 0, "allocate sequenceBits must greater than 0");

    int allocateTotalBits = signBits + dateBits + workerIdBits + sequenceBits;
    Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

    this.workerAssigner = workerAssigner;
    // initialize max value
    this.maxDeltaDays = ~(-1L << dateBits);
    this.maxWorkerId = ~(-1L << workerIdBits);
    this.maxSequence = ~(-1L << sequenceBits);

    getNewWorker();
  }


  @Override
  public long genId() throws IdGeneratorException {
    try {
      return nextId();
    } catch (Exception e) {
      log.error("Generate unique id exception. ", e);
      throw new IdGeneratorException(e);
    }
  }

  private synchronized long nextId() throws Exception {
    LocalDate today = LocalDate.now();
    if (lastDate.compareTo(today) < 0) {
      getNewWorker();
    }

    // Exceed the max sequence, get new worker id
    if (sequence + 1 > maxSequence) {
      getNewWorker();
    }

    sequence = sequence + 1;

    return (DateUtils.dayDiff(epochDate, lastDate)) << (workerIdBits + sequenceBits)
        | (workerId << sequenceBits) | sequence;
  }

  private void getNewWorker() throws Exception {
    WorkerEntity worker = workerAssigner.assignWorker(maxWorkerId);
    if (worker == null) {
      throw new IdGeneratorException("Can not get new worker id, null!");
    }

    if (worker.getWorkerId() > maxWorkerId) {
      throw new IdGeneratorException("Worker is exhausted!");
    }

    if (DateUtils.dayDiff(epochDate, worker.getLastDate()) > maxDeltaDays) {
      throw new IdGeneratorException("Days is exhausted. Refusing UID generate");
    }

    workerId = worker.getWorkerId();
    lastDate = worker.getLastDate();
    sequence = -1;
  }

  @Override
  public String parseId(long id) {
    // parse UID
    long sequence = (id << (TOTAL_BITS - sequenceBits)) >>> (TOTAL_BITS - sequenceBits);
    long workerId = (id << (dateBits + signBits)) >>> (TOTAL_BITS - workerIdBits);
    int deltaDays = (int) (id >>> (workerIdBits + sequenceBits));

    LocalDate thatDate = epochDate.plusDays(deltaDays);

    String thatDateStr = DateUtils.formatByDatePattern(thatDate);

    // format as string
    return String.format("{\"id\":\"%d\",\"lastDate\":\"%s\",\"workerId\":\"%d\",\"sequence\":\"%d\"}",
        id, thatDateStr, workerId, sequence);
  }
}
