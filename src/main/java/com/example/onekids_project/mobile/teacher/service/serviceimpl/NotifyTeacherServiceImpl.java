package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mobile.response.AttachFileMobileResponse;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.CreateNotifyTeacherRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.SearchNotifyTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.notify.ListMobileNotifyTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.notify.MobileNotifiDetailTeacherResponse;
import com.example.onekids_project.mobile.teacher.response.notify.MobileNotifyTecaherResponse;
import com.example.onekids_project.mobile.teacher.service.servicecustom.NotifyTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.WebSystemTitleService;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotifyTeacherServiceImpl implements NotifyTeacherService {

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @Autowired
    WebSystemTitleService webSystemTitleService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListMobileNotifyTeacherResponse findNotifiTeacherForMobile(UserPrincipal principal, SearchNotifyTeacherRequest searchNotifyTeacherRequest) {
        CommonValidate.checkDataNoClassTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        Long idTeacher = principal.getId();
        List<Receivers> receiversList = receiversRepository.findNotifyTeacherForMobile(idSchool, idTeacher, searchNotifyTeacherRequest);
        ListMobileNotifyTeacherResponse listMobileNotifiTeacher = new ListMobileNotifyTeacherResponse();
        List<MobileNotifyTecaherResponse> receiversResponseList = new ArrayList<>();
        receiversList.forEach(x -> {
            MobileNotifyTecaherResponse model = new MobileNotifyTecaherResponse();
            model.setId(x.getId());
            if (x.getAppSend().getIdCreated().equals(SystemConstant.ID_SYSTEM)) {
                model.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
            } else {
                MaUser maUser = maUserRepository.findById(x.getAppSend().getIdCreated()).orElseThrow(() -> new NoSuchElementException(ErrorsConstant.NOT_FOUND_USER));
                String appType = maUser.getAppType();
                if (appType.equals(AppTypeConstant.SUPPER_SCHOOL) || appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.TEACHER)) {
                    if (appType.equals(AppTypeConstant.SCHOOL) || appType.equals(AppTypeConstant.SUPPER_SCHOOL)) {
                        model.setAvatar(AvatarDefaultConstant.AVATAR_SCHOOL);
                    } else if (appType.equals(AppTypeConstant.TEACHER)) {
                        model.setAvatar(AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()));
                    }
                } else {
                    model.setAvatar(AvatarDefaultConstant.AVATAR_SYSTEM);
                }
            }
            model.setTitle(x.getAppSend().getSendTitle());
            String content = x.getAppSend().getSendContent();
            if (StringUtils.isBlank(content) || content.length() <= 50) {
                model.setContent(content);
            } else {
                model.setContent(content.substring(0, 50));
            }
            List<UrlFileAppSend> urlFileAppSendList = x.getAppSend().getUrlFileAppSendList();
            int pictureNumber = (int) urlFileAppSendList.stream().filter(y -> StringUtils.isNotBlank(y.getAttachPicture())).count();
            int fileNumber = (int) urlFileAppSendList.stream().filter(y -> StringUtils.isNotBlank(y.getAttachFile())).count();
            model.setPictureNumber(pictureNumber);
            model.setFileNumber(fileNumber);
            model.setCreatedDate(x.getCreatedDate());
            model.setSeen(x.isUserUnread());
            receiversResponseList.add(model);
        });

        boolean lastPage = receiversResponseList.size() < MobileConstant.MAX_PAGE_ITEM;
        listMobileNotifiTeacher.setDataList(receiversResponseList);
        listMobileNotifiTeacher.setLastPage(lastPage);
        return listMobileNotifiTeacher;
    }

    @Override
    public MobileNotifiDetailTeacherResponse findNotifiTeacherByIdForMobile(Long id, UserPrincipal principal) {
//        CommonValidate.checkDataTeacher(principal);
        Receivers receivers = receiversRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NotFoundException("not found notify detail by id"));

        MobileNotifiDetailTeacherResponse model = new MobileNotifiDetailTeacherResponse();
        List<String> urlPictureList = new ArrayList<>();
        List<AttachFileMobileResponse> mobileFileList = new ArrayList<>();
        model.setTitle(receivers.getAppSend().getSendTitle());
        model.setContent(receivers.getAppSend().getSendContent());
        model.setCreatedDate(receivers.getCreatedDate());

        List<UrlFileAppSend> urlFileAppSendList = receivers.getAppSend().getUrlFileAppSendList();
        urlFileAppSendList.forEach(x -> {
            String urlPicture = x.getAttachPicture();
            String urlFile = x.getAttachFile();
            if (StringUtils.isNotBlank(urlPicture)) {
                urlPictureList.add(urlPicture);
            }
            if (StringUtils.isNotBlank(urlFile)) {
                AttachFileMobileResponse mobileFile = new AttachFileMobileResponse();
                mobileFile.setName(x.getName());
                mobileFile.setUrl(urlFile);
                mobileFileList.add(mobileFile);
            }
        });
        LocalDateTime time = LocalDateTime.now();
        model.setListImage(urlPictureList);
        model.setFileList(mobileFileList);
        receivers.setUserUnread(AppConstant.APP_TRUE);
        receivers.setTimeRead(time);
        receiversRepository.save(receivers);
        return model;
    }

    @Transactional
    @Override
    public boolean createNotififorTeacherToParent(UserPrincipal principal, CreateNotifyTeacherRequest createNotifyTeacherRequest) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataTeacher(principal);
        Long idSchool = principal.getIdSchoolLogin();
        boolean sendApproved = principal.getSchoolConfig().isAppSendApproved();

        Optional<WebSystemTitle> webSystemTitle = webSystemTitleService.findById(AppSendSystemTitle.TEACHER_NOTIFY_TO_PARENT);

        String titleAppTeacher = webSystemTitle.get().getTitle();
        createNotifyTeacherRequest.setSendTitle(titleAppTeacher);

        List<Kids> kidsList = null;
        if (!CollectionUtils.isEmpty(createNotifyTeacherRequest.getIdList())) {
            kidsList = kidsRepository.findAllKids(principal, createNotifyTeacherRequest.getIdList());
            kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        }
        AppSend appSend = modelMapper.map(createNotifyTeacherRequest, AppSend.class);
        if (!CollectionUtils.isEmpty(kidsList)) {
            appSend.setReceivedNumber(kidsList.size());
        }
        appSend.setIdSchool(principal.getIdSchoolLogin());
        appSend.setSendType(AppSendConstant.TYPE_COMMON);
        appSend.setAppType(principal.getAppType());
        appSend.setTypeReicever(AppTypeConstant.PARENT);
        appSend.setCreatedBy(principal.getFullName());
        appSend.setApproved(sendApproved);
        appSend = appSendRepository.save(appSend);

        if (!CollectionUtils.isEmpty(kidsList)) {
            AppSend finalAppSend1 = appSend;
            kidsList.forEach(x -> {
                Receivers receivers = new Receivers();
                if (x.getParent().getMaUser() != null) {
                    receivers.setIdUserReceiver(x.getParent().getMaUser().getId());
                }
                receivers.setUserUnread(AppConstant.APP_FALSE);
                receivers.setApproved(sendApproved);
                receivers.setIdKids(x.getId());
                receivers.setIdClass(x.getMaClass().getId());
                receivers.setIdSchool(x.getIdSchool());
                receivers.setCreatedBy(principal.getFullName());
                receivers.setAppSend(finalAppSend1);
                receiversRepository.save(receivers);
            });
        }


        if (createNotifyTeacherRequest.getMultipartPictureList() != null && createNotifyTeacherRequest.getMultipartPictureList().

                size() > 0) {
            for (MultipartFile multipartFile : createNotifyTeacherRequest.getMultipartPictureList()) {
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
        if (createNotifyTeacherRequest.getMultipartFileList() != null && createNotifyTeacherRequest.getMultipartFileList().

                size() > 0) {
            for (MultipartFile multipartFile : createNotifyTeacherRequest.getMultipartFileList()) {
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


        if (sendApproved) {
            for (Kids x : kidsList) {
                //gửi firebase
                firebaseFunctionService.sendParentByTeacher(10L, x, FirebaseConstant.CATEGORY_NOTIFY, createNotifyTeacherRequest.getSendContent());
            }
        } else {
            //gửi thông báo cho plus để duyệt thông báo
            String title = "Giáo viên " + principal.getFullName() + " gửi thông báo cần duyệt";
            firebaseFunctionService.sendPlusCommonHasPlusList(title, createNotifyTeacherRequest.getSendTitle(), idSchool, FirebaseConstant.CATEGORY_NOTIFY_HISTORY);
        }
        return true;
    }
}
