package com.example.onekids_project.request.AppSend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class CreateSendParentBirthdayRequest {

   @NotBlank
   private String sendContent;

   private String attachPicture;

   private String urlPicture;

   private String urlPictureLocal;

   private Long idKid;

   private List<Long> idKidList;

   @Override
   public String toString() {
      return "CreateSendParentBirthdayRequest{" +
              "sendContent='" + sendContent + '\'' +
              ", attachPicture='" + attachPicture + '\'' +
              ", urlPicture='" + urlPicture + '\'' +
              ", urlPictureLocal='" + urlPictureLocal + '\'' +
              ", idKid=" + idKid +
              ", idKidList=" + idKidList +
              '}';
   }
}
