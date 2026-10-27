package com.qa.framework.utils;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class TestDataLoader {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private TestDataLoader() {
	}

	public static <T> T load(String fileName, Class<T> type) {
		String path = "testdata/" + fileName;
		try (InputStream in = TestDataLoader.class.getClassLoader().getResourceAsStream(path)) {
			if (in == null) {
				throw new IllegalArgumentException("Test data file not found on classpath: " + path);
			}
			return MAPPER.readValue(in, type);
		} catch (IOException e) {
			throw new RuntimeException("Could not read test data file: " + path, e);
		}
	}
}