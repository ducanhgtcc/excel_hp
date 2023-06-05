package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.cronjobs.AttendanceKidsCronjobs;
import com.example.onekids_project.cronjobs.EvaluateKidsCronjobs;
import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.dto.KidsBirthdayDTO;
import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.agent.Brand;
import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.kids.*;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.*;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.importexport.model.KidModel;
import com.example.onekids_project.importexport.model.KidModelImport;
import com.example.onekids_project.integration.dto.SmsResultDTO;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.kids.KidsSearchAdminRequest;
import com.example.onekids_project.master.response.kids.ListStudentAdminResponse;
import com.example.onekids_project.master.response.kids.StudentAdminResponse;
import com.example.onekids_project.mobile.parent.response.home.KidsInforResponse;
import com.example.onekids_project.mobile.parent.response.home.ParentInforObject;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.birthdaymanagement.SearchKidsBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.request.common.StatusCommonRequest;
import com.example.onekids_project.request.createnotifyschool.CreateStudentNotify;
import com.example.onekids_project.request.kids.*;
import com.example.onekids_project.request.kids.transfer.KidsTransferCreateRequest;
import com.example.onekids_project.request.kids.transfer.KidsTransferUpdateRequest;
import com.example.onekids_project.request.kids.transfer.SearchKidsTransferRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyRequest;
import com.example.onekids_project.response.birthdaymanagement.KidBirthdayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListKidsBirthDayResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.common.InforRepresentationResponse;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.finance.kidspackage.KidsInfoDataResponse;
import com.example.onekids_project.response.kids.*;
import com.example.onekids_project.response.phone.AccountLoginResponse;
import com.example.onekids_project.response.phone.PhoneResponse;
import com.example.onekids_project.response.school.AppIconResponse;
import com.example.onekids_project.response.school.ListAppIconResponse;
import com.example.onekids_project.response.sms.SmsConvertResponse;
import com.example.onekids_project.response.studentgroup.KidsGroupResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.AccountCreateData;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.dto.sms.SmsDTO;
import com.example.onekids_project.service.servicecustom.*;
import com.example.onekids_project.service.servicecustom.common.FindSmsService;
import com.example.onekids_project.service.servicecustom.common.PhoneCommonService;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageDefaultService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.service.servicecustom.sms.SmsService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class KidsServiceImpl implements KidsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    PhoneCommonService phoneCommonService;
    int monthCurrent = LocalDate.now().getMonthValue();
    int yearCurrent = LocalDate.now().getYear();
    @Autowired
    private AppSendRepository appSendRepository;
    @Autowired
    private SmsSendRepository smsSendRepository;
    @Autowired
    private SmsReiceiversRepository smsReiceiversRepository;
    @Autowired
    private ReceiversRepository receiversRepository;
    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private KidsTransferRepository kidsTransferRepository;
    @Autowired
    private KidsGroupRepository kidsGroupRepository;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private SchoolConfigRepository schoolConfigRepository;
    @Autowired
    private MaKidPicsRepository maKidPicsRepository;
    @Autowired
    private MaKidPicsService maKidPicsService;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private ParentService parentService;
    @Autowired
    private MaClassService maClassService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private KidsGroupService kidsGroupService;
    @Autowired
    private HeightWeightService heightWeightService;
    @Autowired
    private AppIconParentAddSerivce appIconParentAddSerivce;
    @Autowired
    private KidsExtraInfoService kidsExtraInfoService;
    @Autowired
    private MaUserService maUserService;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private AttendanceKidsCronjobs attendanceKidsCronjobs;
    @Autowired
    private EvaluateKidsCronjobs evaluateKidsCronjobs;
    private String fileNameAvatar;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SmsCodeRepository smsCodeRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private KidsClassDateRepository kidsClassDateRepository;
    @Autowired
    private KidsStatusTimelineRepository kidsStatusTimelineRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private SysInforRepository sysInforRepository;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private AppIconParentService appIconParentService;
    @Autowired
    private EvaluateDateRepository evaluateDateRepository;
    @Autowired
    private EvaluateWeekRepository evaluateWeekRepository;
    @Autowired
    private EvaluateMonthRepository evaluateMonthRepository;
    @Autowired
    private AttendanceArriveKidsRepository attendanceArriveKidsRepository;
    @Autowired
    private AttendanceLeaveKidsRepository attendanceLeaveKidsRepository;
    @Autowired
    private AttendanceEatKidsRepository attendanceEatKidsRepository;
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;
    @Autowired
    private FindSmsService findSmsService;
    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;
    @Autowired
    private SmsReceiversCustomRepository smsReceiversCustomRepository;
    @Autowired
    private FnKidsPackageDefaultService fnKidsPackageDefaultService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private SmsDataService smsDataService;
    @Autowired
    private GroupOutKidsRepository groupOutKidsRepository;

    @Override
    public ListKidsResponse findAllKids(Long idSchool, Pageable pageable) {
        List<Kids> kidsList = kidsRepository.findAllKids(idSchool, pageable);
        if (CollectionUtils.isEmpty(kidsList)) {
            return null;
        }
        List<KidsDTO> kidsDTOList = listMapper.mapList(kidsList, KidsDTO.class);
        ListKidsResponse listStudentResponse = new ListKidsResponse();
        listStudentResponse.setKidList(kidsDTOList);

        return listStudentResponse;
    }

    @Override
    public Optional<KidsDTO> findByIdKid(Long idSchool, Long id) {
        Optional<Kids> kidsOptional = kidsRepository.findByIdAndDelActiveTrue(id);
        if (kidsOptional.isEmpty()) {
            return Optional.empty();
        }
        Optional<KidsDTO> kidsBirthdayDTOOptional = Optional.ofNullable(modelMapper.map(kidsOptional.get(), KidsDTO.class));
        return kidsBirthdayDTOOptional;
    }

    @Override
    public List<KidsInGroupResponse> findAllKidForGroup(UserPrincipal principal) {
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        List<KidsInGroupResponse> responseList = listMapper.mapList(kidsList, KidsInGroupResponse.class);
        responseList.forEach(x -> x.setKidStatus(ConvertData.convertKidsStatus(x.getKidStatus())));
        return responseList;
    }

    private List<Kids> findAllKidSms(List<Long> idKidList) {
        List<Kids> kidsList = new ArrayList<>();
        idKidList.forEach(idKid -> {
            Optional<Kids> kids = kidsRepository.findByIdAndDelActiveTrue(idKid);
            if (kids.isPresent()) {
                if (kids.get().getParent() != null) {
                    kidsList.add(kids.get());
                }
            }
        });
        return kidsList.stream().filter(x -> (x.getParent() != null && x.isSmsReceive())).collect(Collectors.toList());
    }

    @Override
    public boolean createStudentNotifySms(UserPrincipal principal, SmsNotifyRequest smsNotifyRequest) throws ExecutionException, InterruptedException {
        CommonValidate.checkPlusOrTeacher(principal);
        List<Kids> kidsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(smsNotifyRequest.getDataKidNotifySmsList())) {
            kidsList = this.findAllKidSms(smsNotifyRequest.getDataKidNotifySmsList());
        } else if (!CollectionUtils.isEmpty(smsNotifyRequest.getDataClassNotifySmsList())) {
            kidsList = this.findAllKidSms(smsNotifyRequest.getDataClassNotifySmsList());
        } else if (!CollectionUtils.isEmpty(smsNotifyRequest.getDataGradeNotifySmsList())) {
            kidsList = this.findAllKidSms(smsNotifyRequest.getDataGradeNotifySmsList());
        } else if (!CollectionUtils.isEmpty(smsNotifyRequest.getDataGroupNotifySmsList())) {
            kidsList = this.findAllKidSms(smsNotifyRequest.getDataGroupNotifySmsList());
        } else {
            throw new NotFoundException("Không có học sinh nào được chọn");
        }
        List<PhoneResponse> phoneResponseList = phoneCommonService.findPhoneParent(kidsList);
        List<String> phoneSmsList = new ArrayList<>();
        phoneResponseList.forEach(phone -> {
            phoneSmsList.add(phone.getPhone());
        });
        List<String> phoneSms = List.copyOf(Set.copyOf(phoneSmsList));

        SmsDTO smsDTO = getSmsDTOByShoolId(principal.getIdSchoolLogin());
        SmsSend smsSend = modelMapper.map(smsNotifyRequest, SmsSend.class);
        int smsNumber = phoneResponseList.size();
        if (!CollectionUtils.isEmpty(phoneResponseList)) {
            smsSend.setSmsNumber(smsNumber);
        }
        smsSend.setTitleContent(smsNotifyRequest.getSendTitle());
        smsSend.setId_school(principal.getIdSchoolLogin());
        smsSend.setSendType(SmsConstant.TYPE_SMS);
        smsSend.setAppType(principal.getAppType());
        smsSend.setServiceProvider(smsDTO.getSupplierCode());

        String contentSms;
        if (!smsNotifyRequest.getSendTitle().isEmpty()) {
            contentSms = smsNotifyRequest.getSendTitle() + " " + smsNotifyRequest.getSendContent();
        } else contentSms = smsNotifyRequest.getSendContent();

        SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, contentSms);
        int totalSend = smsConvertResponse.getSmsConvert().size() * smsNumber;
        smsSend.setSmsTotal(totalSend);
        if (smsNotifyRequest.isTimer() && smsNotifyRequest.getDateTime() != null) {
            smsSend.setTimeAlarm(smsNotifyRequest.getDateTime());
            smsSend.setSent(AppConstant.APP_FALSE);
            smsSend = smsSendRepository.save(smsSend);
            SmsSend finalSmsSend = smsSend;
            kidsList.forEach(kid -> {
                SmsReceivers smsReceivers = new SmsReceivers();
                smsReceivers.setIdUserReceiver(kid.getParent().getMaUser().getId());
                smsReceivers.setIdKid(kid.getId());
                smsReceivers.setPhone(kid.getParent().getMaUser().getPhone());
                smsReceivers.setIdSchool(kid.getIdSchool());
                smsReceivers.setSmsSend(finalSmsSend);
                smsReiceiversRepository.save(smsReceivers);
            });
        } else {
            Future<List<SmsResultDTO>> rs = smsService.sendSms(principal.getIdSchoolLogin(), phoneSms, contentSms);
            smsSend.setSent(AppConstant.APP_TRUE);
            smsSend = smsSendRepository.save(smsSend);
            List<SmsResultDTO> smsResultDTOS = rs.get();
            SmsSend finalSmsSend = smsSend;
            for (SmsResultDTO result : smsResultDTOS) {
                String phoneCv = result.getPhone();
                String cutPhone = phoneCv.substring(0, 1);
                if (cutPhone.equalsIgnoreCase("84")) {
                    phoneCv = "0".concat(phoneCv.substring(2));
                }

                List<Kids> kidsLists = this.getParentPhone(phoneCv, kidsList);
                Kids kid = kidsLists.get(0);
                Parent parent = kid.getParent();
                SmsReceivers smsReceivers = new SmsReceivers();
                if (parent.getMaUser() != null) {
                    smsReceivers.setIdUserReceiver(parent.getMaUser().getId());
                    smsReceivers.setPhone(parent.getMaUser().getPhone());
                }
                if (!CollectionUtils.isEmpty(parent.getKidsList())) {
                    smsReceivers.setIdSchool(parent.getKidsList().get(0).getIdSchool());
                }
                Optional<SmsCode> smsCode = smsCodeRepository.findById(result.getErrCodeId());
                smsReceivers.setSmsCode(smsCode.get());
                smsReceivers.setIdKid(kid.getId());
                smsReceivers.setSmsSend(finalSmsSend);
                smsReiceiversRepository.save(smsReceivers);
            }
        }
        return true;
    }

    @Override
    public boolean createStudentSmsService(UserPrincipal principal, SmsStudentRequest request) throws ExecutionException, InterruptedException {
        List<Kids> kidsList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        if (SmsConstant.SMS_KID.equals(request.getType())) {
            kidsList = kidsRepository.findByIdInAndIdSchoolAndDelActiveTrue(request.getIdList(), idSchool);
        } else if (SmsConstant.SMS_CLASS.equals(request.getType())) {
            kidsList = kidsRepository.findByIdSchoolAndMaClassIdInAndKidStatusAndDelActiveTrue(idSchool, request.getIdList(), KidsStatusConstant.STUDYING);
        }
        SmsNotifyDataRequest smsNotifyDataRequest = modelMapper.map(request, SmsNotifyDataRequest.class);
        smsDataService.sendSmsKid(kidsList, idSchool, smsNotifyDataRequest);
        return true;
    }

    private List<Kids> getParentPhone(String phone, List<Kids> kidsList) {
        List<Kids> kids = kidsList.stream().filter(x -> x.getParent().getMaUser().getPhone().equalsIgnoreCase(phone)).collect(Collectors.toList());
        return kids;

    }

    @Transactional
    @Override
    public boolean createAccountAndParentForOther(String fullName, String phone, Kids kids) {
        String appType = AppTypeConstant.PARENT;
        String usernameEndExtend = ConvertData.getUsernameIncludeExtend(phone, appType);
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(usernameEndExtend);
        if (maUserOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.USERNAME_EXIST);
        }
        this.createAccountAndParent(fullName, phone, kids);
        return true;
    }

    @Transactional
    @Override
    public List<KidModelImport> convertDataKids(List<KidModelImport> kidModels, UserPrincipal principal) {
        List<KidModelImport> kidModelImports = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        for (KidModelImport model : kidModels) {
            try {
                Optional<Grade> grade = gradeRepository.findByGradeNameAndDelActiveTrueAndSchool_Id(model.getGrade().trim(), idSchool);
                Optional<MaClass> maClass = maClassRepository.findByClassNameAndDelActiveTrueAndIdSchool(model.getClassName().trim(), idSchool);
                if (grade.isPresent() && maClass.isPresent()) {

                    Long check = grade.get().getMaClassList().stream().filter(x -> x.getId().equals(maClass.get().getId())).count();
                    if (check == 1) {

                        CreateKidsRequest createKidsRequest = new CreateKidsRequest();
                        CreateKidMainInforRequest kidMainInforRequest = modelMapper.map(model, CreateKidMainInforRequest.class);
                        kidMainInforRequest.setBirthDay(ConvertData.convertStringToDate(model.getBirthDay()));
                        kidMainInforRequest.setDateStart(ConvertData.convertStringToDate(model.getDateStart()));
                        kidMainInforRequest.setMotherBirthday(ConvertData.convertStringToDate(model.getMotherBirthday()));
                        kidMainInforRequest.setFatherBirthday(ConvertData.convertStringToDate(model.getFatherBirthday()));
                        kidMainInforRequest.setIdGrade(grade.get().getId());
                        kidMainInforRequest.setIdClass(maClass.get().getId());
                        KidsExtraInfoRequest kidsExtraInfo = new KidsExtraInfoRequest();
                        createKidsRequest.setKidMainInfo(kidMainInforRequest);
                        createKidsRequest.setKidsExtraInfo(kidsExtraInfo);
                        List<AppIconResponse> appIconResponseList = new ArrayList<>();
                        List<AppIconParentRequest> parentIconApp;
                        ListAppIconResponse listAppIconResponse = appIconParentService.getAppIconParent(idSchool);
                        if (listAppIconResponse.getAppIconResponseList().size() > 0) {
                            parentIconApp = listMapper.mapList(listAppIconResponse.getAppIconResponseList(), AppIconParentRequest.class);
                        } else {
                            appIconResponseList.addAll(listAppIconResponse.getAppIconResponseList1());
                            appIconResponseList.addAll(listAppIconResponse.getAppIconResponseList2());
                            parentIconApp = listMapper.mapList(appIconResponseList, AppIconParentRequest.class);
                        }
                        createKidsRequest.setParentIconApp(parentIconApp);
                        createKids(principal, createKidsRequest);
                    } else {
                        kidModelImports.add(model);
                    }

                } else {
                    kidModelImports.add(model);
                }
            } catch (Exception e) {
                kidModelImports.add(model);
                logger.info("Lỗi tạo dữ liệu học sinh từ file excel", e);
                continue;
            }
        }
        return kidModelImports;
    }

    @Override
    public boolean sendAccountStudentSms(UserPrincipal principal, List<Long> idStudents) throws
            ExecutionException, InterruptedException {
        List<Kids> kidsList = new ArrayList<>();
        idStudents.forEach(idKid -> {
            Optional<Kids> kids = kidsRepository.findByIdAndDelActiveTrue(idKid);
            kidsList.add(kids.get());
        });
        List<Kids> kidsListFilter = kidsList.stream().filter(x -> (x.getParent() != null && x.isSmsReceive())).collect(Collectors.toList());
        if (!kidsListFilter.isEmpty()) {
            List<AccountLoginResponse> phoneResponseList = phoneCommonService.findAccountParent(kidsListFilter);
            SmsDTO smsDTO = getSmsDTOByShoolId(principal.getIdSchoolLogin());
            WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(SmsTitleConstant.SEND_ACCOUNT_PARENT).orElseThrow();
            int smsNumber = phoneResponseList.size();

            SmsSendCustom smsSendCustom = new SmsSendCustom();
            smsSendCustom.setAppType(AppTypeConstant.SCHOOL);
            smsSendCustom.setIdSchool(principal.getIdSchoolLogin());
            smsSendCustom.setReceivedCount(smsNumber);
            smsSendCustom.setSendTitle(webSystemTitle.getTitle());
            smsSendCustom.setServiceProvider(smsDTO.getSupplierCode());
            smsSendCustom.setSendType(SmsConstant.TYPE_SEND_ACCOUNT);

            smsSendCustom = smsSendCustomRepository.save(smsSendCustom);

            for (AccountLoginResponse x : phoneResponseList) {

                String contentSms = webSystemTitle.getTitle().concat(webSystemTitle.getContent());
                String[] parts = x.getUsername().split("#");
                contentSms = contentSms.replace("{username}", parts[0]);
                contentSms = contentSms.replace("{password}", x.getPasswordShow());

                SmsConvertResponse smsConvertResponse = findSmsService.convertSms(principal, contentSms);
                int totalSend = smsConvertResponse.getSmsConvert().size();

                List<String> phoneSms = Arrays.asList(x.getPhone());
                Future<List<SmsResultDTO>> rs = smsService.sendSms(principal.getIdSchoolLogin(), phoneSms, contentSms);
                List<SmsResultDTO> smsResultDTOS = rs.get();
                SmsReceiversCustom smsReceiversCustom = new SmsReceiversCustom();

                smsResultDTOS.forEach(result -> {
                    String phoneCv = SmsUtils.convertPhone(result.getPhone());
                    Optional<SmsCode> smsCode = smsCodeRepository.findById(result.getErrCodeId());
                    List<Kids> kids = this.getParentPhone(phoneCv, kidsListFilter);

                    if (smsCode.isPresent()) {
                        smsReceiversCustom.setSmsCode(smsCode.get());
                    }
                    smsReceiversCustom.setAppType(AppTypeConstant.PARENT);

                    Parent parent = kids.get(0).getParent();
                    smsReceiversCustom.setIdSchool(kids.get(0).getIdSchool());
                    smsReceiversCustom.setNameUserReceiver(parent.getMaUser().getFullName());
                    smsReceiversCustom.setNumberSms(totalSend);
                    smsReceiversCustom.setPhoneUserReceiver(parent.getMaUser().getPhone());
                    smsReceiversCustom.setIdKid(kids.get(0).getId());
                });
                smsReceiversCustom.setSendContent(contentSms);
                smsReceiversCustom.setSmsSendCustom(smsSendCustom);

                smsReceiversCustomRepository.save(smsReceiversCustom);

            }
        }
        return true;
    }

    @Override
    public List<KidsSmsResponse> seachKidsByClass(UserPrincipal principal, List<Long> idClassList) {
        List<Kids> kids = kidsRepository.findAllKidsClass(principal, idClassList);
        kids = kids.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<KidsSmsResponse> kidsList = listMapper.mapList(kids, KidsSmsResponse.class);
        return kidsList;
    }

    @Override
    public List<KidsSmsResponse> seachKidsByGrade(UserPrincipal principal, List<Long> idGradeList) {
        List<Kids> kids = kidsRepository.findAllKidsGrade(principal, idGradeList);
        kids = kids.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<KidsSmsResponse> kidsList = listMapper.mapList(kids, KidsSmsResponse.class);
        return kidsList;
    }

    @Override
    public ListKidsBirthDayResponse searchKidsBirthDayNew(UserPrincipal principal, SearchKidsBirthDayRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListKidsBirthDayResponse response = new ListKidsBirthDayResponse();
        List<Kids> kidsList = kidsRepository.searchKidsBirthdayNew(idSchool, request);
        long total = kidsRepository.countSearchKidsBirthday(idSchool, request);
        List<KidBirthdayResponse> responseList = listMapper.mapList(kidsList, KidBirthdayResponse.class);
        AtomicReference<Integer> i = new AtomicReference<>(0);
        responseList.forEach(value -> {
            if ((kidsList.get(i.get()).getRepresentation().equalsIgnoreCase(AppConstant.FATHER)) && (kidsList.get(i.get()).getFatherPhone() != null)) {
                value.setKidPhone(kidsList.get(i.get()).getFatherPhone());
            } else if ((kidsList.get(i.get()).getRepresentation().equalsIgnoreCase(AppConstant.MOTHER)) && (kidsList.get(i.get()).getMotherPhone() != null)) {
                value.setKidPhone(kidsList.get(i.get()).getMotherPhone());
            } else value.setKidPhone("");
            i.getAndSet(i.get() + 1);
            int months = ConvertData.convertDateToMonth(value.getBirthDay(), LocalDate.now());
            value.setYearsOld(ConvertData.convertAgeToYears(months));
        });

        response.setTotal(total);
        response.setResponseList(responseList);
        return response;
    }

    @Override
    public boolean mergeKidIntoParent(MergeKidsRequest request) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(request.getIdUser()).orElseThrow();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        kids.setParent(maUser.getParent());
        kidsRepository.save(kids);
        return true;
    }

    @Override
    public KidsInforResponse findKidsInfor(UserPrincipal principal) {
        CommonValidate.checkDataParent(principal);
        KidsInforResponse response = new KidsInforResponse();
        Long idKid = principal.getIdKidLogin();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        response.setAvatar(ConvertData.getAvatarUserSchool(kids.getParent().getMaUser()));
        response.setAddress(StringUtils.isNotBlank(kids.getAddress()) ? kids.getAddress() : "");
        response.setRepresentation(kids.getRepresentation().equals(AppConstant.FATHER) ? AppConstant.FATHER_KEY : AppConstant.MOTHER_KEY);
        List<ParentInforObject> parentList = new ArrayList<>();
        ParentInforObject father = new ParentInforObject();
        ParentInforObject mother = new ParentInforObject();

        father.setFullName(StringUtils.isNotBlank(kids.getFatherName()) ? kids.getFatherName() : "");
        father.setPhone(StringUtils.isNotBlank(kids.getFatherPhone()) ? kids.getFatherPhone() : "");
        father.setEmail(StringUtils.isNotBlank(kids.getFatherEmail()) ? kids.getFatherEmail() : "");
        father.setParent(AppConstant.FATHER_KEY);
        mother.setFullName(StringUtils.isNotBlank(kids.getMotherName()) ? kids.getMotherName() : "");
        mother.setPhone(StringUtils.isNotBlank(kids.getMotherPhone()) ? kids.getMotherPhone() : "");
        mother.setEmail(StringUtils.isNotBlank(kids.getMotherEmail()) ? kids.getMotherEmail() : "");
        mother.setParent(AppConstant.MOTHER_KEY);
        parentList.add(father);
        parentList.add(mother);
        response.setParentList(parentList);
        return response;
    }

    @Override
    public List<KidsInfoDataResponse> findKidsByName(UserPrincipal principal, String fullName) {
        if (StringUtils.isBlank(fullName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.KIDS_NAME);
        }
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndFullNameContaining(principal.getIdSchoolLogin(), fullName);
        return listMapper.mapList(kidsList, KidsInfoDataResponse.class);
    }

    @Transactional
    @Override
    public boolean updateKidsStatus(UserPrincipal principal, StatusCommonRequest request) {
        CommonValidate.checkPlusOrTeacher(principal);
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        KidsStatusTimeline kidsStatusLatest = kidsStatusTimelineRepository.findFirstByKidsIdOrderByStartDateDesc(request.getId()).orElseThrow();
        if (!KidsStatusConstant.LEAVE_SCHOOL.equals(kids.getKidStatus())) {
            this.checkOrderKidsPaymentException(KidsStatusConstant.LEAVE_SCHOOL, kids);
            this.updateStatusForKids(kids, kidsStatusLatest, kids.getKidStatus(), KidsStatusConstant.LEAVE_SCHOOL, request.getDate());
            kids.setKidStatus(KidsStatusConstant.LEAVE_SCHOOL);
            kids.setDateRetain(null);
            kids.setDateLeave(request.getDate());
            kids.setActivated(AppConstant.APP_FALSE);
            kidsRepository.save(kids);
        }
        return true;
    }

    @Override
    @Transactional
    public int updateKidsGroupOut(UserPrincipal principal, KidsGroupOutRequest request) {
        List<Kids> kidsList = kidsRepository.findByIdInAndDelActiveTrue(request.getIdList());
        GroupOutKids groupOutKids = groupOutKidsRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdGroupOut(), principal.getIdSchoolLogin()).orElseThrow();
        LocalDate outDate = request.getDateOut();
        List<Kids> saveList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        kidsList.forEach(x -> {
            long checkOrderLength = this.checkOrderKidsPayment(AppConstant.LEAVE_SCHOOL, x);
            if (checkOrderLength == 0) {
                if (!AppConstant.LEAVE_SCHOOL.equals(x.getKidStatus())) {
                    this.updateOrCreateKidsStatusTimeline(x, AppConstant.LEAVE_SCHOOL, outDate);
                }
                x.setDateLeave(outDate);
                x.setDelActive(AppConstant.APP_FALSE);
                x.setKidStatus(AppConstant.LEAVE_SCHOOL);
                x.setOutDate(outDate);
                x.setGroupOutKids(groupOutKids);
                saveList.add(x);
            } else {
                i.getAndIncrement();
            }
        });
        kidsRepository.saveAll(saveList);
        return i.get();
    }

    @Override
    public boolean restoreKidsGroupOut(UserPrincipal principal, Long id, Long idClass) throws IOException {
        Kids kids = kidsRepository.findByIdAndIdSchool(id, principal.getIdSchoolLogin()).orElseThrow();
        //chuyển lớp cho học sinh
        if (!kids.getMaClass().getId().equals(idClass)) {
            KidsChangeClassRequest request = new KidsChangeClassRequest();
            KidsActionRequest kidsActionRequest = new KidsActionRequest();
            request.setIdClass(idClass);
            kidsActionRequest.setId(id);
            request.setKidList(Collections.singletonList(kidsActionRequest));
            this.actionChangeClassKids(principal, request);
        }
        //cập nhật trạng thái học sinh về đang học
        this.updateOrCreateKidsStatusTimeline(kids, KidsStatusConstant.STUDYING, LocalDate.now());
        kids.setKidStatus(KidsStatusConstant.STUDYING);
        kids.setDelActive(AppConstant.APP_TRUE);
        kids.setGroupOutKids(null);
        kids.setOutDate(null);
        kids.setDateLeave(null);
        kidsRepository.save(kids);
        return true;
    }

    @Override
    public ListStudentResponse searchKidsService(UserPrincipal principal, SearchKidsRequest request) {
        CommonValidate.checkPlusOrTeacher(principal);
        List<Kids> kidsList = kidsRepository.searchKids(principal.getIdSchoolLogin(), request);
        long count = kidsRepository.countSearchKids(principal.getIdSchoolLogin(), request);
        ListStudentResponse response = new ListStudentResponse();
        List<StudentResponse> dataList = new ArrayList<>();
        for (Kids kids : kidsList) {
            StudentResponse model = modelMapper.map(kids, StudentResponse.class);
            model.setPhone(this.getPhoneAccountParent(kids));
            model.setLogin(this.getLoginParent(kids));
            model.setUsername(this.getUsername(kids));
            model.setPassword(this.getPassword(kids));
            model.setPhoneRepresentation(this.getPhoneRepresentation(kids));
            dataList.add(model);
        }
        if (StringUtils.isNotBlank(request.getLoginStatus())) {
            dataList = dataList.stream().filter(x -> x.getLogin().equals(request.getLoginStatus())).collect(Collectors.toList());
            count = dataList.size();
        }
        response.setDataList(dataList);
        response.setTotal(count);
        return response;
    }


    @Override
    public ResponseEntity searchKidsTransferService(UserPrincipal principal, SearchKidsTransferRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.searchKidsTransfer(idSchool, request);
        long count = kidsRepository.countSearchKidsTransfer(idSchool, request);
        List<StudentTransferResponse> data = new ArrayList<>();
        kidsList.forEach(x -> {
            StudentTransferResponse model = modelMapper.map(x, StudentTransferResponse.class);
            model.setCount((int) x.getKidsTransferList().stream().filter(BaseEntity::isDelActive).count());
            data.add(model);
        });
        return NewDataResponse.setDataSearchTotal(data, count);
    }


    @Override
    public ResponseEntity searchKidsTransferByIdService(UserPrincipal principal, Long idKid) {
        List<KidsTransfer> list = kidsTransferRepository.findByKidsIdAndDelActiveTrueOrderByIdDesc(idKid);
        List<KidsTransferResponse> data = listMapper.mapList(list, KidsTransferResponse.class);
        return NewDataResponse.setDataSearch(data);
    }

    @Override
    public void kidsTransferCreateService(UserPrincipal principal, KidsTransferCreateRequest request) throws IOException {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        this.resetKidsTransferInOutStatus(kids.getId(), request.isInStatus(), request.isOutStatus());
        KidsTransfer entity = modelMapper.map(request, KidsTransfer.class);
        entity.setKids(kids);
        if (Objects.nonNull(request.getMultipartFile())) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getMultipartFile(), principal.getIdSchoolLogin(), UploadDownloadConstant.KHAC);
            entity.setName(handleFileResponse.getName());
            entity.setUrl(handleFileResponse.getUrlWeb());
            entity.setUrlLocal(handleFileResponse.getUrlLocal());
        }
        kidsTransferRepository.save(entity);
    }

    @Override
    public void kidsTransferUpdateService(UserPrincipal principal, KidsTransferUpdateRequest request) throws IOException {
        KidsTransfer entity = kidsTransferRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        this.resetKidsTransferInOutStatus(entity.getKids().getId(), request.isInStatus(), request.isOutStatus());
        if (Objects.nonNull(request.getMultipartFile())) {
            HandleFileUtils.deletePictureInFolder(entity.getUrlLocal());
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getMultipartFile(), principal.getIdSchoolLogin(), UploadDownloadConstant.KHAC);
            entity.setName(handleFileResponse.getName());
            entity.setUrl(handleFileResponse.getUrlWeb());
            entity.setUrlLocal(handleFileResponse.getUrlLocal());
        }
        modelMapper.map(request, entity);
        kidsTransferRepository.save(entity);
    }

    @Override
    public void kidsTransferInStatusByIdService(UserPrincipal principal, Long id, boolean status) {
        KidsTransfer entity = kidsTransferRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if (status) {
            this.resetKidsTransferInOutStatus(entity.getKids().getId(), status, false);
        }
        entity.setInStatus(status);
        kidsTransferRepository.save(entity);
    }

    @Override
    public void kidsTransferOutStatusByIdService(UserPrincipal principal, Long id, boolean status) {
        KidsTransfer entity = kidsTransferRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if (status) {
            this.resetKidsTransferInOutStatus(entity.getKids().getId(), false, true);
        }
        entity.setOutStatus(status);
        kidsTransferRepository.save(entity);
    }

    @Override
    public void kidsTransferDeleteByIdService(UserPrincipal principal, Long id) {
        KidsTransfer kidsTransfer = kidsTransferRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        kidsTransfer.setDelActive(false);
        kidsTransferRepository.save(kidsTransfer);
    }

    @Override
    public void kidsTransferDeleteByIdKidListService(UserPrincipal principal, List<Long> idKidList) {
        List<KidsTransfer> kidsTransferList = kidsTransferRepository.findByKidsIdInAndDelActiveTrue(idKidList);
        kidsTransferList.forEach(x -> x.setDelActive(false));
        kidsTransferRepository.saveAll(kidsTransferList);
    }

    /**
     * reset trạng thái đưa, đón của học sinh
     *
     * @param idKid
     * @param inStatus
     * @param outStatus
     */
    @Override
    public void resetKidsTransferInOutStatus(Long idKid, boolean inStatus, boolean outStatus) {
        List<KidsTransfer> kidsTransferInList = new ArrayList<>();
        List<KidsTransfer> kidsTransferOutList = new ArrayList<>();
        if (inStatus) {
            kidsTransferInList = kidsTransferRepository.findByKidsIdAndInStatusTrueAndDelActiveTrue(idKid);
            kidsTransferInList.forEach(x -> x.setInStatus(false));
        }
        if (outStatus) {
            kidsTransferOutList = kidsTransferRepository.findByKidsIdAndOutStatusTrueAndDelActiveTrue(idKid);
            kidsTransferOutList.forEach(x -> x.setOutStatus(false));
        }
        List<KidsTransfer> kidsTransferList = ListUtils.union(kidsTransferInList, kidsTransferOutList);
        kidsTransferRepository.saveAll(kidsTransferList);
    }

    @Override
    public ListStudentGroupOutResponse searchKidsGroupOut(UserPrincipal principal, SearchKidsGroupOutRequest request) {
        List<Kids> kidsList = kidsRepository.searchKidsGroupOut(principal.getIdSchoolLogin(), request);
        long count = kidsRepository.countSearchKidsGroupOut(principal.getIdSchoolLogin(), request);
        ListStudentGroupOutResponse response = new ListStudentGroupOutResponse();
        List<StudentGroupOutResponse> dataList = new ArrayList<>();
        for (Kids kids : kidsList) {
            StudentGroupOutResponse model = modelMapper.map(kids, StudentGroupOutResponse.class);
            dataList.add(model);
        }
        response.setDataList(dataList);
        response.setTotal(count);
        return response;
    }

    @Override
    public ListStudentGroupOutResponse searchKidsGroupOutExcel(UserPrincipal principal, ExcelGroupOutRequest request) {
        List<Kids> kidsList = kidsRepository.searchKidsGroupOutExcel(principal.getIdSchoolLogin(), request);
        ListStudentGroupOutResponse response = new ListStudentGroupOutResponse();
        List<StudentGroupOutResponse> dataList = new ArrayList<>();
        for (Kids kids : kidsList) {
            StudentGroupOutResponse model = modelMapper.map(kids, StudentGroupOutResponse.class);
            dataList.add(model);
        }
        response.setDataList(dataList);
        return response;
    }

    @Override
    public StudentGroupOutDetailResponse searchKidsGroupOutById(UserPrincipal principal, Long id) {
        Kids kids = kidsRepository.findByIdAndIdSchool(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("id kids Not found"));
        StudentGroupOutDetailResponse response = modelMapper.map(kids, StudentGroupOutDetailResponse.class);
        response.setIdClass(kids.getMaClass().getId());
        response.setClassName(kids.getMaClass().getClassName());
        Grade grade = gradeRepository.findById(kids.getIdGrade()).orElseThrow();
        response.setGradeName(grade.getGradeName());
        return response;
    }


    @Override
    public ListStudentAdminResponse searchKidsAdmin(KidsSearchAdminRequest request) {
        List<Long> idSchoolList = ConvertData.getIdSchoolListInAgent(schoolService.findSchoolInAgent(request.getIdAgent()));
        List<Kids> kidsList = kidsRepository.searchKidsAdmin(request, idSchoolList);
        long count = kidsRepository.countSearchAdminKids(request, idSchoolList);
        ListStudentAdminResponse response = new ListStudentAdminResponse();
        List<StudentAdminResponse> dataList = new ArrayList<>();
        kidsList.forEach(kids -> {
            StudentAdminResponse model = modelMapper.map(kids, StudentAdminResponse.class);
            model.setPhone(this.getPhoneAccountParent(kids));
            model.setPhoneRepresentation(this.getPhoneRepresentation(kids));
            model.setLogin(this.getLoginParent(kids));
            model.setUsername(this.getUsername(kids));
            model.setPassword(this.getPassword(kids));
            model.setSchoolName(SchoolUtils.getSchoolName(kids.getIdSchool()));
            model.setInvalidAccount(this.getInvalidaAccount(kids));
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setTotal(count);
        return response;
    }

    private boolean getInvalidaAccount(Kids kids) {
        boolean check = false;
        if (kids.getParent() != null) {
            String username = kids.getParent().getMaUser().getUsername();
            String extendUserString = ConvertData.getExtendUsernameString(username);
            if (StringUtils.isNotBlank(extendUserString)) {
                check = true;
            }
        }
        return check;
    }


    @Override
    @Transactional
    public boolean createKids(UserPrincipal principal, CreateKidsRequest createStudentRequest) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        //check trạng thái và ngày nhập học
        this.checkDateStartAndStatus(createStudentRequest.getKidMainInfo().getKidStatus(), createStudentRequest.getKidMainInfo().getDateStart());
        Long idSchool = principal.getIdSchoolLogin();
        Kids newKids = modelMapper.map(createStudentRequest.getKidMainInfo(), Kids.class);
        MaClass maClass = maClassRepository.findByIdMaClass(idSchool, createStudentRequest.getKidMainInfo().getIdClass()).orElseThrow();
        newKids.setMaClass(maClass);
        newKids.setIdSchool(idSchool);

        this.setFullNameKid(newKids, newKids.getFullName());

        newKids.setActivated(this.getActiveStatus(createStudentRequest.getKidMainInfo().getKidStatus()));
        newKids.setIdSchool(idSchool);
        newKids.setKidCode(GenerateCode.codeKid());
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        newKids.setVerifyCodeSchool(GenerateCode.getNumber());
        newKids.setVerifyCodeAdmin(sysInfor.getVerificationCode());
        this.createUrlAvatarKids(newKids);
        Kids savedKids = kidsRepository.save(newKids);

        /**
         * tạo các thông tin liên quan đến học sinh
         */
        this.setCreateExtraKids(savedKids);
        CreateKidMainInforRequest createKidMainInforRequest = createStudentRequest.getKidMainInfo();
        String phone = null;
        String fullName = null;
        //create parents
        if (AppConstant.FATHER.equals(createKidMainInforRequest.getRepresentation()) && StringUtils.isNotBlank(createKidMainInforRequest.getFatherPhone())) {
            phone = createKidMainInforRequest.getFatherPhone();
            fullName = createKidMainInforRequest.getFatherName();
        } else if (AppConstant.MOTHER.equals(createKidMainInforRequest.getRepresentation()) && StringUtils.isNotBlank(createKidMainInforRequest.getMotherPhone())) {
            phone = createKidMainInforRequest.getMotherPhone();
            fullName = createKidMainInforRequest.getMotherName();
        }
        //create account and parent
        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(fullName)) {
            this.createAccountAndParent(fullName, phone, savedKids);
        }
        //tạo icon cho parent
        appIconParentAddSerivce.createAppIconParentAdd(idSchool, savedKids, createStudentRequest.getParentIconApp());
        //tạo thông tin mở rộng cho học sinh
        kidsExtraInfoService.createKidsExtraInfo(idSchool, savedKids, createStudentRequest.getKidsExtraInfo());
        return true;
    }


    @Override
    public void createAvatar(UserPrincipal principal, MultipartFile multipartFile) throws IOException {
        String fileName = null;
        if (multipartFile != null) {
            fileName = HandleFileUtils.getFileNameOfSchool(principal.getIdSchoolLogin(), multipartFile);
            String urlFolder = HandleFileUtils.getUrl(principal.getIdSchoolLogin(), UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR);
            HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);
        }
        this.fileNameAvatar = fileName;
    }


    @Transactional
    @Override
    public boolean deleteKids(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        CommonUtil.checkDeleteObject(principal);
        this.deleteKidsCommon(id, principal.getIdSchoolLogin());
        return true;
    }

    @Transactional
    @Override
    public boolean deleteKidsAdmin(Long id) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        this.deleteKids(kids);
        return true;
    }

    @Transactional
    @Override
    public boolean restoreKidsAdmin(Long id) {
        Kids kids = kidsRepository.findByIdAndDelActive(id, AppConstant.APP_FALSE).orElseThrow();
        kids.setDelActive(AppConstant.APP_TRUE);
        kids.setOutDate(null);
        this.createAttendanceAndEvaluate(kids);
        return true;
    }

    @Override
    public KidResponse findIdKidExtra(Long id) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        KidResponse kidResponse = new KidResponse();
        KidMainInforResponse kidMainInforResponse = modelMapper.map(kids, KidMainInforResponse.class);
        kidMainInforResponse.setIdClass(kids.getMaClass().getId());
        kidMainInforResponse.setClassName(kids.getMaClass().getClassName());
        Grade grade = gradeRepository.findById(kids.getIdGrade()).orElseThrow();
        kidMainInforResponse.setGradeName(grade.getGradeName());
        kidResponse.setKidMainInfo(kidMainInforResponse);
        return kidResponse;
    }

    @Transactional
    @Override
    public boolean updateKids(UserPrincipal principal, Long idUrl, UpdateKidsRequest updateStudentRequest) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        CommonValidate.checkMatchIdUrlWithBody(idUrl, updateStudentRequest.getKidMainInfo().getId());
        Long idSchool = principal.getIdSchoolLogin();
        UpdateKidMainInforRequest updateKidMainInforRequest = updateStudentRequest.getKidMainInfo();
        this.checkStatusDate(updateKidMainInforRequest);
        Kids odlKids = kidsRepository.findByIdKid(idSchool, updateKidMainInforRequest.getId()).orElseThrow(() -> new NotFoundException("not found kids by id"));
        this.checkOrderKidsPaymentException(updateKidMainInforRequest.getKidStatus(), odlKids);
        this.createEvaluateAndAttendanceForUpdate(odlKids, updateKidMainInforRequest.getKidStatus());
        odlKids.setActivated(this.getActiveStatus(updateStudentRequest.getKidMainInfo().getKidStatus()));
        this.setFullNameKid(odlKids, updateKidMainInforRequest.getFullName());
        //cập nhật thông tin khi cập nhật trạng thái học sinh
        LocalDate newDateWithStatus = this.getDateWithKidsStatus(updateStudentRequest.getKidMainInfo());
        this.updateOrCreateKidsStatusTimeline(odlKids, updateStudentRequest.getKidMainInfo().getKidStatus(), newDateWithStatus);
        modelMapper.map(updateKidMainInforRequest, odlKids);
        Parent oldParent = odlKids.getParent();
        if (oldParent == null) {
            String phone = null;
            String fullName = null;
            if (AppConstant.FATHER.equals(updateStudentRequest.getKidMainInfo().getRepresentation()) && StringUtils.isNotBlank(updateStudentRequest.getKidMainInfo().getFatherPhone())) {
                phone = updateStudentRequest.getKidMainInfo().getFatherPhone();
                fullName = updateStudentRequest.getKidMainInfo().getFatherName();
            } else if (AppConstant.MOTHER.equals(updateStudentRequest.getKidMainInfo().getRepresentation()) && StringUtils.isNotBlank(updateStudentRequest.getKidMainInfo().getMotherPhone())) {
                phone = updateStudentRequest.getKidMainInfo().getMotherPhone();
                fullName = updateStudentRequest.getKidMainInfo().getMotherName();
            }
            if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(fullName)) {
                this.createAccountAndParent(fullName, phone, odlKids);
            }
        }
        this.updateUrlAvatarKids(odlKids);
        Kids newKid = kidsRepository.save(odlKids);
        appIconParentAddSerivce.updateAppIconParentAdd(idSchool, newKid, updateStudentRequest.getParentIconApp());
        kidsExtraInfoService.updateKidsExtraInfo(idSchool, newKid, updateStudentRequest.getKidsExtraInfo());
        return true;
    }


    private void updateOrCreateKidsStatusTimeline(Kids oldKids, String newStatus, LocalDate newDate) {
        if (newDate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NO_DATE);
        }
        Long idKid = oldKids.getId();
        String oldStatus = oldKids.getKidStatus();
        //lấy ra lần thay đổi trạng thái mới nhất
        KidsStatusTimeline kidsStatusLatest = kidsStatusTimelineRepository.findFirstByKidsIdOrderByStartDateDesc(idKid).orElseThrow();
        //cùng trạng thái
        if (newStatus.equals(oldKids.getKidStatus())) {
            //trường hợp bao lưu hoặc nghỉ học
            if (newStatus.equals(KidsStatusConstant.RESERVE) || newStatus.equals(KidsStatusConstant.LEAVE_SCHOOL)) {
                //cập nhật lại ngày bảo lưu hoặc nghỉ học
                if (!kidsStatusLatest.getStartDate().isEqual(newDate)) {
                    //lấy ra lần thay đổi trạng thái trước đó
                    List<KidsStatusTimeline> kidsStatusTimelineList = kidsStatusTimelineRepository.findByKidsIdOrderByStartDateDesc(idKid);
                    KidsStatusTimeline statusTimelineBefore = kidsStatusTimelineList.get(1);
                    LocalDate startDateBefore = statusTimelineBefore.getStartDate();
                    if (newDate.isBefore(startDateBefore) || newDate.isEqual(startDateBefore)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày " + StudentUtil.getConvertKidStatus(newStatus) + " phải lớn ngày " + ConvertData.formartDateDash(startDateBefore));
                    }
                    //set lại 2 khoảng thời gian cho lần trước và lần hiện tại
                    statusTimelineBefore.setEndDate(newDate.minusDays(1));
                    kidsStatusLatest.setStartDate(newDate);
                    kidsStatusTimelineRepository.save(statusTimelineBefore);
                    kidsStatusTimelineRepository.save(kidsStatusLatest);
                }
            }
        } else {
            //khác trạng thái
            this.updateStatusForKids(oldKids, kidsStatusLatest, oldStatus, newStatus, newDate);
//            boolean check = this.checkStartDateEqualNewDate(oldStatus, newStatus, kidsStatusLatest.getStartDate(), newDate);
////            cùng ngày
//            if (check) {
//                kidsStatusLatest.setStatus(newStatus);
//                kidsStatusTimelineRepository.save(kidsStatusLatest);
//            } else {
//                //lớn hơn ngày trước đó
//                //cập nhật lại ngày kết thúc cho lần gần nhất
//                kidsStatusLatest.setEndDate(newDate.minusDays(1));
//                //tạo lần mới
//                KidsStatusTimeline entity = new KidsStatusTimeline();
//                entity.setStartDate(newDate);
//                entity.setStatus(newStatus);
//                entity.setKids(oldKids);
//                kidsStatusTimelineRepository.save(entity);
//            }
        }
    }

    private void updateStatusForKids(Kids kids, KidsStatusTimeline kidsStatusLatest, String oldStatus, String newStatus, LocalDate newDate) {
        //khác trạng thái
        boolean check = this.checkStartDateEqualNewDate(oldStatus, newStatus, kidsStatusLatest.getStartDate(), newDate);
//            cùng ngày
        if (check) {
            kidsStatusLatest.setStatus(newStatus);
            kidsStatusTimelineRepository.save(kidsStatusLatest);
        } else {
            //lớn hơn ngày trước đó
            //cập nhật lại ngày kết thúc cho lần gần nhất
            kidsStatusLatest.setEndDate(newDate.minusDays(1));
            //tạo lần mới
            KidsStatusTimeline entity = new KidsStatusTimeline();
            entity.setStartDate(newDate);
            entity.setStatus(newStatus);
            entity.setKids(kids);
            kidsStatusTimelineRepository.save(entity);
        }
    }

    /**
     * check ngày tạo trước đó và trạng thái thay đổi ngày có trùng nhau ko
     *
     * @param startDate
     * @param newDate
     * @return
     */
    private boolean checkStartDateEqualNewDate(String oldStatus, String newStatus, LocalDate startDate, LocalDate newDate) {
        if (newDate.isBefore(startDate)) {
            if (newStatus.equals(KidsStatusConstant.STUDYING) || newStatus.equals(KidsStatusConstant.STUDY_WAIT)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh đang ở trạng thái " + StudentUtil.getConvertKidStatus(oldStatus).toLowerCase() + " từ ngày " + ConvertData.formartDateDash(startDate) + ", vui lòng chọn lại từ ngày " + ConvertData.formartDateDash(startDate));
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày " + StudentUtil.getConvertKidStatus(newStatus) + " phải lớn hoặc bằng ngày " + ConvertData.formartDateDash(startDate));
        }
        return newDate.isEqual(startDate);
    }

    /**
     * lấy ngày tương ứng với trạng thái
     *
     * @param mainInforRequest
     * @return
     */
    private LocalDate getDateWithKidsStatus(UpdateKidMainInforRequest mainInforRequest) {
        if (mainInforRequest.getKidStatus().equals(KidsStatusConstant.STUDYING) || mainInforRequest.getKidStatus().equals(KidsStatusConstant.STUDY_WAIT)) {
            return LocalDate.now();
        }
        if (mainInforRequest.getKidStatus().equals(KidsStatusConstant.RESERVE)) {
            return mainInforRequest.getDateRetain();
        }
        if (mainInforRequest.getKidStatus().equals(KidsStatusConstant.LEAVE_SCHOOL)) {
            return mainInforRequest.getDateLeave();
        }
        return null;
    }

    @Transactional
    @Override
    public boolean updateKidExtraAdmin(UpdateKidsAdminRequest request) {
        UpdateKidMainInforRequest updateKidMainInforRequest = request.getKidMainInfo();
        this.checkStatusDate(updateKidMainInforRequest);
        Kids odlKids = kidsRepository.findByIdAndDelActiveTrue(updateKidMainInforRequest.getId()).orElseThrow();
        modelMapper.map(updateKidMainInforRequest, odlKids);
        this.checkCreateAccount(odlKids, request);
        kidsRepository.save(odlKids);
        return true;
    }

    @Override
    public boolean updateActiveOneKids(Long idSchool, ActivedOneKidsRequest updateActivedOneKids) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(updateActivedOneKids.getId()).orElseThrow();
        kids.setActivated(updateActivedOneKids.getCheckOneActive());
        kidsRepository.save(kids);
        return true;
    }

    @Transactional
    @Override
    public void actionActiveManyKids(UserPrincipal principal, List<KidsActionRequest> kidsActionRequestList, boolean active) {
        CommonValidate.checkDataPlus(principal);
        kidsActionRequestList.forEach(x -> {
            Kids kid = kidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
            kid.setActivated(active);
            kidsRepository.save(kid);
        });
    }

    @Transactional
    @Override
    public boolean updateActiveKidsSMS(ActivedOneKidsSMSRequest request) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        kids.setSmsReceive(request.getCheckOneActiveSMS());
        kidsRepository.save(kids);
        return true;
    }

    @Transactional
    @Override
    public void actionActiveManyKidsSMS(List<KidsActionRequest> kidsActionRequestList, boolean activeSMS) {
        kidsActionRequestList.forEach(x -> {
            Kids kid = kidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
            kid.setSmsReceive(activeSMS);
            kidsRepository.save(kid);
        });
    }

    @Transactional
    @Override
    public boolean actionChangeClassKids(UserPrincipal principal, KidsChangeClassRequest request) throws IOException {
        Long idClassInput = request.getIdClass();
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClassInput).orElseThrow();
        List<KidsActionRequest> kidsActionRequestList = request.getKidList();
        for (KidsActionRequest x : kidsActionRequestList) {
            Kids kid = kidsRepository.findById(x.getId()).orElseThrow();
            //class chuyển khác class hiện tại
            if (!kid.getMaClass().getId().equals(idClassInput)) {
                maKidPicsService.deleteDataKidPics(kid);
//                this.changeFileJson(kid, request, principal);
                this.updateKidClass(kid.getId(), kid.getMaClass().getId());
                kid.setMaClass(maClass);
                kid.setIdGrade(maClass.getGrade().getId());

                Kids newKid = kidsRepository.save(kid);
                //tạo thông tin bảng chuyển lớp
                this.createKidsClassWhenChangeClass(newKid);
                //tạo thông tin các bảng liên quan
                this.createExtraChangeClass(newKid);
            }
        }
        return true;
    }


    @Transactional
    @Override
    public boolean deleteManyKid(UserPrincipal principal, List<KidsActionRequest> kidsActionRequestList) {
        CommonValidate.checkDataPlus(principal);
        if (CollectionUtils.isEmpty(kidsActionRequestList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.LIST_EMPTY);
        }
        Long idSchool = principal.getIdSchoolLogin();
        kidsActionRequestList.forEach(x -> this.deleteKidsCommon(x.getId(), idSchool));
        return true;
    }

    /**
     * tìm kiếm học sinh theo các fied
     *
     * @param searchKidsExportRequest
     * @return
     */
    @Override
    public List<KidsExportResponse> searchKidsByGradeClass(Long idSchool, SearchKidsExportRequest
            searchKidsExportRequest) {
        List<Kids> kidsOptional = new ArrayList<>();
        if (searchKidsExportRequest.getIdKidsList() != null) {
            for (Long id : searchKidsExportRequest.getIdKidsList()) {
                Kids kids = kidsRepository.findById(id).stream().findFirst().orElse(null);
                kidsOptional.add(kids);
            }
        } else if (searchKidsExportRequest.getIdGroup() != null) {
            KidsGroup kidsGroup = kidsGroupRepository.findByIdKidsGroup(idSchool, searchKidsExportRequest.getIdGroup()).stream().findFirst().orElse(null);
            if (kidsGroup != null) {
                kidsOptional = kidsGroup.getKidsList();
            }
        } else {
            kidsOptional = kidsRepository.searchKidsByGradeClass(idSchool, searchKidsExportRequest);
            if (kidsOptional.isEmpty()) {
                return null;
            }
        }

        List<KidsExportResponse> kidsExportResponses = new ArrayList<>();

        kidsOptional.forEach(kids -> {
            KidsExportResponse kidResponse = new KidsExportResponse();
            KidMainExportRespone kidMainExportRespone = modelMapper.map(kids, KidMainExportRespone.class);

            if (kids.getParent() != null) {
                ParentInforResponse parentInforResponse = parentService.findByIdParent(kids.getParent().getId()).stream().findFirst().orElse(null);
                kidResponse.setParentInfo(parentInforResponse);
            }

//            MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, kidMainExportRespone.getMaClasses()).stream().findFirst().orElse(null);

            GradeDTO gradeDTO = gradeService.findByIdGrade(idSchool, kidMainExportRespone.getIdGrade()).stream().findFirst().orElse(null);


            MaClassDTO classDTO = maClassService.findByIdMaClass(idSchool, kidMainExportRespone.getMaClass().getId()).stream().findFirst().orElse(null);

            if (searchKidsExportRequest.getIdGroup() != null) {
                KidsGroupResponse kidsGroupResponse = kidsGroupService.findByIdKidsGroup(idSchool, searchKidsExportRequest.getIdGroup()).stream().findFirst().orElse(null);
                kidResponse.setKidsGroupResponse(kidsGroupResponse);
            }
            List<String> kidsGroupList = kids.getKidsGroupList().stream().map(KidsGroup::getGroupName).collect(Collectors.toList());
            kidResponse.setKidsGroup(kidsGroupList.toString());

            kidResponse.setKidMainInfo(kidMainExportRespone);

            kidResponse.setGradeDTO(gradeDTO);
            kidResponse.setMaClassDTO(classDTO);
            kidsExportResponses.add(kidResponse);

        });
//        ListKidsResponse listKidsResponse = new ListKidsResponse(kidsDTOList);
//        return (List<KidsExportResponse>) listKidsResponse;
        return kidsExportResponses;
    }

    /**
     * Chuyển đổi đối tượng KidsExportResponse thành KidVM để đổ dữ liệu lên excel
     *
     * @param exportResponseList
     * @return
     */
    @Override
    public List<KidModel> getFileAllKidByGrade(List<KidsExportResponse> exportResponseList, String nameSchool) {

//        List<KidModel> kidModelList = new ArrayList<>();
//
////        SchoolDTO school = schoolService.findByIdSchool(principal.getIdSchoolLogin()).stream().findFirst().orElse(null);
////        String nameSchool  = school.getSchoolName()
//
//        long i = 1;
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//
//        for (KidsExportResponse kidsExportResponse : exportResponseList) {
//            KidModel kidModel = new KidModel();
//            kidModel.setStt(i++);
//
//            String checkStatus = kidsExportResponse.getKidMainInfo().getKidStatus();
//            switch (checkStatus) {
//                case "STUDYING":
//                    kidModel.setKidStatus(StudentStatusEnum.STUDYING.getValue());
//                    break;
//                case "STUDY_WAIT":
//                    kidModel.setKidStatus(StudentStatusEnum.STUDY_WAIT.getValue());
//                    break;
//                case "RESERVE":
//                    kidModel.setKidStatus(StudentStatusEnum.RESERVE.getValue());
//                    break;
//                case "LEAVE_SCHOOL":
//                    kidModel.setKidStatus(StudentStatusEnum.LEAVE_SCHOOL.getValue());
//                    break;
//                default:
//                    kidModel.setKidStatus(kidsExportResponse.getKidMainInfo().getKidStatus());
//
//            }
//            if (kidsExportResponse.getGradeDTO() != null) {
//                kidModel.setGrade(kidsExportResponse.getGradeDTO().getGradeName());
//            }
//            if (kidsExportResponse.getMaClassDTO() != null) {
//                kidModel.setClassName(kidsExportResponse.getMaClassDTO().getClassName());
//            }
//            if (kidsExportResponse.getKidsGroupResponse() != null) {
//                kidModel.setGroupName(kidsExportResponse.getKidsGroupResponse().getGroupName());
//            }
//
//
//            kidModel.setFullName(kidsExportResponse.getKidMainInfo().getFullName());
//            if (kidsExportResponse.getKidMainInfo().getBirthDay() != null) {
//                String birthday = kidsExportResponse.getKidMainInfo().getBirthDay().format(formatter);
//                kidModel.setBirthDay(birthday);
//            } else {
//                kidModel.setBirthDay("");
//            }
//            kidModel.setGender(kidsExportResponse.getKidMainInfo().getGender());
//            kidModel.setNickName(kidsExportResponse.getKidMainInfo().getNickName());
//            kidModel.setAddress(kidsExportResponse.getKidMainInfo().getAddress());
//
//            if (kidsExportResponse.getKidMainInfo().getDateStart() != null) {
//                String dateStart = kidsExportResponse.getKidMainInfo().getDateStart().format(formatter);
//                kidModel.setDateStart(dateStart);
//            } else {
//                kidModel.setDateStart("");
//            }
//            if (kidsExportResponse.getKidMainInfo().getDateRetain() != null) {
//                String dateRetain = kidsExportResponse.getKidMainInfo().getDateRetain().format(formatter);
//                kidModel.setDateRetain(dateRetain);
//            } else {
//                kidModel.setDateRetain("");
//            }
//            if (kidsExportResponse.getKidMainInfo().getDateLeave() != null) {
//                String dateLeave = kidsExportResponse.getKidMainInfo().getDateLeave().format(formatter);
//                kidModel.setDateLeave(dateLeave);
//            } else {
//                kidModel.setDateLeave("");
//            }
//            if (kidsExportResponse.getParentInfo() != null) {
//                kidModel.setRepresentation(kidsExportResponse.getParentInfo().getRepresentation());
//                kidModel.setMotherName(kidsExportResponse.getParentInfo().getMotherName());
//                if (kidsExportResponse.getParentInfo().getMotherBirthday() != null) {
//                    String motherBirthday = kidsExportResponse.getParentInfo().getMotherBirthday().format(formatter);
//                    kidModel.setMotherBirthday(motherBirthday);
//                } else {
//                    kidModel.setMotherBirthday("");
//                }
//                kidModel.setMotherPhone(kidsExportResponse.getParentInfo().getMotherPhone());
//                kidModel.setMotherEmail(kidsExportResponse.getParentInfo().getMotherEmail());
//                kidModel.setMotherJob(kidsExportResponse.getParentInfo().getMotherJob());
//                kidModel.setFatherName(kidsExportResponse.getParentInfo().getFatherName());
//                kidModel.setFatherPhone(kidsExportResponse.getParentInfo().getFatherPhone());
//                if (kidsExportResponse.getParentInfo().getFatherBirthday() != null) {
//                    String fatherBirthday = kidsExportResponse.getParentInfo().getFatherBirthday().format(formatter);
//                    kidModel.setFatherBirthday(fatherBirthday);
//                } else {
//                    kidModel.setFatherBirthday("");
//                }
//                kidModel.setFatherEmail(kidsExportResponse.getParentInfo().getFatherEmail());
//                kidModel.setFatherJob(kidsExportResponse.getParentInfo().getFatherJob());
//            }
//            kidModel.setNote(kidsExportResponse.getKidMainInfo().getNote());
//
//            kidModelList.add(kidModel);
//        }
//
//        return kidModelList;
        return null;
    }

    /**
     * Chuyển đổi đối tượng KidsExportResponse thành KidVM để đổ dữ liệu lên excel NEW
     *
     * @param request
     * @return
     */
    @Override
    public List<ExcelNewResponse> getFileAllKidByGradeNew(SearchKidsExportRequest request, UserPrincipal principal) {
        List<ExcelNewResponse> responseList = new ArrayList<>();
        ExcelNewResponse response = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();
        List<Kids> kidsList = new ArrayList<>();
        Long idSchool = SchoolUtils.getIdSchool();
        if (!CollectionUtils.isEmpty(request.getIdKidsList())) {
            kidsList = kidsRepository.findKidsByIdList(request.getIdKidsList());
        } else if (Objects.nonNull(request.getIdGrade())) {
            kidsList = kidsRepository.findByIdSchoolAndIdGradeAndDelActiveTrue(idSchool, request.getIdGrade());
        } else if (Objects.nonNull(request.getIdClass())) {
            kidsList = kidsRepository.findByIdSchoolAndMaClassIdAndDelActiveTrue(idSchool, request.getIdClass());
        } else if (Objects.nonNull(request.getIdGroup())) {
            Optional<KidsGroup> kidsGroupOptional = kidsGroupRepository.findByIdKidsGroup(idSchool, request.getIdGroup());
            if (kidsGroupOptional.isPresent()) {
                kidsList = kidsGroupOptional.get().getKidsList();
            }
        }
        List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC SINH", AppConstant.EXCEL_SCHOOL + principal.getSchool().getSchoolName(), AppConstant.EXCEL_DATE + ConvertData.convertLocalDateToString(LocalDate.now()), "");
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        response.setSheetName("Danh_sach_hoc_sinh");
        response.setHeaderList(headerList);
        AtomicInteger i = new AtomicInteger();
        kidsList.forEach(x -> {
            i.getAndIncrement();
            List<String> bodyStringList = Arrays.asList(String.valueOf(i), StudentUtil.getConvertKidStatus(x.getKidStatus()), x.getMaClass().getGrade().getGradeName(), x.getMaClass().getClassName(), CollectionUtils.isEmpty(x.getKidsGroupList()) ? "" : x.getKidsGroupList().stream().map(KidsGroup::getGroupName).collect(Collectors.joining("\n")), x.getFullName(), ConvertData.convertLocalDateToString(x.getBirthDay()), x.getGender(),
                    x.getNickName(), x.getAddress(), x.getPermanentAddress(), x.getEthnic(), x.getIdentificationNumber(), x.getRepresentation(), ConvertData.convertLocalDateToString(x.getDateStart()), ConvertData.convertLocalDateToString(x.getDateRetain()), ConvertData.convertLocalDateToString(x.getDateLeave()), x.getMotherName(), ConvertData.convertLocalDateToString(x.getMotherBirthday()), x.getMotherPhone(),
                    x.getMotherEmail(), x.getMotherJob(), x.getFatherName(), ConvertData.convertLocalDateToString(x.getFatherBirthday()), x.getFatherPhone(), x.getFatherEmail(), x.getFatherJob(), x.getNote());
            ExcelDataNew modelData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(modelData);
        });
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }

    @Override
    public Optional<KidsBirthdayDTO> findByIdKidb(UserPrincipal principal, Long idSchoolLogin, Long id) {
        Optional<Kids> kidsOptional = kidsRepository.findByIdAndDelActive(idSchoolLogin, true);
        if (kidsOptional.isEmpty()) {
            return Optional.empty();
        }
        Optional<KidsBirthdayDTO> kidsBirthdayDTOOptional = Optional.ofNullable(modelMapper.map(kidsOptional.get(), KidsBirthdayDTO.class));
        return kidsBirthdayDTOOptional;
    }


    @Transactional
    @Override
    public boolean createStudentNotify(UserPrincipal principal, CreateStudentNotify createStudentNotify) throws
            IOException, FirebaseMessagingException {
        CommonValidate.checkPlusOrTeacher(principal);

        List<Parent> parentKidList = null;
        List<Kids> kidsList = null;
        Long idSchool = principal.getIdSchoolLogin();
        if (!CollectionUtils.isEmpty(createStudentNotify.getDataGradeNotifyList())) {
            kidsList = kidsRepository.findAllKidsGrade(principal, createStudentNotify.getDataGradeNotifyList());
        } else if (!CollectionUtils.isEmpty(createStudentNotify.getDataClassNotifyList())) {
            kidsList = kidsRepository.findAllKidsClass(principal, createStudentNotify.getDataClassNotifyList());
        } else if (!CollectionUtils.isEmpty(createStudentNotify.getDataGroupNotifyList())) {
            kidsList = kidsRepository.findAllKidsGroup(principal, createStudentNotify.getDataGroupNotifyList());
        } else if (!CollectionUtils.isEmpty(createStudentNotify.getDataKidNotifyList())) {
            kidsList = kidsRepository.findAllKids(principal, createStudentNotify.getDataKidNotifyList());
        }
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        List<UrlFileAppSend> urlFileAppSendList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(createStudentNotify.getMultipartFileList())) {
            urlFileAppSendList = this.createFile(idSchool, principal.getFullName(), createStudentNotify.getMultipartFileList());
        }
        AppSend appSend = modelMapper.map(createStudentNotify, AppSend.class);

        appSend.setReceivedNumber(!CollectionUtils.isEmpty(kidsList) ? kidsList.size() : 0);

        appSend.setIdSchool(principal.getIdSchoolLogin());
        appSend.setSendType(AppSendConstant.TYPE_COMMON);
        appSend.setAppType(principal.getAppType());
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setTypeReicever(AppTypeConstant.PARENT);
        appSend.setTimeSend(LocalDateTime.now());
        appSend.setCreatedBy(principal.getFullName());
        appSend = appSendRepository.save(appSend);

        AppSend finalAppSend = appSend;
        kidsList.forEach(x -> {
            Receivers receivers = new Receivers();
            receivers.setIdUserReceiver(x.getParent().getMaUser().getId());
            receivers.setUserUnread(AppConstant.APP_FALSE);
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setIdKids(x.getId());
            receivers.setIdClass(x.getMaClass().getId());
            receivers.setCreatedBy(principal.getFullName());
            receivers.setIdSchool(x.getIdSchool());
            receivers.setAppSend(finalAppSend);
            receiversRepository.save(receivers);
        });
        if (!CollectionUtils.isEmpty(urlFileAppSendList)) {
            urlFileAppSendList.forEach(x -> {
                x.setAppSend(finalAppSend);
                urlFileAppSendRepository.save(x);
            });
        }
        for (Kids x : kidsList) {
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(4L, x, FirebaseConstant.CATEGORY_NOTIFY, createStudentNotify.getSendTitle());
        }
        return true;
    }

    private List<UrlFileAppSend> createFile(Long idSchool, String fullName, List<MultipartFile> multipartFileList) throws IOException {
        List<UrlFileAppSend> urlFileAppSendList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {

            String urlFolder = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THONG_BAO);
            String fileName = HandleFileUtils.getFileNameOfSchool(idSchool, multipartFile);
            HandleFileUtils.createFilePictureToDirectory(urlFolder, multipartFile, fileName, UploadDownloadConstant.WIDTH_OTHER);

            String urlWeb = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_WEB, UploadDownloadConstant.THONG_BAO) + fileName;
            String urlLocal = HandleFileUtils.getUrl(idSchool, UrlFileConstant.URL_LOCAL, UploadDownloadConstant.THONG_BAO) + fileName;
            String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());


            UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
            if (extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
                urlFileAppSend.setAttachPicture(urlWeb);
                urlFileAppSend.setUrlLocalPicture(urlLocal);
                urlFileAppSend.setName(multipartFile.getOriginalFilename());
            } else {
                urlFileAppSend.setAttachFile(urlWeb);
                urlFileAppSend.setUrlLocalFile(urlLocal);
                urlFileAppSend.setName(multipartFile.getOriginalFilename());
            }
            urlFileAppSend.setCreatedBy(fullName);
            urlFileAppSendList.add(urlFileAppSend);
        }
        return urlFileAppSendList;
    }

    @Override
    public KidBirthdayResponse updateApprove(Long idSchoolLogin, UserPrincipal principal, UpdateReiceiversRequest updateReiceiversEditRequest) {
        CommonValidate.checkDataPlus(principal);
        Optional<Kids> kidsOptional = kidsRepository.findByIdKid(idSchoolLogin, updateReiceiversEditRequest.getId());
        if (kidsOptional.isEmpty()) {
            return null;
        }
        Kids oldKids = kidsOptional.get();
        modelMapper.map(updateReiceiversEditRequest, oldKids);
        Kids newKids = kidsRepository.save(oldKids);
        KidBirthdayResponse kidBirthdayResponse = modelMapper.map(newKids, KidBirthdayResponse.class);
        return kidBirthdayResponse;
    }

    private void createUrlAvatarKids(Kids kids) {
        String fileName = this.fileNameAvatar;
        if (StringUtils.isNotBlank(fileName)) {
            String urlWeb = HandleFileUtils.getUrl(kids.getIdSchool(), UrlFileConstant.URL_WEB, UploadDownloadConstant.AVATAR) + fileName;
            String urlLocal = HandleFileUtils.getUrl(kids.getIdSchool(), UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR) + fileName;
            kids.setAvatarKid(urlWeb);
            kids.setAvatarKidLocal(urlLocal);
        }
        this.fileNameAvatar = null;
    }

    private List<KidModel> setKidModel(List<KidsExportResponse> exportResponseList) {
        List<KidModel> kidModelList = new ArrayList<>();
        long i = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (KidsExportResponse kidsExportResponse : exportResponseList) {
            KidModel kidModel = new KidModel();
            kidModel.setStt(i++);

            String checkStatus = kidsExportResponse.getKidMainInfo().getKidStatus();
            switch (checkStatus) {
                case "STUDYING":
                    kidModel.setKidStatus(StudentStatusEnum.STUDYING.getValue());
                    break;
                case "STUDY_WAIT":
                    kidModel.setKidStatus(StudentStatusEnum.STUDY_WAIT.getValue());
                    break;
                case "RESERVE":
                    kidModel.setKidStatus(StudentStatusEnum.RESERVE.getValue());
                    break;
                case "LEAVE_SCHOOL":
                    kidModel.setKidStatus(StudentStatusEnum.LEAVE_SCHOOL.getValue());
                    break;
                default:
                    kidModel.setKidStatus(kidsExportResponse.getKidMainInfo().getKidStatus());

            }
            kidModel.setGrade(kidsExportResponse.getGradeDTO() != null ? kidsExportResponse.getGradeDTO().getGradeName() : "");
            kidModel.setClassName(kidsExportResponse.getMaClassDTO() != null ? kidsExportResponse.getMaClassDTO().getClassName() : "");
//            kidModel.setGroupName(kidsExportResponse.getKidsGroupResponse() != null ? kidsExportResponse.getKidsGroupResponse().getGroupName() : "");
            String code = kidsExportResponse.getKidsGroup().replace("[", "");
            String dataCode = code.replace("]", "");
            kidModel.setGroupName(dataCode);
            kidModel.setFullName(kidsExportResponse.getKidMainInfo().getFullName());
            if (kidsExportResponse.getKidMainInfo().getBirthDay() != null) {
                String birthday = kidsExportResponse.getKidMainInfo().getBirthDay().format(formatter);
                kidModel.setBirthDay(birthday);
            } else {
                kidModel.setBirthDay("");
            }
            kidModel.setGender(kidsExportResponse.getKidMainInfo().getGender());
            kidModel.setNickName(kidsExportResponse.getKidMainInfo().getNickName());
            kidModel.setAddress(kidsExportResponse.getKidMainInfo().getAddress());
            kidModel.setPermanentAddress(kidsExportResponse.getKidMainInfo().getPermanentAddress());
            kidModel.setEthnic(kidsExportResponse.getKidMainInfo().getEthnic());

            if (kidsExportResponse.getKidMainInfo().getDateStart() != null) {
                String dateStart = kidsExportResponse.getKidMainInfo().getDateStart().format(formatter);
                kidModel.setDateStart(dateStart);
            } else {
                kidModel.setDateStart("");
            }
            if (kidsExportResponse.getKidMainInfo().getDateRetain() != null) {
                String dateRetain = kidsExportResponse.getKidMainInfo().getDateRetain().format(formatter);
                kidModel.setDateRetain(dateRetain);
            } else {
                kidModel.setDateRetain("");
            }
            if (kidsExportResponse.getKidMainInfo().getDateLeave() != null) {
                String dateLeave = kidsExportResponse.getKidMainInfo().getDateLeave().format(formatter);
                kidModel.setDateLeave(dateLeave);
            } else {
                kidModel.setDateLeave("");
            }
            if (kidsExportResponse.getParentInfo() != null) {
                kidModel.setRepresentation(kidsExportResponse.getParentInfo().getRepresentation());
                kidModel.setMotherName(kidsExportResponse.getParentInfo().getMotherName());
                if (kidsExportResponse.getParentInfo().getMotherBirthday() != null) {
                    String motherBirthday = kidsExportResponse.getParentInfo().getMotherBirthday().format(formatter);
                    kidModel.setMotherBirthday(motherBirthday);
                } else {
                    kidModel.setMotherBirthday("");
                }
                kidModel.setMotherPhone(kidsExportResponse.getParentInfo().getMotherPhone());
                kidModel.setMotherEmail(kidsExportResponse.getParentInfo().getMotherEmail());
                kidModel.setMotherJob(kidsExportResponse.getParentInfo().getMotherJob());
                kidModel.setFatherName(kidsExportResponse.getParentInfo().getFatherName());
                kidModel.setFatherPhone(kidsExportResponse.getParentInfo().getFatherPhone());
                if (kidsExportResponse.getParentInfo().getFatherBirthday() != null) {
                    String fatherBirthday = kidsExportResponse.getParentInfo().getFatherBirthday().format(formatter);
                    kidModel.setFatherBirthday(fatherBirthday);
                } else {
                    kidModel.setFatherBirthday("");
                }
                kidModel.setFatherEmail(kidsExportResponse.getParentInfo().getFatherEmail());
                kidModel.setFatherJob(kidsExportResponse.getParentInfo().getFatherJob());
            }
            kidModel.setNote(kidsExportResponse.getKidMainInfo().getNote());

            kidModelList.add(kidModel);
        }
        return kidModelList;
    }

    private void updateUrlAvatarKids(Kids kids) {
        String fileName = this.fileNameAvatar;
        if (StringUtils.isNotBlank(fileName)) {
            HandleFileUtils.deletePictureInFolder(kids.getAvatarKidLocal());

            String urlWeb = HandleFileUtils.getUrl(kids.getIdSchool(), UrlFileConstant.URL_WEB, UploadDownloadConstant.AVATAR) + fileName;
            String urlLocal = HandleFileUtils.getUrl(kids.getIdSchool(), UrlFileConstant.URL_LOCAL, UploadDownloadConstant.AVATAR) + fileName;
            kids.setAvatarKid(urlWeb);
            kids.setAvatarKidLocal(urlLocal);
        }
        this.fileNameAvatar = null;
    }

    /**
     * creat account and parent and role default
     *
     * @param fullName
     * @param phone
     * @param kids
     */
    private void createAccountAndParent(String fullName, String phone, Kids kids) {
        if (StringUtils.isBlank(fullName) || StringUtils.isBlank(phone) || kids == null) {
            logger.warn(ErrorsConstant.NO_DATA_ACCOUNT);
            return;
        }
        String appType = AppTypeConstant.PARENT;
        String usernameEndExtend = ConvertData.getUsernameIncludeExtend(phone, appType);
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(usernameEndExtend);
        //create account
        AccountCreateData accountCreateData = new AccountCreateData();
        accountCreateData.setFullName(fullName);
        accountCreateData.setUsername(usernameEndExtend);
        accountCreateData.setPassword(GenerateCode.passwordAuto());
        accountCreateData.setPhone(phone);
        accountCreateData.setAppType(appType);
        accountCreateData.setGender(kids.getRepresentation().equals(AppConstant.FATHER) ? AppConstant.MALE : AppConstant.FEMALE);
        MaUser maUser;
        Parent parent;
        if (maUserOptional.isPresent()) {
            logger.info("username exist: {}", usernameEndExtend);
            maUser = maUserOptional.get();
            parent = maUser.getParent();
            if (CollectionUtils.isEmpty(maUser.getParent().getKidsList())) {
                //TH tạo 2 hs có cùng sdt mới chưa có tài khoản
//                kids.setParent(parent);
//                kidsRepository.save(kids);
                return;
            } else {
                long count = maUser.getParent().getKidsList().stream().filter(a -> a.isDelActive() && !a.getKidStatus().equals(KidsStatusConstant.LEAVE_SCHOOL)).count();
                if (count > 0) {
                    logger.warn("username not create: {}", count);
                    return;
                }
            }

        } else {
            maUser = maUserService.createAccountOther(accountCreateData);
            parent = new Parent();
            parent.setCode(GenerateCode.codeParent());
            parent.setMaUser(maUser);
        }

        //create parent
//        Parent parent = new Parent();
        InforRepresentationResponse inforRepresentationResponse = ConvertData.getInforRepresent(kids);
        parent.setEmail(inforRepresentationResponse.getEmail());
        parent.setIdKidLogin(kids.getId());
        parent.setBirthday(inforRepresentationResponse.getBirthday());
        Parent newParent = parentRepository.save(parent);
        maUser.setParent(newParent);

        parentService.createWalletParent(newParent, kids.getIdSchool());
        //update phone kids
        if (kids.getRepresentation().equals(AppConstant.FATHER)) {
            kids.setFatherPhone(phone);
        } else if (kids.getRepresentation().equals(AppConstant.MOTHER)) {
            kids.setMotherPhone(phone);
        }
        kids.setParent(newParent);
        kidsRepository.save(kids);
    }

    private SmsDTO getSmsDTOByShoolId(long shoolId) {
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setCurrLocalDateTime(LocalDateTime.now());
        Optional<School> school = schoolRepository.findById(shoolId);
        if (school.isPresent()) {
            Brand brand = school.get().getBrand();
            if (brand != null) {
                smsDTO.setBrandName(brand.getBrandName());
                if (brand.isBrandTypeAds()) {
                    smsDTO.setBrandType(0);
                } else {
                    smsDTO.setBrandType(1);
                }
                Supplier supplier = brand.getSupplier();
                if (supplier != null) {
                    smsDTO.setSupplierCode(supplier.getSupplierName());
                    smsDTO.setServiceUrl(supplier.getSupplierLink());
                    smsDTO.setUserName(supplier.getUsernameLink());
                    smsDTO.setPassowrd(supplier.getPasswordLink());
                }
            }
        }
        return smsDTO;
    }

    /**
     * create extra for kids
     *
     * @param kids
     */
    private void setCreateExtraKids(Kids kids) {
        this.createAttendanceAndEvaluate(kids);
        this.createKidsClassWhenChangeClassDate(kids);
        this.createKidsStatusTimeline(kids);
        fnKidsPackageDefaultService.createPackageDefaultForKids(kids);
    }

    private void createKidsStatusTimeline(Kids kids) {
        KidsStatusTimeline entity = new KidsStatusTimeline();
        entity.setStartDate(LocalDate.now());
        entity.setStatus(kids.getKidStatus());
        entity.setKids(kids);
        kidsStatusTimelineRepository.save(entity);
    }

    /**
     * tạo lớp cho học sinh
     *
     * @param kids
     */
    private void createKidsClassWhenChangeClassDate(Kids kids) {
        KidsClassDate kidsClassDate = new KidsClassDate();
        kidsClassDate.setKids(kids);
        kidsClassDate.setMaClass(kids.getMaClass());
        kidsClassDate.setFromDate(kids.getDateStart());
        kidsClassDateRepository.save(kidsClassDate);
    }

    /**
     * cập nhật chuyển lớp
     *
     * @param idKid
     * @param idClass
     */
    private void updateKidClass(Long idKid, Long idClass) {
        List<KidsClassDate> kidsClassDateList = kidsClassDateRepository.findByKidsIdAndMaClassIdOrderByCreatedDateDesc(idKid, idClass);
        if (CollectionUtils.isEmpty(kidsClassDateList)) {
            throw new NoSuchElementException("not found kidsClassDate by id in database");
        }
        LocalDate nowDate = LocalDate.now();
        KidsClassDate oldKidClass = kidsClassDateList.get(0);
        boolean checkTime = ConvertData.checkBeforeTime(LocalTime.now());
        LocalDate toDate;
        if (!nowDate.isAfter(oldKidClass.getFromDate())) {
            toDate = nowDate.plusDays(1);
        } else {
            toDate = checkTime ? nowDate.minusDays(1) : nowDate;
        }
        oldKidClass.setToDate(toDate);
        kidsClassDateRepository.save(oldKidClass);
    }

    /**
     * tạo dữ liệu cho lớp mới khi chuyển lớp
     *
     * @param kid
     */
    private void createKidsClassWhenChangeClass(Kids kid) {
        boolean checkTime = ConvertData.checkBeforeTime(LocalTime.now());
        LocalDate nowDate = LocalDate.now();
        KidsClassDate newKidsClass = new KidsClassDate();
        newKidsClass.setKids(kid);
        newKidsClass.setMaClass(kid.getMaClass());
        newKidsClass.setFromDate(checkTime ? nowDate : nowDate.plusDays(1));
        kidsClassDateRepository.save(newKidsClass);
    }

    private void createExtraChangeClass(Kids kids) {
        if (kids.getKidStatus().equals(KidsStatusConstant.STUDYING)) {
            this.updateAttendanceAndEvaluate(kids);
            this.createEvaluate(kids);
        }
        //chuyển lớp thì chuyển khoản thu
        fnKidsPackageDefaultService.updatePackageDefaultForChangeClass(kids);
    }

    /**
     * tạo điểm danh tuần và tháng cho học sinh khi chuyển lớp
     *
     * @param kids
     */
    private void createEvaluate(Kids kids) {
        evaluateKidsCronjobs.generateEvaluateWeekKidsSchool(kids);
        evaluateKidsCronjobs.generateEvaluateMonthKidsSchool(kids);
    }

    private void updateAttendanceAndEvaluate(Kids kids) {
        LocalTime localTime = LocalTime.of(12, 0, 0);
        LocalTime nowTime = LocalTime.now();
        LocalDate nowDate = LocalDate.now();
        //trước 12h00 thì cập nhật điểm danh, nhận xét ngày cho lớp mới
        //sau 12h00 thì ko cập nhận lại điểm danh, nhận xét ngày cho lớp mới
        if (nowTime.isBefore(localTime)) {
            Optional<AttendanceKids> attendanceKidsOptional = attendanceKidsRepository.findByAttendanceDateAndKidsId(nowDate, kids.getId());
            if (attendanceKidsOptional.isPresent()) {
                AttendanceKids attendanceKids = attendanceKidsOptional.get();
                attendanceKids.setMaClass(kids.getMaClass());
                attendanceKids.setIdGrade(kids.getIdGrade());
                attendanceKidsRepository.save(attendanceKids);
            }
            Optional<EvaluateDate> evaluateDateOptional = evaluateDateRepository.findByKidsIdAndDateAndDelActiveTrue(kids.getId(), nowDate);
            if (evaluateDateOptional.isPresent()) {
                EvaluateDate evaluateDate = evaluateDateOptional.get();
                evaluateDate.setIdClass(kids.getMaClass().getId());
                evaluateDate.setIdGrade(kids.getIdGrade());
                evaluateDateRepository.save(evaluateDate);
            }
        }
    }

    private void changeFileJson(Kids kid, KidsChangeClassRequest request, UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        String url = kid.getPicJsonUrlLocal();
        if (StringUtils.isBlank(url)) return;
        // Nghỉ học chuyển đuôi 0 thành 1
        // Nghỉ học chuyển năm của tên file thành 1970
        if (kid.getKidStatus().equals(KidsStatusConstant.LEAVE_SCHOOL)) {
            logger.info("change for leave school");
            try {
                changeClass(url);
            } catch (IOException e) {
                System.out.println("error 1--");
                e.printStackTrace();
            }
        } else {
            logger.info("change for other status");
            // Rename file ảnh của Kid
            List<MaKidPics> maKidPicsList = kid.getMaKidPicsList();
            for (MaKidPics x : maKidPicsList) {
                String urlLink = x.getUrlLocal();
                String getName = new File(urlLink).getName();
                String idClass = request.getIdClass().toString();
                // ten file moi
                String nameFile = idClass + getName.substring(getName.indexOf("_"));
                try {
                    Files.move(Paths.get(urlLink), Paths.get(urlLink).resolveSibling(nameFile));
                } catch (IOException e) {
                    System.out.println("error 2--");
                    e.printStackTrace();
                }
                String uploadInAlbum = AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + yearCurrent + "\\T" + monthCurrent + "\\" + UploadDownloadConstant.ALBUM + "\\";
                x.setUrlLocal(uploadInAlbum + nameFile);
                x.setUrlWeb(AppConstant.URL_DEFAULT + idSchool + "/" + yearCurrent + "T" + monthCurrent + "/" + UploadDownloadConstant.ALBUM + "/" + nameFile);
                maKidPicsRepository.save(x);
            }
            logger.info("info next--");
            // chuyển lớp đổi 0 thành 1, tạo tên file mới có idclass của lớp mới
//            changeClass(url);
            Path path = Paths.get(url);
            String content = "";
            try {
                content = new String(Files.readAllBytes(Paths.get((url))),
                        StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.out.println("error 3--");
                e.printStackTrace();
            }
            String clasName = request.getIdClass().toString();
            String name = path.getFileName().toString();
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                System.out.println("error 4--");
                e.printStackTrace();
            }
            String[] arr = name.split("_");
            String fileName = clasName + "_" + arr[1] + "_" + ConvertData.getDateMillisecond() + "_0.json";
            MultipartFile fileChangeClass = new MockMultipartFile("file",
                    fileName, "application/json", content.getBytes());
            try {
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFileJsonSavedJson(fileChangeClass, kid.getIdSchool(), "diemdanhjson");
            } catch (IOException e) {
                System.out.println("error 5--");
                e.printStackTrace();
            }
            kid.setPicJsonUrlLocal(AppConstant.UPLOAD_IN_ALBUM + idSchool + "\\" + "diemdanhjson" + "\\" + fileName);
            kid.setPicJsonUrl(AppConstant.URL_DEFAULT + idSchool + "diemdanhjson" + "/" + fileName);
        }
    }

    private void changeClass(String url) throws IOException {
        Path path = Paths.get(url);
        String[] getName = path.getFileName().toString().split("_");
        String fileName = getName[0] + "_" + getName[1] + "_" + getName[2] + "_1.json";
        Files.move(path, path.resolveSibling(fileName));
    }

    private String getPhoneAccountParent(Kids kids) {
        return kids.getParent() != null ? kids.getParent().getMaUser().getPhone() : "";
    }

    private String getLoginParent(Kids kid) {
        String login = AppConstant.LOGIN_YET;
        if (kid.getParent() != null) {
            List<Device> deviceList = kid.getParent().getMaUser().getDeviceList();
            long count = deviceList.stream().filter(Device::isLogin).count();
            if (count > 0) {
                login = AppConstant.LOGIN_YES;
            } else if (!CollectionUtils.isEmpty(deviceList)) {
                login = AppConstant.LOGIN_NO;
            }
        }
        return login;
    }

    private String getUsername(Kids kids) {
        String username = "";
        if (kids.getParent() == null) {
            if (kids.getRepresentation().equals(AppConstant.FATHER)) {
                if (StringUtils.isNotBlank(kids.getFatherName()) && StringUtils.isNotBlank(kids.getFatherPhone())) {
                    username = AppConstant.HANDLE_ACCOUNT;
                } else {
                    username = AppConstant.NO_INFOR_REPPRESENTIION;
                }
            } else if (kids.getRepresentation().equals(AppConstant.MOTHER)) {
                if (StringUtils.isNotBlank(kids.getMotherName()) && StringUtils.isNotBlank(kids.getMotherPhone())) {
                    username = AppConstant.HANDLE_ACCOUNT;
                } else {
                    username = AppConstant.NO_INFOR_REPPRESENTIION;
                }
            }
        } else if (kids.getParent() != null) {
            username = ConvertData.getUsernameNoExtend(kids.getParent().getMaUser().getUsername());
        }
        return username;
    }

    private String getPassword(Kids kid) {
        String password = "";
        if (kid.getParent() != null) {
            password = kid.getParent().getMaUser().getPasswordShow();
        }
        return password;
    }

    /**
     * check ngày và trạng thái học sinh khi update, đối vào bảo lưu và nghỉ học thì ko được để trống ngày
     *
     * @param updateKidMainInforRequest
     */
    private void checkStatusDate(UpdateKidMainInforRequest updateKidMainInforRequest) {
//        this.checkDateStartAndStatus(updateKidMainInforRequest.getKidStatus(), updateKidMainInforRequest.getDateStart());
        if (KidsStatusConstant.STUDYING.equals(updateKidMainInforRequest.getKidStatus()) || KidsStatusConstant.STUDY_WAIT.equals(updateKidMainInforRequest.getKidStatus())) {
            updateKidMainInforRequest.setDateRetain(null);
            updateKidMainInforRequest.setDateLeave(null);
        } else if (KidsStatusConstant.RESERVE.equals(updateKidMainInforRequest.getKidStatus())) {
            if (updateKidMainInforRequest.getDateRetain() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày bảo lưu không được để trống");
            }
            updateKidMainInforRequest.setDateLeave(null);
        } else if (KidsStatusConstant.LEAVE_SCHOOL.equals(updateKidMainInforRequest.getKidStatus())) {
            if (updateKidMainInforRequest.getDateLeave() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày nghỉ học không được để trống");
            }
            updateKidMainInforRequest.setDateRetain(null);
        }
    }

    private long checkOrderKidsPayment(String newStatus, Kids kids) {
        SchoolConfig schoolConfig = schoolConfigRepository.findBySchoolId(SchoolUtils.getIdSchool()).orElseThrow();
        if (!schoolConfig.isCheckCompleteFeesStatus()) {
            if (KidsStatusConstant.LEAVE_SCHOOL.equals(newStatus) && !KidsStatusConstant.LEAVE_SCHOOL.equals(kids.getKidStatus())) {
                List<FnKidsPackage> fnKidsPackageList = kids.getFnKidsPackageList().stream().filter(x -> x.isDelActive() && x.isActive() && x.isApproved()).collect(Collectors.toList());
                return fnKidsPackageList.stream().filter(x -> FinanceUltils.getMoneyCalculate(x) > 0 && x.getPaid() < FinanceUltils.getMoneyCalculate(x)).count();
            }
        }
        return 0;
    }

    private void checkOrderKidsPaymentException(String newStatus, Kids kids) {
        long count = this.checkOrderKidsPayment(newStatus, kids);
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học sinh có " + count + " khoản học phí chưa hoàn thành. Bạn không thể thực hiện thao tác này!");
        }
    }


    private void checkCreateAccount(Kids odlKids, UpdateKidsAdminRequest updateStudentRequest) {
        Parent oldParent = odlKids.getParent();
        if (oldParent == null) {
            String phone = null;
            String fullName = null;
            if (AppConstant.FATHER.equals(updateStudentRequest.getKidMainInfo().getRepresentation()) && StringUtils.isNotBlank(updateStudentRequest.getKidMainInfo().getFatherPhone())) {
                phone = updateStudentRequest.getKidMainInfo().getFatherPhone();
                fullName = updateStudentRequest.getKidMainInfo().getFatherName();
            } else if (AppConstant.MOTHER.equals(updateStudentRequest.getKidMainInfo().getRepresentation()) && StringUtils.isNotBlank(updateStudentRequest.getKidMainInfo().getMotherPhone())) {
                phone = updateStudentRequest.getKidMainInfo().getMotherPhone();
                fullName = updateStudentRequest.getKidMainInfo().getMotherName();
            }
            if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(fullName)) {
                this.createAccountAndParent(fullName, phone, odlKids);
            }
        }
    }

    private String getPhoneRepresentation(Kids kids) {
        String phoneRepresentation = "";
        if (kids.getRepresentation().equals(AppConstant.FATHER)) {
            phoneRepresentation = kids.getFatherPhone();
        } else if (kids.getRepresentation().equals(AppConstant.MOTHER)) {
            phoneRepresentation = kids.getMotherPhone();
        }
        return phoneRepresentation;
    }

    /**
     * kích hoạt học sinh theo trạng thái
     *
     * @param kidStatus
     * @return
     */
    private boolean getActiveStatus(String kidStatus) {
        return kidStatus.equals(KidsStatusConstant.STUDYING);
    }

    private void deleteExtraWithDeleteKid(Kids kid) {
        Long idKid = kid.getId();
        LocalDate nowDate = LocalDate.now();
        this.deleteEvaluate(idKid, nowDate);
        this.deleteAttendance(idKid, nowDate);
    }

    private void deleteAttendance(Long idKid, LocalDate date) {
//        điểm danh ngày hiện tại
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsIdAndAttendanceDate(idKid, date);
        //điểm danh ngày tương lai(tạo do tạo đơn xin nghỉ đã duyệt)
        List<AttendanceKids> attendanceKidsFutureList = attendanceKidsRepository.findAttendanceKidsAfterDate(idKid, date);
        attendanceKidsList.forEach(x -> {
            boolean check = AttendanceKidsUtil.checkHasAttendanceKids(x);
            if (!check) {
                attendanceArriveKidsRepository.deleteById(x.getAttendanceArriveKids().getId());
                attendanceLeaveKidsRepository.deleteById(x.getAttendanceLeaveKids().getId());
                attendanceEatKidsRepository.deleteById(x.getAttendanceEatKids().getId());
                attendanceKidsRepository.deleteById(x.getId());
            }
        });
        attendanceKidsFutureList.forEach(x -> {
            attendanceArriveKidsRepository.deleteById(x.getAttendanceArriveKids().getId());
            attendanceLeaveKidsRepository.deleteById(x.getAttendanceLeaveKids().getId());
            attendanceEatKidsRepository.deleteById(x.getAttendanceEatKids().getId());
            attendanceKidsRepository.deleteById(x.getId());
        });
    }

    private void deleteEvaluate(Long idKid, LocalDate date) {
        LocalDate monday = DateCommonUtils.getMonday(date);
        int year = date.getYear();
        int month = date.getMonthValue();
        List<EvaluateDate> evaluateDateList = evaluateDateRepository.findByKidsIdAndDate(idKid, date);
        List<EvaluateWeek> evaluateWeekList = evaluateWeekRepository.findByKidsIdAndDate(idKid, monday);
        List<EvaluateMonth> evaluateMonthList = evaluateMonthRepository.findByKidsIdAndYearAndMonth(idKid, year, month);
        //nhận xét
        evaluateDateList.forEach(x -> {
            boolean check = EvaluateUtil.checkHasEvaluateDate(x);
            if (!check) {
                evaluateDateRepository.deleteById(x.getId());
            }
        });
        evaluateWeekList.forEach(x -> {
            boolean check = EvaluateUtil.checkHasEvaluateWeek(x);
            if (!check) {
                evaluateWeekRepository.deleteById(x.getId());
            }
        });
        evaluateMonthList.forEach(x -> {
            boolean check = EvaluateUtil.checkHasEvaluateMonth(x);
            if (!check) {
                evaluateMonthRepository.deleteById(x.getId());
            }
        });
    }

    /**
     * cập nhật lại idKidLogin cho phụ huynh
     *
     * @param kids
     */
    private void updateIdKidLoginForParent(Kids kids) {
        Parent parent = kids.getParent();
        if (parent != null) {
            List<Kids> kidsNoDeleteList = parent.getKidsList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(kidsNoDeleteList)) {
                Kids kidValid;
                List<Kids> kidsActiveList = kidsNoDeleteList.stream().filter(Kids::isActivated).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(kidsActiveList)) {
                    kidValid = kidsActiveList.get(0);
                } else {
                    kidValid = kidsNoDeleteList.get(0);
                }
                parent.setIdKidLogin(kidValid.getId());
                parentRepository.save(parent);
            }
        }
    }

    private void setFullNameKid(Kids kids, String fullName) {
        kids.setFirstName(CommonUtil.convertFistName(fullName));
        kids.setLastName(CommonUtil.convertLastName(fullName));
        kids.setFullName(CommonUtil.getFullName(fullName));
    }

    private void deleteKids(Kids kids) {
        kids.setDelActive(AppConstant.APP_FALSE);
        kids.setOutDate(LocalDate.now());
        kidsRepository.save(kids);
        this.deleteExtraWithDeleteKid(kids);
        this.updateIdKidLoginForParent(kids);
    }

    private void createAttendanceAndEvaluate(Kids kids) {
        if (kids.getKidStatus().equals(KidsStatusConstant.STUDYING) && kids.getDateStart().isBefore(LocalDate.now().plusDays(1))) {
            attendanceKidsCronjobs.createAttendanceForKid(kids);
            evaluateKidsCronjobs.generateEvaluateDateKidsSchool(kids);
            evaluateKidsCronjobs.generateEvaluateWeekKidsSchool(kids);
            evaluateKidsCronjobs.generateEvaluateMonthKidsSchool(kids);
        }
    }

    private void checkDateStartAndStatus(String kidsStatus, LocalDate dateStart) {
        LocalDate nowDate = LocalDate.now();
        if (kidsStatus.equals(KidsStatusConstant.STUDYING)) {
            if (dateStart.isAfter(nowDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.START_DATE_STUDYING);
            }
        } else if (kidsStatus.equals(KidsStatusConstant.STUDY_WAIT)) {
            if (dateStart.isBefore(nowDate) || dateStart.isEqual(nowDate)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.START_DATE_WAIT);
            }
        }
    }

    private void createEvaluateAndAttendanceForUpdate(Kids kids, String status) {
        if (!kids.getKidStatus().equals(KidsStatusConstant.STUDYING) && status.equals(KidsStatusConstant.STUDYING)) {
            attendanceKidsCronjobs.createAttendanceForKid(kids);
            evaluateKidsCronjobs.generateEvaluateDateKidsSchool(kids);
            evaluateKidsCronjobs.generateEvaluateWeekKidsSchool(kids);
            evaluateKidsCronjobs.generateEvaluateMonthKidsSchool(kids);
        }
    }

    private void deleteKidsCommon(Long idKid, Long idSchool) {
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(idKid, idSchool).orElseThrow();
        this.deleteKids(kids);
    }
}
