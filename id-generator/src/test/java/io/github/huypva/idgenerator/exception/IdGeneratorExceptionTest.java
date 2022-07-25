package io.github.huypva.idgenerator.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
class IdGeneratorExceptionTest {

  @Test
  public void testConstructorEmpty() {
    Assertions.assertDoesNotThrow(() -> new IdGeneratorException());
  }

  @Test
  public void testConstructorWithMessage() {
    IdGeneratorException idGeneratorException = new IdGeneratorException("Test message");
    Assertions.assertEquals("Test message", idGeneratorException.getMessage());
  }

  @Test
  public void testConstructorWithThrowable() {
    Exception e = new Exception();
    IdGeneratorException idGeneratorException = new IdGeneratorException(e);
    Assertions.assertEquals(e, idGeneratorException.getCause());
  }

  @Test
  public void testConstructorWithThrowableAndMessage() {
    Exception e = new Exception();
    IdGeneratorException idGeneratorException = new IdGeneratorException("Test message", e);
    Assertions.assertEquals("Test message", idGeneratorException.getMessage());
    Assertions.assertEquals(e, idGeneratorException.getCause());
  }

  @Test
  public void testConstructorWithStringFormat() {
    String format = "Test %s %d";
    IdGeneratorException idGeneratorException = new IdGeneratorException(format, "message", 1);
    Assertions.assertEquals("Test message 1", idGeneratorException.getMessage());
  }

}