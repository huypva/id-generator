package io.github.huypva.idgeneratorstarter.autoconfiguration;

import io.github.huypva.idgenerator.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author huypva
 */
@Slf4j
@Import(IdGeneratorTestConfiguration.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class IdGeneratorAutoConfigurationTest {

  @Autowired
  IdGenerator idGenerator;

  @Test
  public void testAutoConfiguration() {
    log.info("Id {}", idGenerator.parseId(idGenerator.genId()));
  }

}