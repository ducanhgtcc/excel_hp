package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.AppIconTeacher;
import com.example.onekids_project.entity.school.AppIconTeacherAdd;
import com.example.onekids_project.repository.AppIconTeacherAddRepository;
import com.example.onekids_project.repository.AppIconTeacherRepository;
import com.example.onekids_project.request.kids.AppIconTeacherRequest;
import com.example.onekids_project.response.school.AppIconTeacherResponse;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;
import com.example.onekids_project.service.servicecustom.AppIconTeacherAddSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AppIconTeacherAddSerivceImpl implements AppIconTeacherAddSerivce {

    @Autowired
    private AppIconTeacherAddRepository appIconTeacherAddRepository;

    @Autowired
    private AppIconTeacherRepository appIconTeacherRepository;

    @Override
    public ListAppIconTeacherResponse findAppIconTeacherAddCreate(Long idSchool) {
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findAppIconTeacher(idSchool);
        ListAppIconTeacherResponse response = new ListAppIconTeacherResponse();
        List<AppIconTeacherResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconTeacher.getMessageName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isMessage(), appIconTeacher.isMessageShow()));
        allList.add(this.getAppIcon(appIconTeacher.getMedicineName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isMedicine(), appIconTeacher.isMedicineShow()));
        allList.add(this.getAppIcon(appIconTeacher.getAbsentName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isAbsent(), appIconTeacher.isAbsentShow()));
        allList.add(this.getAppIcon(appIconTeacher.getHealthName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isHealth(), appIconTeacher.isHealthShow()));
        allList.add(this.getAppIcon(appIconTeacher.getAttendanceName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isAttendance(), appIconTeacher.isAttendanceShow()));
        allList.add(this.getAppIcon(appIconTeacher.getAlbumName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isAlbum(), appIconTeacher.isAlbumShow()));
        allList.add(this.getAppIcon(appIconTeacher.getEvaluateName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isEvaluate(), appIconTeacher.isEvaluateShow()));
        allList.add(this.getAppIcon(appIconTeacher.getStudentFeesName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isStudentFees(), appIconTeacher.isStudentFeesShow()));
        allList.add(this.getAppIcon(appIconTeacher.getVideoName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isVideo(), appIconTeacher.isVideoShow()));
        allList.add(this.getAppIcon(appIconTeacher.getLearnName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isLearn(), appIconTeacher.isLearnShow()));
        allList.add(this.getAppIcon(appIconTeacher.getMenuName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isMenu(), appIconTeacher.isMenuShow()));
        allList.add(this.getAppIcon(appIconTeacher.getBirthdayName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isBirthday(), appIconTeacher.isBirthdayShow()));
        allList.add(this.getAppIcon(appIconTeacher.getCameraName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isCamera(), appIconTeacher.isCameraShow()));
        allList.add(this.getAppIcon(appIconTeacher.getUtilityName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isUtility(), appIconTeacher.isUtilityShow()));
        allList.add(this.getAppIcon(appIconTeacher.getSalaryName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isSalary(), appIconTeacher.isSalaryShow()));
        allList.add(this.getAppIcon(appIconTeacher.getFeedbackName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isFeedback(), appIconTeacher.isFeedbackShow()));
        allList.add(this.getAppIcon(appIconTeacher.getFacebookName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isFacebook(), appIconTeacher.isFacebookShow()));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, AppConstant.APP_TRUE, AppConstant.APP_TRUE, appIconTeacher.isNews(), appIconTeacher.isNewsShow()));
        allList = allList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        response.setAppIconTeacherResponseList(allList);
        if (allList.size() <= 10) {
            response.setAppIconTeacherResponseList1(allList);
        } else {
            response.setAppIconTeacherResponseList1(allList.subList(0, 10));
            response.setAppIconTeacherResponseList2(allList.subList(10, allList.size()));
        }
        return response;
    }

    @Override
    public ListAppIconTeacherResponse findAppIconTeacherAddUpdate(Long idSchool, Long infoEmployeeSchool) {
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findAppIconTeacher(idSchool);
        AppIconTeacherAdd appIconTeacherAdd = appIconTeacherAddRepository.findAppIconTeacherByIdEmployee(idSchool, infoEmployeeSchool);
        ListAppIconTeacherResponse response = new ListAppIconTeacherResponse();
        List<AppIconTeacherResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconTeacher.getMessageName(), appIconTeacherAdd.isMessage(), appIconTeacherAdd.isMessageShow(), appIconTeacher.isMessage(), appIconTeacher.isMessageShow()));
        allList.add(this.getAppIcon(appIconTeacher.getMedicineName(), appIconTeacherAdd.isMedicine(), appIconTeacherAdd.isMedicineShow(), appIconTeacher.isMedicine(), appIconTeacher.isMedicineShow()));
        allList.add(this.getAppIcon(appIconTeacher.getAbsentName(), appIconTeacherAdd.isAbsent(), appIconTeacherAdd.isAbsentShow(), appIconTeacher.isAbsent(), appIconTeacher.isAbsentShow()));
        allList.add(this.getAppIcon(appIconTeacher.getHealthName(), appIconTeacherAdd.isHealth(), appIconTeacherAdd.isHealthShow(), appIconTeacher.isHealth(), appIconTeacher.isHealthShow()));
        allList.add(this.getAppIcon(appIconTeacher.getAttendanceName(), appIconTeacherAdd.isAttendance(), appIconTeacherAdd.isAttendanceShow(), appIconTeacher.isAttendance(), appIconTeacher.isAttendanceShow()));
        allList.add(this.getAppIcon(appIconTeacher.getAlbumName(), appIconTeacherAdd.isAlbum(), appIconTeacherAdd.isAlbumShow(), appIconTeacher.isAlbum(), appIconTeacher.isAlbumShow()));
        allList.add(this.getAppIcon(appIconTeacher.getEvaluateName(), appIconTeacherAdd.isEvaluate(), appIconTeacherAdd.isEvaluateShow(), appIconTeacher.isEvaluate(), appIconTeacher.isEvaluateShow()));
        allList.add(this.getAppIcon(appIconTeacher.getStudentFeesName(), appIconTeacherAdd.isStudentFees(), appIconTeacherAdd.isStudentFeesShow(), appIconTeacher.isStudentFees(), appIconTeacher.isStudentFeesShow()));
        allList.add(this.getAppIcon(appIconTeacher.getVideoName(), appIconTeacherAdd.isVideo(), appIconTeacherAdd.isVideoShow(), appIconTeacher.isVideo(), appIconTeacher.isVideoShow()));
        allList.add(this.getAppIcon(appIconTeacher.getLearnName(), appIconTeacherAdd.isLearn(), appIconTeacherAdd.isLearnShow(), appIconTeacher.isLearn(), appIconTeacher.isLearnShow()));
        allList.add(this.getAppIcon(appIconTeacher.getMenuName(), appIconTeacherAdd.isMenu(), appIconTeacherAdd.isMenuShow(), appIconTeacher.isMenu(), appIconTeacher.isMenuShow()));
        allList.add(this.getAppIcon(appIconTeacher.getBirthdayName(), appIconTeacherAdd.isBirthday(), appIconTeacherAdd.isBirthdayShow(), appIconTeacher.isBirthday(), appIconTeacher.isBirthdayShow()));
        allList.add(this.getAppIcon(appIconTeacher.getCameraName(), appIconTeacherAdd.isCamera(), appIconTeacherAdd.isCameraShow(), appIconTeacher.isCamera(), appIconTeacher.isCameraShow()));
        allList.add(this.getAppIcon(appIconTeacher.getUtilityName(), appIconTeacherAdd.isUtility(), appIconTeacherAdd.isUtilityShow(), appIconTeacher.isUtility(), appIconTeacher.isUtilityShow()));
        allList.add(this.getAppIcon(appIconTeacher.getSalaryName(), appIconTeacherAdd.isSalary(), appIconTeacherAdd.isSalaryShow(), appIconTeacher.isSalary(), appIconTeacher.isSalaryShow()));
        allList.add(this.getAppIcon(appIconTeacher.getFeedbackName(), appIconTeacherAdd.isFeedback(), appIconTeacherAdd.isFeedbackShow(), appIconTeacher.isFeedback(), appIconTeacher.isFeedbackShow()));
        allList.add(this.getAppIcon(appIconTeacher.getFacebookName(), appIconTeacherAdd.isFacebook(), appIconTeacherAdd.isFacebookShow(), appIconTeacher.isFacebook(), appIconTeacher.isFacebookShow()));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, appIconTeacherAdd.isNews(), appIconTeacherAdd.isNewsShow(), appIconTeacher.isNews(), appIconTeacher.isNewsShow()));
        allList = allList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (allList.size() <= 10) {
            response.setAppIconTeacherResponseList1(allList);
        } else {
            response.setAppIconTeacherResponseList1(allList.subList(0, 10));
            response.setAppIconTeacherResponseList2(allList.subList(10, allList.size()));
        }
        return response;
    }

    @Override
    public boolean createAppIconTeacherAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconTeacherRequest> appIconTeacherRequestList) {
        AppIconTeacherAdd appIconTeacherAdd = new AppIconTeacherAdd();
        appIconTeacherAdd = convertIconTeacher(appIconTeacherAdd, appIconTeacherRequestList);

        appIconTeacherAdd.setInfoEmployeeSchool(infoEmployeeSchool);
        appIconTeacherAdd.setIdSchool(idSchool);
        appIconTeacherAddRepository.save(appIconTeacherAdd);
        return true;
    }


    @Override
    public boolean updateAppIconTeacherAdd(Long idSchool, InfoEmployeeSchool infoEmployeeSchool, List<AppIconTeacherRequest> appIconTeacherRequestList) {
        AppIconTeacherAdd appIconTeacherAdd = appIconTeacherAddRepository.findByIdSchoolAndInfoEmployeeSchoolIdAndDelActiveTrue(idSchool, infoEmployeeSchool.getId()).orElseThrow(() -> new NotFoundException("not found appIconTeacher by idInfoEmployeeSchool"));
        appIconTeacherAdd = convertIconTeacher(appIconTeacherAdd, appIconTeacherRequestList);
        appIconTeacherAddRepository.save(appIconTeacherAdd);
        return true;
    }

    /**
     * chuyển đổi kiểu đối tượng về kiểu mảng
     *
     * @param appIconTeacherAdd
     * @param appIconTeacherRequestList
     * @return
     */
    private AppIconTeacherAdd convertIconTeacher(AppIconTeacherAdd appIconTeacherAdd, List<AppIconTeacherRequest> appIconTeacherRequestList) {
        if (!CollectionUtils.isEmpty(appIconTeacherRequestList)) {
            appIconTeacherRequestList.forEach(x -> {
                System.out.println(x);
                if (AppIconName.MESSAGE.equals(x.getIconName())) {
                    appIconTeacherAdd.setMessage(x.isStatus());
                    appIconTeacherAdd.setMessageShow(x.isStatusShow());
                } else if (AppIconName.MEDICINE.equals(x.getIconName())) {
                    appIconTeacherAdd.setMedicine(x.isStatus());
                    appIconTeacherAdd.setMedicineShow(x.isStatusShow());
                } else if (AppIconName.ABSENT.equals(x.getIconName())) {
                    appIconTeacherAdd.setAbsent(x.isStatus());
                    appIconTeacherAdd.setAbsentShow(x.isStatusShow());
                } else if (AppIconName.HEALTH.equals(x.getIconName())) {
                    appIconTeacherAdd.setHealth(x.isStatus());
                    appIconTeacherAdd.setHealthShow(x.isStatusShow());
                } else if (AppIconName.ATTENDANCE.equals(x.getIconName())) {
                    appIconTeacherAdd.setAttendance(x.isStatus());
                    appIconTeacherAdd.setAttendanceShow(x.isStatusShow());
                } else if (AppIconName.ALBUM.equals(x.getIconName())) {
                    appIconTeacherAdd.setAlbum(x.isStatus());
                    appIconTeacherAdd.setAlbumShow(x.isStatusShow());
                } else if (AppIconName.EVALUATE.equals(x.getIconName())) {
                    appIconTeacherAdd.setEvaluate(x.isStatus());
                    appIconTeacherAdd.setEvaluateShow(x.isStatusShow());
                } else if (AppIconName.STUDENT_FEES.equals(x.getIconName())) {
                    appIconTeacherAdd.setStudentFees(x.isStatus());
                    appIconTeacherAdd.setStudentFeesShow(x.isStatusShow());
                } else if (AppIconName.VIDEO.equals(x.getIconName())) {
                    appIconTeacherAdd.setVideo(x.isStatus());
                    appIconTeacherAdd.setVideoShow(x.isStatusShow());
                } else if (AppIconName.LEARN.equals(x.getIconName())) {
                    appIconTeacherAdd.setLearn(x.isStatus());
                    appIconTeacherAdd.setLearnShow(x.isStatusShow());
                } else if (AppIconName.MENU.equals(x.getIconName())) {
                    appIconTeacherAdd.setMenu(x.isStatus());
                    appIconTeacherAdd.setMenuShow(x.isStatusShow());
                } else if (AppIconName.BIRTHDAY.equals(x.getIconName())) {
                    appIconTeacherAdd.setBirthday(x.isStatus());
                    appIconTeacherAdd.setBirthdayShow(x.isStatusShow());
                } else if (AppIconName.SALARY.equals(x.getIconName())) {
                    appIconTeacherAdd.setSalary(x.isStatus());
                    appIconTeacherAdd.setSalaryShow(x.isStatusShow());
                } else if (AppIconName.CAMERA.equals(x.getIconName())) {
                    appIconTeacherAdd.setCamera(x.isStatus());
                    appIconTeacherAdd.setCameraShow(x.isStatusShow());
                } else if (AppIconName.UTILITY.equals(x.getIconName())) {
                    appIconTeacherAdd.setUtility(x.isStatus());
                    appIconTeacherAdd.setUtilityShow(x.isStatusShow());
                } else if (AppIconName.FACEBOOK.equals(x.getIconName())) {
                    appIconTeacherAdd.setFacebook(x.isStatus());
                    appIconTeacherAdd.setFacebookShow(x.isStatusShow());
                } else if (AppIconName.FEEDBACK.equals(x.getIconName())) {
                    appIconTeacherAdd.setFeedback(x.isStatus());
                    appIconTeacherAdd.setFeedbackShow(x.isStatusShow());
                } else if (AppIconName.NEWS_NAME.equals(x.getIconName())) {
                    appIconTeacherAdd.setNews(x.isStatus());
                    appIconTeacherAdd.setNewsShow(x.isStatusShow());
                }
            });
        }
        return appIconTeacherAdd;
    }

    private AppIconTeacherResponse getAppIcon(String name, boolean status, boolean statusShow, boolean rootLockStatus, boolean rootShowStatus) {
        if (!rootShowStatus) {
            return null;
        }
        AppIconTeacherResponse icon = new AppIconTeacherResponse();
        icon.setIconName(name);
        icon.setStatus(status);
        icon.setStatusShow(statusShow);
        icon.setRootLockStatus(rootLockStatus);
        return icon;
    }

}
