package org.smartregister.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.sql.Time;

public class TimingRepeatTimeTypeConverter implements JsonSerializer<Time>, JsonDeserializer<Time> {
	
	@Override
	public Time deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	        throws JsonParseException {
		String time = json.getAsString();

		if (!StringUtils.isEmpty(time)) {
			return Time.valueOf(time);
		}

		return null;
	}
	
	@Override
	public JsonElement serialize(Time src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}
}
