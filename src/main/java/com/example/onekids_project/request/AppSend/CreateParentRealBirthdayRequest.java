package com.example.onekids_project.request.AppSend;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CreateParentRealBirthdayRequest {

    @NotBlank
    private String sendContent;

    private String attachPicture;

    private String urlPicture;

    private String urlPictureLocal;

    private int kidPhone;

    private Long idPeople;

    private List<Long> idPeopleList;

}
