/**
 * 
 */
package org.smartregister.utils;

import java.lang.reflect.Type;

import org.smartregister.domain.Task.TaskPriority;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Samuel Githengi created on 11/12/20
 */
public class PriorityOrdinalConverter implements JsonSerializer<TaskPriority>, JsonDeserializer<TaskPriority> {
	
	@Override
	public TaskPriority deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
	        throws JsonParseException {
		try {
			int ordinal = json.getAsInt();
			if (ordinal < TaskPriority.values().length)
				return TaskPriority.values()[ordinal];
		}
		catch (ClassCastException e) {
			//This is not a numeric try using enum name
		}
		return TaskPriority.get(json.getAsString());
	}
	
	@Override
	public JsonElement serialize(TaskPriority src, Type typeOfSrc, JsonSerializationContext context) {
		return new JsonPrimitive(src.ordinal());
	}
	
}
