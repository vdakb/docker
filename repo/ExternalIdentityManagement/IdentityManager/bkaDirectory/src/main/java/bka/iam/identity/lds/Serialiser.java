package bka.iam.identity.lds;

import java.lang.reflect.Type;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * USES:    gson-2.8.5.jar
 * WHERE:   ORACLE_HOME/server/ThirdParty
 */
public class Serialiser {

  private static final Type TYPE_STRING_ARRAY = new TypeToken<String[]>() {
  }.getType();
  private static final int MAX_COLUMN_LENGTH = 4000;

  public static final String serialise(String[] array) {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(array);
  }

  public static final String[] deserialise(String string) {
    Gson gson = new Gson();
    return gson.fromJson(string, TYPE_STRING_ARRAY);
  }

  public static final String[] bulkSplitString(String serialisedJson) {

    List<String> list = new ArrayList<String>();

    int index = 0;
    while (index < serialisedJson.length()) {
      list.add(serialisedJson.substring(index, Math.min(index + MAX_COLUMN_LENGTH, serialisedJson.length())));
      index += MAX_COLUMN_LENGTH;
    }

    return list.toArray(new String[0]);
  }

  public static final String bulkUnsplitString(String[] splitJson) {

    StringBuilder unsplit = new StringBuilder("");

    for (String element : splitJson) {
      unsplit.append(element);
    }

    return unsplit.toString();
  }
}