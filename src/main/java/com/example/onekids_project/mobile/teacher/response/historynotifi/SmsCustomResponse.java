package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsCustomResponse extends IdResponse {

   private String name;

   private String phone;

   private String sendContent;
   
   private String type;

   private String student;

   private String status;
}
