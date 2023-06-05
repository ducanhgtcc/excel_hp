package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.dto.AppSendDTO;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.master.request.CreateAppSendNotify;
import com.example.onekids_project.master.request.SearchAppSendRequest;
import com.example.onekids_project.master.request.UpdateAppSendNotify;
import com.example.onekids_project.master.response.notify.ListNotifyAdminResponse;
import com.example.onekids_project.master.response.notify.NotifyAdminResponse;
import com.example.onekids_project.mobile.teacher.response.message.MessageTeacheConfirmResponse;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.AppSend.*;
import com.example.onekids_project.response.appsend.AppSendResponse;
import com.example.onekids_project.response.appsend.ListAppSendResponse;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.notifihistory.ReiceiversResponeHistoru;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.SchoolService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.util.UserInforUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class AppSendServiceImpl implements AppSendService {
    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private UrlAppSendRepository urlAppSendRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private SchoolService schoolService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;


    @Override
    public Optional<AppSendDTO> findByIdNotifi(Long idschoolLogin, Long idPrincipal, Long id) {
        Optional<AppSend> optionalAppSend = appSendRepository.findById(id);
        if (optionalAppSend.isEmpty()) {
            return Optional.empty();
        }
        Optional<AppSendDTO> optionalAppSendDTO = Optional.ofNullable(modelMapper.map(optionalAppSend.get(), AppSendDTO.class));
        this.setReadForUserReceiver(optionalAppSend.get().getId());
        return optionalAppSendDTO;
    }

    @Override
    public ListAppSendResponse searchNotifi(Long idSchoolLogin, UserPrincipal principal, SearchContentRequest request) {
        Long idUserReceiver = principal.getId();
        Long idSchool = principal.getIdSchoolLogin();
        List<AppSend> appSendList = appSendRepository.searchNotifi(idSchool, idUserReceiver, request);
        if (CollectionUtils.isEmpty(appSendList)) {
            return null;
        }
        appSendList = appSendList.stream().filter(x -> x.getSendType().equals(AppSendConstant.TYPE_BIRTHDAY) || x.getSendType().equals(AppSendConstant.TYPE_COMMON) || x.getSendType().equals(AppSendConstant.TYPE_CELEBRATE)).collect(Collectors.toList());
        Long idUserR = principal.getId();
        List<AppSendResponse> appSendResponseList = listMapper.mapList(appSendList, AppSendResponse.class);
        appSendResponseList.forEach(x -> {
            x.setNumberFile(x.getUrlFileAppSendList().size());
            Optional<Receivers> receiversOptional = Optional.ofNullable(receiversRepository.findReceiverByIdUserAndIdSend(idUserR, x.getId()));
            x.setUserUnread(receiversOptional.get().isUserUnread());
        });
        long total = appSendRepository.countTotalAccount(idSchool, idUserReceiver, request);
        ListAppSendResponse listAppSendResponse = new ListAppSendResponse();
        listAppSendResponse.setAppSendResponse(appSendResponseList);
        listAppSendResponse.setTotal(total);
        return listAppSendResponse;
    }

    // filter apptype sys
    private List<AppSend> filterNotifySysSmsAppsys(Long idSchoolLogin, List<AppSend> appSendList) {
        return appSendList.stream().filter(x -> x.getSendType().equals(AppSendConstant.TYPE_BIRTHDAY) || x.getSendType().equals(AppSendConstant.TYPE_COMMON)).collect(Collectors.toList());
    }

    @Override
    public ListAppSendResponse findAllNotif(Long idSchool, Pageable pageable, Long id, String sendType) {
        List<AppSend> appSendList = appSendRepository.findAllNotif(idSchool, pageable);
        if (CollectionUtils.isEmpty(appSendList)) {
            return null;
        }
        appSendList = filterNotifySys(id, appSendList);
        List<AppSendResponse> appSendResponseList = listMapper.mapList(appSendList, AppSendResponse.class);
        ListAppSendResponse listAppSendResponse = new ListAppSendResponse();
        listAppSendResponse.setAppSendResponse(appSendResponseList);
        return listAppSendResponse;
    }

//    @Transactional
////    @Override
//    public AppSendResponse createKidsAppsend(UserPrincipal userPrincipal, CreateParentRealBirthdayRequest createSendKidsBirthdayRequest) {
//        Optional<AppSend> appSendOptional = appSendRepository.findByIdAndDelActive(userPrincipal.getIdSchoolLogin(), AppConstant.APP_TRUE);
//        if (appSendOptional.isEmpty()) {
//            return null;
//        }
//        AppSend newAppSend = modelMapper.map(createSendKidsBirthdayRequest, AppSend.class);
//        newAppSend.setSendContent(createSendKidsBirthdayRequest.getSendContent());
//        newAppSend.setAppType(userPrincipal.getAppType());
//        //todo
//        newAppSend.setSendTitle("Chúc mừng sinh nhật");
//        newAppSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
//        newAppSend.setReceivedNumber(createSendKidsBirthdayRequest.getKidPhone());
//        newAppSend.setIdSchool(userPrincipal.getIdSchoolLogin());
//        AppSend saveAppsend = appSendRepository.save(newAppSend);
//        Receivers receivers = new Receivers();
//        receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
//        receivers.setApproved(userPrincipal.getSchoolConfig().isAppSendApproved());
//        receivers.setAppSend(saveAppsend);
//
//        AppSendResponse appSendResponse = modelMapper.map(saveAppsend, AppSendResponse.class);
//        return appSendResponse;
//    }

    @Override
    public AppSendResponse updateKidsBirthdayAppsend(Long idSchoolLogin, UserPrincipal principal, UpdateKidsBirthdayRequest kidsBirthdayEditRequest) {
        Optional<AppSend> appSendOptional = appSendRepository.findById(kidsBirthdayEditRequest.getId());
        if (appSendOptional.isEmpty()) {
            return null;
        }
        AppSend oldAppsend = appSendOptional.get();
        modelMapper.map(kidsBirthdayEditRequest, oldAppsend);
        AppSend newAppsend = appSendRepository.save(oldAppsend);
        AppSendResponse appSendResponse = modelMapper.map(newAppsend, AppSendResponse.class);
        return appSendResponse;
    }

    @Transactional
    @Override
    public boolean createAppsendParent(UserPrincipal userPrincipal, CreateSendParentBirthdayRequest createSendParentBirthdayRequest) throws FirebaseMessagingException {
        Optional<AppSend> appSendOptional = appSendRepository.findByIdAndDelActive(userPrincipal.getIdSchoolLogin(), AppConstant.APP_TRUE);
        Optional<WebSystemTitle> titleSample = webSystemTitleRepository.findByIdAndDelActiveTrue(AppSendSystemTitle.PLUS_BIRTHDAY_TO_KID);
        String title = titleSample.get().getTitle();
        AppSend newAppSend = modelMapper.map(createSendParentBirthdayRequest, AppSend.class);
        newAppSend.setAppType(userPrincipal.getAppType());
        newAppSend.setIdSchool(userPrincipal.getIdSchoolLogin());
        if (createSendParentBirthdayRequest.getIdKidList().size() > 1) {
            newAppSend.setReceivedNumber(createSendParentBirthdayRequest.getIdKidList().size());
        } else {
            newAppSend.setReceivedNumber(1);
        }
        newAppSend.setTimeSend(LocalDateTime.now());
        newAppSend.setSendTitle(title);
        newAppSend.setCreatedBy(userPrincipal.getFullName());
        newAppSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        newAppSend.setTypeReicever(AppTypeConstant.PARENT);
        AppSend saveAppsend = appSendRepository.save(newAppSend);
        UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
        urlFileAppSend.setAppSend(saveAppsend);
        String url = createSendParentBirthdayRequest.getUrlPicture();
        urlFileAppSend.setAttachPicture(url);
        urlFileAppSend.setName(ConvertData.getNamePictureFromUrl(url));
        String urlLocal = createSendParentBirthdayRequest.getUrlPictureLocal();
        urlFileAppSend.setUrlLocalPicture(urlLocal);
        urlFileAppSend.setCreatedBy(userPrincipal.getFullName());
        urlAppSendRepository.save(urlFileAppSend);
        List<Parent> parentList = new ArrayList<>();
        if (createSendParentBirthdayRequest.getIdKid() != null) {
            Receivers receivers = new Receivers();
            Kids kids = kidsRepository.findByIdAndDelActive(createSendParentBirthdayRequest.getIdKid(), AppConstant.APP_TRUE).orElseThrow();
            if (kids.getParent() != null) {
                Long idUserReceivers = kids.getParent().getMaUser().getId();
                parentList.add(kids.getParent());
                receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setIdUserReceiver(idUserReceivers);
                receivers.setAppSend(saveAppsend);
                receivers.setCreatedBy(userPrincipal.getFullName());
                receivers.setIdKids(kids.getId());
                receivers.setIdClass(kids.getMaClass().getId());
                receiversRepository.save(receivers);

                //gửi firebase
                firebaseFunctionService.sendParentByPlus(1L, kids, FirebaseConstant.CATEGORY_BIRTHDAY, createSendParentBirthdayRequest.getSendContent());
            } else {
            }
        } else {
            for (Long id : createSendParentBirthdayRequest.getIdKidList()) {
                Receivers receivers = new Receivers();
                Kids kids = kidsRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE).orElseThrow();
                if (kids.getParent() != null) {
                    parentList.add(kids.getParent());
                    Long idUserReceivers = kids.getParent().getMaUser().getId();
                    receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
                    receivers.setApproved(AppConstant.APP_TRUE);
                    receivers.setIdUserReceiver(idUserReceivers);
                    receivers.setIdKids(kids.getId());
                    receivers.setCreatedBy(userPrincipal.getFullName());
                    receivers.setAppSend(saveAppsend);
                    receivers.setIdClass(kids.getMaClass().getId());
                    receiversRepository.save(receivers);

                    //gửi firebase
                    firebaseFunctionService.sendParentByPlus(1L, kids, FirebaseConstant.CATEGORY_BIRTHDAY, createSendParentBirthdayRequest.getSendContent());
                } else {
                }
            }
        }
        return true;
    }

    @Override
    public List<ReiceiversResponeHistoru> findByIdAppsend(UserPrincipal principal, Long idSchoolLogin, Long id) {
        Optional<AppSend> optionalAppSend = appSendRepository.findById(id);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(optionalAppSend.get().getId());
        ListAppSendResponse listAppSendResponse = new ListAppSendResponse();
        List<ReiceiversResponeHistoru> reiceiversResponeHistoruList = new ArrayList<>();
        receiversList.forEach(x -> {
            ReiceiversResponeHistoru model = new ReiceiversResponeHistoru();
            Optional<MaUser> maUserOptional = maUserRepository.findByIdAndDelActiveTrue(x.getIdUserReceiver());
            model.setNameReiceiver(maUserOptional.get().getFullName());
            if (x.getIdClass() != null) {
                Optional<MaClass> maClassOptional = maClassRepository.findById(x.getIdClass());
                model.setClassName(maClassOptional.get().getClassName());
            } else {
                model.setClassName("");
            }
            if (x.getIdCreated() != null) {
                model.setCreatedBy(maUserOptional.get().getFullName());
                model.setPhone(maUserOptional.get().getPhone());
                if (maUserOptional.get().getAppType().equals(AppTypeConstant.PARENT)) {
                    model.setType(AppConstant.ACCOUNT_TYPE_FEEDBACK_PARENT);
                } else {
                    model.setType(MessageWebConstant.EMPLOYEE);
                }
            }
            model.setApproved(x.isApproved());
            model.setSendDel(x.isSendDel());
            reiceiversResponeHistoruList.add(model);
        });
        return reiceiversResponeHistoruList;
    }

    @Transactional
    @Override
    public AppSendResponse createAppsendParentBirthday(UserPrincipal userPrincipal, CreateParentRealBirthdayRequest createParentRealBirthdayRequest) throws FirebaseMessagingException {
        Optional<WebSystemTitle> titleSample = webSystemTitleRepository.findByIdAndDelActiveTrue(AppSendSystemTitle.PLUS_BIRTHDAY_TO_PARENT);
        String title = titleSample.get().getTitle();
        AppSend newAppSend = modelMapper.map(createParentRealBirthdayRequest, AppSend.class);
        newAppSend.setAppType(userPrincipal.getAppType());
        newAppSend.setIdSchool(userPrincipal.getIdSchoolLogin());
        if (createParentRealBirthdayRequest.getIdPeopleList() != null) {
            newAppSend.setReceivedNumber(createParentRealBirthdayRequest.getIdPeopleList().size());
        } else {
            newAppSend.setReceivedNumber(1);
        }
        newAppSend.setTimeSend(LocalDateTime.now());
        //todo
        newAppSend.setSendTitle(title);
        newAppSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        newAppSend.setCreatedBy(userPrincipal.getFullName());
        newAppSend.setTypeReicever(AppTypeConstant.PARENT);
        AppSend saveAppsend = appSendRepository.save(newAppSend);
        UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
        urlFileAppSend.setAppSend(saveAppsend);
        String url = createParentRealBirthdayRequest.getUrlPicture();
        urlFileAppSend.setAttachPicture(url);
        urlFileAppSend.setName(ConvertData.getNamePictureFromUrl(url));
        String urlLocal = createParentRealBirthdayRequest.getUrlPictureLocal();
        urlFileAppSend.setUrlLocalPicture(urlLocal);
        urlFileAppSend.setCreatedBy(userPrincipal.getFullName());
        urlAppSendRepository.save(urlFileAppSend);
        AppSendResponse appSendResponse = new AppSendResponse();
        List<Parent> parentList = new ArrayList<>();
        List<Kids> kidsList = new ArrayList<>();

        if (createParentRealBirthdayRequest.getIdPeople() != null) {
            Parent parent = parentRepository.findByIdAndDelActiveTrue(createParentRealBirthdayRequest.getIdPeople()).orElseThrow(() -> new NoSuchElementException("not found by parent by id=" + createParentRealBirthdayRequest.getIdPeople()));
            List<Kids> kids = parent.getKidsList().stream().filter(x -> x.isDelActive()).collect(Collectors.toList());
            kidsList.addAll(kids);
            Receivers receivers = new Receivers();
            receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setIdUserReceiver(parent.getMaUser().getId());
            receivers.setAppSend(saveAppsend);
            receiversRepository.save(receivers);
            appSendResponse = modelMapper.map(saveAppsend, AppSendResponse.class);
        } else {
            for (Long id : createParentRealBirthdayRequest.getIdPeopleList()) {
                Optional<Parent> parent = parentRepository.findByIdAndDelActiveTrue(id);
                if (parent.isPresent()) {
                    parentList.add(parent.get());
                    kidsList.addAll(parent.get().getKidsList());
                }
                Receivers receivers = new Receivers();
                receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setIdUserReceiver(id);
                receivers.setAppSend(saveAppsend);
                receiversRepository.save(receivers);
                appSendResponse = modelMapper.map(saveAppsend, AppSendResponse.class);
            }

        }
        kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        for (Kids x : kidsList) {
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(1L, x, FirebaseConstant.CATEGORY_BIRTHDAY, createParentRealBirthdayRequest.getSendContent());
        }
        return appSendResponse;
    }

    @Transactional
    @Override
    public AppSendResponse createAppsendTeacherBirthday(UserPrincipal userPrincipal, CreateParentRealBirthdayRequest createParentRealBirthdayRequest) throws FirebaseMessagingException {
        Optional<WebSystemTitle> titleSample = webSystemTitleRepository.findByIdAndDelActiveTrue(AppSendSystemTitle.PLUS_BIRTHDAY_TO_TEACHER);
        String title = titleSample.get().getTitle();
        Long idSchool = userPrincipal.getIdSchoolLogin();
        Optional<AppSend> appSendOptional = appSendRepository.findByIdAndDelActive(userPrincipal.getIdSchoolLogin(), AppConstant.APP_TRUE);
        AppSend newAppSend = modelMapper.map(createParentRealBirthdayRequest, AppSend.class);
        newAppSend.setAppType(userPrincipal.getAppType());
        newAppSend.setIdSchool(userPrincipal.getIdSchoolLogin());
        if (createParentRealBirthdayRequest.getIdPeopleList() != null) {
            newAppSend.setReceivedNumber(createParentRealBirthdayRequest.getIdPeopleList().size());
        } else {
            newAppSend.setReceivedNumber(1);
        }
        newAppSend.setTimeSend(LocalDateTime.now());
        //todo
        newAppSend.setSendTitle(title);
        newAppSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        newAppSend.setCreatedBy(userPrincipal.getFullName());
        newAppSend.setTypeReicever(AppTypeConstant.TEACHER);
        AppSend saveAppsend = appSendRepository.save(newAppSend);

        UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
        urlFileAppSend.setAppSend(saveAppsend);
        String url = createParentRealBirthdayRequest.getUrlPicture();
        urlFileAppSend.setAttachPicture(url);
        urlFileAppSend.setName(ConvertData.getNamePictureFromUrl(url));
        String urlLocal = createParentRealBirthdayRequest.getUrlPictureLocal();
        urlFileAppSend.setUrlLocalPicture(urlLocal);
        urlFileAppSend.setCreatedBy(userPrincipal.getFullName());
        urlAppSendRepository.save(urlFileAppSend);

        AppSendResponse appSendResponse = new AppSendResponse();
        List<InfoEmployeeSchool> infoEmployeeSchools = new ArrayList<>();
        if (createParentRealBirthdayRequest.getIdPeople() != null) {
            InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(createParentRealBirthdayRequest.getIdPeople(), idSchool).orElseThrow(() -> new NoSuchElementException("not found infor employee 1"));
            if (infoEmployeeSchool.getEmployee() != null) {
                infoEmployeeSchools.add(infoEmployeeSchool);
                Receivers receivers = new Receivers();
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
                //todo
                receivers.setIdUserReceiver(infoEmployeeSchool.getEmployee().getMaUser().getId());
                receivers.setAppSend(saveAppsend);
                receiversRepository.save(receivers);
                appSendResponse = modelMapper.map(saveAppsend, AppSendResponse.class);
            }
        } else {
            for (Long id : createParentRealBirthdayRequest.getIdPeopleList()) {
                InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolRepository.findByIdAndSchoolIdAndDelActiveTrue(id, idSchool).orElseThrow(() -> new NoSuchElementException("not found infor employee 2"));
                if (infoEmployeeSchool.getEmployee() != null) {
                    infoEmployeeSchools.add(infoEmployeeSchool);
                    Receivers receivers = new Receivers();
                    receivers.setApproved(AppConstant.APP_TRUE);
                    receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
                    receivers.setIdUserReceiver(infoEmployeeSchool.getEmployee().getMaUser().getId());
                    receivers.setAppSend(saveAppsend);
                    receiversRepository.save(receivers);
                    appSendResponse = modelMapper.map(saveAppsend, AppSendResponse.class);
                }
            }
        }
        for (InfoEmployeeSchool x : infoEmployeeSchools) {
            //gửi firebase
            firebaseFunctionService.sendTeacherByPlus(1L, x, createParentRealBirthdayRequest.getSendContent(), FirebaseConstant.CATEGORY_BIRTHDAY, x.getSchool().getId());
        }
        return appSendResponse;
    }

    @Override
    public boolean updateRead(Long id, List<AppSendRequest> appSendRequests) {
        appSendRequests.forEach(x -> {
            Optional<AppSend> messageParentOptional = appSendRepository.findByIdAndDelActive(id, true);
            if (messageParentOptional.isPresent()) {
            }
            List<Receivers> receiversOptional = receiversRepository.findAllByAppSendId(x.getId());
            receiversOptional.forEach(y -> {
                y.setUserUnread(AppConstant.APP_TRUE);
                receiversRepository.save(y);
            });
        });
        return true;
    }

    @Override
    public Optional<AppSendDTO> findByIdAppsenda(Long idSchoolLogin, UserPrincipal principal, Long id) {
        Optional<AppSend> optionalAppSend = appSendRepository.findById(id);
        if (optionalAppSend.isEmpty()) {
            return Optional.empty();
        }
        Long idU = principal.getId();
        Long idSend = optionalAppSend.get().getId();
        Optional<Receivers> receiversOptional = Optional.ofNullable(receiversRepository.findReceiverByIdUserAndIdSend(idU, idSend));
        List<Receivers> receiversList = optionalAppSend.get().getReceiversList();
        List<Receivers> test = receiversList.stream().filter(x -> x.getIdUserReceiver() == idU).collect(Collectors.toList());
        test.forEach(x -> {
            x.setUserUnread(AppConstant.APP_TRUE);
            receiversRepository.save(x);
        });
        Optional<AppSendDTO> optionalAppSendDTO = Optional.ofNullable(modelMapper.map(optionalAppSend.get(), AppSendDTO.class));
        return optionalAppSendDTO;
    }

    @Override
    public AppSendResponse updateHistoryAppSend(Long idSchoolLogin, UserPrincipal principal, UpdateSmsAppHistoryRequest updateSmsAppHistoryRequest) {
        Optional<AppSend> appSendOptional = appSendRepository.findById(updateSmsAppHistoryRequest.getId());
        if (appSendOptional.isPresent()) {
            AppSend appSend = appSendOptional.get();
            appSend.setSendDel(AppConstant.APP_TRUE);
            appSendRepository.save(appSend);
        }
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(updateSmsAppHistoryRequest.getId());
        receiversList.forEach(x -> {
            Receivers receivers = new Receivers();
            receivers.setSendDel(AppConstant.APP_TRUE);
            receiversRepository.save(receivers);
        });
        return null;
    }

    @Transactional
    @Override
    public MessageTeacheConfirmResponse appovedHistoryAppsend(UserPrincipal principal, Long id) throws FirebaseMessagingException {
        AppSend appSend = appSendRepository.findById(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        appSend.setApproved(AppConstant.APP_TRUE);
        appSendRepository.save(appSend);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(id);
        for (Receivers x : receiversList) {
            x.setApproved(AppConstant.APP_TRUE);
            receiversRepository.save(x);
            // firebase
            this.sendFireBase(x);
        }

        return null;
    }

    //        fireBasse
    private void sendFireBase(Receivers receivers) throws FirebaseMessagingException {
        String title = receivers.getAppSend().getSendTitle().length() < 50 ? receivers.getAppSend().getSendTitle() : receivers.getAppSend().getSendTitle().substring(0, 50);
        String content = receivers.getAppSend().getSendContent().length() < 50 ? receivers.getAppSend().getSendContent() : receivers.getAppSend().getSendContent().substring(0, 50);

        if (receivers.getAppSend().getAppType().equalsIgnoreCase(AppTypeConstant.TEACHER)) {
            Optional<Kids> kids = kidsRepository.findByIdAndDelActiveTrue(receivers.getIdKids());
            if (kids.isPresent() && kids.get().getParent() != null) {
                List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(kids.get().getParent());
                if (!CollectionUtils.isEmpty(tokenFirebaseObjectList)) {
                    NotifyRequest notifyRequest = new NotifyRequest();
                    notifyRequest.setBody(content);
                    notifyRequest.setTitle(title);
                    FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.NOTIFY_PARENT, notifyRequest, kids.get().getId().toString());
                }
            }
        } else {

            Optional<MaUser> maUser = maUserRepository.findByIdAndDelActiveTrue(receivers.getIdUserReceiver());
            if (maUser.isPresent() && maUser.get().getEmployee() != null) {
                List<InfoEmployeeSchool> infoEmployeeSchool = maUser.get().getEmployee().getInfoEmployeeSchoolList();
                infoEmployeeSchool = infoEmployeeSchool.stream().filter(x -> x.getSchool().getId().equals(receivers.getIdSchool())).collect(Collectors.toList());
                List<TokenFirebaseObject> tokenFirebaseObjectList = null;
                if (!infoEmployeeSchool.isEmpty()) {
                    tokenFirebaseObjectList = firebaseService.getEmployeeTokenFirebases(infoEmployeeSchool);
                }
                if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                    NotifyRequest notifyRequest = new NotifyRequest();
                    notifyRequest.setBody(content);
                    notifyRequest.setTitle(title);
                    FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsTeacher(tokenFirebaseObjectList, FirebaseRouterConstant.NOTIFY_TEACHER, notifyRequest);
                }
            }
        }
    }

    @Override
    public MessageTeacheConfirmResponse revokeAppSendhistory(UserPrincipal principal, Long id) {
        AppSend appSend = appSendRepository.findById(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        appSend.setSendDel(AppConstant.APP_TRUE);
        appSendRepository.save(appSend);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(id);
        receiversList.forEach(x -> {
            x.setSendDel(AppConstant.APP_TRUE);
            receiversRepository.save(x);
        });
        return null;
    }

    @Override
    public MessageTeacheConfirmResponse unrevokeAppSendhistory(UserPrincipal principal, Long id) {
        AppSend appSend = appSendRepository.findById(id).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        appSend.setSendDel(AppConstant.APP_FALSE);
        appSendRepository.save(appSend);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(id);
        receiversList.forEach(x -> {
            x.setSendDel(AppConstant.APP_FALSE);
            receiversRepository.save(x);
        });
        return null;
    }

    @Override
    public SmsAppRequest1 updateManyapproved(Long id, List<SmsAppRequest1> smsAppRequests) {
        smsAppRequests.forEach(x -> {
            Optional<AppSend> appSendOptional = appSendRepository.findById(id);
            if (appSendOptional.isPresent()) {
                AppSend appSend = appSendOptional.get();
                appSend.setApproved(AppConstant.APP_TRUE);
                appSendRepository.save(appSend);
            }
        });
        return null;
    }

    @Override
    public void saveToAppSendParent(UserPrincipal principal, Kids kids, String title, String content, String sendType) {
        if (kids.getParent() != null) {
            AppSend appSend = new AppSend();
            appSend.setReceivedNumber(1);
            appSend.setIdSchool(principal.getIdSchoolLogin());
            appSend.setSendType(sendType);
            appSend.setAppType(principal.getAppType());
            appSend.setApproved(AppConstant.APP_TRUE);
            appSend.setTypeReicever(AppTypeConstant.PARENT);
            appSend.setTimeSend(LocalDateTime.now());
            appSend.setCreatedBy(principal.getFullName());
            appSend.setSendTitle(title);
            appSend.setSendContent(content);
            AppSend appSendSaved = appSendRepository.save(appSend);

            Receivers receivers = new Receivers();
            receivers.setIdUserReceiver(kids.getParent().getMaUser().getId());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setIdKids(kids.getId());
            receivers.setIdClass(kids.getMaClass().getId());
            receivers.setCreatedBy(principal.getFullName());
            receivers.setIdSchool(kids.getIdSchool());
            receivers.setAppSend(appSendSaved);
            receiversRepository.save(receivers);
        }
    }

    @Override
    public void saveToAppSendParentForAuto(Long idSchool, Kids kids, String title, String content, String sendType, String... picture) {
        if (kids.getParent() != null) {
            AppSend appSend = new AppSend();
            appSend.setIdCreated(1L);
            appSend.setCreatedDate(LocalDateTime.now());
            appSend.setReceivedNumber(1);
            appSend.setIdSchool(idSchool);
            appSend.setSendType(sendType);
            appSend.setAppType(AppTypeConstant.SYSTEM);
            appSend.setApproved(AppConstant.APP_TRUE);
            appSend.setTypeReicever(AppTypeConstant.PARENT);
            appSend.setTimeSend(LocalDateTime.now());
            appSend.setCreatedBy(AppConstant.SYSTEM);
            appSend.setSendTitle(title);
            appSend.setSendContent(content);
            AppSend appSendSaved = appSendRepository.save(appSend);

            Receivers receivers = new Receivers();
            receivers.setIdCreated(1L);
            receivers.setCreatedDate(LocalDateTime.now());
            receivers.setIdUserReceiver(kids.getParent().getMaUser().getId());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setIdKids(kids.getId());
            receivers.setIdClass(kids.getMaClass().getId());
            receivers.setCreatedBy(AppConstant.SYSTEM);
            receivers.setIdSchool(kids.getIdSchool());
            receivers.setAppSend(appSendSaved);
            receiversRepository.save(receivers);

            //lưu ảnh khi có đính kèm
            if (picture.length > 0) {
                String pic = picture[0];
                if (StringUtils.isNotBlank(pic)) {
                    UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
                    urlFileAppSend.setIdCreated(1L);
                    urlFileAppSend.setCreatedDate(LocalDateTime.now());
                    urlFileAppSend.setAppSend(appSend);
                    urlFileAppSend.setAttachPicture(pic);
                    urlFileAppSendRepository.save(urlFileAppSend);
                }
            }
        }
    }

    @Override
    public void saveToAppSendEmployee(UserPrincipal principal, InfoEmployeeSchool infoEmployeeSchool, String title, String content, String sendType) {
        if (infoEmployeeSchool.getEmployee() != null) {
            AppSend appSend = new AppSend();
            appSend.setReceivedNumber(1);
            appSend.setIdSchool(principal.getIdSchoolLogin());
            appSend.setSendType(sendType);
            appSend.setAppType(principal.getAppType());
            appSend.setApproved(AppConstant.APP_TRUE);
            appSend.setTypeReicever(infoEmployeeSchool.getAppType());
            appSend.setTimeSend(LocalDateTime.now());
            appSend.setCreatedBy(principal.getFullName());
            appSend.setSendTitle(title);
            appSend.setSendContent(content);
            AppSend appSendSaved = appSendRepository.save(appSend);

            Receivers receivers = new Receivers();
            receivers.setIdUserReceiver(infoEmployeeSchool.getEmployee().getMaUser().getId());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setCreatedBy(principal.getFullName());
            receivers.setIdSchool(infoEmployeeSchool.getSchool().getId());
            receivers.setAppSend(appSendSaved);
            receiversRepository.save(receivers);
        }
    }

    @Override
    public void saveToAppSendEmployeeForAuto(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, String title, String content, String sendType, String... picture) {
        if (infoEmployeeSchool.getEmployee() != null) {
            AppSend appSend = new AppSend();
            appSend.setIdCreated(1L);
            appSend.setCreatedDate(LocalDateTime.now());
            appSend.setReceivedNumber(1);
            appSend.setIdSchool(idSchool);
            appSend.setSendType(sendType);
            appSend.setAppType(AppTypeConstant.SYSTEM);
            appSend.setApproved(AppConstant.APP_TRUE);
            appSend.setTypeReicever(infoEmployeeSchool.getAppType());
            appSend.setTimeSend(LocalDateTime.now());
            appSend.setCreatedBy(AppConstant.SYSTEM);
            appSend.setSendTitle(title);
            appSend.setSendContent(content);
            AppSend appSendSaved = appSendRepository.save(appSend);

            Receivers receivers = new Receivers();
            receivers.setIdCreated(1L);
            receivers.setCreatedDate(LocalDateTime.now());
            receivers.setIdUserReceiver(infoEmployeeSchool.getEmployee().getMaUser().getId());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setCreatedBy(AppConstant.SYSTEM);
            receivers.setIdSchool(infoEmployeeSchool.getSchool().getId());
            receivers.setAppSend(appSendSaved);
            receiversRepository.save(receivers);

            //lưu ảnh khi có đính kèm
            if (picture.length > 0) {
                String pic = picture[0];
                if (StringUtils.isNotBlank(pic)) {
                    UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
                    urlFileAppSend.setIdCreated(1L);
                    urlFileAppSend.setCreatedDate(LocalDateTime.now());
                    urlFileAppSend.setAppSend(appSend);
                    urlFileAppSend.setAttachPicture(pic);
                    urlFileAppSendRepository.save(urlFileAppSend);
                }
            }
        }
    }

    @Override
    public boolean deleteAppsend(UserPrincipal principal, Long idSchoolLogin, Long id) {
        Optional<AppSend> appSendOptional = appSendRepository.findByIdAndDelActive(id, true);
        if (appSendOptional.isEmpty()) {
            return false;
        }
        AppSend deleteAppsend = appSendOptional.get();
        deleteAppsend.setDelActive(AppConstant.APP_FALSE);
        appSendRepository.save(deleteAppsend);
        return true;
    }

    private List<AppSend> filterNotifySys(Long idSchoolLogin, List<AppSend> appSendList) {
        return appSendList.stream()
                .filter(x -> AppTypeConstant.SYSTEM.equals(x.getAppType()) &&
                        !CollectionUtils.isEmpty(x.getReceiversList()) && !CollectionUtils.isEmpty(x.getReceiversList().stream()
                        .filter(y -> y.getIdUserReceiver() == idSchoolLogin).collect(Collectors.toList()))).collect(Collectors.toList());

    }

    private void setReadForUserReceiver(Long id) {
        Receivers receivers = receiversRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        receivers.setUserUnread(AppConstant.APP_TRUE);
        receiversRepository.save(receivers);
    }

    @Override
    @Transactional
    public boolean createAppSendNotify(UserPrincipal principal, CreateAppSendNotify createAppSendNotify) throws IOException, FirebaseMessagingException {

//        List<Long> idSchoolList = null;

        Long idSchool = createAppSendNotify.getIdSchool();
        List<Kids> kidsInSchoolList = new ArrayList<>();
        List<InfoEmployeeSchool> teacherInSchoolList = new ArrayList<>();
        List<InfoEmployeeSchool> plusInSchoolList = new ArrayList<>();
        String title = createAppSendNotify.getSendTitle();
        String content = createAppSendNotify.getSendContent();
//        idSchoolList = Collections.singletonList(createAppSendNotify.getIdSchool());
        List<Parent> parentList = null;
        if (createAppSendNotify.getAppTypeArr().contains(AppTypeConstant.PARENT)) {
            kidsInSchoolList = UserInforUtils.getKidsInSchoolHasAccountForIdSchool(idSchool);
            parentList = kidsInSchoolList.stream().map(Kids::getParent).collect(Collectors.toList());
//            parentList = parentRepository.findAllParentAppSend(idSchoolList);
        }
        List<Employee> plusList = null;
        if (createAppSendNotify.getAppTypeArr().contains(AppTypeConstant.SCHOOL)) {
            plusInSchoolList = UserInforUtils.getPlusInSchoolHasAccountForIdSchool(idSchool);
            plusList = plusInSchoolList.stream().map(InfoEmployeeSchool::getEmployee).collect(Collectors.toList());
//            plusList = employeeRepository.findAllEmployeeAppsend(idSchoolList, AppTypeConstant.SCHOOL);
        }
        List<Employee> employeeList = null;
        if (createAppSendNotify.getAppTypeArr().contains(AppTypeConstant.TEACHER)) {
            teacherInSchoolList = UserInforUtils.getTeacherInSchoolHasAccountForIdSchool(idSchool);
            employeeList = teacherInSchoolList.stream().map(InfoEmployeeSchool::getEmployee).collect(Collectors.toList());
//            employeeList = employeeRepository.findAllEmployeeAAppsend(idSchoolList, AppTypeConstant.TEACHER);
        }
        String name = principal.getFullName();
        int monthCurrent = LocalDate.now().getMonthValue();
        int yearCurrent = LocalDate.now().getYear();
        AppSend appSend = modelMapper.map(createAppSendNotify, AppSend.class);
        appSend.setSendType(AppSendConstant.TYPE_COMMON);
        appSend.setAppType(AppTypeConstant.SYSTEM);

        if (!CollectionUtils.isEmpty(employeeList) && !CollectionUtils.isEmpty(parentList)) {
            appSend.setReceivedNumber(employeeList.size() + parentList.size());
        } else if (!CollectionUtils.isEmpty(employeeList) && !CollectionUtils.isEmpty(parentList) && !CollectionUtils.isEmpty(plusList)) {
            appSend.setReceivedNumber(employeeList.size() + parentList.size() + plusList.size());
        } else {
            if (employeeList != null) {
                appSend.setReceivedNumber(employeeList.size());
            } else if (parentList != null) {
                appSend.setReceivedNumber(parentList.size());
            } else if (plusList != null) {
                appSend.setReceivedNumber(plusList.size());
            }
        }
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setCreatedBy(name);
        appSend.setTimeSend(LocalDateTime.now());
        appSend = appSendRepository.save(appSend);
        // tao thong bao cho teacher
        if (!CollectionUtils.isEmpty(teacherInSchoolList)) {
            AppSend finalAppSend = appSend;
            teacherInSchoolList.forEach(infoEmployee -> {
                Receivers receivers = new Receivers();
//                if (employee.getMaUser() != null) {
                receivers.setIdUserReceiver(infoEmployee.getEmployee().getMaUser().getId());
//                }
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setCreatedBy(name);
//                if (!CollectionUtils.isEmpty(employee.getInfoEmployeeSchoolList())) {
//                    employee.getInfoEmployeeSchoolList().forEach(infoEmployeeSchool -> {
                receivers.setIdSchool(idSchool);
                receivers.setAppSend(finalAppSend);
                receiversRepository.save(receivers);
//                    });
//                }
                receivers.setAppSend(finalAppSend);
                receiversRepository.save(receivers);
            });
        }
        // tao thong bao cho plus
        if (!CollectionUtils.isEmpty(plusInSchoolList)) {
            AppSend finalAppSend = appSend;
            plusInSchoolList.forEach(infoEmployee -> {
                Receivers receivers = new Receivers();
//                if (employee.getMaUser() != null) {
                receivers.setIdUserReceiver(infoEmployee.getEmployee().getMaUser().getId());
//                }
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setCreatedBy(name);
//                if (!CollectionUtils.isEmpty(employee.getInfoEmployeeSchoolList())) {
//                    employee.getInfoEmployeeSchoolList().forEach(infoEmployeeSchool -> {
                receivers.setIdSchool(idSchool);
                receivers.setAppSend(finalAppSend);
                receiversRepository.save(receivers);
//                    });
//                }
                receivers.setAppSend(finalAppSend);
                receiversRepository.save(receivers);
            });
        }

        // tao thong bao cho parent
        if (!CollectionUtils.isEmpty(kidsInSchoolList)) {
            AppSend finalAppSend1 = appSend;
            kidsInSchoolList.forEach(kid -> {
                Receivers receivers = new Receivers();
//                if (parent1.getMaUser() != null) {
                receivers.setIdUserReceiver(kid.getParent().getMaUser().getId());
//                }
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setCreatedBy(name);
//                if (!CollectionUtils.isEmpty(parent1.getKidsList())) {
//                    parent1.getKidsList().forEach(kids -> {
                receivers.setIdKids(kid.getId());
                receivers.setIdClass(kid.getMaClass().getId());
                receivers.setIdSchool(idSchool);
                receivers.setAppSend(finalAppSend1);
                receiversRepository.save(receivers);
//                    });
//                }
                receivers.setAppSend(finalAppSend1);
                receiversRepository.save(receivers);
            });
        }

        if (createAppSendNotify.getMultipartFileList() != null && createAppSendNotify.getMultipartFileList().length > 0) {
            for (MultipartFile multipartFile : createAppSendNotify.getMultipartFileList()) {
                String extension = Objects.requireNonNull(FilenameUtils.getExtension(multipartFile.getOriginalFilename())).toLowerCase();
                UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSavedNoTime(multipartFile, SystemConstant.ID_SYSTEM, UploadDownloadConstant.THONG_BAO);
                urlFileAppSend.setName(handleFileResponse.getName());
                long checkPicture = UploadDownloadConstant.EXTENDSION_PICTURE.stream().filter(x -> x.equals(extension)).count();
                if (checkPicture > 0) {
                    urlFileAppSend.setAttachPicture(handleFileResponse.getUrlWeb());
                    urlFileAppSend.setUrlLocalPicture(handleFileResponse.getUrlLocal());
                } else {
                    urlFileAppSend.setAttachFile(handleFileResponse.getUrlWeb());
                    urlFileAppSend.setUrlLocalFile(handleFileResponse.getUrlLocal());
                }

                urlFileAppSend.setAppSend(appSend);
                urlFileAppSendRepository.save(urlFileAppSend);
            }
        }
        //send firebase
        if (createAppSendNotify.getAppTypeArr().contains(AppTypeConstant.PARENT)) {
            firebaseFunctionService.sendParentCommon(kidsInSchoolList, title, content, idSchool, FirebaseConstant.CATEGORY_NOTIFY);
        }
        if (createAppSendNotify.getAppTypeArr().contains(AppTypeConstant.SCHOOL)) {
            firebaseFunctionService.sendPlusCommon(plusInSchoolList, title, content, idSchool, FirebaseConstant.CATEGORY_NOTIFY);
        }
        if (createAppSendNotify.getAppTypeArr().contains(AppTypeConstant.TEACHER)) {
            firebaseFunctionService.sendTeacherCommon(teacherInSchoolList, title, content, idSchool, FirebaseConstant.CATEGORY_NOTIFY);
        }
        return true;
    }

    @Override
    public boolean updateAppSendNotify(UserPrincipal principal, UpdateAppSendNotify updateAppSendNotify) throws IOException {
        int monthCurrent = LocalDate.now().getMonthValue();
        int yearCurrent = LocalDate.now().getYear();
        Optional<AppSend> oldAppSendOptional = appSendRepository.findByIdAndDelActive(updateAppSendNotify.getId(), AppConstant.APP_TRUE);
        if (oldAppSendOptional.isEmpty()) {
            return false;
        }
        AppSend oldAppSend = oldAppSendOptional.get();
        modelMapper.map(updateAppSendNotify, oldAppSend);
        appSendRepository.save(oldAppSend);
        return true;
    }

    @Override
    public ListNotifyAdminResponse findAllAppSendNotify(SearchAppSendRequest request) {
        ListNotifyAdminResponse response = new ListNotifyAdminResponse();
        List<Long> idSchoolList = ConvertData.getIdSchoolListInAgent(schoolService.findSchoolInAgent(request.getIdAgent()));
        List<AppSend> appSendList = appSendRepository.searchNotifyAdmin(request, idSchoolList);
        long total = appSendRepository.countSearchNotifyAdmin(request, idSchoolList);
        List<NotifyAdminResponse> dataList = new ArrayList<>();
        appSendList.forEach(x -> {
            NotifyAdminResponse model = modelMapper.map(x, NotifyAdminResponse.class);
            model.setFileNumber(x.getUrlFileAppSendList().size());
            dataList.add(model);
        });
        response.setDataList(dataList);
        response.setTotal(total);
        return response;
    }

    @Override
    public boolean deleteAppSendNotify(Long[] idAppSend) {
        for (Long id : idAppSend) {
            AppSend appSend = appSendRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE).orElseThrow();
            appSendRepository.deleteById(appSend.getId());
        }
        return true;
    }

    @Override
    public ReceiversRequest updateReadReceiver(Long id, List<ReceiversRequest> receiversRequests) {
        receiversRequests.forEach(x -> {
            Optional<Receivers> receiversOptional = receiversRepository.findByIdAndDelActiveTrue(id);
            if (receiversOptional.isPresent()) {
                Receivers receivers = receiversOptional.get();
                receivers.setUserUnread(true);
                receiversRepository.save(receivers);
            }
        });
        return null;
    }


}





