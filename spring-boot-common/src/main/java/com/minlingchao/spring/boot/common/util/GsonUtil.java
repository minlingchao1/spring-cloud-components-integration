package com.minlingchao.spring.boot.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @author minlingchao
 * @version V1.0
 * @Description: json 工具类
 * @date 2018/10/16 4:40 PM
 */
public class GsonUtil {

  public static Gson gson(){
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(new com.google.common.reflect.TypeToken<Map<String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
    gsonBuilder.disableHtmlEscaping();
    Gson gson = gsonBuilder.create();
    return gson;
  }

  /**
   * pojo转换为json字符串
   */
  public static String toJson(Object object) {
    return gson().toJson(object);
  }


  /**
   * json转换为pojo
   */
  public static <T> T fromJson(String json, Class<T> clazz) {
    return gson().fromJson(json, clazz);
  }

  /**
   * json转为list
   */
  public static <T> List<T> listFromJson(String json, Class<T> clazz) {
    Type type = new ParameterizedTypeImpl(clazz);
    return gson().fromJson(json, type);
  }


  /**
   * json 转list map
   */
  public static <T> List<Map<String, Object>> listMapFromJson(String json) {
    return gson().fromJson(json, new TypeToken<List<Map<String, Object>>>() {
    }.getType());
  }


  /**
   * json 转map
   */
  public static <T> Map<String, Object> mapFromJson(String json) {
    return gson().fromJson(json, new TypeToken<Map<String, Object>>() {
    }.getType());
  }


  public static class ParameterizedTypeImpl implements ParameterizedType {
    Class clazz;
    public ParameterizedTypeImpl(Class clz) {
      clazz = clz;
    }

    @Override
    public Type[] getActualTypeArguments() {
      return new Type[]{clazz};
    }

    @Override
    public Type getRawType() {
      return List.class;
    }

    @Override
    public Type getOwnerType() {
      return null;
    }
  }
}
