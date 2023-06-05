package com.example.onekids_project.response.celebrate;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CelebrateSampleResponse extends IdResponse {
    private String type;

    private String title;

    private String content;

    private String gender;

    private String date;

    private String month;

    private String urlPicture;

    private boolean smsSend;

    private boolean appSend;

    private boolean active;

}
