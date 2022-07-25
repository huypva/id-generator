package io.github.huypva.idgenerator.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.reflect.TypeToken;
import io.github.huypva.idgenerator.utils.JacksonUtilsTest.TestObject;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author huypva
 */
class GsonUtilsTest {

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
    TestObject testObject = GsonUtils.fromJson(jsonString, TestObject.class);

    Assertions.assertEquals(1, testObject.getId());
    Assertions.assertEquals("sample data", testObject.getData());
  }

  @Test
  void testParseCollectionJson() {
    String jsonData = "[{\"id\":1,\"data\":\"data 1\"},{\"id\":2,\"data\":\"data 2\"}]";

    List<TestObject> objectList = GsonUtils.fromJson(jsonData, new TypeToken<List<TestObject>>(){});
    Assertions.assertEquals(2, objectList.size());
    Assertions.assertEquals(1, objectList.get(0).getId());
    Assertions.assertEquals("data 2", objectList.get(1).getData());
  }

  @Test
  void toJson() {
    TestObject testObject = new TestObject(1, "sample data");

    Assertions.assertEquals("{\"id\":1,\"data\":\"sample data\"}", GsonUtils.toJson(testObject));
  }
}