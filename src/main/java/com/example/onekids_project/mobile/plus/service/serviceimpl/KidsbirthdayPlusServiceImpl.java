package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.birthday.BirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.request.birthday.SearchKidsBirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.response.birthday.CoutbirthDayPlusResponse;
import com.example.onekids_project.mobile.plus.response.birthday.KidsBirthdayPlusResponse;
import com.example.onekids_project.mobile.plus.response.birthday.ListKidsBirthdayPlusResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.KidsbirthdayPlusMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KidsbirthdayPlusServiceImpl implements KidsbirthdayPlusMobileService {

    @Autowired
    MediaRepository mediaRepository;
    @Autowired
    SysInforRepository sysInforRepository;
    @Autowired
    ListMapper listMapper;
    @Autowired
    private ReceiversRepository receiversRepository;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private AppSendRepository appSendRepository;
    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;


    @Override
    public ListKidsBirthdayPlusResponse searchKidsBirthdayPlus(UserPrincipal principal, SearchKidsBirthdayPlusRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.findKidByIdSchool(idSchool, request);
        ListKidsBirthdayPlusResponse listKidsBirthdayPlusResponse = new ListKidsBirthdayPlusResponse();
        List<KidsBirthdayPlusResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsBirthdayPlusResponse model = new KidsBirthdayPlusResponse();
            model.setId(x.getId());
            model.setNameKid(x.getFullName());
            model.setBirthDay(ConvertData.convertDateString(x.getBirthDay()));
            model.setAvatar(ConvertData.getAvatarKid(x));
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(idSchool, x.getId());
            model.setStatus(receiversList.size() < 1 ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            responseList.add(model);
        });
        listKidsBirthdayPlusResponse.setResponseList(responseList);
        return listKidsBirthdayPlusResponse;
    }


    @Override
    public ListKidsBirthdayPlusResponse findBirthWeekList(UserPrincipal principal, SearchKidsBirthdayPlusRequest request, LocalDateTime localDate) {
        Long idSchool = principal.getIdSchoolLogin();
        List<KidsBirthdayPlusResponse> responseList = new ArrayList<>();
        ListKidsBirthdayPlusResponse model = new ListKidsBirthdayPlusResponse();
        LocalDateTime monday = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).plusWeeks(-1);
        List<Kids> kidsList = kidsRepository.searchKidsBirthWeekforplus(idSchool, request, monday.toLocalDate());
        kidsList.forEach(x -> {
            KidsBirthdayPlusResponse model1 = new KidsBirthdayPlusResponse();
            model1.setAvatar(ConvertData.getAvatarKid(x));
            model1.setBirthDay(ConvertData.fomaterLocalDate(x.getBirthDay()));
            model1.setNameKid(x.getFullName());
            model1.setId(x.getId());
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(x.getIdSchool(), x.getId());
            model1.setStatus(receiversList.size() < 1 ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            responseList.add(model1);
        });
        model.setResponseList(responseList);


        return model;
    }

    @Override
    public ListKidsBirthdayPlusResponse searchMonthBirthdayPlus(UserPrincipal principal, SearchKidsBirthdayPlusRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.searchMonthBirthday(idSchool, request);
        ListKidsBirthdayPlusResponse listKidsBirthdayPlusResponse = new ListKidsBirthdayPlusResponse();
        List<KidsBirthdayPlusResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            KidsBirthdayPlusResponse model = new KidsBirthdayPlusResponse();
            model.setId(x.getId());
            model.setNameKid(x.getFullName());
            model.setBirthDay(ConvertData.convertDateString(x.getBirthDay()));
            model.setAvatar(ConvertData.getAvatarKid(x));
            List<Receivers> receiversList = receiversRepository.findBirthdayStatus(idSchool, x.getId());
            model.setStatus(receiversList.size() < 1 ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            responseList.add(model);
        });
        listKidsBirthdayPlusResponse.setResponseList(responseList);
        return listKidsBirthdayPlusResponse;
    }

    @Override
    public CoutbirthDayPlusResponse coutbirthday(UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        LocalDate localDate = LocalDate.now();
        CoutbirthDayPlusResponse model = new CoutbirthDayPlusResponse();
        LocalDate monday = localDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).plusWeeks(-1);
        List<Kids> kidsList = kidsRepository.findKidByIdSchoolcout(idSchool);
        List<Kids> kidsListweek = kidsRepository.searchKidsBirthWeekforplusnew(idSchool, monday);
        List<Kids> kidsListmonth = kidsRepository.searchMonthBirthdaycout(idSchool);
        model.setDay(kidsList.size());
        model.setWeek(kidsListweek.size());
        model.setMonth(kidsListmonth.size());
        return model;
    }

    @Transactional
    @Override
    public boolean sendKidsBirthday(UserPrincipal principal, BirthdayPlusRequest request) throws FirebaseMessagingException {
        Optional<WebSystemTitle> titleSample = webSystemTitleRepository.findByIdAndDelActiveTrue(AppSendSystemTitle.PLUS_BIRTHDAY_TO_KID);
        String title = titleSample.get().getTitle();
        AppSend newAppSend = modelMapper.map(request, AppSend.class);
        newAppSend.setAppType(principal.getAppType());
        newAppSend.setIdSchool(principal.getIdSchoolLogin());
        if (request.getIdKidList() != null) {
            newAppSend.setReceivedNumber(request.getIdKidList().size());
        } else {
            newAppSend.setReceivedNumber(1);
        }
        newAppSend.setTimeSend(LocalDateTime.now());
        newAppSend.setSendTitle(title);
        newAppSend.setCreatedBy(principal.getFullName());
        newAppSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        newAppSend.setTypeReicever(AppTypeConstant.PARENT);
        newAppSend.setCreatedBy(principal.getFullName());
        newAppSend.setApproved(AppConstant.APP_TRUE);
        appSendRepository.save(newAppSend);

        UrlFileAppSend urlFileAppSend = new UrlFileAppSend();
        urlFileAppSend.setAppSend(newAppSend);
        urlFileAppSend.setAttachPicture(request.getUrlPicture());
        urlFileAppSend.setCreatedBy(principal.getFullName());
        urlFileAppSendRepository.save(urlFileAppSend);
        List<Kids> kidsList = new ArrayList<>();
        request.getIdKidList().forEach(x -> {
            Receivers receivers = new Receivers();
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            receivers.setIdKids(x);
            receivers.setIdClass(kids.getMaClass().getId());
            receivers.setIdSchool(principal.getIdSchoolLogin());
            receivers.setApproved(AppConstant.APP_TRUE);
            receivers.setCreatedBy(principal.getFullName());
            if (kids.getParent() != null) {
                kidsList.add(kids);
                receivers.setIdUserReceiver(kids.getParent().getMaUser().getId());
            }
            receivers.setAppSend(newAppSend);
            receiversRepository.save(receivers);
        });
        for (Kids x: kidsList){
            //gửi firebase
            firebaseFunctionService.sendParentByPlus(1L, x, FirebaseConstant.CATEGORY_BIRTHDAY, request.getSendContent());
        }
        return true;
    }

}
