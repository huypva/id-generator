package io.github.huypva.idgenerator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import io.github.huypva.idgenerator.exception.JacksonParserException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
@Slf4j
class JacksonUtilsTest {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TestObject {
    private int id;
    private String data;
  }

  @Test
  void testFromJson() {
    String jsonString = "{\"id\":1,\"data\":\"sample data\"}";
    TestObject testObject = JacksonUtils.fromJson(jsonString, TestObject.class);

    Assertions.assertEquals(1, testObject.getId());
    Assertions.assertEquals("sample data", testObject.getData());
  }

  @Test
  void testParseCollectionJson() {
    String jsonString = "[{\"id\":1,\"data\":\"data 1\"},{\"id\":2,\"data\":\"data 2\"}]";

    List<TestObject> objectList = JacksonUtils.fromJson(jsonString, new TypeReference<List<TestObject>>(){});
    Assertions.assertEquals(2, objectList.size());
    Assertions.assertEquals(1, objectList.get(0).getId());
    Assertions.assertEquals("data 2", objectList.get(1).getData());
  }

  @Test
  void testToJson() {
    TestObject testObject = new TestObject(1, "sample data");

    Assertions.assertEquals("{\"id\":1,\"data\":\"sample data\"}", JacksonUtils.toJson(testObject));
  }

  @Test
  void testGetField() {
    String jsonString = "{\"id\":1,\"data\":\"sample data\"}";

    Assertions.assertEquals(1, JacksonUtils.getField(jsonString,"id" ).asInt());
    Assertions.assertEquals("sample data", JacksonUtils.getField(jsonString,"data" ).asText());
  }

  @Test
  void testThrowException() {
    String jsonString = "{\"uid\":1,\"data\":\"sample data\"}";
    Assertions.assertThrows(
        JacksonParserException.class, () -> JacksonUtils.fromJson(jsonString, TestObject.class));

    String jsonString2 = "[{\"uid\":1,\"data\":\"data 1\"},{\"uid\":2,\"data\":\"data 2\"}]";
    Assertions.assertThrows(JacksonParserException.class, () -> JacksonUtils.fromJson(jsonString2, new TypeReference<List<TestObject>>(){}));

    String jsonString3 = "{\"uid\":1\"data\":\"sample data\"}";
    Assertions.assertThrows(JacksonParserException.class, () -> JacksonUtils.getField(jsonString3, "id"));
  }

}