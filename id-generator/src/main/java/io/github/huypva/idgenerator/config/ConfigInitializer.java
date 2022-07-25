package io.github.huypva.idgenerator.config;

import io.github.huypva.idgenerator.config.entity.ConfigEntity;

/**
 * @author huypva
 */
public interface ConfigInitializer {

  ConfigEntity initializeConfig() throws Exception;
}
