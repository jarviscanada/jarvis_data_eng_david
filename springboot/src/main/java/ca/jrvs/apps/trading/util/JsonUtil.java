package ca.jrvs.apps.trading.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

  /**
   * Converts json String to JSONObject
   *
   * @param json String to convert to JSONObject
   * @return JSONObject
   */
  public static JSONObject generateJsonObject(String json) throws JSONException {
    JSONObject jsonObject = null;
    jsonObject = new JSONObject(json);
    return jsonObject;
  }

  /**
   * Convert JSONObject to list of Json POJO objects of type T
   *
   * @param jsonObject to convert to list of POJO
   * @param clazz      typing of List
   * @param rootName   is name of JSON root to ignore, eg: for quote table use quote to ignore
   *                   header
   * @return List of Json POJO  objects
   * @throws IOException
   */
  public static <T> List<T> parseBatchJson(JSONObject jsonObject, Class<T> clazz, String rootName)
      throws IOException {
    T testObject = null;
    ObjectMapper mapper = new ObjectMapper();
    List<T> jsonList = new ArrayList<T>();

    mapper.configure(
        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Iterator<String> keys = jsonObject.keys();
    while (keys.hasNext()) {
      String key = keys.next();
      if (jsonObject.get(key) instanceof JSONObject) {
        ObjectReader reader = mapper.reader(clazz).withRootName(rootName);
        testObject = reader.readValue((jsonObject.get(key)).toString());
        jsonList.add(testObject);
      }
    }
    return jsonList;
  }

}
