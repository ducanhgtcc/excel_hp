package com.example.onekids_project.cronjobs.dynamic;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.CelebrateSample;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.repository.CelebrateSampleRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Component
public class SchedulerConfig implements SchedulingConfigurer, DisposableBean {

    @Autowired
    private CelebrateSampleRepository celebrateSampleRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;

    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;

    @Autowired
    private AppSendService appSendService;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;
    @Autowired
    private SmsDataService smsDataService;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private PortBean portBean;

    private final String TIME_SEND = "0 30 9 ";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (portBean.checkPortReturn()) {
            log.info("---get cron celebrate in database---");
            taskRegistrar.setScheduler(this.taskExecutor());
            StringBuilder cronExpressions = this.getCronExpression();
            Map<Long, String> cronExpressions1 = this.getCronMap();
            Set<Long> longSet = cronExpressions1.keySet();
            //thực thi công việc
            longSet.forEach(id -> {
                Runnable runnable = () -> {
                    System.out.println("Task executed celebrate at ->" + new Date());
                    if (portBean.checkPortReturn()) {
                        this.getRunExpressions(id);
                    }
                };
                taskRegistrar.addTriggerTask(runnable, this.checkTrigger(cronExpressions, taskRegistrar, cronExpressions1.get(id)));
            });
        }
    }

    /**
     * thực thi công việc
     *
     * @param id
     */
    private void getRunExpressions(Long id) {
        CelebrateSample x = celebrateSampleRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(2L).orElseThrow();
        if (SampleConstant.PARENT.equals(x.getType()) || SampleConstant.KIDS.equals(x.getType())) {
            List<Kids> kidsList = kidsRepository.getKidsListForCelebrateAuto(x.getIdSchool(), x.getGender(), x.getType());
            //send firebase
            if (x.isAppSend()) {
                kidsList.forEach(y -> {
                    Long idSchool = y.getIdSchool();
                    try {
                        appSendService.saveToAppSendParentForAuto(idSchool, y, webSystemTitle.getTitle(), x.getContent(), FirebaseConstant.CATEGORY_NOTIFY);
                        firebaseFunctionService.sendParentCommon(Collections.singletonList(y), webSystemTitle.getTitle(), x.getContent(), idSchool, FirebaseConstant.CATEGORY_NOTIFY);
                    } catch (FirebaseMessagingException e) {
                        e.printStackTrace();
                    }
                });
            }
            //send sms
            if (x.isSmsSend()) {
                SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                smsNotifyDataRequest.setSendContent(x.getContent());
                kidsList.forEach(y -> {
                    try {
                        smsDataService.sendSmsKid(Collections.singletonList(y), y.getIdSchool(), smsNotifyDataRequest);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        if (SampleConstant.TEACHER.equals(x.getType()) || SampleConstant.PLUS.equals(x.getType())) {
            String type = SampleConstant.TEACHER.equals(x.getType()) ? AppTypeConstant.TEACHER : AppTypeConstant.SCHOOL;
            List<InfoEmployeeSchool> infoEmployeeSchoolList = infoEmployeeSchoolRepository.getInfoListForCelebrateAuto(x.getIdSchool(), type, x.getGender());
            //send firebase
            if (x.isAppSend()) {
                infoEmployeeSchoolList.forEach(y -> appSendService.saveToAppSendEmployeeForAuto(y.getSchool().getId(), y, webSystemTitle.getTitle(), x.getContent(), FirebaseConstant.CATEGORY_NOTIFY));
                if (SampleConstant.PLUS.equals(x.getType())) {
                    infoEmployeeSchoolList.forEach(y -> {
                        try {
                            firebaseFunctionService.sendPlusCommon(Collections.singletonList(y), webSystemTitle.getTitle(), x.getContent(), y.getSchool().getId(), FirebaseConstant.CATEGORY_NOTIFY);
                        } catch (FirebaseMessagingException e) {
                            e.printStackTrace();
                        }
                    });
                } else if (SampleConstant.TEACHER.equals(x.getType())) {
                    infoEmployeeSchoolList.forEach(y -> {
                        try {
                            firebaseFunctionService.sendTeacherCommon(infoEmployeeSchoolList, webSystemTitle.getTitle(), x.getContent(), y.getSchool().getId(), FirebaseConstant.CATEGORY_NOTIFY);
                        } catch (FirebaseMessagingException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            //send sms
            if (x.isSmsSend()) {
                SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                smsNotifyDataRequest.setSendContent(x.getContent());
                infoEmployeeSchoolList.forEach(y -> {
                    try {
                        smsDataService.sendSmsEmployee(infoEmployeeSchoolList, y.getSchool().getId(), smsNotifyDataRequest, SampleConstant.PLUS.equals(x.getType()) ? AppTypeConstant.SCHOOL : AppTypeConstant.TEACHER);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    //add trigger
    private Trigger checkTrigger(StringBuilder cronExpressions, ScheduledTaskRegistrar taskRegistrar, String cron) {
        return triggerContext -> {
            StringBuilder newCronExpression = this.getCronExpression();
            //check nếu cronjob đã thay đổi thì lấy gọi lại "get cron" để thay đổi lại lịch trình
            if (!StringUtils.equalsAnyIgnoreCase(newCronExpression, cronExpressions)) {
                System.out.println("lịch trình đã thay đổi");
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

    private Map<Long, String> getCronMap() {
        Map<Long, String> longStringMap = new HashMap<>();
        List<CelebrateSample> celebrateSampleList = celebrateSampleRepository.findAllByDelActiveTrue();
        celebrateSampleList.forEach(x -> {
            StringBuilder cronExpression = this.getCronOne(x);
            longStringMap.put(x.getId(), cronExpression.toString());
        });
        return longStringMap;
    }

    private StringBuilder getCronExpression() {
        StringBuilder cronExpression = new StringBuilder();
        List<CelebrateSample> celebrateSampleList = celebrateSampleRepository.findAllByDelActiveTrue();
        celebrateSampleList.forEach(x -> {
            cronExpression.append(this.getCronOne(x)).append("|");
        });
        return cronExpression;
    }

    private StringBuilder getCronOne(CelebrateSample x) {
        StringBuilder cronExpression = new StringBuilder();
        cronExpression.append(TIME_SEND).append(x.getDate()).append(" ").append(x.getMonth()).append(" ?");
        return cronExpression;
    }

    @Override
    public void destroy() {
        System.out.println("destroy schedule");
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private Executor taskExecutor() {
        return Executors.newScheduledThreadPool(20);
    }
}
