package codedcosmos.cometbot.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CometSave {
	
	private File file;
	private Properties properties;
	private HashMap<String, String> values;
	
	public CometSave(File file) throws IOException {
		// If file doesn't exist recreate
		if (!file.exists() || !file.isFile()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		this.file = file;
		
		// Load properties file
		load();
	}
	
	private void load() throws IOException {
		properties = new Properties();
		FileInputStream inputStream = new FileInputStream(file);
		properties.load(inputStream);
		inputStream.close();
		
		values = new HashMap<String, String>();
		
		for(String key : properties.stringPropertyNames()) {
			String value = properties.getProperty(key);
			values.put(key, value);
		}
	}
	
	public void save() throws IOException {
		// Update
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			properties.setProperty(key, value);
		}
		
		// Store
		FileOutputStream outputStream = new FileOutputStream(file);
		properties.store(outputStream, null);
		outputStream.close();
	}
	
	public void set(String key, String value) {
		values.put(key, value);
	}
	
	public void expect(HashMap<String, String> values) {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			if (!this.values.containsKey(key)) {
				this.values.put(key, value);
			}
		}
	}
	
	public String get(String key) {
		return values.get(key);
	}
	
	public String getOr(String key, String defaultValue) {
		return values.getOrDefault(key, defaultValue);
	}
	
	
	public HashMap<String, String> getValues() {
		return values;
	}
}
