package com.omelianenko.pokemonbattle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class ResourceReader {

    public static List<String> readFromResource(String resourcePath) {
        try (InputStream inputStream = ResourceReader.class.getClassLoader()
            .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new RuntimeException("File not found in resources: " + resourcePath);
            }

            List<String> lines = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .filter(line -> !line.trim().isEmpty())
                .toList();

            if (lines.isEmpty()) {
                throw new RuntimeException("File is empty: " + resourcePath);
            }
            return lines;

        } catch (IOException e) {
            throw new RuntimeException("Read file error: " + resourcePath, e);
        }
    }

}
