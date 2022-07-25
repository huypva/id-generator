package io.github.huypva.idgenerator.utils;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
class DateUtilsTest {

  @Test
  void formatByDatePatternTest() {
    LocalDate localDate = LocalDate.of(2022, 01, 01);
    Assertions.assertEquals("2022-01-01", DateUtils.formatByDatePattern(localDate));
  }

  @Test
  void dayDiffTest() {
    LocalDate day = LocalDate.of(2022, 01, 01);
    LocalDate nextDay = LocalDate.of(2022, 01, 02);
    Assertions.assertEquals(1, DateUtils.dayDiff(day, nextDay));
  }
}