package com.example.onekids_project.request.parentdiary;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMessageRequest extends IdRequest {
    private String schoolReply;

    @Override
    public String toString() {
        return "CreateMessageRequest{" +
                "schoolReply='" + schoolReply + '\'' +
                "} " + super.toString();
    }
}
