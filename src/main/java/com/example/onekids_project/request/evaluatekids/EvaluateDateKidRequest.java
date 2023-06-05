package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EvaluateDateKidRequest extends IdRequest {
    private String learnContent;

    private String eatContent;

    private String sleepContent;

    private String sanitaryContent;

    private String healtContent;

    private String commonContent;

    private List<MultipartFile> multipartFileList;

    private List<Long> fileDeleteList;

//    private boolean schoolReadReply;

    private boolean teacherReplyDel;

    private boolean schoolReplyDel;

    private String schoolReplyContent;

    private String teacherReplyContent;

}
