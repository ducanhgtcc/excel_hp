//package com.example.onekids_project.entity.sms;
//
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * A SmsHistory.
// */
//@Getter
//@Setter
//@Entity
//@Table(name = "sms_history")
//public class SmsHistory implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(length = 2000)
//    private String phoneNumber;
//
//    private String status;
//    private String statusCode;
//
//    @Column(length = 1000)
//    private String content;
//
//    private String sendBy;
//
//    private LocalDateTime sendTime;
//
//    private Long schoolId;
//
//
//}
