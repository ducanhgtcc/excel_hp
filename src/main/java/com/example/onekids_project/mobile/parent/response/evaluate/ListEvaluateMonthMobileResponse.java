package com.example.onekids_project.mobile.parent.response.evaluate;

import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.ReplyTypeMobileObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ListEvaluateMonthMobileResponse extends IdResponse {
    private String month;

    private String content;

    private List<AttachFileMobileResponse> fileList;

    //false->chưa đọc, true->đã đọc
    private boolean parentRead;

    //true->phụ huynh đã phản hồi, false->phụ huynh chưa phản hồi
    private boolean parentReply;

    //true->Nhà trường hoặc giáo viên đã phản hồi, false là chưa phản hồi
    private boolean teacherOrSchoolReply;

    List<ReplyTypeMobileObject> replyList;

    public ListEvaluateMonthMobileResponse() {
        this.fileList = new ArrayList<>();
        this.replyList = new ArrayList<>();
    }

}
