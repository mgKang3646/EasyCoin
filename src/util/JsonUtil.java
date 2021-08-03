package util;

import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonUtil {
	
	public StringWriter sendLocalhost(String localhost) { // 메소드 이름 수정해야됨
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("localhost",localhost);
		return getStringWriter(job.build());
	}
	
	private StringWriter getStringWriter(JsonObject obj) {
		StringWriter sw = new StringWriter();
		Json.createWriter(sw).writeObject(obj);
		return sw;
	}

}
