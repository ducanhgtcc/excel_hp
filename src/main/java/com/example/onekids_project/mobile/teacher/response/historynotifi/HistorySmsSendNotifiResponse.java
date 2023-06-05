package com.example.onekids_project.mobile.teacher.response.historynotifi;

import com.example.onekids_project.mobile.response.ReplyMobileDateObject;
import com.example.onekids_project.response.base.IdResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistorySmsSendNotifiResponse extends IdResponse {

   private String name;

   private String phone;

   private String type;

   private String className;

   private String sendContent;

   private  boolean fail;

   private int numberSend;
}
