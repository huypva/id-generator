package io.github.huypva.idgenerator.config.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Entity for Config
 *
 * @author huypva
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigEntity {

  private int dateBits;
  private int workerIdBits;
  private int sequenceBits;
  private LocalDate epochDate;
}
