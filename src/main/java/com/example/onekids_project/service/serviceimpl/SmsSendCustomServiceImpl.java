package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.SmsCode;
import com.example.onekids_project.entity.user.SmsReceiversCustom;
import com.example.onekids_project.entity.user.SmsSendCustom;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.response.historynotifi.SmsCustomResponse;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.notifihistory.SearchSmsSendCustomRequest;
import com.example.onekids_project.request.smsNotify.SmsCustomRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.notifihistory.ListSmsCustomResponse;
import com.example.onekids_project.response.notifihistory.SmsCustomNewResponse;
import com.example.onekids_project.response.phone.AccountLoginResponse;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.SmsSendCustomService;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import com.example.onekids_project.service.servicecustom.common.PhoneCommonService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import com.example.onekids_project.util.SmsUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Service
public class SmsSendCustomServiceImpl implements SmsSendCustomService {
    @Autowired
    InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private SmsReceiversCustomRepository smsReceiversCustomRepository;
    @Autowired
    private SmsCodeRepository smsCodeRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private PhoneCommonService phoneCommonService;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private FindSmsService findSmsService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private SmsDataService smsDataService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private AppSendService appSendService;

    @Override
    public ListSmsCustomResponse searchSmsCustom(UserPrincipal principal, SearchSmsSendCustomRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        ListSmsCustomResponse response = new ListSmsCustomResponse();
        List<SmsSendCustom> smsSendCustomList = smsSendCustomRepository.searchSmsCustom(idSchool, request);
        List<SmsCustomNewResponse> responseList = new ArrayList<>();
        smsSendCustomList.forEach(x -> {
            SmsCustomNewResponse model = new SmsCustomNewResponse();
            MaUser maUser = maUserRepository.findById(x.getIdCreated()).orElseThrow();
            if (maUser != null) {
                model.setCreatedBy(maUser.getFullName());
            }
            int coutSuccess;
            int coutFail;
            int coutFail2;
            int coutSms;
            if (x.getSmsReceiversCustomList().size() > 0) {
                List<SmsReceiversCustom> smsReceiversCustomList = smsReceiversCustomRepository.findSmsReceiverCustom(x.getId());
                coutSuccess = (int) smsReceiversCustomList.stream().filter(a -> a.getSmsCode() != null).filter(a -> a.getSmsCode().getId() == 1).count();
                coutFail = (int) smsReceiversCustomList.stream().filter(b -> b.getSmsCode() != null).filter(b -> b.getSmsCode().getId() != 1).count();
                coutFail2 = (int) smsReceiversCustomList.stream().filter(c -> c.getSmsCode() == null).count();
                model.setCoutFail(coutFail + coutFail2);
                model.setCoutSuccess(coutSuccess);
                model.setCoutAll(x.getReceivedCount());
            }
            coutSms = x.getSmsReceiversCustomList().stream().mapToInt(SmsReceiversCustom::getNumberSms).sum();
            model.setCreatedDate(x.getCreatedDate());
            model.setCoutSms(coutSms);
            model.setId(x.getId());
            x.getSmsReceiversCustomList().forEach(y -> model.setSendContent(y.getSendContent()));
            responseList.add(model);
        });
        long total = smsSendCustomRepository.coutSearchSmsCustom(idSchool, request);
        List<SmsCustomNewResponse> historySmsSendNewResponseList = listMapper.mapList(responseList, SmsCustomNewResponse.class);
        response.setTotal(total);
        response.setResponseList(historySmsSendNewResponseList);
        return response;
    }

    @Override
    public List<SmsCustomResponse> findDetailHistory(UserPrincipal principal, Long id) {
        SmsSendCustom smsSendCustom = smsSendCustomRepository.findById(id).orElseThrow();
        Long idSmsSend = smsSendCustom.getId();
        List<SmsReceiversCustom> smsReceiversCustomList = smsReceiversCustomRepository.findByIdSmsSend(idSmsSend);
        List<SmsCustomResponse> responseList = new ArrayList<>();
        smsReceiversCustomList.forEach(x -> {
            SmsCustomResponse model = new SmsCustomResponse();
            model.setId(x.getId());
            model.setPhone(x.getPhoneUserReceiver());
            model.setSendContent(x.getSendContent());
            model.setType(x.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT) ? MessageWebConstant.PARENT : MessageWebConstant.EMPLOYEE);
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                MaClass maClass = maClassRepository.findById(kids.getMaClass().getId()).orElseThrow();
                model.setStudent(kids.getFullName() + " - " + maClass.getClassName());
            }
            model.setName(x.getNameUserReceiver());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<SmsCustomResponse> findDetailHistoryFail(UserPrincipal principal, Long id) {
        SmsSendCustom smsSendCustom = smsSendCustomRepository.findById(id).orElseThrow();
        Long idSmsSend = smsSendCustom.getId();
        List<SmsReceiversCustom> smsReceiversCustomList = smsReceiversCustomRepository.findByIdSmsSendFail(idSmsSend);
        List<SmsCustomResponse> responseList = new ArrayList<>();
        smsReceiversCustomList.forEach(x -> {
            SmsCustomResponse model = new SmsCustomResponse();
            model.setId(x.getId());
            model.setPhone(x.getPhoneUserReceiver());
            model.setSendContent(x.getSendContent());
            model.setType(x.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT) ? MessageWebConstant.PARENT : MessageWebConstant.EMPLOYEE);
            model.setName(x.getNameUserReceiver());
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                MaClass maClass = maClassRepository.findById(kids.getMaClass().getId()).orElseThrow();
                model.setStudent(kids.getFullName() + " - " + maClass.getClassName());
            }
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<SmsCustomResponse> findDetailHistoryAll(UserPrincipal principal, Long id) {
        SmsSendCustom smsSendCustom = smsSendCustomRepository.findById(id).orElseThrow();
        Long idSmsSend = smsSendCustom.getId();
        List<SmsReceiversCustom> smsReceiversCustomList = smsReceiversCustomRepository.findByIdSmsSendAll(idSmsSend);
        List<SmsCustomResponse> responseList = new ArrayList<>();
        smsReceiversCustomList.forEach(x -> {
            SmsCustomResponse model = new SmsCustomResponse();
            model.setId(x.getId());
            model.setPhone(x.getPhoneUserReceiver());
            model.setSendContent(x.getSendContent());
            model.setType(x.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT) ? MessageWebConstant.PARENT : MessageWebConstant.EMPLOYEE);
            if (x.getSmsCode() != null) {
                if (x.getSmsCode().getId() == 1) {
                    model.setStatus(MessageWebConstant.SUCCESS);
                } else {
                    model.setStatus(MessageWebConstant.FAIL);
                }
            } else {
                model.setStatus(MessageWebConstant.FAIL);
            }
            model.setName(x.getNameUserReceiver());
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                MaClass maClass = maClassRepository.findById(kids.getMaClass().getId()).orElseThrow();
                model.setStudent(kids.getFullName() + " - " + maClass.getClassName());
            }
            responseList.add(model);
        });
        return responseList;
    }

    @Transactional
    @Override
    public boolean sendAccountStudentSms(UserPrincipal principal, List<Kids> kidsList) throws ExecutionException, InterruptedException {
        SmsUtils.checkSendSms(infoEmployeeSchoolRepository.findByDelActiveTrueAndSchool_IdAndEmployee_MaUser_Id(principal.getIdSchoolLogin(), principal.getId()));
        if (CollectionUtils.isEmpty(kidsList)) {
            throw new NotFoundException(SmsConstant.NO_KID);
        }
        List<AccountLoginResponse> phoneResponseList = phoneCommonService.findAccountParent(kidsList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(school).orElseThrow(() -> new NotFoundException(SmsConstant.NO_BRAND));
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_ACCOUNT_PARENT).orElseThrow(() -> new NotFoundException("not found webSystemTitle by id"));
        SmsSendCustom smsSendCustom = smsSendCustomRepository.save(this.setPropertiesSmsCustom(webSystemTitle, smsDTO, phoneResponseList.size(), principal.getIdSchoolLogin()));
        for (AccountLoginResponse x : phoneResponseList) {
            String contentSms = SmsUtils.convertContentSendAccount(x, webSystemTitle);
            SmsConvertResponse smsConvertResponse = SmsUtils.convertSms(contentSms);
            int totalSend = smsConvertResponse.getSmsConvert().size();
            if (!SmsUtils.checkSmsBuget(school, contentSms, SmsConstant.ONE)) {
                throw new NotFoundException(SmsConstant.PASS_BUGET);
            }
            Future<SmsResultDTO> smsResult = smsService.sendSmsOne(principal.getIdSchoolLogin(), x.getPhone(), contentSms);
            smsReceiversCustomRepository.save(this.setPropertiesSmsReceiversCustom(smsResult, kidsList, totalSend, smsSendCustom, contentSms));
        }
        return true;
    }

    @Transactional
    @Override
    public boolean sendAccountOneStudentSms(UserPrincipal principal, Kids kids) throws ExecutionException, InterruptedException {
        SmsUtils.checkSendSms(infoEmployeeSchoolRepository.findByDelActiveTrueAndSchool_IdAndEmployee_MaUser_Id(principal.getIdSchoolLogin(), principal.getId()));
        if (kids.getParent() == null) {
            throw new NotFoundException(SmsConstant.NO_KID);
        }
        AccountLoginResponse accountResponse = phoneCommonService.findAccountOneParent(kids);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(school).orElseThrow(() -> new NotFoundException(SmsConstant.NO_BRAND));
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_ACCOUNT_PARENT).orElseThrow(() -> new NotFoundException("not found webSystemTitle by id"));
        SmsSendCustom smsSendCustom = smsSendCustomRepository.save(this.setPropertiesSmsCustom(webSystemTitle, smsDTO, SmsConstant.ONE, principal.getIdSchoolLogin()));
        String contentSms = SmsUtils.convertContentSendAccount(accountResponse, webSystemTitle);
        SmsConvertResponse smsConvertResponse = SmsUtils.convertSms(contentSms);
        int totalSend = smsConvertResponse.getSmsConvert().size();
        if (!SmsUtils.checkSmsBuget(school, contentSms, SmsConstant.ONE)) {
            throw new NotFoundException(SmsConstant.PASS_BUGET);
        }
        Future<SmsResultDTO> smsResult = smsService.sendSmsOne(principal.getIdSchoolLogin(), accountResponse.getPhone(), contentSms);
        smsReceiversCustomRepository.save(this.setPropertiesSmsReceiversOneCustom(smsResult, kids, totalSend, smsSendCustom, contentSms));
        return true;
    }

    @Transactional
    @Override
    public boolean sendAccountEmployeeSms(UserPrincipal principal, List<InfoEmployeeSchool> infoEmployeeSchoolList) throws ExecutionException, InterruptedException {
        SmsUtils.checkSendSms(infoEmployeeSchoolRepository.findByDelActiveTrueAndSchool_IdAndEmployee_MaUser_Id(principal.getIdSchoolLogin(), principal.getId()));
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new NotFoundException(SmsConstant.NO_EMPLOYEE);
        }
        List<AccountLoginResponse> phoneResponseList = phoneCommonService.findAccountEmployessWithInfo(infoEmployeeSchoolList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(school).orElseThrow(() -> new NotFoundException(SmsConstant.NO_BRAND));
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_ACCOUNT_TEACHER).orElseThrow(() -> new NotFoundException("not found webSystemTitle by id"));
        SmsSendCustom smsSendCustom = smsSendCustomRepository.save(this.setPropertiesSmsCustom(webSystemTitle, smsDTO, phoneResponseList.size(), principal.getIdSchoolLogin()));
        for (AccountLoginResponse x : phoneResponseList) {
            String contentSms = SmsUtils.convertContentSendAccount(x, webSystemTitle);
            SmsConvertResponse smsConvertResponse = SmsUtils.convertSms(contentSms);
            int totalSend = smsConvertResponse.getSmsConvert().size();
            if (!SmsUtils.checkSmsBuget(school, contentSms, SmsConstant.ONE)) {
                throw new NotFoundException(SmsConstant.PASS_BUGET);
            }
            Future<SmsResultDTO> smsResult = smsService.sendSmsOne(principal.getIdSchoolLogin(), x.getPhone(), contentSms);
            smsReceiversCustomRepository.save(this.setPropertiesSmsReceiversTeacherCustom(smsResult, infoEmployeeSchoolList, totalSend, smsSendCustom, contentSms));
        }
        return true;
    }

    @Transactional
    @Override
    public void sendSmsCustom(UserPrincipal principal, SmsCustomRequest request) throws ExecutionException, InterruptedException {
        for (ExcelData excelData : request.getBodyList()) {
            Optional<Kids> kidsOptional = kidsRepository.findByKidCodeAndIdSchoolAndDelActiveTrue(excelData.getPro2(), principal.getIdSchoolLogin());
            if (kidsOptional.isPresent()) {
                Kids kids = kidsOptional.get();
                SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                smsNotifyDataRequest.setSendTitle(request.getTitle());
                smsNotifyDataRequest.setSendContent(excelData.getPro5());
                smsDataService.sendSmsKid(Collections.singletonList(kids), principal.getIdSchoolLogin(), smsNotifyDataRequest);
            }
        }
    }

    @Override
    public void sendFirebaseCustom(UserPrincipal principal, SmsCustomRequest request) throws FirebaseMessagingException {
        for (ExcelData excelData : request.getBodyList()) {
            Optional<Kids> kidsOptional = kidsRepository.findByKidCodeAndIdSchoolAndDelActiveTrue(excelData.getPro2(), principal.getIdSchoolLogin());
            if (kidsOptional.isPresent()) {
                Kids kids = kidsOptional.get();
                String title = request.getTitle();
                String content = excelData.getPro5();
                appSendService.saveToAppSendParent(principal, kids, title, content, AppSendConstant.TYPE_COMMON);
                firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, content, principal.getIdSchoolLogin(), FirebaseConstant.CATEGORY_NOTIFY);
            }
        }
    }


    private SmsReceiversCustom setPropertiesSmsReceiversCustom(Future<SmsResultDTO> smsResult, List<Kids> kidsList, int totalSend, SmsSendCustom smsSendCustom, String contentSms) throws ExecutionException, InterruptedException {
        SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();
        String phoneCv = SmsUtils.convertPhone(smsResult.get().getPhone());
        SmsCode smsCode = smsCodeRepository.findById(smsResult.get().getErrCodeId()).orElseThrow();
        Kids kids = SmsUtils.getParentPhone(phoneCv, kidsList);
        smsReceiversCustom.setSmsCode(smsCode);
        smsReceiversCustom.setAppType(AppTypeConstant.PARENT);
        Parent parent = kids.getParent();
        smsReceiversCustom.setIdSchool(kids.getIdSchool());
        smsReceiversCustom.setNameUserReceiver(parent.getMaUser().getFullName());
        smsReceiversCustom.setNumberSms(totalSend);
        smsReceiversCustom.setPhoneUserReceiver(parent.getMaUser().getPhone());
        smsReceiversCustom.setIdKid(kids.getId());
        smsReceiversCustom.setSendContent(contentSms);
        smsReceiversCustom.setSmsSendCustom(smsSendCustom);
        return smsReceiversCustom;
    }

    private SmsReceiversCustom setPropertiesSmsReceiversTeacherCustom(Future<SmsResultDTO> smsResult, List<InfoEmployeeSchool> infoEmployeeSchoolList, int totalSend, SmsSendCustom smsSendCustom, String contentSms) throws ExecutionException, InterruptedException {
        SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();
        String phoneCv = SmsUtils.convertPhone(smsResult.get().getPhone());
        SmsCode smsCode = smsCodeRepository.findById(smsResult.get().getErrCodeId()).orElseThrow();
        InfoEmployeeSchool info = SmsUtils.getTeacherPhone(phoneCv, infoEmployeeSchoolList);
        smsReceiversCustom.setSmsCode(smsCode);
        smsReceiversCustom.setAppType(AppTypeConstant.TEACHER);
        smsReceiversCustom.setIdSchool(info.getSchool().getId());
        smsReceiversCustom.setNameUserReceiver(info.getFullName());
        smsReceiversCustom.setNumberSms(totalSend);
        smsReceiversCustom.setPhoneUserReceiver(info.getEmployee().getMaUser().getPhone());
        smsReceiversCustom.setSendContent(contentSms);
        smsReceiversCustom.setSmsSendCustom(smsSendCustom);
        return smsReceiversCustom;
    }

    private SmsReceiversCustom setPropertiesSmsReceiversOneCustom(Future<SmsResultDTO> smsResult, Kids kids, int totalSend, SmsSendCustom smsSendCustom, String contentSms) throws ExecutionException, InterruptedException {
        SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();
        SmsCode smsCode = smsCodeRepository.findById(smsResult.get().getErrCodeId()).orElseThrow();
        smsReceiversCustom.setSmsCode(smsCode);
        smsReceiversCustom.setAppType(AppTypeConstant.PARENT);
        Parent parent = kids.getParent();
        smsReceiversCustom.setIdSchool(kids.getIdSchool());
        smsReceiversCustom.setNameUserReceiver(parent.getMaUser().getFullName());
        smsReceiversCustom.setNumberSms(totalSend);
        smsReceiversCustom.setPhoneUserReceiver(parent.getMaUser().getPhone());
        smsReceiversCustom.setIdKid(kids.getId());
        smsReceiversCustom.setSendContent(contentSms);
        smsReceiversCustom.setSmsSendCustom(smsSendCustom);
        return smsReceiversCustom;
    }

    private SmsSendCustom setPropertiesSmsCustom(WebSystemTitle webSystemTitle, SmsDTO smsDTO, int smsNumber, Long idSchool) {
        SmsSendCustom smsSendCustom = new SmsSendCustom();
        smsSendCustom.setAppType(AppTypeConstant.SCHOOL);
        smsSendCustom.setIdSchool(idSchool);
        smsSendCustom.setReceivedCount(smsNumber);
        smsSendCustom.setSendTitle(webSystemTitle.getTitle());
        smsSendCustom.setServiceProvider(smsDTO.getSupplierCode());
        smsSendCustom.setSendType(SmsConstant.TYPE_SEND_ACCOUNT);
        return smsSendCustom;
    }
}





