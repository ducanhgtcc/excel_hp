package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateDateRequest extends IdRequest {
    private boolean isApproved;

    private EvaluateLearnRequest evaluateLearn;

    private EvaluateEatRequest evaluateEat;

    private EvaluateSleepRequest evaluateSleep;

    private EvaluateSanitaryRequest evaluateSanitary;

    private EvaluateHealtRequest evaluateHealt;

    private EvaluateCommonRequest evaluateCommon;

    private EvaluateAttachFileRequest evaluateAttachFile;

    private EvaluateParentReplyRequest evaluateParentReply;

    private EvaluateSchoolReplyRequest evaluateSchoolReply;

    private EvaluateTeacherReplyRequest evaluateTeacherReply;

}
