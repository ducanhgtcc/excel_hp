package com.example.onekids_project.firebase.serviceimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.DeviceTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.ConfigNotifyParent;
import com.example.onekids_project.entity.school.ConfigNotifyPlus;
import com.example.onekids_project.entity.school.ConfigNotifyTeacher;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.model.TokenErrorsModel;
import com.example.onekids_project.firebase.model.TokenModel;
import com.example.onekids_project.firebase.servicecustom.FirebaseDataService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.model.firebase.ContentFirebaseModel;
import com.example.onekids_project.model.firebase.FirebaseModel;
import com.example.onekids_project.model.firebase.TokenFirebaseModel;
import com.example.onekids_project.repository.DeviceRepository;
import com.example.onekids_project.repository.FirebaseHistoryRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.util.ConvertData;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * date 2021-03-31 14:24
 *
 * @author lavanviet
 */
@Service
public class FirebaseDataServiceImpl implements FirebaseDataService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private FirebaseHistoryRepository firebaseHistoryRepository;
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public void sendFirebaseKids(List<Kids> dataList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException {
        List<TokenModel> tokenList = this.getTokenKidsList(dataList);
        String title = model.getTitle();
        String body = this.getTimeSendString(model.getBody());
        this.sendTokenList(tokenList, title, body, category, idSchool, AppTypeConstant.PARENT);
    }

    @Override
    public void sendFirebaseKidsNew(List<Long> idList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException {
        List<TokenModel> tokenList = this.getTokenKidsListNew(idList);
        String title = model.getTitle();
        String body = this.getTimeSendString(model.getBody());
        this.sendTokenList(tokenList, title, body, category, idSchool, AppTypeConstant.PARENT);
    }

    @Override
    public void sendFirebaseTeacherSameClass(List<InfoEmployeeSchool> dataList, ContentFirebaseModel model, String category, Long idSchool, Long idClass) throws FirebaseMessagingException {
        List<TokenModel> tokenList = this.getTokenInfoEmployeeListSameId(dataList, idClass);
        String title = model.getTitle();
        String body = this.getTimeSendString(model.getBody());
        this.sendTokenList(tokenList, title, body, category, idSchool, AppTypeConstant.TEACHER);
    }

    @Override
    public void sendFirebaseTeacher(List<InfoEmployeeSchool> dataList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException {
        List<TokenModel> tokenList = this.getTokenTeacherList(dataList);
        String title = model.getTitle();
        String body = this.getTimeSendString(model.getBody());
        this.sendTokenList(tokenList, title, body, category, idSchool, AppTypeConstant.TEACHER);
    }

    @Override
    public void sendFirebasePlusSameSchool(List<InfoEmployeeSchool> dataList, ContentFirebaseModel model, String category, Long idSchool) throws FirebaseMessagingException {
        List<TokenModel> tokenList = this.getTokenInfoEmployeeListSameId(dataList, idSchool);
        String title = model.getTitle();
        String body = this.getTimeSendString(model.getBody());
        this.sendTokenList(tokenList, title, body, category, idSchool, AppTypeConstant.SCHOOL);
    }


    /**
     * @param dataList: danh sách học sinh
     * @return
     */
    private List<TokenModel> getTokenKidsList(List<Kids> dataList) {
        List<TokenModel> tokenList = new ArrayList<>();
        dataList = dataList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
        dataList.forEach(x -> {
            MaUser maUser = x.getParent().getMaUser();
            this.setTokenList(x.getId(), maUser, tokenList);
        });
        return tokenList;
    }

    private List<TokenModel> getTokenKidsListNew(List<Long> dataList) {
        List<TokenModel> tokenList = new ArrayList<>();
        Collection<TokenFirebaseModel> tokenFirebaseList = deviceRepository.getTokeFirebase(dataList, TokenFirebaseModel.class);
        tokenFirebaseList.forEach(x -> {
            TokenModel model = new TokenModel();
            model.setId(x.getId());
            model.setTokenList(Collections.singletonList(x.getToken()));
            tokenList.add(model);
        });
        return tokenList;
    }

    /**
     * @param dataList: danh sách nhân sự
     * @param id:       teacher->idClass, plus->idSchool
     * @return
     */
    private List<TokenModel> getTokenInfoEmployeeListSameId(List<InfoEmployeeSchool> dataList, Long id) {
        List<TokenModel> tokenList = new ArrayList<>();
        List<Employee> employeeList = dataList.stream().map(InfoEmployeeSchool::getEmployee).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        employeeList.forEach(x -> this.setTokenList(id, x.getMaUser(), tokenList));
        return tokenList;
    }

    private List<TokenModel> getTokenTeacherList(List<InfoEmployeeSchool> dataList) {
        List<TokenModel> tokenList = new ArrayList<>();
        List<Employee> employeeList = dataList.stream().map(InfoEmployeeSchool::getEmployee).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        employeeList.forEach(x -> this.setTokenList(x.getIdClassLogin(), x.getMaUser(), tokenList));
        return tokenList;
    }

    /**
     * @param id:       kids->idKid, teacher->idClass, plus->idSchool
     * @param maUser
     * @param tokenList
     */
    private void setTokenList(Long id, MaUser maUser, List<TokenModel> tokenList) {
        List<Device> deviceList = maUser.getDeviceList().stream().filter(a -> a.isLogin() && (a.getType().equals(DeviceTypeConstant.ANDROID) || a.getType().equals(DeviceTypeConstant.IOS))).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(deviceList)) {
            TokenModel model = new TokenModel();
            model.setId(id);
            model.setTokenList(deviceList.stream().map(Device::getTokenFirebase).collect(Collectors.toList()));
            tokenList.add(model);
        }
    }


    /**
     * @param tokenList    danh sách token gửi
     * @param title        tiêu đề gửi
     * @param body         nội dung gửi
     * @param typeReceiver kiểu người nhận: parent, teacher, plus: AppTypeConstant
     * @param category     loại gửi:  FirebaseConstant-catogory
     * @throws FirebaseMessagingException
     */
    private void sendTokenList(List<TokenModel> tokenList, String title, String body, String category, Long idSchool, String typeReceiver) throws FirebaseMessagingException {
        this.checkBeforeSendFirebase(title, body, typeReceiver);
        FirebaseModel firebaseModel = this.getFirebaseModel(typeReceiver, category, idSchool);
        if (firebaseModel != null && CollectionUtils.isNotEmpty(tokenList)) {
            List<TokenErrorsModel> tokenErrorsList = new ArrayList<>();
            for (TokenModel x : tokenList) {
                Notification notify = Notification.builder().setTitle(title).setBody(body).build();
                Map<String, String> mapData = new HashMap<>();
                mapData.put(FirebaseConstant.ACTION_KEY, FirebaseConstant.ACTION_VALUE);
                mapData.put(FirebaseConstant.ROUTER_KEY, firebaseModel.getRouter());
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    mapData.put(FirebaseConstant.ID_KID, x.getId().toString());
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    mapData.put(FirebaseConstant.ID_CLASS, x.getId().toString());
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    mapData.put(FirebaseConstant.ID_SCHOOL, x.getId().toString());
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_APP_TYPE);
                }
                Aps aps = Aps.builder().setSound(FirebaseConstant.DEFAULT).build();
                ApnsConfig apnsConfig = ApnsConfig.builder().setAps(aps).build();
                MulticastMessage message = MulticastMessage.builder()
                        .setApnsConfig(apnsConfig)
                        .setNotification(notify)
                        .putAllData(mapData)
                        .addAllTokens(x.getTokenList())
                        .setAndroidConfig(AndroidConfig.builder().setTtl(firebaseModel.getExpiredAndroid()).setCollapseKey(body).build())
                        .setApnsConfig(ApnsConfig.builder().putHeader(FirebaseConstant.APNS_EXPIRATION, String.valueOf(firebaseModel.getExpiredIos())).setAps(Aps.builder().build()).build())
                        .build();
                BatchResponse response = FirebaseMessaging.getInstance(FirebaseApp.getInstance(typeReceiver)).sendMulticast(message);
//                if (response.getFailureCount() > 0) {
//                    List<SendResponse> sendResponseList = response.getResponses();
//                    for (int i = 0; i < sendResponseList.size(); i++) {
//                        if (!sendResponseList.get(i).isSuccessful()) {
//                            TokenErrorsModel errorsModel = new TokenErrorsModel();
//                            errorsModel.setMessageId(sendResponseList.get(i).getMessageId());
//                            errorsModel.setTokenFirebase(x.getTokenList().get(i));
//                            errorsModel.setAppType(typeReceiver);
//                            errorsModel.setRouter(firebaseModel.getRouter());
//                            errorsModel.setTitle(title);
//                            errorsModel.setBody(body);
//                            tokenErrorsList.add(errorsModel);
//                        }
//                    }
//                }
//                this.saveErrorsToken(tokenErrorsList);
            }
        }
    }


    /**
     * lưu các token lỗi
     *
     * @param tokenErrorsModelList
     */
    private void saveErrorsToken(List<TokenErrorsModel> tokenErrorsModelList) {
//        if (CollectionUtils.isNotEmpty(tokenErrorsModelList)) {
//            List<FirebaseHistory> firebaseHistoryList = listMapper.mapList(tokenErrorsModelList, FirebaseHistory.class);
//            firebaseHistoryRepository.saveAll(firebaseHistoryList);
//        }
    }

    /**
     * kiểm tra thông tin gửi
     *
     * @param title
     * @param body
     * @param typeReceiver
     */
    private void checkBeforeSendFirebase(String title, String body, String typeReceiver) {
        if (StringUtils.isBlank(title)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_EMPTY_TITILE);
        }
        if (StringUtils.isBlank(body)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_EMPTY_BODY);
        }
        if (!AppTypeConstant.PARENT.equals(typeReceiver) && !AppTypeConstant.TEACHER.equals(typeReceiver) && !AppTypeConstant.SCHOOL.equals(typeReceiver)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.APPTYPE_INVALID);
        }
    }

    private String getTimeSendString(String body) {
        return ConvertData.getDateAndTime().concat(". ").concat(body);
    }

    private FirebaseModel getFirebaseModel(String typeReceiver, String category, Long idSchool) {
        FirebaseModel model = new FirebaseModel();
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActiveTrue(idSchool);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        School school = schoolOptional.get();
        ConfigNotifyParent configParent = school.getConfigNotifyParent();
        ConfigNotifyTeacher configTeacher = school.getConfigNotifyTeacher();
        ConfigNotifyPlus configPlus = school.getConfigNotifyPlus();
        switch (category) {
            case FirebaseConstant.CATEGORY_NOTIFY:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isNotify()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_NOTIFY).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isNotify()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_NOTIFY).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isNotify()) {
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_NOTIFY_PLUS);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_MESSAGE:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isMessage()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_MESSAGE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_MESSAGE_COMMON);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isMessage()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_MESSAGE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_MESSAGE_COMMON);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isMessage()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_MESSAGE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_MESSAGE_COMMON);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_MEDICINE:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isMedicine()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_MEDICINE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_MEDICINE_COMMON);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isMedicine()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_MEDICINE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_MEDICINE_COMMON);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isMedicine()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_MEDICINE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_MEDICINE_COMMON);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ABSENT:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isAbsent()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_ABSENT).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_ABSENT_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isAbsent()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_ABSENT).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_ABSENT_TEACHER_PLUS);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isAbsent()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_ABSENT).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_ABSENT_TEACHER_PLUS);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ALBUM:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isAlbum()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_ALBUM).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_ALBUM_COMMON);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isAlbumApproved()) {
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_ALBUM_COMMON);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_EVALUATE:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isEvaluate()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_EVALUATE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_EVALUATE_PARENT_PLUS);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isEvaluate()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_EVALUATE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_EVALUATE_TEACHER);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isEvaluate()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_EVALUATE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_EVALUATE_PARENT_PLUS);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ATTENDANCE:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MINI_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MINI_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isAttendance()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_ATTENDANCE).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_ATTENDANCE);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_FEEDBACK:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isFeedback()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_FEEDBACK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_FEEDBACK_PARENT_TEACHER);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isFeedback()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_FEEDBACK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_FEEDBACK_PARENT_TEACHER);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isFeedback()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_FEEDBACK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_FEEDBACK_PLUS);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_BIRTHDAY:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isBirthday()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_BIRTHDAY).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isBirthday()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_BIRTHDAY).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);//todo
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_PHONEBOOK:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isPhonebook()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isPhonebook()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_WALLET:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isWallet()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ORDER_SHOW:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isOrderShow()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isOrderShow()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ORDER_PAYMENT:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isOrderPayment()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isOrderPayment()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ORDER_NOTIFY:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    if (!configParent.isOrderNotify()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    if (!configTeacher.isOrderNotify()) {
                        logger.info(ErrorsConstant.FIREBASE_CATEGORY_INACTIVE.replace("{category}", FirebaseConstant.CATEGORY_PHONEBOOK).replace("{apptype}", typeReceiver));
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_ABSENT_TEACHER:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    model.setRouter(FirebaseConstant.ROUTER_FEES_TEACHER);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    model.setRouter(FirebaseConstant.ROUTER_FEES_TEACHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            case FirebaseConstant.CATEGORY_NEWS:
                boolean check = AppTypeConstant.SCHOOL.equals(typeReceiver) && configPlus.isNews() || AppTypeConstant.TEACHER.equals(typeReceiver) && configTeacher.isNews() || AppTypeConstant.PARENT.equals(typeReceiver) && configParent.isNews();
                if (!check) {
                    return null;
                }
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                model.setRouter(FirebaseConstant.ROUTER_NEWS);
                break;
            case FirebaseConstant.CATEGORY_CASH_INTERNAL:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isCashInternal()) {
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_CASH_INTERNAL);
                }
                break;
            case FirebaseConstant.CATEGORY_NOTIFY_HISTORY:
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    if (!configPlus.isNotify()) {
                        return null;
                    }
                    model.setRouter(FirebaseConstant.ROUTER_NOTIFY_HISTORY_PLUS);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
                break;
            default: {
                model.setExpiredAndroid(FirebaseConstant.TIME_EXPIRE_MEDIUM_ANDROID);
                model.setExpiredIos(FirebaseConstant.TIME_EXPIRE_MEDIUM_IOS);
                if (AppTypeConstant.PARENT.equals(typeReceiver)) {
                    model.setRouter(FirebaseConstant.ROUTER_HOME_PARENT);
                } else if (AppTypeConstant.TEACHER.equals(typeReceiver)) {
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else if (AppTypeConstant.SCHOOL.equals(typeReceiver)) {
                    model.setRouter(FirebaseConstant.ROUTER_HOME_TEACHER);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.FIREBASE_ROUTER_EMPTY);
                }
            }
        }
        return model;
    }
}
