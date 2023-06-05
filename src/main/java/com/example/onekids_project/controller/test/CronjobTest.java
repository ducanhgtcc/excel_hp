//package com.example.onekids_project.controller.test;
//
//import com.example.onekids_project.entity.DemoCron;
//import com.example.onekids_project.entity.school.CelebrateSample;
//import com.example.onekids_project.integration.dto.SmsResultDTO;
//import com.example.onekids_project.repository.CelebrateSampleRepository;
//import com.example.onekids_project.repository.DemoCronRepository;
//import com.example.onekids_project.response.common.DataResponse;
//import com.example.onekids_project.response.common.NewDataResponse;
//import com.example.onekids_project.security.model.CurrentUser;
//import com.example.onekids_project.security.model.UserPrincipal;
//import com.example.onekids_project.service.servicecustom.sms.SmsService;
//import lombok.Getter;
//import lombok.Setter;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.ss.formula.functions.T;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//
///**
// * date 2021-06-23 11:49 AM
// *
// * @author nguyễn văn thụ
// */
//@RestController
//@RequestMapping("/test/cron")
//public class CronjobTest {
//
//    @Autowired
//    private DemoCronRepository demoCronRepository;
//    @Autowired
//    private CelebrateSampleRepository celebrateSampleRepository;
//
//    @RequestMapping(method = RequestMethod.PUT)
//    public ResponseEntity sendSms() {
//        StringBuilder exCron = new StringBuilder();
//        List<CelebrateSample> celebrateSampleList = celebrateSampleRepository.findAll();
//
//        celebrateSampleList.forEach(x->{
//            exCron.append("0/5 * * ").append(x.getDate()).append(" ").append(x.getMonth()).append(" ?* |");
//        });
//        DemoCron demoCronOld = demoCronRepository.findById(3L).orElseThrow();
//        demoCronOld.setCronjob(exCron.toString());
//        demoCronOld.setName("Test1");
//        demoCronRepository.save(demoCronOld);
//        return NewDataResponse.setDataCustom(demoCronOld, "123");
//    }
//}

