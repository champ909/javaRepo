package techit.util;

import java.io.StringReader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

public class GsonUtil {

	private static Gson gson = new Gson();

	public static Gson getInstance() {
		return gson;
	}
	
	public static <T extends Object> T fromJson(String jsonString, Class<T> entity) {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new StringReader(jsonString));
		reader.setLenient(true);
		return gson.fromJson(reader, entity);
	}
}
