package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EvaluateWeekDetailRequest extends IdRequest {
    private String content;

    private List<MultipartFile> multipartFileList;

    private List<Long> fileDeleteList;

    private boolean schoolReadReply;

    private boolean teacherReplyDel;

    private boolean schoolReplyDel;

    private String schoolReplyContent;

    private String teacherReplyContent;
}
