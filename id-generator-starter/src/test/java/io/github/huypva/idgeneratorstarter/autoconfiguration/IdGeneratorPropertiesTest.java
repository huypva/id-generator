package io.github.huypva.idgeneratorstarter.autoconfiguration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author huypva
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigDataApplicationContextInitializer.class)
@EnableConfigurationProperties(IdGeneratorProperties.class)
@ActiveProfiles("test")
class IdGeneratorPropertiesTest {

  @Autowired
  IdGeneratorProperties props;

  @Test
  void testProperties() {
    Assertions.assertNotNull(props.getDatasource());
    Assertions.assertEquals("com.mysql.cj.jdbc.Driver", props.getDatasource().getDriverClassName());
    Assertions.assertEquals("jdbc:mysql://127.0.0.1:3306/id_generator", props.getDatasource().getJdbcUrl());
    Assertions.assertEquals("user", props.getDatasource().getUsername());
    Assertions.assertEquals("password", props.getDatasource().getPassword());
  }

}