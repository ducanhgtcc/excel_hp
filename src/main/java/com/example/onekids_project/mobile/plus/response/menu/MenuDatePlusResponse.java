package com.example.onekids_project.mobile.plus.response.menu;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuDatePlusResponse {
    private String nameMeal;

    private String time;
    
    private List<String> foodList;
}
