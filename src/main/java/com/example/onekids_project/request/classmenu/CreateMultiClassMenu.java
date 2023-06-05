package com.example.onekids_project.request.classmenu;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateMultiClassMenu {
    private List<CreateTabAllClassMenu> createTabAllClassMenu;
    private List<String> weekClassMenu;
    private List<Long> listIdClass;
}
