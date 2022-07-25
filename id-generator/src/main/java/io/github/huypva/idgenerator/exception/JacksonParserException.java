package io.github.huypva.idgenerator.exception;

/**
 * @author huypva
 */
public class JacksonParserException extends RuntimeException {

  public JacksonParserException() {
    super();
  }

  public JacksonParserException(String message) {
    super(message);
  }

  public JacksonParserException(Throwable cause) {
    super(cause);
  }

  public JacksonParserException(String message, Throwable cause) {
    super(message, cause);
  }
}
