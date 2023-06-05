package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.BirthdaySample;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.*;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.notifihistory.*;
import com.example.onekids_project.request.smsNotify.VerifyCodeRequest;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ListAppSendNewResponse;
import com.example.onekids_project.response.notifihistory.ListSmsSendResponse;
import com.example.onekids_project.response.notifihistory.ReiceiverSmsSchedule;
import com.example.onekids_project.response.notifihistory.SmsSendResponse;
import com.example.onekids_project.response.phone.PhoneResponse;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import com.example.onekids_project.service.servicecustom.SmsSendService;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import com.example.onekids_project.service.servicecustom.common.PhoneCommonService;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import com.example.onekids_project.util.GenerateCode;
import com.example.onekids_project.util.SmsUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


@Service
public class SmsSendServiceImpl implements SmsSendService {
    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private SmsReiceiversRepository smsReiceiversRepository;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    private @Autowired
    InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private FindSmsService findSmsService;

    @Autowired
    private SmsReceiversCustomRepository smsReceiversCustomRepository;

    @Autowired
    private BirthdaySampleRepository birthdaySampleRepository;

    @Autowired
    private PhoneCommonService phoneCommonService;

    @Autowired
    private SchoolRepository schoolRepository;


    @Override
    public ListSmsSendResponse searchSmsSend(UserPrincipal principal, SearchSmsSendRequest searchSmsSendRequest) {
        Long idSchool = principal.getIdSchoolLogin();
        List<SmsSend> smsSendList = smsSendRepository.searchSmsSend(idSchool, searchSmsSendRequest);
        if (CollectionUtils.isEmpty(smsSendList)) {
            return null;
        }
        smsSendList = filterNotifySys(idSchool, smsSendList);
        List<SmsSendResponse> smsSendResponseList = listMapper.mapList(smsSendList, SmsSendResponse.class);
        smsSendResponseList.forEach(x -> {
            x.setStatusSend("Chờ gửi");
            x.setCoutUserSent(x.getSmsReceiversList().size());
            MaUser maUser = maUserRepository.findById(x.getIdCreated()).orElseThrow();
            x.setNameUserSend(maUser.getFullName());
        });
        ListSmsSendResponse listSmsSendResponse = new ListSmsSendResponse();
        listSmsSendResponse.setSmsSendResponses(smsSendResponseList);
        return listSmsSendResponse;
    }

    private List<SmsSend> filterNotifySys(Long idSchool, List<SmsSend> smsSendList) {
        return smsSendList.stream().filter(x -> !x.isSent() && !x.getAppType().equals(AppTypeConstant.SYSTEM)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteSmsSend(Long id) {
        SmsSend smsSend = smsSendRepository.findById(id).orElseThrow();
        List<SmsReceivers> smsReceiversList = smsReiceiversRepository.findByIdSmsSend(smsSend.getId());
        smsReceiversList.forEach(x -> smsReiceiversRepository.deleteById(x.getId()));
        smsSendRepository.deleteById(id);
        return true;
    }

    @Override
    public AppSendResponse updateSmsApp(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppRequest smsAppRequestEditRequest) {
        Optional<AppSend> appSendOptional = appSendRepository.findById(smsAppRequestEditRequest.getId());
        if (appSendOptional.isEmpty()) {
            return null;
        }
        AppSend oldAppsend = appSendOptional.get();
        modelMapper.map(smsAppRequestEditRequest, oldAppsend);
        oldAppsend.setApproved(true);
        AppSend newAppSend = appSendRepository.save(oldAppsend);
        AppSendResponse appSendResponse = modelMapper.map(newAppSend, AppSendResponse.class);
        return appSendResponse;

    }

    @Override
    public AppSendResponse updateSmsAppforrevoke(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppRevokeRequest updateSmsAppRevokeRequest) {
        Optional<AppSend> appSendOptional = appSendRepository.findById(updateSmsAppRevokeRequest.getId());
        if (appSendOptional.isEmpty()) {
            return null;
        }
        AppSend oldAppsend = appSendOptional.get();
        modelMapper.map(updateSmsAppRevokeRequest, oldAppsend);
        oldAppsend.setSendDel(true);
        AppSend newAppSend = appSendRepository.save(oldAppsend);
        AppSendResponse appSendResponse = modelMapper.map(newAppSend, AppSendResponse.class);
        return appSendResponse;
    }

    @Override
    public boolean deleteApp(Long id) {
        Optional<AppSend> appSendOptional = appSendRepository.findByIdAndDelActive(id, true);
        if (appSendOptional.isEmpty()) {
            return false;
        }
        AppSend deleteAppSend = appSendOptional.get();
        appSendRepository.delete(deleteAppSend);
        return true;
    }

    // hủy thu hồi
    @Override
    public AppSendResponse updateSmsAppforrevokeun(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppRevokeRequest updateSmsAppRevokeRequest) {
        Optional<AppSend> appSendOptional = appSendRepository.findById(updateSmsAppRevokeRequest.getId());
        if (appSendOptional.isEmpty()) {
            return null;
        }
        AppSend oldAppsend = appSendOptional.get();
        modelMapper.map(updateSmsAppRevokeRequest, oldAppsend);
        oldAppsend.setSendDel(false);
        AppSend newAppSend = appSendRepository.save(oldAppsend);
        AppSendResponse appSendResponse = modelMapper.map(newAppSend, AppSendResponse.class);
        return appSendResponse;
    }

    @Override
    public SmsNotifiRequest updateApproved(Long id, List<SmsNotifiRequest> smsNotifiRespone) {
        smsNotifiRespone.forEach(x -> {
            Optional<AppSend> appSendOptional = appSendRepository.findByIdAndDelActive(x.getId(), true);
            if (appSendOptional.isPresent()) {
                AppSend appSend = appSendOptional.get();
                appSend.setApproved(true);
                appSendRepository.save(appSend);
            }
        });
        return null;
    }

    // filter apptype teacher
    private List<AppSend> filterNotifySysSmsApp(Long idSchoolLogin, List<AppSend> appSendList) {
        return appSendList.stream().filter(x -> x.getSendType().equals(AppSendConstant.TYPE_BIRTHDAY) || x.getSendType().equals(AppSendConstant.TYPE_COMMON) || x.getSendType().equals(AppSendConstant.TYPE_CELEBRATE)).collect(Collectors.toList());
    }

    // filter apptype sys
    private List<AppSend> filterNotifySysSmsAppsys(Long idSchoolLogin, List<AppSend> appSendList) {
//        return appSendList.stream().filter(x -> x.getAppType().equals(AppTypeConstant.SYSTEM)).collect(Collectors.toList());
        return appSendList.stream().filter(x -> x.getSendType().equals(AppSendConstant.TYPE_BIRTHDAY) || x.getSendType().equals(AppSendConstant.TYPE_COMMON)).collect(Collectors.toList());
    }


    @Override
    public boolean findSendSms() {
        List<SmsSend> smsSend = smsSendRepository.findByStatusSend();
        smsSend.forEach(send -> {
            String content;
            if (send.getTitleContent() != null) {
                content = send.getTitleContent() + " " + send.getSendContent();
            } else content = send.getSendContent();
            List<String> phones = new ArrayList<>();
            send.getSmsReceiversList().forEach(receiver -> {
                String phone = receiver.getPhone();
                phones.add(phone);
            });


            Future<List<SmsResultDTO>> rs = smsService.sendSms(send.getId_school(), phones, content);

            try {
                if (rs.get() != null) {
                    send.setSent(AppConstant.APP_TRUE);
                    send = smsSendRepository.save(send);
                    List<SmsResultDTO> smsResultDTOS = rs.get();
                    SmsSend finalSmsSend = send;
                    smsResultDTOS.forEach(result -> {
                        String phoneCv = result.getPhone();
                        String cutPhone = phoneCv.substring(0, 1);
                        if (cutPhone.equalsIgnoreCase("84")) {
                            phoneCv = "0".concat(phoneCv.substring(2));
                        }
                        String finalPhoneCv = phoneCv;
                        finalSmsSend.getSmsReceiversList().forEach(parent -> {
                            if (finalPhoneCv.equalsIgnoreCase(parent.getPhone())) {
                                SmsCode smsCode = this.checkSmsCode(result.getErrCodeId());
                                parent.setSmsCode(smsCode);
                                smsReiceiversRepository.save(parent);

                            }
                        });
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        });


        return true;

    }

    @Override
    public List<ReiceiverSmsSchedule> findByIdSmsss(Long idSchoolLogin, Long id) {
        Optional<SmsSend> optionalSmsSend = smsSendRepository.findById(id);

        List<SmsReceivers> smsReceiversList1 = smsReiceiversRepository.findAll();
        List<SmsReceivers> smsReceiversList2 = smsReceiversList1.stream().filter(x -> x.getSmsSend().getId() == id).collect(Collectors.toList());
        ListSmsSendResponse listSmsSendResponse = new ListSmsSendResponse();
        List<ReiceiverSmsSchedule> reiceiverSmsScheduleList = new ArrayList<>();
        smsReceiversList2.forEach(x -> {
            ReiceiverSmsSchedule model = new ReiceiverSmsSchedule();
            Optional<MaUser> maUserOptional = maUserRepository.findByIdAndDelActiveTrue(x.getIdUserReceiver());
            Optional<SmsSend> smsSendOptional = smsSendRepository.findById(x.getSmsSend().getId());
            model.setReiceiverName(maUserOptional.get().getFullName());
            model.setPhone(x.getPhone());
            model.setContent(smsSendOptional.get().getSendContent());
            reiceiverSmsScheduleList.add(model);
        });
        return reiceiverSmsScheduleList;
    }

    @Override
    public SmsNotifiRequest updateManyApproved(Long id, UserPrincipal principal, List<SmsNotifiRequest> smsNotifiRequests) {
        smsNotifiRequests.forEach(x -> {
            Optional<AppSend> appSendOptional = appSendRepository.findById(x.getId());
            if (appSendOptional.isPresent()) {
                AppSend messageParent = appSendOptional.get();
                messageParent.setApproved(AppConstant.APP_TRUE);
                appSendRepository.save(messageParent);
            }
        });
        return null;
    }

    @Override
    public boolean createVerifyAccountKid(UserPrincipal principal, VerifyCodeRequest verifyCodeRequest) throws ExecutionException, InterruptedException {
        long idSchool = principal.getIdSchoolLogin();
        Long id = verifyCodeRequest.getId();
        String phone = verifyCodeRequest.getPhone();
        String appType = verifyCodeRequest.getAppType();
        if (appType.equalsIgnoreCase(AppTypeConstant.PARENT)) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
            String code = GenerateCode.getNumber();
            kids.setVerifyCodeSchool(code);
            kidsRepository.save(kids);
            Optional<WebSystemTitle> webSystemTitleKid = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_CODE_VERIFY_KID);
            String content = this.convertContent(webSystemTitleKid.get(), code);
            String title = webSystemTitleKid.get().getTitle();
            Future<List<SmsResultDTO>> rs = sendCode(content, phone, idSchool);
            this.saveSmsSendParent(principal, kids, rs.get(), content, title);

        } else if (appType.equalsIgnoreCase(AppTypeConstant.TEACHER)) {
            String code = GenerateCode.getNumber();
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
            infoEmployeeSchool.setVerifyCodeSchool(code);
            infoEmployeeSchoolRepository.save(infoEmployeeSchool);
            Optional<WebSystemTitle> webSystemTitleTeacher = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_CODE_VERIFY_TEACHER);
            String content = this.convertContent(webSystemTitleTeacher.get(), code);
            String title = webSystemTitleTeacher.get().getTitle();
            Future<List<SmsResultDTO>> rs = sendCode(content, phone, idSchool);
            this.saveSmsSendEmployee(principal, infoEmployeeSchool, rs.get(), content, title, AppTypeConstant.TEACHER);

        } else if (appType.equalsIgnoreCase(AppTypeConstant.SCHOOL)) {
            String code = GenerateCode.getNumber();
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndDelActiveTrue(id).orElseThrow();
            infoEmployeeSchool.setVerifyCodeAdmin(code);
            infoEmployeeSchoolRepository.save(infoEmployeeSchool);
            Optional<WebSystemTitle> webSystemTitlePlus = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_CODE_VERIFY_PLUS);
            String title = webSystemTitlePlus.get().getTitle();
            String content = this.convertContent(webSystemTitlePlus.get(), code);
            Future<List<SmsResultDTO>> rs = sendCode(content, phone, idSchool);
            this.saveSmsSendEmployee(principal, infoEmployeeSchool, rs.get(), content, title, AppTypeConstant.SCHOOL);
        }
        return true;
    }

    @Override
    public void sendAutoSmsBirthday() {
        LocalDate localDate = LocalDate.now();
        this.sendSmsKids(localDate);
        this.sendSmsParents(localDate);
        this.sendSmsEmpolyees(localDate);
    }

    @Override
    public ListAppSendNewResponse searchSmsAppSysNew(UserPrincipal principal, SearchSmsAppRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idUser = principal.getId();
        ListAppSendNewResponse response = new ListAppSendNewResponse();
        List<AppSend> appSendList = appSendRepository.searchSmsAppnew(idSchool, idUser, request);
        long total = appSendRepository.coutSearchSmsAppnew(idSchool, idUser, request);
        List<AppSendResponse> appSendResponseList = listMapper.mapList(appSendList, AppSendResponse.class);
        appSendResponseList.forEach(x -> {
            int coutIsApprove = 0;
            x.setNumberFile(x.getUrlFileAppSendList().size());
            x.setCoutUserSent(x.getReceiversList().size());
            if (x.getSendType().equals("common")) {
                x.setSendType("Chung");
            }
            coutIsApprove = (int) x.getReceiversList().stream().filter(y -> y.isApproved() == AppConstant.APP_FALSE).count();
            x.setApproveRe(coutIsApprove > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        });
        response.setTotal(total);
        response.setResponseList(appSendResponseList);
        return response;
    }

    @Override
    public ListAppSendNewResponse searchSmsAppTeacherNew(UserPrincipal principal, SearchSmsAppRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListAppSendNewResponse response = new ListAppSendNewResponse();
        List<AppSend> appSendList = appSendRepository.searchSmsAppTeachernew(idSchool, request);
        long total = appSendRepository.coutSearchSmsAppTeachernew(idSchool, request);
        List<AppSendResponse> appSendResponseList = listMapper.mapList(appSendList, AppSendResponse.class);
        appSendResponseList.forEach(x -> {
            int coutIsApprove = 0;
            x.setCoutUserSent(x.getReceiversList().size());
            x.setNumberFile(x.getUrlFileAppSendList().size());
            if (x.getSendType().equals(AppConstant.TYPE_COMMON)) {
                x.setSendType("Chung");
            }
            coutIsApprove = (int) x.getReceiversList().stream().filter(y -> y.isApproved() == AppConstant.APP_FALSE).count();
            x.setApproveRe(coutIsApprove > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
        });
        response.setTotal(total);
        response.setResponseList(appSendResponseList);
        return response;
    }

    @Transactional
    @Override
    public boolean sendStudentSms(UserPrincipal principal, List<Kids> kidsList, String content) throws ExecutionException, InterruptedException {
        SmsUtils.checkSendSms(infoEmployeeSchoolRepository.findByDelActiveTrueAndSchool_IdAndEmployee_MaUser_Id(principal.getIdSchoolLogin(), principal.getId()));
        if (CollectionUtils.isEmpty(kidsList)) {
            throw new NotFoundException(SmsConstant.NO_KID);
        }
        String contentSms = SmsUtils.converContentWithTitle(principal, content);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<PhoneResponse> phoneResponseList = phoneCommonService.findPhoneParent(kidsList);
        List<String> phoneSmsList = phoneResponseList.stream().map(PhoneResponse::getPhone).collect(Collectors.toList());
        phoneSmsList = List.copyOf(Set.copyOf(phoneSmsList));
        if (!SmsUtils.checkSmsBuget(school, contentSms, phoneSmsList.size())) {
            throw new NotFoundException(SmsConstant.PASS_BUGET);
        }
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(school).orElseThrow(() -> new NotFoundException(SmsConstant.NO_BRAND));
        SmsSend smsSendSave = smsSendRepository.save(this.setPropertiesSmsSend(phoneSmsList.size(), principal, smsDTO, contentSms));
        Future<List<SmsResultDTO>> rs = smsService.sendSmsMulti(principal.getIdSchoolLogin(), phoneSmsList, contentSms);
        for (SmsResultDTO smsResult : rs.get()) {
            Kids kid = SmsUtils.getParentPhone(SmsUtils.convertPhone(smsResult.getPhone()), kidsList);
            SmsReceivers smsReceivers = this.setPropertiesSmsReceivers(kid, smsResult);
            smsReceivers.setSmsSend(smsSendSave);
            smsReiceiversRepository.save(smsReceivers);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean sendEmployeeSms(UserPrincipal principal, List<InfoEmployeeSchool> infoEmployeeSchoolList, String content) throws ExecutionException, InterruptedException {
        SmsUtils.checkSendSms(infoEmployeeSchoolRepository.findByDelActiveTrueAndSchool_IdAndEmployee_MaUser_Id(principal.getIdSchoolLogin(), principal.getId()));
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new NotFoundException(SmsConstant.NO_KID);
        }
        String contentSms = SmsUtils.converContentWithTitle(principal, content);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<PhoneResponse> phoneResponseList = phoneCommonService.findPhoneWithInfo(infoEmployeeSchoolList);
        List<String> phoneSmsList = phoneResponseList.stream().map(PhoneResponse::getPhone).collect(Collectors.toList());
        phoneSmsList = List.copyOf(Set.copyOf(phoneSmsList));
        if (!SmsUtils.checkSmsBuget(school, contentSms, phoneSmsList.size())) {
            throw new NotFoundException(SmsConstant.PASS_BUGET);
        }
        SmsDTO smsDTO = SmsUtils.getSmsDTOByShoolId(school).orElseThrow(() -> new NotFoundException(SmsConstant.NO_BRAND));
        SmsSend smsSendSave = smsSendRepository.save(this.setPropertiesSmsSend(phoneSmsList.size(), principal, smsDTO, contentSms));
        Future<List<SmsResultDTO>> rs = smsService.sendSmsMulti(principal.getIdSchoolLogin(), phoneSmsList, contentSms);
        for (SmsResultDTO smsResult : rs.get()) {
            InfoEmployeeSchool infoEmployeeSchool = SmsUtils.getTeacherPhone(SmsUtils.convertPhone(smsResult.getPhone()), infoEmployeeSchoolList);
            SmsReceivers smsReceivers = this.setPropertiesSmsReceiversTeacher(infoEmployeeSchool, smsResult);
            smsReceivers.setSmsSend(smsSendSave);
            smsReiceiversRepository.save(smsReceivers);
        }
        return true;
    }

    private SmsReceivers setPropertiesSmsReceiversTeacher(InfoEmployeeSchool infoEmployeeSchool, SmsResultDTO smsResult) {
        SmsReceivers smsReceivers = new SmsReceivers();

        smsReceivers.setIdUserReceiver(infoEmployeeSchool.getEmployee().getMaUser().getId());
        smsReceivers.setPhone(infoEmployeeSchool.getEmployee().getMaUser().getPhone());
        smsReceivers.setIdSchool(infoEmployeeSchool.getSchool().getId());
        SmsCode smsCode = smsCodeRepository.findById(smsResult.getErrCodeId()).orElseThrow();
        smsReceivers.setSmsCode(smsCode);
        return smsReceivers;
    }

    private SmsReceivers setPropertiesSmsReceivers(Kids kid, SmsResultDTO smsResult) {
        Parent parent = kid.getParent();
        SmsReceivers smsReceivers = new SmsReceivers();
        if (parent.getMaUser() != null) {
            smsReceivers.setIdUserReceiver(parent.getMaUser().getId());
            smsReceivers.setPhone(parent.getMaUser().getPhone());
        }
        if (!CollectionUtils.isEmpty(parent.getKidsList())) {
            smsReceivers.setIdSchool(kid.getIdSchool());
        }
        SmsCode smsCode = smsCodeRepository.findById(smsResult.getErrCodeId()).orElseThrow();
        smsReceivers.setSmsCode(smsCode);
        smsReceivers.setIdKid(kid.getId());
        return smsReceivers;
    }

    private SmsSend setPropertiesSmsSend(int smsNumber, UserPrincipal principal, SmsDTO smsDTO, String contentSms) {
        SmsSend smsSend = new SmsSend();
        smsSend.setSmsNumber(smsNumber);
        smsSend.setSendContent(contentSms);
        smsSend.setTitleContent(principal.getSysConfig().isShowTitleSms() ? principal.getSysConfig().getTitleContentSms() : null);
        smsSend.setId_school(principal.getIdSchoolLogin());
        smsSend.setSendType(SmsConstant.TYPE_SMS);
        smsSend.setAppType(principal.getAppType());
        smsSend.setServiceProvider(smsDTO.getSupplierCode());
        SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, contentSms);
        int totalSend = smsConvertResponse.getSmsConvert().size() * smsNumber;
        smsSend.setSmsTotal(totalSend);
        smsSend.setSent(AppConstant.APP_TRUE);
        return smsSend;
    }

    private void sendSmsKids(LocalDate localDate) {
        List<Kids> kidsList = kidsRepository.searchKidsBirthdayNoSchool(localDate);
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(kidsList)) {
            this.checkSendSmsKids(kidsList);
        }
    }

    private void checkSendSmsKids(List<Kids> kids) {
        List<Long> idSchool = kids.stream().map(Kids::getIdSchool).collect(Collectors.toList());
        idSchool = List.copyOf(Set.copyOf(idSchool));

        for (Long x : idSchool) {
            List<Kids> kidsList = kids.stream().filter(y -> y.getParent() != null && y.getIdSchool().equals(x)).collect(Collectors.toList());

            Optional<BirthdaySample> birthdaySample = birthdaySampleRepository.findByIdSchoolAndBirthdayTypeAndActiveTrueAndSmsSendTrue(x, AppConstant.KID_NAME);

            if (birthdaySample.isPresent()) {
                NotifyRequest notifyRequest = new NotifyRequest();
                Optional<WebSystemTitle> webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(1L);
                notifyRequest.setTitle(webSystemTitle.get().getTitle());
                notifyRequest.setBody(birthdaySample.get().getContent());
                for (Kids y : kidsList) {

                }
            }
        }
    }

    private void sendSmsParents(LocalDate localDate) {
        List<Parent> parentList = parentRepository.findParentAllBirthdayNoSchool(localDate);
        if (CollectionUtils.isNotEmpty(parentList)) {
            List<Kids> kidsList = new ArrayList<>();
            parentList.forEach(x -> {
                kidsList.addAll(x.getKidsList());
            });
            List<Kids> kidsListFt = kidsList.stream().filter(x -> x.getKidStatus().equals(AppConstant.STUDYING)).collect(Collectors.toList());
//            this.checkSendSmsParent(kidsListFt);
        }
    }

    private void sendSmsEmpolyees(LocalDate localDate) {
        List<InfoEmployeeSchool> infoEmployeeSchoolsList = infoEmployeeSchoolRepository.findEmployeeAllBirthdayNoSchool(localDate);
        if (CollectionUtils.isNotEmpty(infoEmployeeSchoolsList)) {
//            this.checkSendSmsEmployee(infoEmployeeSchoolsList);
        }
    }

    private String convertContent(WebSystemTitle webSystemTitle, String code) {
        String content = webSystemTitle.getTitle().concat(webSystemTitle.getContent());
        content = content.replace("{code}", code);
        return content;
    }

    private Future<List<SmsResultDTO>> sendCode(String content, String phone, long idSchool) {
        List<String> phones = Arrays.asList(phone);
        Future<List<SmsResultDTO>> rs = smsService.sendSms(idSchool, phones, content);
        return rs;
    }

    private void saveSmsSendParent(UserPrincipal principal, Kids kids, List<SmsResultDTO> result, String content, String title) {
        SmsDTO smsDTO = smsService.getSmsDTOByShoolId(principal.getIdSchoolLogin());
        SmsSendCustom smsSendCustom = new SmsSendCustom();
        smsSendCustom.setAppType(AppTypeConstant.SCHOOL);
        smsSendCustom.setIdSchool(principal.getIdSchoolLogin());
        smsSendCustom.setReceivedCount(1);
        smsSendCustom.setSendTitle(title);
        smsSendCustom.setServiceProvider(smsDTO.getSupplierCode());
        smsSendCustom.setSendType(SmsConstant.TYPE_SEND_VERIFY_ACCOUNT);
        smsSendCustom = smsSendCustomRepository.save(smsSendCustom);

        SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, content);
        int totalSend = smsConvertResponse.getSmsConvert().size();
        String phoneCv = SmsUtils.convertPhone(result.get(0).getPhone());
        SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();
        Optional<SmsCode> smsCode = smsCodeRepository.findById(result.get(0).getErrCodeId());
        if (smsCode.isPresent()) {
            smsReceiversCustom.setSmsCode(smsCode.get());
        }
        smsReceiversCustom.setAppType(AppTypeConstant.PARENT);

        Parent parent = kids.getParent();
        smsReceiversCustom.setIdSchool(kids.getIdSchool());
        if (parent != null) {
            smsReceiversCustom.setNameUserReceiver(parent.getMaUser().getFullName());
        }
        smsReceiversCustom.setNumberSms(totalSend);
        smsReceiversCustom.setPhoneUserReceiver(phoneCv);
        smsReceiversCustom.setIdKid(kids.getId());
        smsReceiversCustom.setSendContent(content);
        smsReceiversCustom.setSmsSendCustom(smsSendCustom);

        smsReceiversCustomRepository.save(smsReceiversCustom);

//        SmsSend smsSend = new SmsSend();
//        smsSend.setSmsNumber(1);
//        smsSend.setId_school(principal.getIdSchoolLogin());
//        smsSend.setSendType(SmsConstant.TYPE_SMS);
//        smsSend.setAppType(principal.getAppType());
//        smsSend.setSendContent(content);
//        smsSend.setServiceProvider(smsDTO.getSupplierCode());
//        smsSend.setSent(AppConstant.APP_TRUE);
//        smsSend = smsSendRepository.save(smsSend);
//        SmsSend finalSmsSend = smsSend;
//
//
//        String phoneCv = this.checkPhone(result.get(0).getPhone());
//        SmsReceivers smsReceivers = new SmsReceivers();
//        smsReceivers.setIdUserReceiver(kids.getParent().getMaUser().getId());
//        smsReceivers.setPhone(phoneCv);
//        smsReceivers.setIdSchool(principal.getIdSchoolLogin());
//        SmsCode smsCode = this.checkSmsCode(result.get(0).getErrCodeId());
//        smsReceivers.setSmsCode(smsCode);
//        smsReceivers.setSmsSend(finalSmsSend);
//        smsReiceiversRepository.save(smsReceivers);

    }

    private SmsCode checkSmsCode(Long id) {
        Optional<SmsCode> smsCode = smsCodeRepository.findById(id);
        if (!smsCode.isPresent()) {
            smsCode = smsCodeRepository.findById(SmsConstant.SMS_CODE_NEW);
        }
        return smsCode.get();
    }

    private String checkPhone(String phone) {
        String cutPhone = phone.substring(0, 2);
        if (cutPhone.equals("84")) {
            phone = "0".concat(phone.substring(2));
        }
        return phone;
    }

    private void saveSmsSendEmployee(UserPrincipal principal, InfoEmployeeSchool infoEmployeeSchool, List<SmsResultDTO> result, String content, String title, String appType) {
        SmsDTO smsDTO = smsService.getSmsDTOByShoolId(principal.getIdSchoolLogin());
        String phoneCv = SmsUtils.convertPhone(result.get(0).getPhone());
        SmsSendCustom smsSendCustom = new SmsSendCustom();
        smsSendCustom.setAppType(appType);
        smsSendCustom.setIdSchool(principal.getIdSchoolLogin());
        smsSendCustom.setReceivedCount(1);
        smsSendCustom.setSendTitle(title);
        smsSendCustom.setServiceProvider(smsDTO.getSupplierCode());
        smsSendCustom.setSendType(SmsConstant.TYPE_SEND_VERIFY_ACCOUNT);
        smsSendCustom = smsSendCustomRepository.save(smsSendCustom);


        SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, content);
        int totalSend = smsConvertResponse.getSmsConvert().size();

        SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();
        Optional<SmsCode> smsCode = smsCodeRepository.findById(result.get(0).getErrCodeId());
        if (smsCode.isPresent()) {
            smsReceiversCustom.setSmsCode(smsCode.get());
        }
        smsReceiversCustom.setAppType(AppTypeConstant.TEACHER);

        smsReceiversCustom.setIdSchool(principal.getIdSchoolLogin());
        smsReceiversCustom.setNameUserReceiver(infoEmployeeSchool.getFullName());
        smsReceiversCustom.setNumberSms(totalSend);
        smsReceiversCustom.setPhoneUserReceiver(phoneCv);
        smsReceiversCustom.setSendContent(content);
        smsReceiversCustom.setSmsSendCustom(smsSendCustom);
        smsReceiversCustomRepository.save(smsReceiversCustom);


//        SmsSend smsSend = new SmsSend();
//        smsSend.setSmsNumber(1);
//        smsSend.setId_school(principal.getIdSchoolLogin());
//        smsSend.setSendType(SmsConstant.TYPE_SMS);
//        smsSend.setAppType(principal.getAppType());
//        smsSend.setSendContent(content);
//        smsSend.setServiceProvider(smsDTO.getSupplierCode());
//        smsSend.setSent(AppConstant.APP_TRUE);
//        smsSend = smsSendRepository.save(smsSend);
//        SmsSend finalSmsSend = smsSend;
//
//        String phoneCv = this.checkPhone(result.get(0).getPhone());
//        SmsReceivers smsReceivers = new SmsReceivers();
//        if (infoEmployeeSchool.getEmployee() != null) {
//            smsReceivers.setIdUserReceiver(infoEmployeeSchool.getEmployee().getMaUser().getId());
//        }
//        smsReceivers.setPhone(phoneCv);
//        smsReceivers.setIdSchool(principal.getIdSchoolLogin());
//        SmsCode smsCode = this.checkSmsCode(result.get(0).getErrCodeId());
//        smsReceivers.setSmsCode(smsCode);
//        smsReceivers.setSmsSend(finalSmsSend);
//        smsReiceiversRepository.save(smsReceivers);
    }

//    @Override
//    public List<SmsSendCaladarResponse> findByIdSmsss(Long idSchoolLogin, Long id) {
//        Optional<SmsSend> optionalSmsSend = smsSendRepository.findById(id);
//        List<SmsReceivers> smsReceiversList = smsReiceiversRepository.findAllByAppSendId(optionalSmsSend.get().getId());
//        ListAppSendResponse listAppSendResponse = new ListAppSendResponse();
//        List<ReiceiversResponeHistoru> reiceiversResponeHistoruList = new ArrayList<>();
//        receiversList.forEach(x -> {
//            ReiceiversResponeHistoru model = new ReiceiversResponeHistoru();
//            if (x.getIdClass() != null) {
//                Optional<MaClass> maClassOptional = maClassRepository.findById(x.getIdClass());
//                model.setClassName(maClassOptional.get().getClassName());
//            } else {
//                model.setClassName("");
//            }
//            if (x.getIdCreated() != null) {
//                Optional<MaUser> maUserOptional = maUserRepository.findById(x.getIdCreated());
//                model.setCreatedBy(maUserOptional.get().getFullName());
//                model.setPhone(maUserOptional.get().getPhone());
//                if (maUserOptional.get().getAppType().equals("parent")) {
//                    model.setType(AppConstant.ACCOUNT_TYPE_FEEDBACK_PARENT);
//                } else {
//                    model.setType("Nhân viên");
//                }
//            }
//
//            reiceiversResponeHistoruList.add(model);
//        });
//        return reiceiversResponeHistoruList;
//    }

//    public static String generateCronExpression(final String seconds, final String minutes, final String hours) {
//        return String.format("%1$s %2$s %3$s %4$s %5$s %6$s %7$s", seconds, minutes, hours, "*", "*","?", "*");
//    }

}




