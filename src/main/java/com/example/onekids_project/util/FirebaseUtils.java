package com.example.onekids_project.util;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.DeviceTypeConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.NotifyPesonConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.Device;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.request.NotifyRequest;
import com.example.onekids_project.firebase.response.FirebaseResponse;
import com.example.onekids_project.firebase.response.TokenFirebaseObject;
import com.example.onekids_project.firebase.servicecustom.FirebaseService;
import com.example.onekids_project.model.firebase.ContentFirebaseModel;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FirebaseUtils {

    private static WebSystemTitleRepository webSystemTitleRepository;

    private static FirebaseService firebaseService;

    private static InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    public FirebaseUtils(WebSystemTitleRepository webSystemTitleRepository, FirebaseService firebaseService, InfoEmployeeSchoolRepository infoEmployeeSchoolRepository) {
        this.webSystemTitleRepository = webSystemTitleRepository;
        this.firebaseService = firebaseService;
        this.infoEmployeeSchoolRepository = infoEmployeeSchoolRepository;
    }

    // gửi firre base tới one teacher
//    public static void sendFirebaseTeacher(List<InfoEmployeeSchool> infoEmployeeSchoolList, NotifyRequest notifyRequest, String router) throws FirebaseMessagingException {
//        List<TokenFirebaseObject> tokenFirebaseObjectListTeacher = firebaseService.getEmployeeTokenFirebases(infoEmployeeSchoolList);
//        if (CollectionUtils.isNotEmpty(tokenFirebaseObjectListTeacher)) {
//            FirebaseResponse firebaseResponse = firebaseService.sendMulticastAndHandleErrorsTeacher(tokenFirebaseObjectListTeacher, router, notifyRequest);
//        }
//    }


    // lấy token firebase của 1 phụ huynh
    public static List<TokenFirebaseObject> getParentOneTokenFirebases(Kids kids) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
        //chưa check null
        if (kids.getParent() != null) {
            MaUser maUser = kids.getParent().getMaUser();
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

    // lấy token firebase của nhiều phụ huynh
    public static List<TokenFirebaseObject> getParentTokenFirebases(List<Kids> kidsList) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
        if (CollectionUtils.isNotEmpty(kidsList)) {
            List<Parent> parentList = kidsList.stream().filter(y -> y.getParent() != null).map(z -> z.getParent()).collect(Collectors.toList());
            parentList = parentList.stream().filter(x -> x.getMaUser() != null).collect(Collectors.toList());
            parentList.forEach(x -> {
                MaUser maUser = x.getMaUser();
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
        }
        return dataList;
    }

    // lấy token firebase của 1 giáo viên
    public static List<TokenFirebaseObject> getEmployeeTokenFirebases(List<InfoEmployeeSchool> infoEmployeeSchoolList) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
//        List<InfoEmployeeSchool> infoEmployeeSchoolListNew = null;
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee() != null).collect(Collectors.toList());
        infoEmployeeSchoolList.forEach(x -> {
            Long id = x.getEmployee().getId();
            Long idM = x.getEmployee().getMaUser().getId();
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

    // lấy token firebase của nhiều plus
    public static List<TokenFirebaseObject> getPlusTokenFirebases(List<InfoEmployeeSchool> infoEmployeeSchoolList, String function) {
        List<TokenFirebaseObject> dataList = new ArrayList<>();
        //lọc ra những thông tin nhân viên đã có tài khoản và chức năng gửi thông báo được check là true
        List<InfoEmployeeSchool> infoEmployeeSchoolListNew = null;
        infoEmployeeSchoolListNew = infoEmployeeSchoolList.stream().filter(x -> x.getEmployee().getId() != null && getStatusFunction(x, function)).collect(Collectors.toList());
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

    // check phân quyền chức năng cho plus
    private static boolean getStatusFunction(InfoEmployeeSchool x, String function) {
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
        }
        return status;
    }

    //---------------------------new code------------------------------

    public static ContentFirebaseModel getWelSystemTitle(Long id) {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(id).orElseThrow(() -> new NoSuchElementException("not found websystemtitle by id=" + id));
        ContentFirebaseModel model = new ContentFirebaseModel();
        model.setTitle(webSystemTitle.getTitle());
        model.setBody(webSystemTitle.getContent());
        return model;
    }

    public static List<InfoEmployeeSchool> getTeacherList(Long idClass) {
        return infoEmployeeSchoolRepository.findByAppTypeAndExEmployeeClassListMaClassIdAndDelActiveTrue(AppTypeConstant.TEACHER, idClass);
    }

    public static List<InfoEmployeeSchool> getPlusList(Long idSchool) {
        return infoEmployeeSchoolRepository.findByAppTypeAndSchoolIdAndDelActiveTrue(AppTypeConstant.SCHOOL, idSchool);
    }

}
