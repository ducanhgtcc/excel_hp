package com.example.onekids_project.response.classmenu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileAndPictureMenuResponse {
    private String name;
    private String url;
    private Long idMenuFile;
    private Long idUrlMenuFile;
    private String extensionUrlFile;
}
