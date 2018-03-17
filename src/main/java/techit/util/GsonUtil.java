package techit.util;

import com.google.gson.Gson;

public class GsonUtil {

	private static Gson gson = new Gson();

	public static Gson getInstance() {
		return gson;
	}
}
