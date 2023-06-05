package com.example.onekids_project.cronjobs.dynamic;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.NotificationManage;
import com.example.onekids_project.entity.school.NotificationManageDate;
import com.example.onekids_project.entity.user.FeedBack;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * date 2021-08-10 10:04 AM
 *
 * @author nguyễn văn thụ
 */
@Slf4j
@Component
public class NotificationsManageCronjob implements SchedulingConfigurer, DisposableBean {

    @Autowired
    private NotificationManageRepository notificationManageRepository;
    @Autowired
    private NotificationManageDateRepository notificationManageDateRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;
    @Autowired
    private FeedBackRepository feedBackRepository;
    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private FnCashInternalSchoolRepository fnCashInternalSchoolRepository;
    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;
    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;

    @Autowired
    private PortBean portBean;

    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (portBean.checkPortReturn()) {
            log.info("--get cron notify in database---");
            taskRegistrar.setScheduler(this.taskExecutor());
            StringBuilder cronExpressions = this.getCronExpression();
            Map<String, String> cronExpressions1 = this.getCronMap();
            Set<String> longSet = cronExpressions1.keySet();
            //thực thi công việc
            for (String idString : longSet) {
                Long id = Long.valueOf(idString.split("-")[0]);
                Runnable runnable = () -> {
                    log.info("Task executed notify at ->" + new Date());
                    if (portBean.checkPortReturn()) {
                        this.getRunExpressions(id);
                    }
                };
                taskRegistrar.addTriggerTask(runnable, this.checkTrigger(cronExpressions, taskRegistrar, cronExpressions1.get(idString)));
            }
        }
    }

    //add trigger
    private Trigger checkTrigger(StringBuilder cronExpressions, ScheduledTaskRegistrar taskRegistrar, String cron) {
        return triggerContext -> {
            StringBuilder newCronExpression = this.getCronExpression();
            //check nếu cronjob đã thay đổi thì lấy gọi lại "get cron" để thay đổi lại lịch trình
            if (!StringUtils.equalsAnyIgnoreCase(newCronExpression, cronExpressions)) {
                log.info("lịch trình đã thay đổi");
                taskRegistrar.setTriggerTasksList(new ArrayList<>());
                taskRegistrar.setCronTasksList(new ArrayList<>());
                configureTasks(taskRegistrar); // calling recursively.
                taskRegistrar.destroy(); // destroys previously scheduled tasks.
                taskRegistrar.setScheduler(executor);
                taskRegistrar.afterPropertiesSet(); // this will schedule the task with new cron changes.
                return null; // return null when the cron changed so the trigger will stop.
            }
//            System.out.println("lịch trình chưa thay đổi");
            CronTrigger crontrigger = new CronTrigger(cron);
            return crontrigger.nextExecutionTime(triggerContext);
        };
    }

    @Override
    public void destroy() {
        System.out.println("destroy schedule");
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private Executor taskExecutor() {
        return Executors.newScheduledThreadPool(5);
    }

    /**
     * thực thi công việc
     */
    private void getRunExpressions(Long id) {
        NotificationManage notificationManage = notificationManageRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        if (notificationManage.isStatus()) {
            if (NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE.equals(notificationManage.getType())) {
                this.getNotifyAttendanceArrive(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE.equals(notificationManage.getType())) {
                this.getNotifyAttendanceLeave(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_ATTENDANCE_EAT.equals(notificationManage.getType())) {
                this.getNotifyAttendanceEat(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_MESSAGE.equals(notificationManage.getType())) {
                this.getNotifyMessageParent(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_MEDICINE.equals(notificationManage.getType())) {
                this.getNotifyMedicine(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_ABSENT.equals(notificationManage.getType())) {
                this.getNotifyAbsent(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_BIRTHDAY.equals(notificationManage.getType())) {
                this.getNotifyBirthday(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_FEES.equals(notificationManage.getType())) {
                this.getNotifyFeesParent(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_FEES.equals(notificationManage.getType()) && AppTypeConstant.SCHOOL.equals(notificationManage.getTypeReceive())) {
                this.getNotifyFeesPlus(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_SALARY.equals(notificationManage.getType()) && AppTypeConstant.SCHOOL.equals(notificationManage.getTypeReceive())) {
                this.getNotifySalaryPlus(notificationManage);
            }
            if (NotificationConstant.NOTIFICATION_CASH_INTERNAL.equals(notificationManage.getType()) && AppTypeConstant.SCHOOL.equals(notificationManage.getTypeReceive())) {
                this.getNotifyCashInternal(notificationManage);
            }
        }
        this.getNotifyHome(id);
    }

    /**
     * thông báo điểm danh đến cho phụ huynh và giáo viên
     */
    private void getNotifyAttendanceArrive(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_ATTENDANCE_ARRIVE);
        //gui firebase hoc sinh
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.PARENT.equals(notificationManage.getTypeReceive())) {
            for (Kids x : kidsList) {
                String content = notificationManage.getContent();
                content = content.replace(FirebaseConstant.REPLACE_KID_NAME_DOW, x.getFullName());
                try {
                    NotificationUtils.sendFirebaseParent(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                } catch (FirebaseMessagingException e) {
                    log.error("Error Send Firebase attendance arrive for parent");
                    e.printStackTrace();
                }
            }
            log.info("Gửi firebase học sinh chưa điểm danh đến cho phụ huynh thành công");
        }
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).count();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase attendance arrive for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase học sinh chưa điểm danh đến cho giáo viên thành công");
        }
    }

    /**
     * Thông báo điểm danh về cho phụ huynh và giáo viên
     */
    private void getNotifyAttendanceLeave(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_ATTENDANCE_LEAVE);
        //gui firebase hoc sinh
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.PARENT.equals(notificationManage.getTypeReceive())) {
            for (Kids x : kidsList) {
                String content = notificationManage.getContent();
                content = content.replace(FirebaseConstant.REPLACE_KID_NAME_DOW, x.getFullName());
                try {
                    NotificationUtils.sendFirebaseParent(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                } catch (FirebaseMessagingException e) {
                    log.error("Error Send Firebase attendance leave for parent");
                    e.printStackTrace();
                }
            }
            log.info("Gửi firebase học sinh chưa điểm danh về cho phụ huynh thành công");
        }
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).count();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase attendance leave for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase học sinh chưa điểm danh về cho giáo viên thành công");
        }
    }

    /**
     * Thông báo điểm danh ăn cho giáo viên
     */
    private void getNotifyAttendanceEat(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_ATTENDANCE_EAT);
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).count();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase attendance eat for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase học sinh chưa điểm danh ăn cho giáo viên thành công");
        }
    }

    /**
     * Thông báo lời nhắn cho giáo viên
     */
    private void getNotifyMessageParent(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_MESSAGE);
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).mapToLong(b -> b.getMessageParentList().stream().filter(c -> !c.isConfirmStatus()).count()).sum();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase message for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase lời nhắn chưa xác nhận về cho giáo viên thành công");
        }
    }

    /**
     * Thông báo dặn thuốc cho giáo viên
     */
    private void getNotifyMedicine(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_MEDICINE);
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).mapToLong(b -> b.getMedicineList().stream().filter(c -> !c.isConfirmStatus()).count()).sum();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase medicine for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase dặn thuốc chưa xác nhận về cho giáo viên thành công");
        }
    }

    /**
     * Thông báo xin nghỉ cho giáo viên
     */
    private void getNotifyAbsent(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_ABSENT);
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).mapToLong(b -> b.getAbsentLetterList().stream().filter(c -> !c.isConfirmStatus()).count()).sum();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase absent for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase xin nghỉ chưa xác nhận về cho giáo viên thành công");
        }
    }

    /**
     * Thông báo sinh nhật cho giáo viên
     */
    private void getNotifyBirthday(NotificationManage notificationManage) {
        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_BIRTHDAY);
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.TEACHER.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = NotificationUtils.getInfoEmployeeList(kidsList);
            //chỉ thông báo đến các giáo viên có lớp đang chọn
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                Long idClass = x.getEmployee().getIdClassLogin();
                if (idClass != 0) {
                    long count = kidsList.stream().filter(a -> a.getMaClass().getId().equals(idClass)).filter(b -> b.getBirthDay().getMonth().equals(LocalDate.now().getMonth()) && b.getBirthDay().getDayOfMonth() == LocalDate.now().getDayOfMonth()).count();
                    String className = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow().getClassName();
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count)).replace(FirebaseConstant.REPLACE_CLASS_NAME, className);
                    if (count > 0) {
                        try {
                            NotificationUtils.sendFirebaseTeacher(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase absent for teacher");
                            e.printStackTrace();
                        }
                    }
                }
            }
            log.info("Gửi firebase học sinh có sinh nhận hôm nay về cho giáo viên thành công");
        }
    }

    /**
     * thông báo hóa đơn chưa hoàn thành cho phụ huynh
     */
    private void getNotifyFeesParent(NotificationManage notificationManage) {
        LocalDate date = LocalDate.now();
//        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_FEES);
        List<Kids> kidsList = fnOrderKidsRepository.findByYearAndMonthAndKidsIdSchoolAndDelActiveTrue(date.getYear(), date.getMonthValue(), notificationManage.getIdSchool()).stream().map(FnOrderKids::getKids).collect(Collectors.toList());
        //gui firebase hoc sinh
        if (AppTypeConstant.PARENT.equals(notificationManage.getTypeReceive())) {
            for (Kids x : kidsList) {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), date.getMonth().getValue(), date.getYear());
                OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
                if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getEnoughNumber() < orderNumberModel.getTotalNumber()) {
                    String content = notificationManage.getContent();
                    content = content.replace(FirebaseConstant.REPLACE_KID_NAME_DOW, x.getFullName()).replace(FirebaseConstant.REPLACE_MONTH, (date.getMonth().getValue() + "-" + date.getYear()));
                    try {
                        NotificationUtils.sendFirebaseParent(x, notificationManage.getTitle(), content, notificationManage.getIdSchool(), "");
                    } catch (FirebaseMessagingException e) {
                        log.error("Error Send Firebase fees for parent");
                        e.printStackTrace();
                    }
                }
            }
            log.info("Gửi firebase học sinh chưa hoàn thành học phí cho phụ huynh thành công");
        }
    }

    /**
     * Thông báo lời nhắn, dặn thuốc, ... mới
     */
    private void getNotifyHome(Long id) {
        LocalDateTime date = LocalDateTime.now();
        NotificationManage notificationManage = notificationManageRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        NotificationManage notificationManage1 = notificationManageRepository.findByIdAndDelActiveTrue(id + 1).orElseThrow();
        NotificationManage notificationManage2 = notificationManageRepository.findByIdAndDelActiveTrue(id + 2).orElseThrow();
        NotificationManage notificationManage3 = notificationManageRepository.findByIdAndDelActiveTrue(id + 3).orElseThrow();
        NotificationManage notificationManage4 = notificationManageRepository.findByIdAndDelActiveTrue(id + 4).orElseThrow();
        NotificationManage notificationManage5 = notificationManageRepository.findByIdAndDelActiveTrue(id + 5).orElseThrow();
        if (NotificationConstant.NOTIFICATION_HOME.equals(notificationManage.getType()) && (notificationManage.isStatus() || notificationManage1.isStatus() || notificationManage2.isStatus() || notificationManage3.isStatus() || notificationManage4.isStatus() || notificationManage5.isStatus())) {
            List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_HOME);
            //gui firebase hoc sinh
            if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.PARENT.equals(notificationManage.getTypeReceive())) {
                for (Kids x : kidsList) {
                    StringBuilder str = new StringBuilder();
                    if (notificationManage.isStatus()) {
                        long count = x.getMessageParentList().stream().filter(c -> !c.isParentMessageDel() && !c.isConfirmStatus() && c.isParentUnread() && date.getDayOfMonth() == c.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == c.getCreatedDate().getMonth().getValue() && date.getYear() == c.getCreatedDate().getYear()).count();
                        if (count > 0) {
                            String content = notificationManage.getContent();
                            content = content.replace(FirebaseConstant.REPLACE_KID_NAME_DOW, x.getFullName()).replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count));
                            str.append(content).append("\n");
                        }
                    }
                    if (notificationManage1.isStatus()) {
                        long count = x.getMedicineList().stream().filter(c -> !c.isParentMedicineDel() && !c.isConfirmStatus() && c.isParentUnread() && date.getDayOfMonth() == c.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == c.getCreatedDate().getMonth().getValue() && date.getYear() == c.getCreatedDate().getYear()).count();
                        if (count > 0) {
                            String content = notificationManage1.getContent();
                            content = content.replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count));
                            str.append(content).append("\n");
                        }
                    }
                    if (notificationManage2.isStatus()) {
                        long count = x.getAbsentLetterList().stream().filter(c -> !c.isParentAbsentDel() && !c.isConfirmStatus() && c.isParentUnread() && date.getDayOfMonth() == c.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == c.getCreatedDate().getMonth().getValue() && date.getYear() == c.getCreatedDate().getYear()).count();
                        if (count > 0) {
                            String content = notificationManage2.getContent();
                            content = content.replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count));
                            str.append(content).append("\n");
                        }
                    }
                    if (notificationManage3.isStatus()) {
                        int count = NotificationUtils.getAlbumNewNumber(x.getId());
                        if (count > 0) {
                            String content = notificationManage3.getContent();
                            content = content.replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count));
                            str.append(content).append("\n");
                        }
                    }
                    if (notificationManage4.isStatus()) {
                        long countDay = CollectionUtils.isNotEmpty(x.getEvaluateDateList()) ? x.getEvaluateDateList().stream().filter(a -> a.isDelActive() && a.isApproved() && !a.isParentRead() && (StringUtils.isNotBlank(a.getLearnContent()) || StringUtils.isNotBlank(a.getEatContent()) || StringUtils.isNotBlank(a.getSleepContent()) || StringUtils.isNotBlank(a.getSanitaryContent()) || StringUtils.isNotBlank(a.getHealtContent()) || StringUtils.isNotBlank(a.getCommonContent()) || !CollectionUtils.isEmpty(a.getEvaluateAttachFileList())) && date.getDayOfMonth() == a.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == a.getCreatedDate().getMonth().getValue() && date.getYear() == a.getCreatedDate().getYear()).count() : 0;
                        long countWeek = CollectionUtils.isNotEmpty(x.getEvaluateWeekList()) ? x.getEvaluateWeekList().stream().filter(a -> a.isDelActive() && a.isApproved() && !a.isParentRead() && (StringUtils.isNotBlank(a.getContent()) || !CollectionUtils.isEmpty(a.getEvaluateWeekFileList())) && date.getDayOfMonth() == a.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == a.getCreatedDate().getMonth().getValue() && date.getYear() == a.getCreatedDate().getYear()).count() : 0;
                        long countMonth = CollectionUtils.isNotEmpty(x.getEvaluateMonthList()) ? x.getEvaluateMonthList().stream().filter(a -> a.isDelActive() && a.isApproved() && !a.isParentRead() && (StringUtils.isNotBlank(a.getContent()) || !CollectionUtils.isEmpty(a.getEvaluateMonthFileList())) && date.getDayOfMonth() == a.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == a.getCreatedDate().getMonth().getValue() && date.getYear() == a.getCreatedDate().getYear()).count() : 0;
                        long countPeriodic = CollectionUtils.isNotEmpty(x.getEvaluatePeriodicList()) ? x.getEvaluatePeriodicList().stream().filter(a -> a.isDelActive() && a.isApproved() && !a.isParentRead() && (StringUtils.isNotBlank(a.getContent()) || !CollectionUtils.isEmpty(a.getEvaluatePeriodicFileList())) && date.getDayOfMonth() == a.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == a.getCreatedDate().getMonth().getValue() && date.getYear() == a.getCreatedDate().getYear()).count() : 0;
                        long count = countDay + countWeek + countMonth + countPeriodic;
                        if (count > 0) {
                            String content = notificationManage4.getContent();
                            content = content.replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count));
                            str.append(content).append("\n");
                        }
                    }
                    if (notificationManage5.isStatus()) {
                        List<FeedBack> feedBackList = feedBackRepository.findByIdKidAndIdSchoolAndSchoolConfirmStatusFalseAndDelActiveTrue(x.getId(), x.getIdSchool());
                        long count = feedBackList.stream().filter(a -> a.isParentUnread() && date.getDayOfMonth() == a.getCreatedDate().getDayOfMonth() && date.getMonth().getValue() == a.getCreatedDate().getMonth().getValue() && date.getYear() == a.getCreatedDate().getYear()).count();
                        if (count > 0) {
                            String content = notificationManage5.getContent();
                            content = content.replace(FirebaseConstant.REPLACE_NUMBER, String.valueOf(count));
                            str.append(content).append("\n");
                        }
                    }
                    if (!str.toString().equals("")) {
                        String content = "Học sinh {kid_name} có:\n";
                        content = content.replace(FirebaseConstant.REPLACE_KID_NAME_DOW, x.getFullName());
                        String title = "Danh sách thông báo mới";
                        try {
                            NotificationUtils.sendFirebaseParent(x, title, content + str.toString(), notificationManage.getIdSchool(), "");
                        } catch (FirebaseMessagingException e) {
                            log.error("Error Send Firebase home for parent");
                            e.printStackTrace();
                        }
                    }
                }
                log.info("Thông báo màn Home cho phụ huynh thành công");
            }
        }
    }

    /**
     * Thông báo học phí cho plus
     */
    private void getNotifyFeesPlus(NotificationManage notificationManage) {

        long moneyTotal;
        long moneyPaid;
        long numberSuccess = 0;
        long numberNoSuccess = 0;
        List<FnKidsPackage> fnKidsPackageAllList = new ArrayList<>();
//        List<Kids> kidsList = NotificationUtils.getKidsList(notificationManage.getIdSchool(), NotificationConstant.NOTIFICATION_FEES);
        LocalDate date = LocalDate.now();
        if (notificationManage.getMonth() == -1) {
            date = date.minusMonths(1);
        } else if (notificationManage.getMonth() == 1) {
            date = date.plusMonths(1);
        }
        LocalDate finalDate = date;
        List<Kids> kidsList = fnOrderKidsRepository.findByYearAndMonthAndKidsIdSchoolAndDelActiveTrue(finalDate.getYear(), finalDate.getMonthValue(), notificationManage.getIdSchool()).stream().map(FnOrderKids::getKids).collect(Collectors.toList());
        for (Kids kids : kidsList) {
            List<FnKidsPackage> fnKidsPackageList = kids.getFnKidsPackageList().stream().filter(a -> a.isApproved() && a.getYear() == finalDate.getYear() && a.getMonth() == finalDate.getMonthValue()).collect(Collectors.toList());
            fnKidsPackageAllList.addAll(fnKidsPackageList);
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
            if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getEnoughNumber() == orderNumberModel.getTotalNumber()) {
                numberSuccess++;
            }
            if (orderNumberModel.getEnoughNumber() < orderNumberModel.getTotalNumber()) {
                numberNoSuccess++;
            }
        }
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageAllList);
        moneyTotal = (long) (orderMoneyModel.getMoneyTotalIn() - orderMoneyModel.getMoneyTotalOut());
        moneyPaid = (long) (orderMoneyModel.getMoneyTotalPaidIn() - orderMoneyModel.getMoneyTotalPaidOut());
        long moneyNoPaid = moneyTotal - moneyPaid;
        int percent = notificationManage.getNotificationManageDateList().stream().filter(a -> a.getPercent() != null && a.getDay() != null && a.getDay() == finalDate.getDayOfMonth()).collect(Collectors.toList()).get(0).getPercent();
        if (this.checkFireBase(moneyTotal, moneyPaid, percent)) {
            //fire base nhân sự
            if (CollectionUtils.isNotEmpty(kidsList) && AppTypeConstant.SCHOOL.equals(notificationManage.getTypeReceive())) {
                List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndDelActiveTrue(notificationManage.getIdSchool(), AppConstant.EMPLOYEE_STATUS_WORKING, AppTypeConstant.SCHOOL);
                String title = notificationManage.getTitle();
                for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                    String strDate = ConvertData.formartDateDash(finalDate);
                    String strMonth = finalDate.getMonthValue() + "-" + finalDate.getYear();
                    title = title.replace(FirebaseConstant.REPLACE_MONTH, strMonth);
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_DATE, strDate)
                            .replace(FirebaseConstant.REPLACE_MONEY_TOTAL, FinanceUltils.formatMoney(moneyTotal))
                            .replace(FirebaseConstant.REPLACE_MONEY_PAID, FinanceUltils.formatMoney(moneyPaid))
                            .replace(FirebaseConstant.REPLACE_MONEY_NO_PAID, FinanceUltils.formatMoney(moneyNoPaid))
                            .replace(FirebaseConstant.REPLACE_NUMBER_SUCCESS, String.valueOf(numberSuccess))
                            .replace(FirebaseConstant.REPLACE_NUMBER_NO_SUCCESS, String.valueOf(numberNoSuccess));
                    try {
                        NotificationUtils.sendFirebasePlus(x, title, content, notificationManage.getIdSchool(), "");
                    } catch (FirebaseMessagingException e) {
                        log.error("Error Send Firebase fees for plus");
                        e.printStackTrace();
                    }
                }
                log.info("Gửi thông báo học phí thành công");
            }
        }
    }

    /**
     * Thông báo công lương cho plus
     */
    private void getNotifySalaryPlus(NotificationManage notificationManage) {
//        LocalDate date = LocalDate.now();
        long moneyTotal;
        long moneyPaid;
        long numberSuccess = 0;
        long numberNoSuccess = 0;
        List<FnEmployeeSalary> fnEmployeeSalaryAllList = new ArrayList<>();
        LocalDate date = LocalDate.now();
        if (notificationManage.getMonth() == -1) {
            date = date.minusMonths(1);
        } else if (notificationManage.getMonth() == 1) {
            date = date.plusMonths(1);
        }
        LocalDate finalDate = date;
//        List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolId(notificationManage.getIdSchool());
        List<InfoEmployeeSchool> infoEmployeeSchoolList = fnOrderEmployeeRepository.findByYearAndMonthAndInfoEmployeeSchoolSchoolIdAndDelActiveTrue(finalDate.getYear(), finalDate.getMonthValue(), notificationManage.getIdSchool()).stream().map(FnOrderEmployee::getInfoEmployeeSchool).collect(Collectors.toList());
        for (InfoEmployeeSchool infoEmployeeSchool : infoEmployeeSchoolList) {
            List<FnEmployeeSalary> fnEmployeeSalaryList = infoEmployeeSchool.getFnEmployeeSalaryList().stream().filter(a -> a.isApproved() && a.getYear() == finalDate.getYear() && a.getMonth() == finalDate.getMonthValue()).collect(Collectors.toList());
            fnEmployeeSalaryAllList.addAll(fnEmployeeSalaryList);
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderEmployeeModel(fnEmployeeSalaryList);
            if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getEnoughNumber() == orderNumberModel.getTotalNumber()) {
                numberSuccess++;
            }
            if (orderNumberModel.getEnoughNumber() < orderNumberModel.getTotalNumber()) {
                numberNoSuccess++;
            }
        }
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyEmployeeModel(fnEmployeeSalaryAllList);
        moneyTotal = (long) (orderMoneyModel.getMoneyTotalOut() - orderMoneyModel.getMoneyTotalIn());
        moneyPaid = (long) (orderMoneyModel.getMoneyTotalPaidOut() - orderMoneyModel.getMoneyTotalPaidIn());
        long moneyNoPaid = moneyTotal - moneyPaid;
        int percent = notificationManage.getNotificationManageDateList().stream().filter(a -> a.getPercent() != null && a.getDay() != null && a.getDay() == finalDate.getDayOfMonth()).collect(Collectors.toList()).get(0).getPercent();
        if (this.checkFireBase(moneyTotal, moneyPaid, percent)) {
            //fire base nhân sự
            if (AppTypeConstant.SCHOOL.equals(notificationManage.getTypeReceive())) {
                String title = notificationManage.getTitle();
                for (InfoEmployeeSchool x : infoEmployeeSchoolList.stream().filter(a -> AppTypeConstant.SCHOOL.equals(a.getAppType()) && AppConstant.EMPLOYEE_STATUS_WORKING.equals(a.getEmployeeStatus())).collect(Collectors.toList())) {
                    String strDate = ConvertData.formartDateDash(finalDate);
                    String strMonth = finalDate.getMonthValue() + "-" + finalDate.getYear();
                    title = title.replace(FirebaseConstant.REPLACE_MONTH, strMonth);
                    String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_DATE, strDate)
                            .replace(FirebaseConstant.REPLACE_MONEY_TOTAL, FinanceUltils.formatMoney(moneyTotal))
                            .replace(FirebaseConstant.REPLACE_MONEY_PAID, FinanceUltils.formatMoney(moneyPaid))
                            .replace(FirebaseConstant.REPLACE_MONEY_NO_PAID, FinanceUltils.formatMoney(moneyNoPaid))
                            .replace(FirebaseConstant.REPLACE_NUMBER_SUCCESS, String.valueOf(numberSuccess))
                            .replace(FirebaseConstant.REPLACE_NUMBER_NO_SUCCESS, String.valueOf(numberNoSuccess));
                    try {
                        NotificationUtils.sendFirebasePlus(x, title, content, notificationManage.getIdSchool(), "");
                    } catch (FirebaseMessagingException e) {
                        log.error("Error Send Firebase Salary for plus");
                        e.printStackTrace();
                    }
                }
            }
            log.info("Gửi thông báo công lương thành công");
        }
    }

    /**
     * Thông báo thu chi nội bộ
     */
    private void getNotifyCashInternal(NotificationManage notificationManage) {
        LocalDate date = LocalDate.now();
        List<FnCashInternal> fnCashInternalList = fnCashInternalSchoolRepository.getInternalPayTotalNotify(notificationManage.getIdSchool(), date);
        double moneyIn = fnCashInternalList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(FnCashInternal::getMoney).sum();
        double moneyOut = fnCashInternalList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(FnCashInternal::getMoney).sum();
        //fire base nhân sự
        if (CollectionUtils.isNotEmpty(fnCashInternalList) && AppTypeConstant.SCHOOL.equals(notificationManage.getTypeReceive())) {
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.findBySchoolIdAndEmployeeStatusAndAppTypeAndDelActiveTrue(notificationManage.getIdSchool(), AppConstant.EMPLOYEE_STATUS_WORKING, AppTypeConstant.SCHOOL);
            String title = notificationManage.getTitle();
            for (InfoEmployeeSchool x : infoEmployeeSchoolList) {
                String strDate = ConvertData.formartDateDash(date);
                String strMonth = date.getMonthValue() + "-" + date.getYear();
                title = title.replace(FirebaseConstant.REPLACE_MONTH, strMonth);
                String content = notificationManage.getContent().replace(FirebaseConstant.REPLACE_DATE, strDate)
                        .replace(FirebaseConstant.REPLACE_MONEY_IN, FinanceUltils.formatMoney((long) moneyIn))
                        .replace(FirebaseConstant.REPLACE_MONEY_OUT, FinanceUltils.formatMoney((long) moneyOut));
                try {
                    NotificationUtils.sendFirebasePlus(x, title, content, notificationManage.getIdSchool(), "");
                } catch (FirebaseMessagingException e) {
                    log.error("Error Send Firebase CashInternal for plus");
                    e.printStackTrace();
                }
            }
            log.info("Gửi thông báo Thu chi nội bộ thành công");

        }
    }

    private boolean checkFireBase(long moneyTotal, long moneyPaid, int percent) {
        float moneyPercent = Math.abs((float) moneyPaid / moneyTotal) * 100;
        return moneyTotal > 0 && moneyPercent < percent;
    }

    private Map<String, String> getCronMap() {
        Map<String, String> longStringMap = new HashMap<>();
        List<NotificationManageDate> dateList = notificationManageDateRepository.findAllByNotificationManageStatusTrueAndNotificationManageDelActiveTrueAndStatusTrueAndDelActiveTrue();
        AtomicInteger i = new AtomicInteger(0);
        dateList.forEach(x -> {
            i.getAndIncrement();
            StringBuilder cronExpression = this.getCronOne(x);
            longStringMap.put(x.getNotificationManage().getId() + "-" + i, cronExpression.toString());
        });
        return longStringMap;
    }

    private StringBuilder getCronExpression() {
        StringBuilder cronExpression = new StringBuilder();
        List<NotificationManageDate> dateList = notificationManageDateRepository.findAllByNotificationManageStatusTrueAndNotificationManageDelActiveTrueAndStatusTrueAndDelActiveTrue();
        dateList.forEach(x -> {
            cronExpression.append(this.getCronOne(x)).append("|");
        });
        return cronExpression;
    }

    private StringBuilder getCronOne(NotificationManageDate x) {
        StringBuilder cronExpression = new StringBuilder();
        cronExpression.append("0").append(" ").append(x.getMinute() != null ? x.getMinute().toString() : "*").append(" ")
                .append(x.getHour() != null ? x.getHour().toString() : "*").append(" ")
                .append(x.getDay() != null ? x.getDay().toString() : "*").append(" ")
                .append(x.getMonth() != null ? x.getMonth().toString() : "*").append(" ?");
        return cronExpression;
    }

}
