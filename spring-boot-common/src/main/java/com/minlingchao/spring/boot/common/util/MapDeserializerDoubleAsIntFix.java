package com.minlingchao.spring.boot.common.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author minlingchao
 * @version V1.0
 * @Description:
 * @date 2018/12/27 6:02 PM
 */
public class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, Object>> {

  @Override
  public Map<String, Object> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    return (Map<String, Object>) read(jsonElement);
  }

  public Object read(JsonElement in) {
    if(in.isJsonArray()){
      List<Object> list = new ArrayList<>();
      JsonArray arr = in.getAsJsonArray();
      for (JsonElement anArr : arr) {
        list.add(read(anArr));
      }
      return list;
    }else if(in.isJsonObject()){
      Map<String, Object> map = new LinkedTreeMap<String, Object>();
      JsonObject obj = in.getAsJsonObject();
      Set<Entry<String, JsonElement>> entitySet = obj.entrySet();
      for(Entry<String, JsonElement> entry: entitySet){
        map.put(entry.getKey(), read(entry.getValue()));
      }
      return map;
    }else if( in.isJsonPrimitive()){
      JsonPrimitive prim = in.getAsJsonPrimitive();
      if(prim.isBoolean()){
        return prim.getAsBoolean();
      }else if(prim.isString()){
        return prim.getAsString();
      }else if(prim.isNumber()){
        Number num = prim.getAsNumber();
        // here you can handle double int/long values
        // and return any type you want
        // this solution will transform 3.0 float to long values
        if(Math.ceil(num.doubleValue())  == num.longValue()){
          return num.longValue();
        }
        else{
          return num.doubleValue();
        }
      }
    }
    return null;
  }

}
