package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.mobile.plus.request.sms.SendAccountRequest;
import com.example.onekids_project.mobile.plus.request.sms.SendSmsFailRequest;
import com.example.onekids_project.mobile.plus.request.sms.SendSmsRequest;
import com.example.onekids_project.mobile.plus.service.servicecustom.SmsPlusService;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.SmsSendRepository;
import com.example.onekids_project.request.base.IdRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.SmsSendCustomService;
import com.example.onekids_project.service.servicecustom.SmsSendService;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class SmsPlusServiceImpl implements SmsPlusService {

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private SmsSendCustomService smsSendCustomService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Transactional
    @Override
    public boolean sendAccountStudentGrade(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsGrade(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendCustomService.sendAccountStudentSms(principal, kidsList);
    }

    @Transactional
    @Override
    public boolean sendSmsStudentGrade(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsGrade(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendService.sendStudentSms(principal, kidsList, request.getContent());
    }

    @Transactional
    @Override
    public boolean sendAccountStudentClass(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsClass(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendCustomService.sendAccountStudentSms(principal, kidsList);
    }

    @Transactional
    @Override
    public boolean sendSmsStudentClass(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsClass(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendService.sendStudentSms(principal, kidsList, request.getContent());
    }

    @Transactional
    @Override
    public boolean sendAccountStudentMulti(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKids(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendCustomService.sendAccountStudentSms(principal, kidsList);
    }

    @Transactional
    @Override
    public boolean sendSmsStudentMulti(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKids(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendService.sendStudentSms(principal, kidsList, request.getContent());
    }

    @Transactional
    @Override
    public boolean sendSmsEmployeeDeparment(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findInfoEmployeeDeparmentList(principal.getIdSchoolLogin(), request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getEmployee() != null).collect(Collectors.toList());
        return smsSendService.sendEmployeeSms(principal, infoEmployeeSchoolList, request.getContent());
    }

    @Transactional
    @Override
    public boolean sendSmsEmployeeMulti(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findInfoEmployeeSchool(principal.getIdSchoolLogin(), request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getEmployee() != null).collect(Collectors.toList());
        return smsSendService.sendEmployeeSms(principal, infoEmployeeSchoolList, request.getContent());
    }

    @Transactional
    @Override
    public boolean sendSmsFail(UserPrincipal principal, SendSmsFailRequest request) {
        CommonValidate.checkDataPlus(principal);
        SmsSend smsSend = smsSendRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        //todo check send type
        return false;
    }

    @Transactional
    @Override
    public boolean sendAccountEmployeeMulti(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findInfoEmployeeSchool(principal.getIdSchoolLogin(), request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getEmployee() != null).collect(Collectors.toList());
        return smsSendCustomService.sendAccountEmployeeSms(principal, infoEmployeeSchoolList);
    }

    @Transactional
    @Override
    public boolean sendAccountStudentGroup(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsGroup(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendCustomService.sendAccountStudentSms(principal, kidsList);
    }

    @Transactional
    @Override
    public boolean sendSmsStudentGroup(UserPrincipal principal, SendSmsRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsGroup(principal, request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getParent() != null).collect(Collectors.toList());
        return smsSendService.sendStudentSms(principal, kidsList, request.getContent());
    }

    @Transactional
    @Override
    public boolean sendAccountStudent(UserPrincipal principal, IdRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        return smsSendCustomService.sendAccountOneStudentSms(principal, kids);
    }

    @Transactional
    @Override
    public boolean sendAccountEmployeeDeparment(UserPrincipal principal, SendAccountRequest request) throws ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findInfoEmployeeDeparmentList(principal.getIdSchoolLogin(), request.getIdList()).stream().filter(x -> x.isSmsReceive() && x.getEmployee() != null).collect(Collectors.toList());
        return smsSendCustomService.sendAccountEmployeeSms(principal, infoEmployeeSchoolList);
    }


}
