package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class EvaluateDateKidPlusResponse extends IdResponse {

    private LocalDate date;

    private String learnContent = "";

    private String eatContent = "";

    private String sleepContent = "";

    private String sanitaryContent = "";

    private String healtContent = "";

    private String commonContent = "";

    private boolean approved;

    private List<AttachFileMobileResponse> fileList = new ArrayList<>();

    private List<ReplyTypeEditObject> replyList = new ArrayList<>();
}
