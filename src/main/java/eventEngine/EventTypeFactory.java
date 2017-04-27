package eventEngine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import eventEngine.Event.Priority;
import eventTypes.CompoundChecker;
import eventTypes.RegexChecker;
import logParser.RegexHelper;

public class EventTypeFactory {

	private static final Logger logger = Logger.getLogger(EventTypeFactory.class.getName());

	HashMap<String,EventTypeInterface> eventTypeMap;
	
	private static boolean stringCompare(String str1, String str2)
	{
		  return (str1 == null ? str2 == null : str1.equals(str2));
	}
	
	
	
	
	public EventTypeFactory() {
		
	}

	public EventTypeFactory(HashMap<String,EventTypeInterface> eventTypeMap) {
		this.eventTypeMap = eventTypeMap;
		
	}

	static private class RegexHelperDeserializer implements JsonDeserializer<RegexHelper> {
		public RegexHelper deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			return new RegexHelper(json.getAsJsonPrimitive().getAsString());
		}
	}

	static private class EventPriorityDeserializer implements JsonDeserializer<Event.Priority> {
		public Event.Priority deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {

			// logger.log(Level.INFO,"Cannot map
			// priority:"+json.getAsJsonPrimitive().getAsString().toLowerCase());
			logger.log(Level.FINEST, "Trying to map priority:" + json.getAsJsonPrimitive().getAsString().toLowerCase());

			switch (json.getAsJsonPrimitive().getAsString().toLowerCase()) {

			case "high":
				return Event.Priority.HIGH;
			case "medium":
				return Event.Priority.MEDIUM;
			case "low":
				return Event.Priority.LOW;
			case "info":
				return Event.Priority.INFO;
			default:
				logger.log(Level.WARNING,
						"Cannot map priority:" + json.getAsJsonPrimitive().getAsString().toLowerCase());

			}
			return Event.Priority.INFO;
		}
	}

	private void registerSimpleCheckers(SimpleEventDataModel[] events) {
	for (SimpleEventDataModel config : events) {

		if (stringCompare(config.disable,"true")) {
			continue;
		}

		if (config.eventType != null && config.eventType.contentEquals("regex")) {
			logger.log(Level.INFO,"Added checker:" + config.eventName);
			eventTypeMap.put(config.eventName,new RegexChecker(config));
		}

	}
}
	
	public void generateCheckers(String checkerFileName) {

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(checkerFileName), StandardCharsets.UTF_8));
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(RegexHelper.class, new RegexHelperDeserializer());
			gsonBuilder.registerTypeAdapter(Event.Priority.class, new EventPriorityDeserializer());
			// Get the config from JSON into data model objects

			// Iterate through the data and make the checkers.
			// Done this way to keep the de-serialization of json simple, use
			// the type parameter to make the correct
			// object, could probably be done a bit cleverer but this will do
			// for now.
			Gson gson = gsonBuilder.create();

			TopEventDataModel model = gson.fromJson(br, TopEventDataModel.class);
			assert(model!=null);
			
			br.close();
			
			registerSimpleCheckers(model.events);
			
			
			for (CompoundEventDataModel config : model.checkers) {
				config.validateEvents(eventTypeMap);

				if (config.disable != null && config.disable.contentEquals("true")) {
					continue;
				}

				logger.log(Level.INFO, "Added checker:" + config.checkerName);
				eventTypeMap.put(config.checkerName,new CompoundChecker(config));
			}

		} catch (IOException e) {
			// do nothing at the moment

		}

	}

}
