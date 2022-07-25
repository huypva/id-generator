package io.github.huypva.idgenerator;

import io.github.huypva.idgenerator.exception.IdGeneratorException;

/**
 * @author huypva
 */
public interface IdGenerator {

  long genId() throws IdGeneratorException;

  String parseId(long id);
}
