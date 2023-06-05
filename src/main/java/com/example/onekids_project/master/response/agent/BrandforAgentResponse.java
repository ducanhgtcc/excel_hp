package com.example.onekids_project.master.response.agent;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandforAgentResponse extends IdResponse {

    private String brandName;

    private boolean used;
}
