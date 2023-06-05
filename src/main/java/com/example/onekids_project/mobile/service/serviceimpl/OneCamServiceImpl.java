package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.AttendanceArriveKids;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.AttendanceLeaveKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.onecam.OneCamNews;
import com.example.onekids_project.entity.onecam.OneCamSetting;
import com.example.onekids_project.entity.school.*;
import com.example.onekids_project.entity.system.News;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.plus.response.home.SchoolPlusResponse;
import com.example.onekids_project.mobile.response.NewsOneCamMobResponse;
import com.example.onekids_project.mobile.response.onecam.CamOneModel;
import com.example.onekids_project.mobile.response.onecam.ParentCamResponse;
import com.example.onekids_project.mobile.response.onecam.PlusCamResponse;
import com.example.onekids_project.mobile.response.onecam.TeacherCamResponse;
import com.example.onekids_project.mobile.service.OneCamService;
import com.example.onekids_project.mobile.service.servicecustom.DeviceCamService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lavanviet
 */
@Service
public class OneCamServiceImpl implements OneCamService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SysInforRepository sysInforRepository;
    @Autowired
    private OneCamNewsRepository oneCamNewsRepository;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NotifySchoolRepository notifySchoolRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private DeviceCamService deviceCamService;
    @Autowired
    private OneCamSettingRepository oneCamSettingRepository;

    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Override
    public List<ParentCamResponse> getCameraParentList(String idDevice) {
        deviceCamService.checkLogoutDeviceCame(idDevice);
        List<ParentCamResponse> responseList = new ArrayList<>();
        List<Kids> kidsList = StudentUtil.getKidList();
        kidsList.forEach(a -> {
            School school = schoolRepository.findById(a.getIdSchool()).orElseThrow();
            OneCamSetting oneCamSetting = a.getMaClass().getOneCamSetting();
            ParentCamResponse dataModel = new ParentCamResponse();
            dataModel.setId(a.getId());
            dataModel.setFullName(a.getFullName());
            dataModel.setAvatar(AvatarUtils.getAvatarKids(a));
            dataModel.setClassName(a.getMaClass().getClassName());
            dataModel.setLogo(this.getLogoSchool(school));
            dataModel.setViewLimitStatus(oneCamSetting.isViewLimitStatus());
            dataModel.setViewLimitNumber(oneCamSetting.getViewLimitNumber());
            dataModel.setViewLimitText(oneCamSetting.getViewLimitText());
            dataModel.setSchoolName(school.getSchoolName());
            String viewCam = this.checkViewOneCame(a, a.getMaClass().getId());
            List<Camera> cameraList = new ArrayList<>();
            if (StringUtils.isBlank(viewCam)) {
                cameraList = cameraRepository.findByIdSchoolAndDelActiveTrueAndMaClassList_Id(school.getId(), a.getMaClass().getId());
            } else {
                dataModel.setNoViewCameraStatus(true);
                dataModel.setNoViewCameraText(viewCam);
            }
            dataModel.setCamOneModelList(this.getCamOneList(cameraList));
            dataModel.setLinkList(this.getLinkList());
            responseList.add(dataModel);
        });
        return responseList;
    }

    @Override
    public List<TeacherCamResponse> getCameraTeacherList(String idDevice) {
        deviceCamService.checkLogoutDeviceCame(idDevice);
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        List<TeacherCamResponse> responseList = new ArrayList<>();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow();
        Employee employee = maUser.getEmployee();
        InfoEmployeeSchool infoEmployeeSchool = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.isActivated() && x.isDelActive()).collect(Collectors.toList()).get(0);
        String avatar = AvatarUtils.getAvatarInfoEmployee(infoEmployeeSchool);
        List<MaClass> maClassList = EmployeeUtil.getClassFromEmployee(employee);
        maClassList.forEach(x -> {
            School school = schoolRepository.findById(x.getIdSchool()).orElseThrow();
            TeacherCamResponse dataModel = new TeacherCamResponse();
            dataModel.setIdClass(x.getId());
            dataModel.setClassName(x.getClassName());
            dataModel.setSchoolName(school.getSchoolName());
            dataModel.setLogo(this.getLogoSchool(school));
            dataModel.setFullName(maUser.getFullName());
            dataModel.setAvatar(avatar);
            List<Camera> cameraList = cameraRepository.findByIdSchoolAndDelActiveTrueAndMaClassList_Id(school.getId(), x.getId());
            dataModel.setCamOneModelList(this.getCamOneList(cameraList));
            dataModel.setLinkList(this.getLinkList());
            responseList.add(dataModel);
        });
        return responseList;
    }

    @Override
    public List<PlusCamResponse> getCameraPlusList(String idDevice) {
        deviceCamService.checkLogoutDeviceCame(idDevice);
        List<PlusCamResponse> responseList = new ArrayList<>();
        List<SchoolPlusResponse> list = EmployeeUtil.getSchoolList(UserInforUtils.getEmployee(PrincipalUtils.getUserPrincipal().getId()));
        list.forEach(x -> {
            Long idSchool = x.getId();
            List<Camera> cameraList = cameraRepository.findAllByIdSchoolAndCamActiveTrueAndDelActiveTrue(idSchool);
            School school = schoolRepository.findById(idSchool).orElseThrow();
            PlusCamResponse dataModel = new PlusCamResponse();
            dataModel.setAvatar(x.getAvatar());
            dataModel.setFullName(x.getPlusName());
            dataModel.setSchoolName(x.getSchoolName());
            dataModel.setLogo(this.getLogoSchool(school));
            dataModel.setLinkList(this.getLinkList());
            dataModel.setCamOneModelList(this.getCamOneList(cameraList));
            responseList.add(dataModel);
        });
        return responseList;
    }

    @Override
    public void logoutCameraService(String idDevice) {
        deviceCamService.logoutCamService(idDevice);
    }

    private String getLogoSchool(School school) {
        String logo = school.getSchoolAvatar();
        if (StringUtils.isNotBlank(logo)) {
            return logo;
        }
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        return sysInfor.getOneCamePicture();
    }

    private List<NewsOneCamMobResponse> getLinkList() {
        Long idSchool = SchoolUtils.getIdSchool();
        List<NewsOneCamMobResponse> list = new ArrayList<>();
        OneCamNews oneCamNews = oneCamNewsRepository.findByIdSchool(idSchool).orElseThrow();
        List<News> newsList = newsRepository.findByDelActiveTrueOrderByIdDesc();
        if (oneCamNews.getOneCamNumber() > 0) {
            List<News> oneCamList = newsList.stream().filter(News::isAppOneCame).collect(Collectors.toList());
            for (int i = 0; i < oneCamNews.getOneCamNumber(); i++) {
                News a = oneCamList.get(i);
                NewsOneCamMobResponse model = new NewsOneCamMobResponse();
                model.setId(a.getId());
                model.setDate(a.getCreatedDate().toLocalDate());
                model.setLink(a.getLink());
                model.setPicture(a.getUrlAttachPicture());
                model.setTitle(a.getTitle());
                model.setType("onecam");
                list.add(model);
            }
        }
        if (oneCamNews.getOneKidsNumber() > 0) {
            List<News> oneCamList = newsList.stream().filter(News::isAppParent).collect(Collectors.toList());
            for (int i = 0; i < oneCamNews.getOneKidsNumber(); i++) {
                News a = oneCamList.get(i);
                NewsOneCamMobResponse model = new NewsOneCamMobResponse();
                model.setId(a.getId());
                model.setDate(a.getCreatedDate().toLocalDate());
                model.setLink(a.getLink());
                model.setPicture(a.getUrlAttachPicture());
                model.setTitle(a.getTitle());
                model.setType("onekids");
                list.add(model);
            }
        }
        if (oneCamNews.getSchoolNumber() > 0) {
            List<NotifySchool> notifySchoolList = notifySchoolRepository.findByIdSchoolAndActiveTrueAndDelActiveTrueOrderByIdDesc(idSchool);
            for (int i = 0; i < oneCamNews.getSchoolNumber(); i++) {
                NotifySchool a = notifySchoolList.get(i);
                NewsOneCamMobResponse model = new NewsOneCamMobResponse();
                model.setId(a.getId());
                model.setDate(a.getCreatedDate().toLocalDate());
                model.setLink(a.getLink());
                model.setTitle(a.getTitle());
                List<NotifySchoolAttachFile> pictureList = a.getNotifySchoolAttachFileList();
                if (CollectionUtils.isNotEmpty(pictureList)) {
                    model.setPicture(pictureList.get(0).getUrl());
                }
                model.setType("oneschool");
                list.add(model);
            }
        }
        if (oneCamNews.isExtendLinkStatus()) {
            NewsOneCamMobResponse model = new NewsOneCamMobResponse();
            model.setLink(oneCamNews.getExtendLink());
            model.setType("extendLink");
            list.add(model);
        } else {
            List<NotifySchool> notifySchoolList = notifySchoolRepository.findByIdSchoolAndActiveTrueAndDelActiveTrueOrderByIdDesc(idSchool);
            notifySchoolList.forEach(a -> {
                NewsOneCamMobResponse model = new NewsOneCamMobResponse();
                model.setId(a.getId());
                model.setDate(a.getCreatedDate().toLocalDate());
                model.setLink(a.getLink());
                model.setTitle(a.getTitle());
                model.setType("extendSchool");
                List<NotifySchoolAttachFile> pictureList = a.getNotifySchoolAttachFileList();
                if (CollectionUtils.isNotEmpty(pictureList)) {
                    model.setPicture(pictureList.get(0).getUrl());
                }
                list.add(model);
            });
        }
        return list;
    }

    private List<CamOneModel> getCamOneList(List<Camera> cameraList) {
        List<CamOneModel> list = new ArrayList<>();
        cameraList.stream().filter(x -> x.getDvrCamera().isDvrActive() && x.isCamActive()).forEach(x -> {
            DvrCamera dvrCamera = x.getDvrCamera();
            CamOneModel model = new CamOneModel();

            model.setDvrType(dvrCamera.getDvrType());
            model.setAdminDvrAcc(dvrCamera.getAdminDvrAcc());
            model.setAdminDvrPassword(dvrCamera.getAdminDvrPassword());
            model.setSchoolDomain(dvrCamera.getSchoolDomain());
            model.setCamPort(dvrCamera.getCamPort());
            model.setDeviceSN(dvrCamera.getDeviceSN());
            model.setPortDVR(dvrCamera.getPortDVR());

            model.setCamName(x.getCamName());
            model.setCamChanel(x.getCamChanelOneCam());
            model.setCamStream(x.getCamStreamOneCam());
            model.setLinkCam(x.getDvrCamera().getDvrType().equals(AppConstant.TYPE_OTHER) ? x.getLinkCam() : x.getDvrCamera().getLinkDvr() + x.getLinkCam());
            list.add(model);
        });
        return list;
    }


    private String checkViewOneCame(Kids kid, Long idClass) {
        Long idKid = kid.getId();
        OneCamSetting oneCamSetting = oneCamSettingRepository.findByMaClassId(idClass).orElseThrow();
        if (oneCamSetting.isTimeViewStatus()) {
            boolean checkRangeTime1 = this.checkRangeTime(oneCamSetting.getStartTime1(), oneCamSetting.getEndTime1());
            boolean checkRangeTime2 = this.checkRangeTime(oneCamSetting.getStartTime2(), oneCamSetting.getEndTime2());
            boolean checkRangeTime3 = this.checkRangeTime(oneCamSetting.getStartTime3(), oneCamSetting.getEndTime3());
            boolean checkRangeTime4 = this.checkRangeTime(oneCamSetting.getStartTime4(), oneCamSetting.getEndTime4());
            boolean checkRangeTime5 = this.checkRangeTime(oneCamSetting.getStartTime5(), oneCamSetting.getEndTime5());
            if (!checkRangeTime1 && !checkRangeTime2 && !checkRangeTime3 && !checkRangeTime4 && !checkRangeTime5) {
                return oneCamSetting.getTimeViewText();
            }
        }
        if (oneCamSetting.isArriveViewStatus() || oneCamSetting.isLeaveNoViewStatus()) {
            AttendanceKids attendanceKids = attendanceKidsRepository.findByDelActiveTrueAndAttendanceDateAndKids_Id(LocalDate.now(), idKid);
            if (Objects.isNull(attendanceKids)) {
                logger.warn("No attendance width idKid={}, date={}", idKid, LocalDate.now());
                return null;
            }
            AttendanceArriveKids arriveKids = attendanceKids.getAttendanceArriveKids();
            AttendanceLeaveKids leaveKids = attendanceKids.getAttendanceLeaveKids();
            if (oneCamSetting.isArriveViewStatus()) {
                if (!arriveKids.isMorning() && !arriveKids.isAfternoon() && !arriveKids.isEvening()) {
                    return oneCamSetting.getArriveViewText();
                }
            }
            if (oneCamSetting.isLeaveNoViewStatus()) {
                if (leaveKids.isStatusLeave()) {
                    return oneCamSetting.getLeaveNoViewText();
                }
            }
        }
        return null;
    }

    private boolean checkRangeTime(LocalTime start, LocalTime end) {
        if (Objects.nonNull(start) || Objects.nonNull(end)) {
            LocalTime nowTime = LocalTime.now();
            if (Objects.nonNull(start) && Objects.nonNull(end)) {
                return nowTime.isAfter(start) && nowTime.isBefore(end);
            } else if (Objects.nonNull(start)) {
                return nowTime.isAfter(start);
            } else {
                return nowTime.isBefore(end);
            }
        }
        return false;
    }
}
