package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.AppIconPlus;
import com.example.onekids_project.entity.school.AppIconPlusAdd;
import com.example.onekids_project.repository.AppIconPlusAddRepository;
import com.example.onekids_project.repository.AppIconPlusRepository;
import com.example.onekids_project.request.kids.AppIconPlusRequest;
import com.example.onekids_project.response.school.AppIconPlusResponse;
import com.example.onekids_project.response.school.ListAppIconPlusResponse;
import com.example.onekids_project.service.servicecustom.AppIconPlusAddSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AppIconPlusAddSerivceImpl implements AppIconPlusAddSerivce {

    @Autowired
    private AppIconPlusAddRepository appIconPlusAddRepository;

    @Autowired
    private AppIconPlusRepository appIconPlusRepository;

    int splitNumber = 15;

    @Override
    public ListAppIconPlusResponse findAppIconPlusAddCreate(Long idSchool) {
        AppIconPlus appIconPlus = appIconPlusRepository.findAppIconPlus(idSchool);
        ListAppIconPlusResponse response = new ListAppIconPlusResponse();
        List<AppIconPlusResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconPlus.getEmployeeName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isEmployee(), appIconPlus.isEmployeeShow()));
        allList.add(this.getAppIcon(appIconPlus.getKidsName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isKids(), appIconPlus.isKidsShow()));
        allList.add(this.getAppIcon(appIconPlus.getMessageName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isMessage(), appIconPlus.isMessageShow()));
        allList.add(this.getAppIcon(appIconPlus.getMedicineName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isMedicine(), appIconPlus.isMedicineShow()));
        allList.add(this.getAppIcon(appIconPlus.getAbsentName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isAbsent(), appIconPlus.isAbsentShow()));
        allList.add(this.getAppIcon(appIconPlus.getAttendanceName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isAttendance(), appIconPlus.isAttendanceShow()));
        allList.add(this.getAppIcon(appIconPlus.getEvaluateName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isEvaluate(), appIconPlus.isEvaluateShow()));
        allList.add(this.getAppIcon(appIconPlus.getAlbumName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isAlbum(), appIconPlus.isAlbumShow()));
        allList.add(this.getAppIcon(appIconPlus.getHealthName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isHealth(), appIconPlus.isHealthShow()));
        allList.add(this.getAppIcon(appIconPlus.getLearnName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isLearn(), appIconPlus.isLearnShow()));
        allList.add(this.getAppIcon(appIconPlus.getMenuName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isMenu(), appIconPlus.isMenuShow()));
        allList.add(this.getAppIcon(appIconPlus.getVideoName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isVideo(), appIconPlus.isVideoShow()));
        allList.add(this.getAppIcon(appIconPlus.getCameraName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isCamera(), appIconPlus.isCameraShow()));
        allList.add(this.getAppIcon(appIconPlus.getBirthdayName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isBirthday(), appIconPlus.isBirthdayShow()));
        allList.add(this.getAppIcon(appIconPlus.getStudentFeesName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isStudentFees(), appIconPlus.isStudentFeesShow()));
        allList.add(this.getAppIcon(appIconPlus.getSalaryName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isSalary(), appIconPlus.isSalary()));
        allList.add(this.getAppIcon(appIconPlus.getFeedbackName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isFeedback(), appIconPlus.isFeedbackShow()));
        allList.add(this.getAppIcon(appIconPlus.getSmsAppHistoryName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isSmsAppHistory(), appIconPlus.isSmsAppHistoryShow()));
        allList.add(this.getAppIcon(appIconPlus.getNotifyName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isNotify(), appIconPlus.isNotifyShow()));
        allList.add(this.getAppIcon(appIconPlus.getFacebookName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isFacebook(), appIconPlus.isFacebookShow()));
        allList.add(this.getAppIcon(appIconPlus.getCashInternalName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isCashInternal(), appIconPlus.isCashInternalShow()));
        allList.add(this.getAppIcon(appIconPlus.getWalletName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isWallet(), appIconPlus.isWalletShow()));
//        allList.add(this.getAppIcon(appIconPlus.getNotifySchoolName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isNotifySchool(), appIconPlus.isNotifySchoolShow()));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconPlus.isNews(), appIconPlus.isNewsShow()));
        allList = allList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (allList.size() <= splitNumber) {
            response.setAppIconPlusResponseList1(allList);
        } else {
            response.setAppIconPlusResponseList1(allList.subList(0, splitNumber));
            response.setAppIconPlusResponseList2(allList.subList(splitNumber, allList.size()));
        }
        return response;
    }

    @Override
    public ListAppIconPlusResponse findAppIconPlusAddUpdate(Long idSchool, Long infoEmployeeSchool) {
        AppIconPlus appIconPlus = appIconPlusRepository.findAppIconPlus(idSchool);
        AppIconPlusAdd appIconPlusAdd = appIconPlusAddRepository.findAppIconPlusByIdEmployee(idSchool, infoEmployeeSchool);
        ListAppIconPlusResponse response = new ListAppIconPlusResponse();
        List<AppIconPlusResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconPlus.getEmployeeName(), appIconPlusAdd.isEmployee(), appIconPlusAdd.isEmployeeShow(), appIconPlus.isEmployee(), appIconPlus.isEmployeeShow()));
        allList.add(this.getAppIcon(appIconPlus.getKidsName(), appIconPlusAdd.isKids(), appIconPlusAdd.isKidsShow(), appIconPlus.isKids(), appIconPlus.isKidsShow()));
        allList.add(this.getAppIcon(appIconPlus.getMessageName(), appIconPlusAdd.isMessage(), appIconPlusAdd.isMessageShow(), appIconPlus.isMessage(), appIconPlus.isMessageShow()));
        allList.add(this.getAppIcon(appIconPlus.getMedicineName(), appIconPlusAdd.isMedicine(), appIconPlusAdd.isMedicineShow(), appIconPlus.isMedicine(), appIconPlus.isMedicineShow()));
        allList.add(this.getAppIcon(appIconPlus.getAbsentName(), appIconPlusAdd.isAbsent(), appIconPlusAdd.isAbsentShow(), appIconPlus.isAbsent(), appIconPlus.isAbsentShow()));
        allList.add(this.getAppIcon(appIconPlus.getAttendanceName(), appIconPlusAdd.isAttendance(), appIconPlusAdd.isAttendanceShow(), appIconPlus.isAttendance(), appIconPlus.isAttendanceShow()));
        allList.add(this.getAppIcon(appIconPlus.getEvaluateName(), appIconPlusAdd.isEvaluate(), appIconPlusAdd.isEvaluateShow(), appIconPlus.isEvaluate(), appIconPlus.isEvaluateShow()));
        allList.add(this.getAppIcon(appIconPlus.getAlbumName(), appIconPlusAdd.isAlbum(), appIconPlusAdd.isAlbumShow(), appIconPlus.isAlbum(), appIconPlus.isAlbumShow()));
        allList.add(this.getAppIcon(appIconPlus.getHealthName(), appIconPlusAdd.isHealth(), appIconPlusAdd.isHealthShow(), appIconPlus.isHealth(), appIconPlus.isHealthShow()));
        allList.add(this.getAppIcon(appIconPlus.getLearnName(), appIconPlusAdd.isLearn(), appIconPlusAdd.isLearnShow(), appIconPlus.isLearn(), appIconPlus.isLearnShow()));
        allList.add(this.getAppIcon(appIconPlus.getMenuName(), appIconPlusAdd.isMenu(), appIconPlusAdd.isMenuShow(), appIconPlus.isMenu(), appIconPlus.isMenuShow()));
        allList.add(this.getAppIcon(appIconPlus.getVideoName(), appIconPlusAdd.isVideo(), appIconPlusAdd.isVideoShow(), appIconPlus.isVideo(), appIconPlus.isVideoShow()));
        allList.add(this.getAppIcon(appIconPlus.getCameraName(), appIconPlusAdd.isCamera(), appIconPlusAdd.isCameraShow(), appIconPlus.isCamera(), appIconPlus.isCameraShow()));
        allList.add(this.getAppIcon(appIconPlus.getBirthdayName(), appIconPlusAdd.isBirthday(), appIconPlusAdd.isBirthdayShow(), appIconPlus.isBirthday(), appIconPlus.isBirthdayShow()));
        allList.add(this.getAppIcon(appIconPlus.getStudentFeesName(), appIconPlusAdd.isStudentFees(), appIconPlusAdd.isStudentFeesShow(), appIconPlus.isStudentFees(), appIconPlus.isStudentFeesShow()));
        allList.add(this.getAppIcon(appIconPlus.getSalaryName(), appIconPlusAdd.isSalary(), appIconPlusAdd.isSalaryShow(), appIconPlus.isSalary(), appIconPlus.isSalary()));
        allList.add(this.getAppIcon(appIconPlus.getFeedbackName(), appIconPlusAdd.isFeedback(), appIconPlusAdd.isFeedbackShow(), appIconPlus.isFeedback(), appIconPlus.isFeedbackShow()));
        allList.add(this.getAppIcon(appIconPlus.getSmsAppHistoryName(), appIconPlusAdd.isSmsAppHistory(), appIconPlusAdd.isSmsAppHistoryShow(), appIconPlus.isSmsAppHistory(), appIconPlus.isSmsAppHistoryShow()));
        allList.add(this.getAppIcon(appIconPlus.getNotifyName(), appIconPlusAdd.isNotify(), appIconPlusAdd.isNotifyShow(), appIconPlus.isNotify(), appIconPlus.isNotifyShow()));
        allList.add(this.getAppIcon(appIconPlus.getFacebookName(), appIconPlusAdd.isFacebook(), appIconPlusAdd.isFacebookShow(), appIconPlus.isFacebook(), appIconPlus.isFacebookShow()));
        allList.add(this.getAppIcon(appIconPlus.getCashInternalName(), appIconPlusAdd.isCashInternal(), appIconPlusAdd.isCashInternalShow(), appIconPlus.isCashInternal(), appIconPlus.isCashInternalShow()));
        allList.add(this.getAppIcon(appIconPlus.getWalletName(), appIconPlusAdd.isWallet(), appIconPlusAdd.isWalletShow(), appIconPlus.isWallet(), appIconPlus.isWalletShow()));
//        allList.add(this.getAppIcon(appIconPlus.getNotifySchoolName(), appIconPlusAdd.isNotifySchool(), appIconPlusAdd.isNotifySchoolShow(), appIconPlus.isNotifySchool(), appIconPlus.isNotifySchoolShow()));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, appIconPlusAdd.isNews(), appIconPlusAdd.isNewsShow(), appIconPlus.isNews(), appIconPlus.isNewsShow()));
        allList = allList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (allList.size() <= splitNumber) {
            response.setAppIconPlusResponseList1(allList);
        } else {
            response.setAppIconPlusResponseList1(allList.subList(0, splitNumber));
            response.setAppIconPlusResponseList2(allList.subList(splitNumber, allList.size()));
        }
        return response;
    }

    @Override
    public boolean createAppIconPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconPlusRequest> appIconPlusRequestList) {
        AppIconPlusAdd appIconPlusAdd = new AppIconPlusAdd();
        appIconPlusAdd = convertIconPlus(appIconPlusAdd, appIconPlusRequestList);
        appIconPlusAdd.setInfoEmployeeSchool(infoEmployeeSchool);
        appIconPlusAdd.setIdSchool(idSchool);
        appIconPlusAddRepository.save(appIconPlusAdd);
        return true;
    }


    @Override
    public boolean updateAppIconPlusAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconPlusRequest> appIconPlusRequestList) {
        AppIconPlusAdd appIconPlusAdd = appIconPlusAddRepository.findAppIconPlusByIdEmployee(idSchool, infoEmployeeSchool.getId());
        appIconPlusAdd = convertIconPlus(appIconPlusAdd, appIconPlusRequestList);
        appIconPlusAddRepository.save(appIconPlusAdd);
        return true;
    }

    /**
     * chuyển đổi kiểu đối tượng về kiểu mảng
     *
     * @param appIconPlusAdd
     * @param appIconPlusRequestList
     * @return
     */
    private AppIconPlusAdd convertIconPlus(AppIconPlusAdd appIconPlusAdd, List<AppIconPlusRequest> appIconPlusRequestList) {
        if (!CollectionUtils.isEmpty(appIconPlusRequestList)) {
            appIconPlusRequestList.forEach(x -> {
                System.out.println(x);
                if (AppIconName.EMPLOYEE.equals(x.getIconName())) {
                    appIconPlusAdd.setEmployee(x.isStatus());
                    appIconPlusAdd.setEmployeeShow(x.isStatusShow());
                }
                if (AppIconName.KIDS.equals(x.getIconName())) {
                    appIconPlusAdd.setKids(x.isStatus());
                    appIconPlusAdd.setKidsShow(x.isStatusShow());
                }
                if (AppIconName.MESSAGE.equals(x.getIconName())) {
                    appIconPlusAdd.setMessage(x.isStatus());
                    appIconPlusAdd.setMessageShow(x.isStatusShow());
                } else if (AppIconName.MEDICINE.equals(x.getIconName())) {
                    appIconPlusAdd.setMedicine(x.isStatus());
                    appIconPlusAdd.setMedicineShow(x.isStatusShow());
                } else if (AppIconName.ABSENT.equals(x.getIconName())) {
                    appIconPlusAdd.setAbsent(x.isStatus());
                    appIconPlusAdd.setAbsentShow(x.isStatusShow());
                } else if (AppIconName.ATTENDANCE.equals(x.getIconName())) {
                    appIconPlusAdd.setAttendance(x.isStatus());
                    appIconPlusAdd.setAttendanceShow(x.isStatusShow());
                } else if (AppIconName.EVALUATE.equals(x.getIconName())) {
                    appIconPlusAdd.setEvaluate(x.isStatus());
                    appIconPlusAdd.setEvaluateShow(x.isStatusShow());
                } else if (AppIconName.ALBUM.equals(x.getIconName())) {
                    appIconPlusAdd.setAlbum(x.isStatus());
                    appIconPlusAdd.setAlbumShow(x.isStatusShow());
                } else if (AppIconName.HEALTH.equals(x.getIconName())) {
                    appIconPlusAdd.setHealth(x.isStatus());
                    appIconPlusAdd.setHealthShow(x.isStatusShow());
                } else if (AppIconName.LEARN.equals(x.getIconName())) {
                    appIconPlusAdd.setLearn(x.isStatus());
                    appIconPlusAdd.setLearnShow(x.isStatusShow());
                } else if (AppIconName.MENU.equals(x.getIconName())) {
                    appIconPlusAdd.setMenu(x.isStatus());
                    appIconPlusAdd.setMenuShow(x.isStatusShow());
                } else if (AppIconName.VIDEO.equals(x.getIconName())) {
                    appIconPlusAdd.setVideo(x.isStatus());
                    appIconPlusAdd.setVideoShow(x.isStatusShow());
                } else if (AppIconName.CAMERA.equals(x.getIconName())) {
                    appIconPlusAdd.setCamera(x.isStatus());
                    appIconPlusAdd.setCameraShow(x.isStatusShow());
                } else if (AppIconName.BIRTHDAY.equals(x.getIconName())) {
                    appIconPlusAdd.setBirthday(x.isStatus());
                    appIconPlusAdd.setBirthdayShow(x.isStatusShow());
                } else if (AppIconName.STUDENT_FEES.equals(x.getIconName())) {
                    appIconPlusAdd.setStudentFees(x.isStatus());
                    appIconPlusAdd.setStudentFeesShow(x.isStatusShow());
                } else if (AppIconName.SALARY.equals(x.getIconName())) {
                    appIconPlusAdd.setSalary(x.isStatus());
                    appIconPlusAdd.setSalaryShow(x.isStatusShow());
                } else if (AppIconName.FEEDBACK.equals(x.getIconName())) {
                    appIconPlusAdd.setFeedback(x.isStatus());
                    appIconPlusAdd.setFeedbackShow(x.isStatusShow());
                } else if (AppIconName.SMS_APP_HISTORY_NAME.equals(x.getIconName())) {
                    appIconPlusAdd.setSmsAppHistory(x.isStatus());
                    appIconPlusAdd.setSmsAppHistoryShow(x.isStatusShow());
                } else if (AppIconName.NOTIFY_NAME.equals(x.getIconName())) {
                    appIconPlusAdd.setNotify(x.isStatus());
                    appIconPlusAdd.setNotifyShow(x.isStatusShow());
                } else if (AppIconName.UTILITY.equals(x.getIconName())) {
                    appIconPlusAdd.setUtility(x.isStatus());
                    appIconPlusAdd.setUtilityShow(x.isStatusShow());
                } else if (AppIconName.FACEBOOK.equals(x.getIconName())) {
                    appIconPlusAdd.setFacebook(x.isStatus());
                    appIconPlusAdd.setFacebookShow(x.isStatusShow());
                } else if (AppIconName.CASH_INTERNAL_NAME.equals(x.getIconName())) {
                    appIconPlusAdd.setCashInternal(x.isStatus());
                    appIconPlusAdd.setCashInternalShow(x.isStatusShow());
                } else if (AppIconName.WALLET_NAME.equals(x.getIconName())) {
                    appIconPlusAdd.setWallet(x.isStatus());
                    appIconPlusAdd.setWalletShow(x.isStatusShow());
                } else if (AppIconName.NOTIFY_SCHOOL_NAME.equals(x.getIconName())) {
                    appIconPlusAdd.setNotifySchool(x.isStatus());
                    appIconPlusAdd.setNotifySchoolShow(x.isStatusShow());
                } else if (AppIconName.NEWS_NAME.equals(x.getIconName())) {
                    appIconPlusAdd.setNews(x.isStatus());
                    appIconPlusAdd.setNewsShow(x.isStatusShow());
                }
            });
        }
        return appIconPlusAdd;
    }

    private AppIconPlusResponse getAppIcon(String name, boolean status, boolean statusShow, boolean rootLockStatus, boolean rootShowStatus) {
        if (!rootShowStatus) {
            return null;
        }
        AppIconPlusResponse icon = new AppIconPlusResponse();
        icon.setIconName(name);
        icon.setStatus(status);
        icon.setStatusShow(statusShow);
        icon.setRootLockStatus(rootLockStatus);
        return icon;
    }

}
