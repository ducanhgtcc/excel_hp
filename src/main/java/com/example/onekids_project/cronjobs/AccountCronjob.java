package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.service.servicecustom.MaUserService;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.util.ConvertData;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-10-19 16:36
 *
 * @author lavanviet
 */
@PropertySource(value = "cronjob.properties")
@Component
public class AccountCronjob {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PortBean portBean;

    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private MaUserService maUserService;
    @Autowired
    private SysInforRepository sysInforRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private AppSendService appSendService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    /**
     * kiểm tra tài khoản nào ko có con thì thực hiện set startDateDelete=now
     * nếu từ ko có chuyển về có thì set startDateDelete=null
     */
    @Scheduled(cron = "${account.checkUser}")
    public void checkAccount() {
        logger.info("---start check account no exist children---");
        portBean.checkPortForCronjob();
        List<MaUser> maUserList = maUserRepository.findByDelActiveTrue();
        List<MaUser> saveList = new ArrayList<>();
        maUserList.forEach(x -> {
            if (x.getAppType().equals(AppTypeConstant.PARENT)) {
                long count = x.getParent().getKidsList().stream().filter(BaseEntity::isDelActive).count();
                MaUser maUser = setStartDateDeleteForUser(count, x);
                if (maUser != null) {
                    saveList.add(maUser);
                }
            } else if (x.getAppType().equals(AppTypeConstant.TEACHER) || x.getAppType().equals(AppTypeConstant.SCHOOL)) {
                long count = x.getEmployee().getInfoEmployeeSchoolList().stream().filter(BaseEntity::isDelActive).count();
                MaUser maUser = setStartDateDeleteForUser(count, x);
                if (maUser != null) {
                    saveList.add(maUser);
                }
            }
        });
        maUserRepository.saveAll(saveList);
        logger.info("---end check account no exist children---");
    }

    /**
     * chuyển tài khoản người dùng sang kiểu xóa hẳn
     */
    @Scheduled(cron = "${account.deleteCompleteUser}")
    public void deleteAccount() {
        logger.info("---start delete complete auto account no exist children---");
        portBean.checkPortForCronjob();
        LocalDate nowDate = LocalDate.now();
        List<School> schoolList = schoolRepository.findAllByDelActiveTrue();
        schoolList.forEach(x -> {
            boolean checkDelete = x.getSysConfig().isDeleteAccountStatus();
            if (checkDelete) {
                int dateNumber = x.getSysConfig().getDeleteAccountDate();
                List<MaUser> maUserList = maUserRepository.findByStartDateDeleteIsNotNullAndDelActiveTrue();
                maUserList.forEach(y -> {
                    long diffNumber = ChronoUnit.DAYS.between(y.getStartDateDelete(), nowDate);
                    if (diffNumber >= dateNumber) {
                        maUserService.deleteCompleteAccount(y.getId(), AccountTypeConstant.DELETE_AUTO);
                    }
                });
            }
        });
        logger.info("---end delete complete auto account no exist children---");
    }

    /**
     * thông báo cho các tài khoản sắp hết hạn dùng thử, dùng thật
     */
    @Scheduled(cron = "${account.expired}")
    public void sendNotifyForAccountTrial() {
        logger.info("---start notify trial account---");
        portBean.checkPortForCronjob();
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        if (sysInfor.isAccountTrialStatus()) {
            WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(76L).orElseThrow();
            String title = webSystemTitle.getTitle();
            List<MaUser> maUserList = maUserRepository.getUserExpired(sysInfor.getAccountTrailNumber());
            List<MaUser> plusUserList = maUserList.stream().filter(x -> x.getAppType().equals(AppTypeConstant.SCHOOL)).collect(Collectors.toList());
            List<MaUser> teacherUserList = maUserList.stream().filter(x -> x.getAppType().equals(AppTypeConstant.TEACHER)).collect(Collectors.toList());
            List<MaUser> parentUserList = maUserList.stream().filter(x -> x.getAppType().equals(AppTypeConstant.PARENT)).collect(Collectors.toList());
            List<Long> idSchooList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue().stream().map(BaseEntity::getId).collect(Collectors.toList());
            idSchooList.forEach(x -> {
                List<InfoEmployeeSchool> plusList = new ArrayList<>();
                List<InfoEmployeeSchool> teacherList = new ArrayList<>();
                List<Kids> kidsList = new ArrayList<>();
                plusUserList.forEach(a -> plusList.addAll(a.getEmployee().getInfoEmployeeSchoolList().stream().filter(b -> b.isDelActive() && b.isActivated() && b.getSchool().getId().equals(x)).collect(Collectors.toList())));
                teacherUserList.forEach(a -> teacherList.addAll(a.getEmployee().getInfoEmployeeSchoolList().stream().filter(b -> b.isDelActive() && b.isActivated() && b.getSchool().getId().equals(x)).collect(Collectors.toList())));
                parentUserList.forEach(a -> kidsList.addAll(a.getParent().getKidsList().stream().filter(b -> b.isDelActive() && b.isActivated() && b.getIdSchool().equals(x)).collect(Collectors.toList())));

                //plus
                plusList.forEach(y -> {
                    MaUser maUser = y.getEmployee().getMaUser();
                    String dateString = maUser.isTrialStatus() ? ConvertData.convertDateString(maUser.getToDemoDate()) : ConvertData.convertDateString(maUser.getToDate());
                    String content = webSystemTitle.getContent().replace("{app_type}", "plus").replace("{date}", dateString);
                    try {
                        firebaseFunctionService.sendPlusCommon(Collections.singletonList(y), title, content, x, FirebaseConstant.CATEGORY_NOTIFY);
                        appSendService.saveToAppSendEmployeeForAuto(x, y, title, content, AppSendConstant.TYPE_SYS);
                    } catch (FirebaseMessagingException e) {
                        logger.error("error send firebase in trial account");
                    }
                });

                //teacher
                teacherList.forEach(y -> {
                    MaUser maUser = y.getEmployee().getMaUser();
                    String dateString = maUser.isTrialStatus() ? ConvertData.convertDateString(maUser.getToDemoDate()) : ConvertData.convertDateString(maUser.getToDate());
                    String content = webSystemTitle.getContent().replace("{app_type}", "teacher").replace("{date}", dateString);
                    try {
                        firebaseFunctionService.sendTeacherCommon(Collections.singletonList(y), title, content, x, FirebaseConstant.CATEGORY_NOTIFY);
                        appSendService.saveToAppSendEmployeeForAuto(x, y, title, content, AppSendConstant.TYPE_SYS);
                    } catch (FirebaseMessagingException e) {
                        logger.error("error send firebase in trial account");
                    }
                });

                //kids
                kidsList.forEach(y -> {
                    MaUser maUser = y.getParent().getMaUser();
                    String dateString = maUser.isTrialStatus() ? ConvertData.convertDateString(maUser.getToDemoDate()) : ConvertData.convertDateString(maUser.getToDate());
                    String content = StringUtils.normalizeSpace(webSystemTitle.getContent().replace("{app_type}", "").replace("{date}", dateString));
                    try {
                        firebaseFunctionService.sendParentCommon(Collections.singletonList(y), title, content, x, FirebaseConstant.CATEGORY_NOTIFY);
                        appSendService.saveToAppSendParentForAuto(x, y, title, content, AppSendConstant.TYPE_SYS);
                    } catch (FirebaseMessagingException e) {
                        logger.error("error send firebase in trial account");
                    }
                });
            });
        }
        logger.info("---end notify trial account---");
    }

    /**
     * chuyển sang trạng thái active=false cho các tài khoản hết hạn
     */
    @Scheduled(cron = "${account.expired.handle}")
    public void handleForAccountTrial() {
        logger.info("---start handle trial account---");
        portBean.checkPortForCronjob();
        List<MaUser> maUserList = maUserRepository.getUserExpiredHandle();
        maUserList.forEach(x -> x.setActivated(AppConstant.APP_FALSE));
        maUserRepository.saveAll(maUserList);
        logger.info("account number for handle: {}", maUserList.size());
        logger.info("---end handle trial account---");
    }

    @Scheduled(cron = "${school.expired}")
    public void sendNotifyForSchoolTrial() {
        logger.info("---start notify trial school---");
        portBean.checkPortForCronjob();
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(77L).orElseThrow();
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        String title = webSystemTitle.getTitle();
        List<School> schoolList = schoolRepository.getSchoolExpired(sysInfor.getSchoolTrailNumber());
        schoolList.forEach(x -> {
            String dateString = x.isTrialStatus() ? ConvertData.convertDateString(x.getDemoEnd()) : ConvertData.convertDateString(x.getDateEnd());
            String content = webSystemTitle.getContent().replace("{date}", dateString);
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndAppTypeAndEmployeeStatusAndDelActiveTrue(x.getId(), AppTypeConstant.SCHOOL, EmployeeConstant.STATUS_WORKING);
            try {
                firebaseFunctionService.sendPlusCommon(infoEmployeeSchoolList, title, content, x.getId(), FirebaseConstant.CATEGORY_NOTIFY);
                infoEmployeeSchoolList.forEach(a -> appSendService.saveToAppSendEmployeeForAuto(x.getId(), a, title, content, AppSendConstant.TYPE_SYS));
            } catch (FirebaseMessagingException e) {
                logger.error("error send firebase in trial school");
            }
        });
        logger.info("---end notify trial school---");
    }

    @Scheduled(cron = "${school.expired.handle}")
    public void handleForSchoolTrial() {
        logger.info("---start handle trial school---");
        portBean.checkPortForCronjob();
        List<School> schoolList = schoolRepository.getSchoolExpiredHandle();
        schoolList.forEach(x -> x.setSchoolActive(AppConstant.APP_FALSE));
        schoolRepository.saveAll(schoolList);
        logger.info("school number for handle: {}", schoolList.size());
        logger.info("---end handle trial school---");
    }

    private static MaUser setStartDateDeleteForUser(long count, MaUser x) {
        if (count == 0) {
            if (x.getStartDateDelete() == null) {
                x.setStartDateDelete(LocalDate.now());
                return x;
            }
        } else {
            if (x.getStartDateDelete() != null) {
                x.setStartDateDelete(null);
                return x;
            }
        }
        return null;
    }
}
