package com.example.onekids_project.request.evaluatekids;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvaluateDateApprovedRequest extends IdRequest {
    private boolean approved;

    @Override
    public String toString() {
        return "EvaluateDateApprovedRequest{" +
                "approved=" + approved +
                "} " + super.toString();
    }
}
