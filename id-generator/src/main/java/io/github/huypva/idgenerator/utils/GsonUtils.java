package io.github.huypva.idgenerator.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author huypva
 */
public class GsonUtils {

  private static final Gson GSON = new GsonBuilder().create();

  private GsonUtils() {}

  public static <T> T fromJson(String json, Class<T> clazz) {
    return GSON.fromJson(json, clazz);
  }

  public static <T> T fromJson(String json, TypeToken<T> type) {
    return GSON.fromJson(json, type.getType());
  }

  public static String toJson(Object obj) {
    return GSON.toJson(obj);
  }


}
