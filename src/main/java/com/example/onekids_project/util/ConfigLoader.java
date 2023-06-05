package com.example.onekids_project.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties=null;
    private static ConfigLoader instance=null;
    String proFileName="config.properties";
    private ConfigLoader() {
        InputStream inputStream= getClass().getClassLoader().getResourceAsStream(proFileName);//Đọc file config.properties
        if(inputStream!=null) {
            properties= new Properties();
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static ConfigLoader getInstance() {
        if(instance==null) {
            instance= new ConfigLoader();
        }
        return instance;
    }

    //Hàm đọc các file properties
    public String getValue(String key) {
        if(properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return null;
    }
}
