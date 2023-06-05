package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.sample.WishesSample;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.request.BirthdayTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.birthday.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.BirthdayTeacherService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BirthdayTeacherServiceImpl implements BirthdayTeacherService {

    @Autowired
    KidsRepository kidsRepository;

    @Autowired
    ReceiversRepository receiversRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private UrlAppSendRepository urlAppSendRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private WishesSampleRepository wishesSampleRepository;


    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Override
    public ListBirthdayTeacherResponse findBirthdayList(UserPrincipal principal, LocalDateTime localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<BirthdayTeacherResponse> birthdayTeacherResponse = new ArrayList<>();
        ListBirthdayTeacherResponse model = new ListBirthdayTeacherResponse();
        List<Kids> dataList = kidsRepository.searchKidsBirthday(principal, localDate);
        dataList.forEach(x -> {
            BirthdayTeacherResponse birthdayTeacher = new BirthdayTeacherResponse();
            if (x.getAvatarKid() != null) {
                birthdayTeacher.setAvatar(x.getAvatarKid());
            } else birthdayTeacher.setAvatar(AvatarDefaultConstant.AVATAR_KIDS);
            birthdayTeacher.setBirthDay(ConvertData.fomaterLocalDate(x.getBirthDay()));
            birthdayTeacher.setNameKid(x.getFullName());
            birthdayTeacher.setId(x.getId());
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(x.getIdSchool(), x.getId());
            if (receiversList.size() < 1) {
                birthdayTeacher.setStatus(true); // chưa gửi
            } else birthdayTeacher.setStatus(false);// đã gửi
            birthdayTeacherResponse.add(birthdayTeacher);
        });
        model.setDataList(birthdayTeacherResponse);
        return model;
    }

    @Override
    public ListBirthdayTeacherResponse findBirthWeekList(UserPrincipal principal, LocalDateTime localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<BirthdayTeacherResponse> birthdayTeacherResponse = new ArrayList<>();
        ListBirthdayTeacherResponse model = new ListBirthdayTeacherResponse();
        LocalDateTime monday = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).plusWeeks(-1);
        List<Kids> dataList = kidsRepository.searchKidsBirthWeek(principal, monday.toLocalDate());
        dataList.forEach(x -> {
            BirthdayTeacherResponse birthdayTeacher = new BirthdayTeacherResponse();
            if (x.getAvatarKid() != null) {
                birthdayTeacher.setAvatar(x.getAvatarKid());
            } else birthdayTeacher.setAvatar(AvatarDefaultConstant.AVATAR_KIDS);
            birthdayTeacher.setBirthDay(ConvertData.fomaterLocalDate(x.getBirthDay()));
            birthdayTeacher.setNameKid(x.getFullName());
            birthdayTeacher.setId(x.getId());
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(x.getIdSchool(), x.getId());
            if (receiversList.size() < 1) {
                birthdayTeacher.setStatus(true); // chưa gửi
            } else birthdayTeacher.setStatus(false);// đã gửi
            birthdayTeacherResponse.add(birthdayTeacher);
        });
        model.setDataList(birthdayTeacherResponse);


        return model;
    }

    @Override
    public ListBirthdayTeacherResponse findBirthMonthList(UserPrincipal principal, LocalDateTime localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<BirthdayTeacherResponse> birthdayTeacherResponse = new ArrayList<>();
        ListBirthdayTeacherResponse model = new ListBirthdayTeacherResponse();
        List<Kids> dataList = kidsRepository.searchKidsBirthMonth(principal, localDate);
        dataList.forEach(x -> {
            BirthdayTeacherResponse birthdayTeacher = new BirthdayTeacherResponse();
            if (x.getAvatarKid() != null) {
                birthdayTeacher.setAvatar(x.getAvatarKid());
            } else birthdayTeacher.setAvatar(AvatarDefaultConstant.AVATAR_KIDS);
            birthdayTeacher.setBirthDay(ConvertData.fomaterLocalDate(x.getBirthDay()));
            birthdayTeacher.setNameKid(x.getFullName());
            birthdayTeacher.setId(x.getId());
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(x.getIdSchool(), x.getId());
            if (receiversList.size() < 1) {
                birthdayTeacher.setStatus(true); // chưa gửi
            } else birthdayTeacher.setStatus(false); // đã gửi
            birthdayTeacherResponse.add(birthdayTeacher);
        });
        model.setDataList(birthdayTeacherResponse);
        return model;
    }

    @Transactional
    @Override
    public boolean createBirthday(UserPrincipal userPrincipal, BirthdayTeacherRequest birthdayTeacherRequest) throws FirebaseMessagingException {
        CommonValidate.checkDataTeacher(userPrincipal);
        Optional<WebSystemTitle> titleSample = webSystemTitleRepository.findByIdAndDelActiveTrue(AppSendSystemTitle.TEACHER_BIRTHDAY_TO_PARENT);
        String title = titleSample.get().getTitle();

        AppSend newAppSend = modelMapper.map(birthdayTeacherRequest, AppSend.class);
        newAppSend.setAppType(userPrincipal.getAppType());
        newAppSend.setIdSchool(userPrincipal.getIdSchoolLogin());
        if (birthdayTeacherRequest.getIdKidList() != null) {
            newAppSend.setReceivedNumber(birthdayTeacherRequest.getIdKidList().size());
        } else {
            newAppSend.setReceivedNumber(1);
        }
        newAppSend.setTimeSend(LocalDateTime.now());
        newAppSend.setSendTitle(title);
        newAppSend.setCreatedBy(userPrincipal.getFullName());
        newAppSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        newAppSend.setTypeReicever(AppTypeConstant.PARENT);
        newAppSend.setCreatedBy(userPrincipal.getFullName());
        newAppSend.setApproved(AppConstant.APP_TRUE);
        appSendRepository.save(newAppSend);

        UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
        urlFileAppSend.setAppSend(newAppSend);
        urlFileAppSend.setAttachPicture(birthdayTeacherRequest.getUrlPicture());
        urlFileAppSend.setCreatedBy(userPrincipal.getFullName());
        urlAppSendRepository.save(urlFileAppSend);
        List<Kids> kidsList = new ArrayList<>();
        birthdayTeacherRequest.getIdKidList().forEach(id -> {
            Receivers receivers = new Receivers();
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(id).orElseThrow();
            receivers.setIdKids(id);
            receivers.setIdClass(kids.getMaClass().getId());
            receivers.setIdSchool(userPrincipal.getIdSchoolLogin());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setCreatedBy(userPrincipal.getFullName());
            if (kids.getParent() != null) {
                kidsList.add(kids);
                receivers.setIdUserReceiver(kids.getParent().getMaUser().getId());
            }
            receivers.setAppSend(newAppSend);
            receiversRepository.save(receivers);
        });
        for (Kids x : kidsList) {
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(1L, x, FirebaseConstant.CATEGORY_BIRTHDAY, birthdayTeacherRequest.getSendContent());
        }
        return true;
    }

    //    fireBasse
    private void sendFireBase(List<Kids> kids, String title, String content) throws FirebaseMessagingException {
        kids = kids.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
//        content = content.length() < 50 ? content : content.substring(0, 50);
        for (Kids x : kids) {
            List<TokenFirebaseObject> tokenFirebaseObjectList = firebaseService.getParentOneTokenFirebases(x.getParent());
            if (CollectionUtils.isNotEmpty(tokenFirebaseObjectList)) {
                NotifyRequest notifyRequest = new NotifyRequest();
                notifyRequest.setBody(content.length() < 50 ? content : content.substring(0, 50));
                notifyRequest.setTitle(title);
                FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsParent(tokenFirebaseObjectList, FirebaseRouterConstant.BIRTHDAY_PARENT, notifyRequest, x.getId().toString());
            }
        }
    }

    @Override
    public BirthdaySampleTeacherResponse findWishTeacher(UserPrincipal principal) {
//        CommonValidate.checkDataTeacher(principal);
        List<WishesSample> wishesSampleList = new ArrayList<>();
        if (principal.getSchoolConfig().isShowWishSys()) {
            wishesSampleList = wishesSampleRepository.searchWishesSampleTeacher(0L);
            wishesSampleList.addAll(wishesSampleRepository.searchWishesSampleTeacher(principal.getIdSchoolLogin()));
        } else {
            wishesSampleList = wishesSampleRepository.searchWishesSampleTeacher(principal.getIdSchoolLogin());
        }


        List<WishesSampleTeacherResponse> wishesSampleResponseList = listMapper.mapList(wishesSampleList, WishesSampleTeacherResponse.class);

        BirthdaySampleTeacherResponse listWishesSampleResponse = new BirthdaySampleTeacherResponse();
        listWishesSampleResponse.setDataList(wishesSampleResponseList);
        return listWishesSampleResponse;
    }

    @Override
    public SumBirthdayTeacherResponse sumBirthday(UserPrincipal principal) {
        LocalDateTime datatime = LocalDateTime.now();
        SumBirthdayTeacherResponse data = new SumBirthdayTeacherResponse();
        ListBirthdayTeacherResponse listBirthday = this.findBirthdayListPlus(principal, datatime);
        ListBirthdayTeacherResponse listBirthdayWeek = this.findBirthWeekList(principal, datatime);
        ListBirthdayTeacherResponse listBirthdayMonth = this.findBirthMonthList(principal, datatime);
        data.setSumDay((long) listBirthday.getDataList().size());
        data.setSumWeek((long) listBirthdayWeek.getDataList().size());
        data.setSumMonth((long) listBirthdayMonth.getDataList().size());
        return data;
    }

    public ListBirthdayTeacherResponse findBirthdayListPlus(UserPrincipal principal, LocalDateTime localDate) {
        CommonValidate.checkDataTeacher(principal);
        List<BirthdayTeacherResponse> birthdayTeacherResponse = new ArrayList<>();
        ListBirthdayTeacherResponse model = new ListBirthdayTeacherResponse();
        List<Kids> dataList = kidsRepository.searchKidsBirthday(principal, localDate);
        dataList.forEach(x -> {
            BirthdayTeacherResponse birthdayTeacher = new BirthdayTeacherResponse();
            if (x.getAvatarKid() != null) {
                birthdayTeacher.setAvatar(x.getAvatarKid());
            } else birthdayTeacher.setAvatar(AvatarDefaultConstant.AVATAR_KIDS);
            birthdayTeacher.setBirthDay(ConvertData.fomaterLocalDate(x.getBirthDay()));
            birthdayTeacher.setNameKid(x.getFullName());
            birthdayTeacher.setId(x.getId());
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(x.getIdSchool(), x.getId());
            if (receiversList.size() < 1) {
                birthdayTeacher.setStatus(true); // chưa gửi
            } else birthdayTeacher.setStatus(false);// đã gửi
            birthdayTeacherResponse.add(birthdayTeacher);
        });
        model.setDataList(birthdayTeacherResponse);
        return model;
    }

}
