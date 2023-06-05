package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.plus.request.notify.NotifyPlusRequest;
import com.example.onekids_project.mobile.plus.response.notify.NotifyPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.NotifyPlusService;
import com.example.onekids_project.model.kids.KidsInfoModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotifyPlusServiceImpl implements NotifyPlusService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @Autowired
    WebSystemTitleService webSystemTitleService;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Transactional
    @Override
    public NotifyPlusResponse createNotifyGroup(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKidsGroup(principal, request.getIdList()).stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        return this.setPropertisAppSendStudent(request, kidsList, principal);
    }

    @Transactional
    @Override
    public NotifyPlusResponse createNotifyStudent(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        List<Kids> kidsList = kidsRepository.findAllKids(principal, request.getIdList()).stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        return this.setPropertisAppSendStudent(request, kidsList, principal);
    }

    @Transactional
    @Override
    public NotifyPlusResponse createNotifyClass(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
//        List<Kids> kidsList = kidsRepository.findAllKidsClass(principal, request.getIdList()).stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        Collection<KidsInfoModel> kidsList = kidsRepository.getKidsClassNotify(principal.getIdSchoolLogin(), request.getIdList(), KidsInfoModel.class);
        return this.setPropertisAppSendStudentNew(request, kidsList, principal);
    }

    @Transactional
    @Override
    public NotifyPlusResponse createNotifyGrade(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
//        List<Kids> kidsList = kidsRepository.findAllKidsGrade(principal, request.getIdList()).stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        Collection<KidsInfoModel> kidsList = kidsRepository.getKidsGradeNotify(principal.getIdSchoolLogin(), request.getIdList(), KidsInfoModel.class);
        return this.setPropertisAppSendStudentNew(request, kidsList, principal);
    }

    @Transactional
    @Override
    public NotifyPlusResponse createNotifyEmployee(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findInfoEmployeeSchool(principal.getIdSchoolLogin(), request.getIdList()).stream().filter(x -> x.getEmployee() != null).collect(Collectors.toList());
        return this.setPropertisAppSendEmployee(request, infoEmployeeSchoolList, principal);
    }

    @Transactional
    @Override
    public NotifyPlusResponse createNotifyDeparment(UserPrincipal principal, NotifyPlusRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findInfoEmployeeDeparmentList(principal.getIdSchoolLogin(), request.getIdList()).stream().filter(x -> x.getEmployee() != null).collect(Collectors.toList());
        return this.setPropertisAppSendEmployee(request, infoEmployeeSchoolList, principal);
    }

    private NotifyPlusResponse setPropertisAppSendEmployee(NotifyPlusRequest request, List<InfoEmployeeSchool> infoEmployeeSchoolList, UserPrincipal principal) throws IOException, FirebaseMessagingException {

        NotifyPlusResponse reponse = new NotifyPlusResponse();
        if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new NotFoundException("Không có dữ liệu");
        }
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee() != null).distinct().collect(Collectors.toList());
        Long idSchool = principal.getIdSchoolLogin();
        Optional<WebSystemTitle> webSystemTitle;
        String titleAppPlus = "";
        webSystemTitle = webSystemTitleService.findById(AppSendSystemTitle.PLUS_NOTIFY_TO_EMPLOYEE);
        if (webSystemTitle.isPresent()) {
            titleAppPlus = webSystemTitle.get().getTitle();
        }
        AppSend appSend = new AppSend();
        appSend.setReceivedNumber(infoEmployeeSchoolList.size());
        appSend.setSendContent(request.getSendContent());
        appSend.setSendTitle(titleAppPlus);
        appSend.setIdSchool(principal.getIdSchoolLogin());
        appSend.setSendType(AppSendConstant.TYPE_COMMON);
        appSend.setAppType(principal.getAppType());
        appSend.setTypeReicever(AppTypeConstant.TEACHER);
        appSend.setCreatedBy(principal.getFullName());
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend = appSendRepository.save(appSend);
        AppSend finalAppSend = appSend;
        String finalTitleAppPlus = titleAppPlus;
        infoEmployeeSchoolList.forEach(x -> {
            Receivers receivers = new Receivers();
            receivers.setIdUserReceiver(x.getEmployee().getMaUser().getId());
            receivers.setUserUnread(AppConstant.APP_FALSE);
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setIdUserReceiver(x.getEmployee().getMaUser().getId());
            receivers.setIdSchool(x.getSchool().getId());
            receivers.setCreatedBy(principal.getFullName());
            receivers.setAppSend(finalAppSend);
            receiversRepository.save(receivers);
        });
        this.setPropertisMultiFile(request, appSend, idSchool);
        reponse.setCheck(AppConstant.APP_TRUE);
        for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
            //gửi firebase
            firebaseFunctionService.sendTeacherByPlus(4L, x, request.getSendContent(), FirebaseConstant.CATEGORY_NOTIFY, idSchool);
        }

        return reponse;
    }


    private NotifyPlusResponse setPropertisAppSendStudent(NotifyPlusRequest request, List<Kids> kidsList, UserPrincipal principal) throws IOException, FirebaseMessagingException {
        NotifyPlusResponse reponse = new NotifyPlusResponse();
        if (!CollectionUtils.isEmpty(kidsList)) {
            Long idSchool = principal.getIdSchoolLogin();
            Optional<WebSystemTitle> webSystemTitle;
            String titleAppPlus = "";
            webSystemTitle = webSystemTitleService.findById(AppSendSystemTitle.PLUS_NOTIFY_TO_PARENT);
            if (webSystemTitle.isPresent()) {
                titleAppPlus = webSystemTitle.get().getTitle();
            }
            AppSend appSend = new AppSend();
            appSend.setReceivedNumber(kidsList.size());
            appSend.setSendContent(request.getSendContent());
            appSend.setSendTitle(titleAppPlus);
            appSend.setIdSchool(principal.getIdSchoolLogin());
            appSend.setSendType(AppSendConstant.TYPE_COMMON);
            appSend.setAppType(principal.getAppType());
            appSend.setTypeReicever(AppTypeConstant.PARENT);
            appSend.setCreatedBy(principal.getFullName());
            appSend.setApproved(AppConstant.APP_TRUE);
            appSend = appSendRepository.save(appSend);
            AppSend finalAppSend1 = appSend;
            List<Receivers> receiversList = new ArrayList<>();
            String userCreate = principal.getFullName();
            for (Kids x : kidsList) {
                Receivers receivers = new Receivers();
                receivers.setIdUserReceiver(x.getParent().getMaUser().getId());
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setIdKids(x.getId());
                receivers.setIdClass(x.getMaClass().getId());
                receivers.setIdUserReceiver(x.getParent().getMaUser().getId());
                receivers.setIdSchool(idSchool);
                receivers.setCreatedBy(userCreate);
                receivers.setAppSend(finalAppSend1);
                receiversList.add(receivers);
            }
            receiversRepository.saveAll(receiversList);
            this.setPropertisMultiFile(request, appSend, idSchool);
            //gửi firebase
            Instant start = Instant.now();
            firebaseFunctionService.sendParentByPlusList(4L, kidsList, FirebaseConstant.CATEGORY_NOTIFY, request.getSendContent(), idSchool);
            Instant end = Instant.now();
            System.out.println(Duration.between(start, end).toMillis());

            reponse.setCheck(AppConstant.APP_TRUE);
        }
        return reponse;
    }

    private NotifyPlusResponse setPropertisAppSendStudentNew(NotifyPlusRequest request, Collection<KidsInfoModel> kidsList, UserPrincipal principal) throws IOException, FirebaseMessagingException {
        NotifyPlusResponse reponse = new NotifyPlusResponse();
        if (!CollectionUtils.isEmpty(kidsList)) {

            Long idSchool = principal.getIdSchoolLogin();
            Optional<WebSystemTitle> webSystemTitle;
            String titleAppPlus = "";
            webSystemTitle = webSystemTitleService.findById(AppSendSystemTitle.PLUS_NOTIFY_TO_PARENT);
            if (webSystemTitle.isPresent()) {
                titleAppPlus = webSystemTitle.get().getTitle();
            }
            AppSend appSend = new AppSend();
            appSend.setReceivedNumber(kidsList.size());
            appSend.setSendContent(request.getSendContent());
            appSend.setSendTitle(titleAppPlus);
            appSend.setIdSchool(idSchool);
            appSend.setSendType(AppSendConstant.TYPE_COMMON);
            appSend.setAppType(principal.getAppType());
            appSend.setTypeReicever(AppTypeConstant.PARENT);
            appSend.setCreatedBy(principal.getFullName());
            appSend.setApproved(AppConstant.APP_TRUE);
            appSend = appSendRepository.save(appSend);

            AppSend finalAppSend1 = appSend;
            List<Receivers> receiversList = new ArrayList<>();
            String userCreate = principal.getFullName();
            kidsList.forEach(x -> {
                Receivers receivers = new Receivers();
                receivers.setIdUserReceiver(x.getIdUser());
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setApproved(AppConstant.APP_TRUE);
                receivers.setIdKids(x.getId());
                receivers.setIdClass(x.getIdClass());
                receivers.setIdUserReceiver(x.getIdUser());
                receivers.setIdSchool(idSchool);
                receivers.setCreatedBy(userCreate);
                receivers.setAppSend(finalAppSend1);
                receiversList.add(receivers);
            });

            Instant start4 = Instant.now();
            receiversRepository.saveAll(receiversList);
            Instant end4 = Instant.now();
            logger.info("{} save receiver: {}", getClass().getSimpleName(), Duration.between(start4, end4).toMillis());

            this.setPropertisMultiFile(request, appSend, idSchool);
            //gửi firebase
            Instant start = Instant.now();
            List<Long> idKidList = kidsList.stream().map(KidsInfoModel::getId).collect(Collectors.toList());
            firebaseFunctionService.sendParentByPlusListNew(4L, idKidList, FirebaseConstant.CATEGORY_NOTIFY, request.getSendContent(), idSchool);
            Instant end = Instant.now();
            logger.info("{} sent firebase: {}", getClass().getSimpleName(), Duration.between(start, end).toMillis());
            reponse.setCheck(AppConstant.APP_TRUE);
        }
        return reponse;
    }

    private void setPropertisMultiFile(NotifyPlusRequest request, AppSend appSend, Long idSchool) throws IOException {
        if (!CollectionUtils.isEmpty(request.getMultipartImageList())) {
            for (MultipartFile multipartFile : request.getMultipartImageList()) {
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(multipartFile, idSchool, UploadDownloadConstant.THONG_BAO);
                String urlWeb = handleFileResponse.getUrlWeb();
                String urlLocal = handleFileResponse.getUrlLocal();
                UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
                urlFileAppSend.setAttachPicture(urlWeb);
                urlFileAppSend.setUrlLocalPicture(urlLocal);
                urlFileAppSend.setName(multipartFile.getOriginalFilename());
                urlFileAppSend.setAppSend(appSend);
                urlFileAppSendRepository.save(urlFileAppSend);
            }
        }
        if (!CollectionUtils.isEmpty(request.getMultipartFileList())) {
            for (MultipartFile multipartFile : request.getMultipartFileList()) {
                UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
                HandleFileResponse handleFileResponse = HandleFileUtils.getUrlFieldOrPictureSaved(multipartFile, idSchool, UploadDownloadConstant.THONG_BAO);
                String urlWeb = handleFileResponse.getUrlWeb();
                String urlLocal = handleFileResponse.getUrlLocal();
                urlFileAppSend.setAttachFile(urlWeb);
                urlFileAppSend.setUrlLocalFile(urlLocal);
                urlFileAppSend.setName(multipartFile.getOriginalFilename());
                urlFileAppSend.setAppSend(appSend);
                urlFileAppSendRepository.save(urlFileAppSend);
            }
        }

    }
}
