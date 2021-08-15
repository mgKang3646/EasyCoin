package json;

import java.io.BufferedReader;

import javax.json.JsonObject;

public interface JsonReceive {
	public JsonObject getJsonObject(BufferedReader br);
	public void processJsonQuery(JsonObject object);
}
