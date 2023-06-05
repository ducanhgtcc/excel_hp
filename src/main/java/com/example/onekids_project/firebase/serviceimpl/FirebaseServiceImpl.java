package com.example.onekids_project.firebase.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.BirthdaySample;
import com.example.onekids_project.entity.user.*;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseDataService;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.util.FilterDataUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class FirebaseServiceImpl implements FirebaseService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String title = "Chúc mừng sinh nhật";
    private final String SYSTEM = "system";
    private final String SCHOOL = "school";

    @Autowired
    private FirebaseHistoryRepository firebaseHistoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private CelebrateSampleRepository celebrateSampleRepository;

    @Autowired
    private BirthdaySampleRepository birthdaySampleRepository;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    private FirebaseDataService firebaseDataService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private SmsDataService smsDataService;
    @Autowired
    private AppSendService appSendService;

    @Override
    public void sendToToken(String token, String route, Long id, NotifyRequest notifyRequest) throws FirebaseMessagingException {
        Notification notify = Notification.builder().setTitle(notifyRequest.getTitle()).setBody(notifyRequest.getBody()).build();
        Map<String, String> mapData = new HashMap<>();
        mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
        mapData.put(FirebaseConstant.ROUTER_KEY, route);
        mapData.put(FirebaseConstant.ID_KEY, id.toString());
        Message message = Message.builder()
                .setNotification(notify)
                .putAllData(mapData)
                .setToken(token)
                .build();
        String response = FirebaseMessaging.getInstance().send(message);
        logger.info(response + " messages were sent successfully token=" + token);
    }

    @Override
    public void sendMulticast(List<String> tokenList, String route, Long id, NotifyRequest notifyRequest) throws FirebaseMessagingException {
        if (!CollectionUtils.isEmpty(tokenList)) {
            Notification notify = Notification.builder().setTitle(notifyRequest.getTitle()).setBody(notifyRequest.getBody()).build();
            Map<String, String> mapData = new HashMap<>();
            mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
            mapData.put(FirebaseConstant.ROUTER_KEY, route);
            mapData.put(FirebaseConstant.ID_KEY, id.toString());

            MulticastMessage message = MulticastMessage.builder()
                    .setNotification(notify)
                    .putAllData(mapData)
                    .addAllTokens(tokenList)
                    .build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            logger.info(response + " messages were sent tokens=" + tokenList);
        }
    }


    @Transactional
    @Override
    public FirebaseResponse sendMulticastAndHandleErrorsParent(List<TokenFirebaseObject> tokenFirebaseObjectList, String router, NotifyRequest notifyRequest, String idKid) throws FirebaseMessagingException {
        FirebaseResponse model = new FirebaseResponse();
        List<String> tokenList = tokenFirebaseObjectList.stream().filter(x -> x.getTokenFirebase() != null).map(x -> x.getTokenFirebase()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tokenList)) {
            Notification notify = Notification.builder().setTitle(notifyRequest.getTitle()).setBody(notifyRequest.getBody()).build();
            Map<String, String> mapData = new HashMap<>();
            mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
            mapData.put(FirebaseConstant.ROUTER_KEY, router);
            mapData.put(FirebaseConstant.ID_KID, idKid);
            Aps aps = Aps.builder().setSound("default").build();


            ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();
            MulticastMessage message = MulticastMessage.builder()
                    .setApnsConfig(apnsConfig)
                    .setNotification(notify)
                    .putAllData(mapData)
                    .addAllTokens(tokenList)
                    .build();

//            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            BatchResponse response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("parent")).sendMulticast(message);

            int failCount = response.getFailureCount();
            if (failCount > 0) {
                logger.warn("number token failure: " + failCount);
            }
            List<String> failedTokensFail = new ArrayList<>();
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokensFail.add(tokenList.get(i));
                    }
                }
                logger.warn("List of tokens that caused failures: " + failedTokensFail);

                this.saveHistoryFirebase(failedTokensFail, tokenFirebaseObjectList, notifyRequest, AppTypeConstant.PARENT, router);
            }
            model.setSuccessNubmer(tokenList.size() - failCount);
            model.setFailNumber(failCount);

        }
        return model;

    }

    @Override
    public FirebaseResponse sendMulticastAndHandleErrorsTeacherClass(List<TokenFirebaseObject> tokenFirebaseObjectList, String router, NotifyRequest notifyRequest, String idClass) throws FirebaseMessagingException {
        FirebaseResponse model = new FirebaseResponse();
        List<String> tokenList = tokenFirebaseObjectList.stream().map(x -> x.getTokenFirebase()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tokenList)) {
            Notification notify = Notification.builder().setTitle(notifyRequest.getTitle()).setBody(notifyRequest.getBody()).build();
            Map<String, String> mapData = new HashMap<>();
            mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
            mapData.put(FirebaseConstant.ROUTER_KEY, router);
            mapData.put(FirebaseConstant.ID_CLASS, idClass);
            Aps aps = Aps.builder().setSound("default").build();


            ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();
            MulticastMessage message = MulticastMessage.builder()
                    .setApnsConfig(apnsConfig)
                    .setNotification(notify)
                    .putAllData(mapData)
                    .addAllTokens(tokenList)
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("teacher")).sendMulticast(message);

            int failCount = response.getFailureCount();
            if (failCount > 0) {
                logger.warn("number token failure: " + failCount);
            }
            List<String> failedTokensFail = new ArrayList<>();
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokensFail.add(tokenList.get(i));
                    }
                }
                logger.warn("List of tokens that caused failures: " + failedTokensFail);

                this.saveHistoryFirebase(failedTokensFail, tokenFirebaseObjectList, notifyRequest, AppTypeConstant.TEACHER, router);
            }
            model.setSuccessNubmer(tokenList.size() - failCount);
            model.setFailNumber(failCount);

        }
        return model;

    }

    @Override
    public FirebaseResponse sendMulticastAndHandleErrorsTeacher(List<TokenFirebaseObject> tokenFirebaseObjectList, String router, NotifyRequest notifyRequest) throws FirebaseMessagingException {
        FirebaseResponse model = new FirebaseResponse();
        List<String> tokenList = tokenFirebaseObjectList.stream().map(x -> x.getTokenFirebase()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tokenList)) {
            Notification notify = Notification.builder().setTitle(notifyRequest.getTitle()).setBody(notifyRequest.getBody()).build();
            Map<String, String> mapData = new HashMap<>();
            mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
            mapData.put(FirebaseConstant.ROUTER_KEY, router);
            mapData.put(FirebaseConstant.ID_CLASS, "");
            Aps aps = Aps.builder().setSound("default").build();


            ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();
            MulticastMessage message = MulticastMessage.builder()
                    .setApnsConfig(apnsConfig)
                    .setNotification(notify)
                    .putAllData(mapData)
                    .addAllTokens(tokenList)
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("teacher")).sendMulticast(message);

            int failCount = response.getFailureCount();
            if (failCount > 0) {
                logger.warn("number token failure: " + failCount);
            }
            List<String> failedTokensFail = new ArrayList<>();
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokensFail.add(tokenList.get(i));
                    }
                }
                logger.warn("List of tokens that caused failures: " + failedTokensFail);

                this.saveHistoryFirebase(failedTokensFail, tokenFirebaseObjectList, notifyRequest, AppTypeConstant.TEACHER, router);
            }
            model.setSuccessNubmer(tokenList.size() - failCount);
            model.setFailNumber(failCount);

        }
        return model;

    }

    public void sendOneAndHandleErrors(FirebaseHistory firebaseHistory) throws FirebaseMessagingException {
        String tokenList = firebaseHistory.getTokenFirebase();
        if (StringUtils.isNotBlank(tokenList)) {
            Notification notify = Notification.builder().setTitle(firebaseHistory.getTitle()).setBody(firebaseHistory.getBody()).build();
            Map<String, String> mapData = new HashMap<>();
            mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
            mapData.put(FirebaseConstant.ROUTER_KEY, firebaseHistory.getRouter());
            Aps aps = Aps.builder().setSound("default").build();


            ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();
            MulticastMessage message = MulticastMessage.builder()
                    .setApnsConfig(apnsConfig)
                    .setNotification(notify)
                    .putAllData(mapData)
                    .addToken(tokenList)
                    .build();

            BatchResponse response;
            if (firebaseHistory.getAppType().equalsIgnoreCase(AppTypeConstant.TEACHER)) {
                response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("teacher")).sendMulticast(message);
            } else if (firebaseHistory.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT)) {
                response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("parent")).sendMulticast(message);
            } else {
                response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("plus")).sendMulticast(message);
            }
//            if (response.getFailureCount() > 0) {
//                int numberSend = firebaseHistory.getNumberSend();
//                if (numberSend <= FirebaseConstant.SEND_AGAIN) {
//                    firebaseHistory.setNumberSend(numberSend + 1);
//                    firebaseHistoryRepository.save(firebaseHistory);
//                } else {
//                    firebaseHistoryRepository.deleteById(firebaseHistory.getId());
//                }
//            }

        }
    }

    @Override
    public FirebaseResponse sendMulticastAndHandleErrorsPlus(List<TokenFirebaseObject> tokenFirebaseObjectList, String router, NotifyRequest notifyRequest, Long idSchool) throws FirebaseMessagingException {
        FirebaseResponse model = new FirebaseResponse();
        List<String> tokenList = tokenFirebaseObjectList.stream().map(TokenFirebaseObject::getTokenFirebase).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(tokenList)) {
            Notification notify = Notification.builder().setTitle(notifyRequest.getTitle()).setBody(notifyRequest.getBody()).build();
            Map<String, String> mapData = new HashMap<>();
            mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
            mapData.put(FirebaseConstant.ROUTER_KEY, router);
            mapData.put(FirebaseConstant.ID_SCHOOL, idSchool.toString());
            Aps aps = Aps.builder().setSound("default").build();


            ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();
            MulticastMessage message = MulticastMessage.builder()
                    .setApnsConfig(apnsConfig)
                    .setNotification(notify)
                    .putAllData(mapData)
                    .addAllTokens(tokenList)
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance(FirebaseApp.getInstance("plus")).sendMulticast(message);

            int failCount = response.getFailureCount();
            if (failCount > 0) {
                logger.warn("number token failure: " + failCount);
            }
            List<String> failedTokensFail = new ArrayList<>();
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        failedTokensFail.add(tokenList.get(i));
                    }
                }
                logger.warn("List of tokens that caused failures: " + failedTokensFail);

                this.saveHistoryFirebase(failedTokensFail, tokenFirebaseObjectList, notifyRequest, AppTypeConstant.SCHOOL, router);
            }
            model.setSuccessNubmer(tokenList.size() - failCount);
            model.setFailNumber(failCount);

        }
        return model;
    }


    private List<TokenFirebaseObject> setTokenUser(List<String> stringList, List<TokenFirebaseObject> tokenFirebaseObjectList) {
        return tokenFirebaseObjectList.stream().filter(x -> stringList.stream().anyMatch(y -> y.equals(x.getTokenFirebase()))).collect(Collectors.toList());
    }

    private void saveHistoryFirebase(List<String> failedTokensFail, List<TokenFirebaseObject> tokenFirebaseObjectList, NotifyRequest notifyRequest, String appType, String router) {

//        List<TokenFirebaseObject> failList = this.setTokenUser(failedTokensFail, tokenFirebaseObjectList);
//        failList.forEach(x -> {
//            FirebaseHistory firebaseHistory = modelMapper.map(x, FirebaseHistory.class);
//            firebaseHistory.setBody(notifyRequest.getBody());
//            firebaseHistory.setTitle(notifyRequest.getTitle());
//            firebaseHistory.setAppType(appType);
//            firebaseHistory.setRouter(router);
//            firebaseHistoryRepository.save(firebaseHistory);
//        });

    }

    @Override
    public List<TokenFirebaseObject> getEmployeeTokenFirebases(List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee() != null).collect(Collectors.toList());
        infoEmployeeSchoolList.forEach(x -> {
            MaUser maUser = x.getEmployee().getMaUser();

            List<Device> deviceList = maUser.getDeviceList();
            //chỉ lấy những device đang đăng nhập
            deviceList = deviceList.stream().filter(a -> a.isLogin() && (a.getType().equals(DeviceTypeConstant.ANDROID) || a.getType().equals(DeviceTypeConstant.IOS))).collect(Collectors.toList());
            deviceList.forEach(y -> {
                TokenFirebaseObject model = new TokenFirebaseObject();
                model.setId(maUser.getId());
                model.setFullName(maUser.getFullName());
                model.setTokenFirebase(y.getTokenFirebase());
                dataList.add(model);
            });
        });
        return dataList;
    }

    @Override
    public List<TokenFirebaseObject> getPlusTokenFirebases(List<InfoEmployeeSchool> infoEmployeeSchoolList, String function) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
        List<InfoEmployeeSchool> infoEmployeeSchoolListNew = null;
        infoEmployeeSchoolListNew = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee() != null && getStatusFunction(x, function)).collect(Collectors.toList());
        infoEmployeeSchoolListNew.forEach(x -> {
            MaUser maUser = x.getEmployee().getMaUser();
            List<Device> deviceList = maUser.getDeviceList();
            //chỉ lấy những device đang đăng nhập
            deviceList = deviceList.stream().filter(a -> a.isLogin() && (a.getType().equals(DeviceTypeConstant.ANDROID) || a.getType().equals(DeviceTypeConstant.IOS))).collect(Collectors.toList());
            deviceList.forEach(y -> {
                TokenFirebaseObject model = new TokenFirebaseObject();
                model.setId(maUser.getId());
                model.setFullName(maUser.getFullName());
                model.setTokenFirebase(y.getTokenFirebase());
                dataList.add(model);
            });
        });
        return dataList;
    }

    @Override
    public void sendFirebaseFail() throws FirebaseMessagingException {
//        List<FirebaseHistory> firebaseHistoryList = firebaseHistoryRepository.findAll();
//        for (FirebaseHistory x : firebaseHistoryList) {
//            sendOneAndHandleErrors(x);
//        }
    }

    @Transactional
    @Override
    public void sendAutoFirebaseBirthday() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        LocalDate nowDate = LocalDate.now();
        this.sendFirebaseKids(nowDate, false);
        this.sendFirebaseParents(nowDate, false);
        this.sendFirebaseEmpolyees(nowDate, false);
    }

    @Transactional
    @Override
    public void sendAutoFirebaseBirthdaySystem() throws FirebaseMessagingException, ExecutionException, InterruptedException {
        LocalDate nowDate = LocalDate.now();
        this.sendFirebaseKids(nowDate, true);
        this.sendFirebaseParents(nowDate, true);
        this.sendFirebaseEmpolyees(nowDate, true);
    }

    /**
     * system=true là của hệ thống, false là của trường
     *
     * @param localDate
     * @param system
     * @throws FirebaseMessagingException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private void sendFirebaseKids(LocalDate localDate, boolean system) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        List<Kids> kidsList = kidsRepository.searchKidsBirthdayNoSchool(localDate);
        for (Kids x : kidsList) {
            Long idSchool = system ? SystemConstant.ID_SYSTEM : x.getIdSchool();
            Optional<BirthdaySample> birthdaySampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayType(idSchool, AppConstant.KID_NAME);
            this.sendFirebaseAndSmsToParent(birthdaySampleOptional, x, SampleConstant.KIDS);
        }
    }


    private void sendFirebaseParents(LocalDate localDate, boolean system) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        List<Kids> kidsList = kidsRepository.searchParentBirthdayNoSchool(localDate);
        kidsList = kidsList.stream().filter(FilterDataUtils.distinctBy(Kids::getId)).collect(Collectors.toList());
        for (Kids x : kidsList) {
            Long idSchool = system ? SystemConstant.ID_SYSTEM : x.getIdSchool();
            Optional<BirthdaySample> birthdaySampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayType(idSchool, AppConstant.PARENT_NAME);
            this.sendFirebaseAndSmsToParent(birthdaySampleOptional, x, SampleConstant.PARENT);
        }
    }


    private void sendFirebaseEmpolyees(LocalDate localDate, boolean system) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        List<InfoEmployeeSchool> infoEmployeeSchoolsList = infoEmployeeSchoolRepository.findEmployeeAllBirthdayNoSchool(localDate);
        infoEmployeeSchoolsList = infoEmployeeSchoolsList.stream().filter(FilterDataUtils.distinctBy(InfoEmployeeSchool::getId)).collect(Collectors.toList());
        for (InfoEmployeeSchool x : infoEmployeeSchoolsList) {
            Long idSchool = system ? SystemConstant.ID_SYSTEM : x.getSchool().getId();
            Optional<BirthdaySample> birthdaySampleOptional = birthdaySampleRepository.findByIdSchoolAndBirthdayType(idSchool, AppConstant.TEACHER_NAME);
            this.sendFirebaseAndSmsToEmployee(birthdaySampleOptional, x);
        }
    }

    private void sendFirebaseAndSmsToParent(Optional<BirthdaySample> birthdaySampleOptional, Kids kids, String sampleType) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        if (birthdaySampleOptional.isPresent()) {
            BirthdaySample birthdaySample = birthdaySampleOptional.get();
            Long idSchool = kids.getIdSchool();
            String content = "";
            if (sampleType.equals(SampleConstant.KIDS)) {
                content = birthdaySample.getContent().replace("{ten}", kids.getFullName());
            } else if (sampleType.equals(SampleConstant.PARENT)) {
                content = birthdaySample.getContent().replace("{ten}", "");
            }
            //gửi firebase
            if (birthdaySample.isAppSend()) {
                firebaseFunctionService.sendParentByPlus(1L, kids, FirebaseConstant.CATEGORY_BIRTHDAY, content);
                String picture = StringUtils.isNotBlank(birthdaySample.getUrlPicture()) ? birthdaySample.getUrlPicture() : PictureConstant.BIRTHDAY_SCHOOL;
                appSendService.saveToAppSendParentForAuto(idSchool, kids, title, content, AppSendConstant.TYPE_BIRTHDAY, picture);
            }
            //gửi sms
            if (birthdaySample.isSmsSend()) {
                SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                smsNotifyDataRequest.setSendContent(content);
                smsDataService.sendSmsKid(Collections.singletonList(kids), idSchool, smsNotifyDataRequest);
            }
        }
    }

    private void sendFirebaseAndSmsToEmployee(Optional<BirthdaySample> birthdaySampleOptional, InfoEmployeeSchool infoEmployeeSchool) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        if (birthdaySampleOptional.isPresent()) {
            BirthdaySample birthdaySample = birthdaySampleOptional.get();
            Long idSchool = infoEmployeeSchool.getSchool().getId();
            String content = birthdaySample.getContent().replace("{ten}", infoEmployeeSchool.getFullName());
            //gửi firebase
            if (birthdaySample.isAppSend()) {
                firebaseFunctionService.sendTeacherByPlus(1L, infoEmployeeSchool, content, FirebaseConstant.CATEGORY_BIRTHDAY, idSchool);
                String picture = StringUtils.isNotBlank(birthdaySample.getUrlPicture()) ? birthdaySample.getUrlPicture() : PictureConstant.BIRTHDAY_SCHOOL;
                appSendService.saveToAppSendEmployeeForAuto(idSchool, infoEmployeeSchool, title, content, AppSendConstant.TYPE_BIRTHDAY, picture);
            }
            //gửi sms
            if (birthdaySample.isSmsSend()) {
                SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                smsNotifyDataRequest.setSendContent(content);
                smsDataService.sendSmsEmployee(Collections.singletonList(infoEmployeeSchool), idSchool, smsNotifyDataRequest, AppTypeConstant.TEACHER);
            }
        }
    }

    private void saveAppSendKids(NotifyRequest notifyRequest, Long idSchool, Kids kids) {
        AppSend appSend = new AppSend();
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setSendTitle(notifyRequest.getTitle());
        appSend.setSendContent(notifyRequest.getBody());
        appSend.setIdSchool(idSchool);
        appSend.setIdCreated(0L);
        appSend.setCreatedDate(LocalDateTime.now());
        appSend.setTypeReicever(AppTypeConstant.PARENT);
        appSend.setReceivedNumber(1);
        appSend.setAppType(AppTypeConstant.SYSTEM);
        appSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        AppSend appSendSave = appSendRepository.save(appSend);
        Receivers receivers = new Receivers();
        receivers.setAppSend(appSendSave);
        receivers.setIdSchool(appSendSave.getIdSchool());
        receivers.setIdKids(kids.getId());
        receivers.setIdUserReceiver(kids.getParent().getMaUser().getId());
        receivers.setApproved(AppConstant.APP_TRUE);
        receivers.setIdClass(kids.getMaClass().getId());
        receivers.setIdCreated(0L);
        receivers.setCreatedDate(LocalDateTime.now());
        receiversRepository.save(receivers);
    }

    private void saveAppSendParent(NotifyRequest notifyRequest, Long idSchool, List<Parent> parentList) {
        AppSend appSend = new AppSend();
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setSendTitle(notifyRequest.getTitle());
        appSend.setSendContent(notifyRequest.getBody());
        appSend.setIdSchool(idSchool);
        appSend.setTypeReicever(AppTypeConstant.PARENT);
        appSend.setReceivedNumber(parentList.size());
        appSend.setIdCreated(0L);
        appSend.setCreatedDate(LocalDateTime.now());
        appSend.setAppType(AppTypeConstant.SYSTEM);
        appSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        AppSend appSendSave = appSendRepository.save(appSend);
        parentList.forEach(x -> {
            Receivers receivers = new Receivers();
            receivers.setAppSend(appSendSave);
            receivers.setIdSchool(appSendSave.getIdSchool());
            receivers.setIdUserReceiver(x.getMaUser().getId());
            receivers.setIdCreated(0L);
            receivers.setCreatedDate(LocalDateTime.now());
            receivers.setApproved(AppConstant.APP_TRUE);
            receiversRepository.save(receivers);
        });

    }

    private void saveAppSendEmployee(NotifyRequest notifyRequest, Long idSchool, List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        AppSend appSend = new AppSend();
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setSendTitle(notifyRequest.getTitle());
        appSend.setSendContent(notifyRequest.getBody());
        appSend.setIdSchool(idSchool);
        appSend.setReceivedNumber(infoEmployeeSchoolList.size());
        appSend.setAppType(AppTypeConstant.SYSTEM);
        appSend.setTypeReicever(AppTypeConstant.TEACHER);
        appSend.setIdCreated(0L);
        appSend.setCreatedDate(LocalDateTime.now());
        appSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        AppSend appSendSave = appSendRepository.save(appSend);
        infoEmployeeSchoolList.forEach(x -> {
            Receivers receivers = new Receivers();
            receivers.setAppSend(appSendSave);
            receivers.setIdSchool(appSendSave.getIdSchool());
            receivers.setIdCreated(0L);
            receivers.setCreatedDate(LocalDateTime.now());
            receivers.setIdUserReceiver(x.getEmployee().getMaUser().getId());
            receivers.setApproved(AppConstant.APP_TRUE);
            receiversRepository.save(receivers);
        });
    }

    private void saveAppSendEmployeeOne(NotifyRequest notifyRequest, Long idSchool, InfoEmployeeSchool infoEmployee) {
        AppSend appSend = new AppSend();
        appSend.setApproved(AppConstant.APP_TRUE);
        appSend.setSendTitle(notifyRequest.getTitle());
        appSend.setSendContent(notifyRequest.getBody());
        appSend.setIdSchool(idSchool);
        appSend.setReceivedNumber(1);
        appSend.setAppType(AppTypeConstant.SYSTEM);
        appSend.setTypeReicever(AppTypeConstant.TEACHER);
        appSend.setIdCreated(0L);
        appSend.setCreatedDate(LocalDateTime.now());
        appSend.setSendType(AppSendConstant.TYPE_BIRTHDAY);
        AppSend appSendSave = appSendRepository.save(appSend);
        Receivers receivers = new Receivers();
        receivers.setAppSend(appSendSave);
        receivers.setIdSchool(appSendSave.getIdSchool());
        receivers.setIdCreated(0L);
        receivers.setCreatedDate(LocalDateTime.now());
        receivers.setIdUserReceiver(infoEmployee.getEmployee().getMaUser().getId());
        receivers.setApproved(AppConstant.APP_TRUE);
        receiversRepository.save(receivers);
    }

    @Override
    public List<TokenFirebaseObject> getParentOneTokenFirebases(Parent parent) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
        //chưa check null
        if (parent != null) {
            MaUser maUser = parent.getMaUser();
            if (maUser != null) {
                List<Device> deviceList = maUser.getDeviceList();
                //chỉ lấy những device đang đăng nhập
                deviceList = deviceList.stream().filter(a -> a.isLogin() && (a.getType().equals(DeviceTypeConstant.ANDROID) || a.getType().equals(DeviceTypeConstant.IOS))).collect(Collectors.toList());
                deviceList.forEach(y -> {
                    TokenFirebaseObject model = new TokenFirebaseObject();
                    model.setId(maUser.getId());
                    model.setFullName(maUser.getFullName());
                    model.setTokenFirebase(y.getTokenFirebase());
                    dataList.add(model);
                });
            }
        }
        return dataList;
    }

    private boolean getStatusFunction(InfoEmployeeSchool x, String function) {
        boolean status = false;
        switch (function) {
            case NotifyPesonConstant.MESSAGE:
                status = x.getEmployeeNotify().isMessage();
                break;
            case NotifyPesonConstant.MEDICINE:
                status = x.getEmployeeNotify().isMedicine();
                break;
            case NotifyPesonConstant.ABSENT:
                status = x.getEmployeeNotify().isAbsent();
                break;
            case NotifyPesonConstant.FEEDBACK:
                status = x.getEmployeeNotify().isFeedback();
                break;
            case NotifyPesonConstant.SYSTEM:
                status = x.getEmployeeNotify().isSys();
                break;
            case NotifyPesonConstant.DEFAULT:
                status = true;
                break;
        }
        return status;
    }
}
