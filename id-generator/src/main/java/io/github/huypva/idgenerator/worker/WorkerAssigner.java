package io.github.huypva.idgenerator.worker;

import io.github.huypva.idgenerator.worker.entity.WorkerEntity;

/**
 * @author huypva
 */
public interface WorkerAssigner {

  WorkerEntity assignWorker(long maxWorkerId) throws Exception;
}
