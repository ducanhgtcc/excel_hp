package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.AppIconParent;
import com.example.onekids_project.entity.school.AppIconParentAdd;
import com.example.onekids_project.repository.AppIconParentAddRepository;
import com.example.onekids_project.repository.AppIconParentRepository;
import com.example.onekids_project.request.kids.AppIconParentRequest;
import com.example.onekids_project.response.school.AppIconResponse;
import com.example.onekids_project.response.school.ListAppIconResponse;
import com.example.onekids_project.service.servicecustom.AppIconParentAddSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AppIconParentAddSerivceImpl implements AppIconParentAddSerivce {

    @Autowired
    AppIconParentAddRepository appIconParentAddRepository;

    @Autowired
    AppIconParentRepository appIconParentRepository;

    @Override
    public ListAppIconResponse findAppIconParentAddCreate(Long idSchool) {
        AppIconParent appIconParent = appIconParentRepository.findAppIconParent(idSchool);
        ListAppIconResponse listAppIconResponse = new ListAppIconResponse();
        List<AppIconResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconParent.getMessageName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isMessage(), appIconParent.isMessageShow()));
        allList.add(this.getAppIcon(appIconParent.getMedicineName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isMedicine(), appIconParent.isMedicineShow()));
        allList.add(this.getAppIcon(appIconParent.getAbsentName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isAbsent(), appIconParent.isAbsentShow()));
        allList.add(this.getAppIcon(appIconParent.getAlbumName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isAlbum(), appIconParent.isAlbumShow()));
        allList.add(this.getAppIcon(appIconParent.getEvaluateName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isEvaluate(), appIconParent.isEvaluateShow()));
        allList.add(this.getAppIcon(appIconParent.getAttendanceName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isAttendance(), appIconParent.isAttendanceShow()));
        allList.add(this.getAppIcon(appIconParent.getStudentFeesName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isStudentFees(), appIconParent.isStudentFeesShow()));
        allList.add(this.getAppIcon(appIconParent.getLearnName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isLearn(), appIconParent.isLearnShow()));
        allList.add(this.getAppIcon(appIconParent.getMenuName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isMenu(), appIconParent.isMenuShow()));
        allList.add(this.getAppIcon(appIconParent.getVideoName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isVideo(), appIconParent.isVideoShow()));
        allList.add(this.getAppIcon(appIconParent.getCameraName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isCamera(), appIconParent.isCameraShow()));
        allList.add(this.getAppIcon(appIconParent.getKidsInfoName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isKidsInfo(), appIconParent.isKidsInfoShow()));
//        allList.add(this.getAppIcon(appIconParent.getUtilityName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isUtility(), appIconParent.isUtilityShow()));
        allList.add(this.getAppIcon(appIconParent.getFacebookName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isFacebook(), appIconParent.isFacebookShow()));
        allList.add(this.getAppIcon(appIconParent.getFeedbackName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isFeedback(), appIconParent.isFeedbackShow()));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconParent.isNews(), appIconParent.isNewsShow()));
        allList = allList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        listAppIconResponse.setAppIconResponseList(allList);
        if (allList.size() <= 10) {
            listAppIconResponse.setAppIconResponseList1(allList);
        } else {
            listAppIconResponse.setAppIconResponseList1(allList.subList(0, 10));
            listAppIconResponse.setAppIconResponseList2(allList.subList(10, allList.size()));
        }
        return listAppIconResponse;
    }

    @Override
    public ListAppIconResponse
    findAppIconParentAddUpdate(Long idSchool, Long idKids) {
        AppIconParent appIconParent = appIconParentRepository.findAppIconParent(idSchool);
        AppIconParentAdd appIconParentAdd = appIconParentAddRepository.findAppIconParentByIdKid(idSchool, idKids);
        ListAppIconResponse listAppIconResponse = new ListAppIconResponse();
        List<AppIconResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconParent.getMessageName(), appIconParentAdd.isMessage(), appIconParentAdd.isMessageShow(), appIconParent.isMessage(), appIconParent.isMessageShow()));
        allList.add(this.getAppIcon(appIconParent.getMedicineName(), appIconParentAdd.isMedicine(), appIconParentAdd.isMedicineShow(), appIconParent.isMedicine(), appIconParent.isMedicineShow()));
        allList.add(this.getAppIcon(appIconParent.getAbsentName(), appIconParentAdd.isAbsent(), appIconParentAdd.isAbsentShow(), appIconParent.isAbsent(), appIconParent.isAbsentShow()));
        allList.add(this.getAppIcon(appIconParent.getAlbumName(), appIconParentAdd.isAlbum(), appIconParentAdd.isAlbumShow(), appIconParent.isAlbum(), appIconParent.isAlbumShow()));
        allList.add(this.getAppIcon(appIconParent.getEvaluateName(), appIconParentAdd.isEvaluate(), appIconParentAdd.isEvaluateShow(), appIconParent.isEvaluate(), appIconParent.isEvaluateShow()));
        allList.add(this.getAppIcon(appIconParent.getAttendanceName(), appIconParentAdd.isAttendance(), appIconParentAdd.isAttendanceShow(), appIconParent.isAttendance(), appIconParent.isAttendanceShow()));
        allList.add(this.getAppIcon(appIconParent.getStudentFeesName(), appIconParentAdd.isStudentFees(), appIconParentAdd.isStudentFeesShow(), appIconParent.isStudentFees(), appIconParent.isStudentFeesShow()));
        allList.add(this.getAppIcon(appIconParent.getLearnName(), appIconParentAdd.isLearn(), appIconParentAdd.isLearnShow(), appIconParent.isLearn(), appIconParent.isLearnShow()));
        allList.add(this.getAppIcon(appIconParent.getMenuName(), appIconParentAdd.isMenu(), appIconParentAdd.isMenuShow(), appIconParent.isMenu(), appIconParent.isMenuShow()));
        allList.add(this.getAppIcon(appIconParent.getVideoName(), appIconParentAdd.isVideo(), appIconParentAdd.isVideoShow(), appIconParent.isVideo(), appIconParent.isVideoShow()));
        allList.add(this.getAppIcon(appIconParent.getCameraName(), appIconParentAdd.isCamera(), appIconParentAdd.isCameraShow(), appIconParent.isCamera(), appIconParent.isCameraShow()));
        allList.add(this.getAppIcon(appIconParent.getKidsInfoName(), appIconParentAdd.isKidsInfo(), appIconParentAdd.isKidsInfoShow(), appIconParent.isKidsInfo(), appIconParent.isKidsInfoShow()));
//        allList.add(this.getAppIcon(appIconParent.getUtilityName(), appIconParentAdd.isUtility(), appIconParentAdd.isUtilityShow(), appIconParent.isUtility(), appIconParent.isUtilityShow()));
        allList.add(this.getAppIcon(appIconParent.getFacebookName(), appIconParentAdd.isFacebook(), appIconParentAdd.isFacebookShow(), appIconParent.isFacebook(), appIconParent.isFacebookShow()));
        allList.add(this.getAppIcon(appIconParent.getFeedbackName(), appIconParentAdd.isFeedback(), appIconParentAdd.isFeedbackShow(), appIconParent.isFeedback(), appIconParent.isFeedbackShow()));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, appIconParentAdd.isNews(), appIconParentAdd.isNewsShow(), appIconParent.isNews(), appIconParent.isNewsShow()));
        allList = allList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (allList.size() <= 10) {
            listAppIconResponse.setAppIconResponseList1(allList);
        } else {
            listAppIconResponse.setAppIconResponseList1(allList.subList(0, 10));
            listAppIconResponse.setAppIconResponseList2(allList.subList(10, allList.size()));
        }
        return listAppIconResponse;
    }

    private AppIconResponse getAppIcon(String name, boolean status, boolean statusShow, boolean rootLockStatus, boolean rootShowStatus) {
        if (!rootShowStatus) {
            return null;
        }
        AppIconResponse icon = new AppIconResponse();
        icon.setIconName(name);
        icon.setStatus(status);
        icon.setStatusShow(statusShow);
        icon.setRootLockStatus(rootLockStatus);
        return icon;
    }

    @Override
    public boolean createAppIconParentAdd(Long idSchool, Kids kid, List<AppIconParentRequest> appIconParentRequestList) {
        AppIconParentAdd appIconParentAdd = new AppIconParentAdd();
        appIconParentAdd = convertIconParent(appIconParentAdd, appIconParentRequestList);

        appIconParentAdd.setKid(kid);
        appIconParentAdd.setIdSchool(idSchool);
        appIconParentAddRepository.save(appIconParentAdd);
        return true;
    }

    @Override
    public boolean updateAppIconParentAdd(Long idSchool, Kids kid, List<AppIconParentRequest> appIconParentRequestList) {
        AppIconParentAdd appIconParentAdd = appIconParentAddRepository.findAppIconParentByIdKid(idSchool, kid.getId());
        if (appIconParentAdd == null) {
            return false;
        }

        appIconParentAdd = convertIconParent(appIconParentAdd, appIconParentRequestList);
        appIconParentAddRepository.save(appIconParentAdd);
        return true;
    }

    /**
     * chuyển đổi kiểu đối tượng về kiểu mảng
     *
     * @param appIconParentAdd
     * @param appIconParentRequestList
     * @return
     */
    private AppIconParentAdd convertIconParent(AppIconParentAdd appIconParentAdd, List<AppIconParentRequest> appIconParentRequestList) {
        if (!CollectionUtils.isEmpty(appIconParentRequestList)) {
            appIconParentRequestList.forEach(x -> {
                System.out.println(x);
                if (AppIconName.MESSAGE.equals(x.getIconName())) {
                    appIconParentAdd.setMessage(x.isStatus());
                    appIconParentAdd.setMessageShow(x.isStatusShow());
                } else if (AppIconName.MEDICINE.equals(x.getIconName())) {
                    appIconParentAdd.setMedicine(x.isStatus());
                    appIconParentAdd.setMedicineShow(x.isStatusShow());
                } else if (AppIconName.ABSENT.equals(x.getIconName())) {
                    appIconParentAdd.setAbsent(x.isStatus());
                    appIconParentAdd.setAbsentShow(x.isStatusShow());
                } else if (AppIconName.ALBUM.equals(x.getIconName())) {
                    appIconParentAdd.setAlbum(x.isStatus());
                    appIconParentAdd.setAlbumShow(x.isStatusShow());
                } else if (AppIconName.EVALUATE.equals(x.getIconName())) {
                    appIconParentAdd.setEvaluate(x.isStatus());
                    appIconParentAdd.setEvaluateShow(x.isStatusShow());
                } else if (AppIconName.ATTENDANCE.equals(x.getIconName())) {
                    appIconParentAdd.setAttendance(x.isStatus());
                    appIconParentAdd.setAttendanceShow(x.isStatusShow());
                } else if (AppIconName.STUDENT_FEES.equals(x.getIconName())) {
                    appIconParentAdd.setStudentFees(x.isStatus());
                    appIconParentAdd.setStudentFeesShow(x.isStatusShow());
                } else if (AppIconName.LEARN.equals(x.getIconName())) {
                    appIconParentAdd.setLearn(x.isStatus());
                    appIconParentAdd.setLearnShow(x.isStatusShow());
                } else if (AppIconName.MENU.equals(x.getIconName())) {
                    appIconParentAdd.setMenu(x.isStatus());
                    appIconParentAdd.setMenuShow(x.isStatusShow());
                } else if (AppIconName.VIDEO.equals(x.getIconName())) {
                    appIconParentAdd.setVideo(x.isStatus());
                    appIconParentAdd.setVideoShow(x.isStatusShow());
                } else if (AppIconName.CAMERA.equals(x.getIconName())) {
                    appIconParentAdd.setCamera(x.isStatus());
                    appIconParentAdd.setCameraShow(x.isStatusShow());
                } else if (AppIconName.KIDS_INFO.equals(x.getIconName())) {
                    appIconParentAdd.setKidsInfo(x.isStatus());
                    appIconParentAdd.setKidsInfoShow(x.isStatusShow());
                } else if (AppIconName.UTILITY.equals(x.getIconName())) {
                    appIconParentAdd.setUtility(x.isStatus());
                    appIconParentAdd.setUtilityShow(x.isStatusShow());
                } else if (AppIconName.FACEBOOK.equals(x.getIconName())) {
                    appIconParentAdd.setFacebook(x.isStatus());
                    appIconParentAdd.setFacebookShow(x.isStatusShow());
                } else if (AppIconName.FEEDBACK.equals(x.getIconName())) {
                    appIconParentAdd.setFeedback(x.isStatus());
                    appIconParentAdd.setFeedbackShow(x.isStatusShow());
                } else if (AppIconName.NEWS_NAME.equals(x.getIconName())) {
                    appIconParentAdd.setNews(x.isStatus());
                    appIconParentAdd.setNewsShow(x.isStatusShow());
                }
            });
        }
        return appIconParentAdd;
    }

}
