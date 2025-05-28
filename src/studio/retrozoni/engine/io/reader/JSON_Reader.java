package studio.retrozoni.engine.io.reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import studio.retrozoni.engine.io.Filer;

import java.io.*;
import java.net.URL;
import java.util.List;

public class JSON_Reader extends Filer {

	private JSONObject Json;
	private JSONParser parse;
	private FileReader reader;
	
	public JSON_Reader(String fileName) {
		super(fileName);
		parse = new JSONParser();
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Json = (JSONObject) parse.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public JSON_Reader(File filename) {
		super(filename);
		parse = new JSONParser();
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			Json = (JSONObject) parse.parse(reader);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public JSON_Reader(URL url) {
		super(url);
		InputStream istream = null;
		try {
			istream = url.openStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Reader isr = new InputStreamReader(istream);
		parse = new JSONParser();
		try {
			Json = (JSONObject) parse.parse(isr);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public int Length() {
		return Json.size();
	}
	
	public Object get(String key) {
		Object value = Json.get(key);
		if(value instanceof Long)
			return (int)(long)Json.get(key);
		if(value instanceof String)
			return (String)Json.get(key);
		if(value instanceof Boolean)
			return (boolean)Json.get(key);
		if(value instanceof Double)
			return (double)Json.get(key);
		if(value instanceof List)
			return (Object)Json.get(key);
		if(value instanceof JSONObject)
			return (JSONObject)Json.get(key);
		throw new RuntimeException("Value not exist");
	}
	
	public List<?> getList(String key) {
		Object value = Json.get(key);
		if(value instanceof List)
			return (List<?>)Json.get(key);
		throw new RuntimeException("Value not exist");
	}
	
}
