package ru.clevertec.knyazev.util;

import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class YAMLParser {
	private final Map<String, Object> yamlProperties;
	
	public YAMLParser(String propertyFile) {
		Yaml yaml = new Yaml();
		InputStream fileSettingsStream = this.getClass().getClassLoader().getResourceAsStream(propertyFile);
		yamlProperties =  yaml.load(fileSettingsStream);
	}
	
	@SuppressWarnings("unchecked")
	public String getProperty(String propertyObject, String propertyName) {
		Map<String, Object> objectProperties = (Map<String, Object>) yamlProperties.get(propertyObject);
        return String.valueOf(objectProperties.get(propertyName));
	}
	
	@SuppressWarnings("unchecked")
	public String getProperty(String propertyObject, String propertySubObject, String propertyName) {
		Map<Object, Object> objectProperties = (Map<Object, Object>) yamlProperties.get(propertyObject);
		Map<Object, Object> subObjectProperties = (Map<Object, Object>) objectProperties.get(propertySubObject);
        return String.valueOf(subObjectProperties.get(propertyName));
	}
}
