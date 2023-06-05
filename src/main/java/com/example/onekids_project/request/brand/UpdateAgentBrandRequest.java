package com.example.onekids_project.request.brand;

import com.example.onekids_project.request.base.IdRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAgentBrandRequest extends IdRequest {

    private String agentName;

    private boolean agentActive;

    private String brandName;

}
