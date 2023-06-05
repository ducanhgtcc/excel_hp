package com.example.onekids_project.mobile.plus.response.evaluate;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.response.ReplyTypeEditObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EvaluateKidPlusResponse extends IdResponse {
    private String name;

    private boolean approved;

    private String content;

    //true->có phản hồi phụ huynh(có nội dung) và chưa đọc, false là các TH còn lại
    private boolean readStatus;

    //true là cùng lớp, false là khác lớp
    private boolean sameClass = AppConstant.APP_TRUE;

    private List<AttachFileMobileResponse> fileList;

    private List<ReplyTypeEditObject> replyList;
}
