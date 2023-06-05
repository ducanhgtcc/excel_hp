package com.example.onekids_project.request.AppSend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateSendTeacherBirthdayRequest {

   @NotBlank
   private String sendContent;

   private String attachPicture;

   @NotNull
   private Long idEmployee;
}
