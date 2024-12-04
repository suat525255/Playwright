package com.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * reads the properties file configuration.properties
 */
public class ConfigurationReader {

    static Properties properties;

    static {

        try(FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/configuration.properties")){

            properties = new Properties();
            properties.load(fileInputStream);

        } catch (IOException e) {
            System.out.println("error while reading proporties file");;
        }
    }

    public static String get(String keyName) {
        return properties.getProperty(keyName);
    }

}