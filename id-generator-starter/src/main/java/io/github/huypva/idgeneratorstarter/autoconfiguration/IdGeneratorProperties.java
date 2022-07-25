package io.github.huypva.idgeneratorstarter.autoconfiguration;

import com.zaxxer.hikari.HikariConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author huypva
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties("id-generator")
public class IdGeneratorProperties {

  private HikariConfig datasource;
}
