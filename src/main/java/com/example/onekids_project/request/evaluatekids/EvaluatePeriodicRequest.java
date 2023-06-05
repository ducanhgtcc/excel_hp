package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.request.kids.KidOtherRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluatePeriodicRequest {
    private Long id;

    private boolean isApproved;

    private String content;

    private String urlFileList;

    private KidOtherRequest kids;
}
