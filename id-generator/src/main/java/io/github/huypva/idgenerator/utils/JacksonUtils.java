package io.github.huypva.idgenerator.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.huypva.idgenerator.exception.JacksonParserException;

/**
 * @author huypva
 */
public class JacksonUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static <T> T fromJson(String json, Class<T> clazz) throws JacksonParserException {
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new JacksonParserException(e);
    }
  }

  public static <T> T fromJson(String json, TypeReference<T> type) throws JacksonParserException {
    try {
      return OBJECT_MAPPER.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new JacksonParserException(e);
    }
  }

  public static String toJson(Object obj) throws JacksonParserException {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new JacksonParserException(e);
    }
  }

  public static JsonNode getField(String jsonString, String fieldName)
      throws JacksonParserException {
    try {
      ObjectNode node = new ObjectMapper().readValue(jsonString, ObjectNode.class);
      return node.get(fieldName);
    } catch (JsonProcessingException e) {
      throw new JacksonParserException(e);
    }
  }

}
