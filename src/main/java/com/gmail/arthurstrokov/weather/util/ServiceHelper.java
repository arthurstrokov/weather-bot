package com.gmail.arthurstrokov.weather.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Артур Александрович Строков
 * @email astrokov@clevertec.ru
 * @created 05.10.2022
 */

public class ServiceHelper {
    private ServiceHelper() {
    }

    public static String getJsonFromFile(String filename) {
        StringBuilder myJson = new StringBuilder();
        Gson gson = new Gson();
        JsonElement je = null;
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNext()) {
                myJson = new StringBuilder(scanner.useDelimiter("\\Z").next());
            }
            gson = new GsonBuilder().setPrettyPrinting().create();
            je = JsonParser.parseString(String.valueOf(myJson));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return gson.toJson(je);
    }
}
