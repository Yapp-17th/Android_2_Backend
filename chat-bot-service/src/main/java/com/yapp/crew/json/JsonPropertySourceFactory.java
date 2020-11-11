package com.yapp.crew.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

public class JsonPropertySourceFactory implements PropertySourceFactory {

	@Autowired
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		Map readValue = objectMapper.readValue(resource.getInputStream(), Map.class);
		return new MapPropertySource("json-property", readValue);
	}
}
