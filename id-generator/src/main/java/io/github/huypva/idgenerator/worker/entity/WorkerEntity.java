package io.github.huypva.idgenerator.worker.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Entity for Worker
 *
 * @author huypva
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkerEntity {

  /**
   * Last date
   */
  private LocalDate lastDate;

  /**
   * worker id
   */
  private long workerId;

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
