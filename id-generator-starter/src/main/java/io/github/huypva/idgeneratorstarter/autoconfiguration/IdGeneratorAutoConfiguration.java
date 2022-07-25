package io.github.huypva.idgeneratorstarter.autoconfiguration;

import com.zaxxer.hikari.HikariDataSource;
import io.github.huypva.idgenerator.IdGenerator;
import io.github.huypva.idgenerator.IdGeneratorImpl;
import io.github.huypva.idgenerator.config.ConfigInitializer;
import io.github.huypva.idgenerator.config.ConfigInitializerImpl;
import io.github.huypva.idgenerator.worker.WorkerAssigner;
import io.github.huypva.idgenerator.worker.WorkerAssignerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huypva
 */
@Slf4j
@Configuration
@ConditionalOnClass(IdGeneratorProperties.class)
@EnableConfigurationProperties(IdGeneratorProperties.class)
public class IdGeneratorAutoConfiguration {

  @ConditionalOnMissingBean
  @Bean
  IdGenerator idGenerator(IdGeneratorProperties props) throws Exception {
    HikariDataSource dataSource = new HikariDataSource(props.getDatasource());
    ConfigInitializer configInitializer = new ConfigInitializerImpl(dataSource);
    WorkerAssigner workerAssigner = new WorkerAssignerImpl(dataSource);
    return new IdGeneratorImpl(configInitializer, workerAssigner);
  }

}
