package com.example.onekids_project.util;

import java.util.ArrayList;
import java.util.List;

public class MenuUtils {

    public static List<String> ConvertStringMeal(String nameMeal) {
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        String[] strArray = nameMeal.split("\n");
        for (int i = 0; i < strArray.length; i++) {
            list.add(strArray[i]);
        }
        return list;
    }
}
