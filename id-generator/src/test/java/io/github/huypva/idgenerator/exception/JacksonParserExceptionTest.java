package io.github.huypva.idgenerator.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
class JacksonParserExceptionTest {

  @Test
  public void testConstructorEmpty() {
    Assertions.assertDoesNotThrow(() -> new JacksonParserException());
  }

  @Test
  public void testConstructorWithMessage() {
    JacksonParserException jacksonParserException = new JacksonParserException("Test message");
    Assertions.assertEquals("Test message", jacksonParserException.getMessage());
  }

  @Test
  public void testConstructorWithThrowable() {
    Exception e = new Exception();
    JacksonParserException jacksonParserException = new JacksonParserException(e);
    Assertions.assertEquals(e, jacksonParserException.getCause());
  }

  @Test
  public void testConstructorWithThrowableAndMessage() {
    Exception e = new Exception();
    JacksonParserException jacksonParserException = new JacksonParserException("Test message", e);
    Assertions.assertEquals("Test message", jacksonParserException.getMessage());
    Assertions.assertEquals(e, jacksonParserException.getCause());
  }

}