package com.example.onekids_project.cronjobs;

import com.example.onekids_project.bean.PortBean;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.EmployeeConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.service.servicecustom.employeesaraly.EmployeeSalaryService;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageService;
import com.example.onekids_project.service.servicecustom.finance.FnOrderKidsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * date 2021-03-13 15:32
 *
 * @author lavanviet
 */
@PropertySource(value = "cronjob.properties")
@Component
public class FinanceCronjob {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private FnKidsPackageService fnKidsPackageService;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnOrderKidsService fnOrderKidsService;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private EmployeeSalaryService employeeSalaryService;

    @Autowired
    private PortBean portBean;

    /**
     * khởi tạo các khoản thu cho học sinh các trường theo ngày config
     */
    @Transactional
    @Scheduled(cron = "${finance.kidsPackage}")
    public void createKidsPackage() throws ExecutionException, InterruptedException {
        logger.info("---------start auto create kidsPackage---------");
        portBean.checkPortForCronjob();

        List<String> groupTypeList = Arrays.asList("A", "B", "C", "D");
        // Download contents of all the web pages asynchronously
        List<CompletableFuture<String>> pageContentFutures = groupTypeList.stream()
                .map(this::createPackageTask).collect(Collectors.toList());

        // Create a combined Future using allOf()
        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()]));

        // When all the Futures are completed, call `future.join()` to get their results
        // and collect the results in a list
        CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> {
            return pageContentFutures.stream().map(CompletableFuture::join)
                    .collect(Collectors.toList());
        });
        // Count the number of web pages having the "CompletableFuture" keyword.
        CompletableFuture<Long> countFuture = allPageContentsFuture.thenApply(pageContents -> {
            return pageContents.stream().filter(pageContent -> pageContent.contains("CompletableFuture")).count();
        });

        logger.info("number of complete task: " + countFuture.get());
        logger.info("----------end auto create kidsPackage----------");
    }

    private CompletableFuture<String> createPackageTask(String type) {
        return CompletableFuture.supplyAsync(() -> {
            List<School> schoolList = schoolRepository.findAllByGroupTypeAndSchoolActiveTrueAndDelActiveTrue(type);
            logger.info("type count: {},  {}", type, schoolList.size());
            LocalDate nowDate = LocalDate.now();
            int dateOfMonth = nowDate.getDayOfMonth();
            schoolList.forEach(x -> {
                SchoolConfig schoolConfig = x.getSchoolConfig();
                if (schoolConfig.isAutoGenerateFeesKids()) {
                    Integer integer = schoolConfig.getAutoNexMonthFeesDate();
                    if (integer == dateOfMonth) {
                        List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatusAndIsActivatedTrue(x.getId(), KidsStatusConstant.STUDYING);
                        kidsList.forEach(y -> fnKidsPackageService.generateKidsPackageAuto(x, y, nowDate));
                    }
                }
            });
            return "CompletableFuture Completed" + type;
        });
    }

    /**
     * tự động tạo order cho học sinh khi config được tạo ngày đầu tháng
     */
    @Transactional
    @Scheduled(cron = "${finance.orderKids}")
    public void createOrderKids() {
        logger.info("---------start auto create orderKids---------");
        portBean.checkPortForCronjob();
        List<School> schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        schoolList.forEach(x -> {
            if (x.getSchoolConfig().isAutoFeesApprovedKids()) {
                List<Kids> kidsList = kidsRepository.findByIdSchoolAndDelActiveTrueAndKidStatus(x.getId(), KidsStatusConstant.STUDYING);
                fnOrderKidsService.generateOrderKidsAuto(kidsList);
            }
        });
        logger.info("----------end auto create orderKids----------");
    }


    /**
     * khởi tạo các khoản từ mặc định cho nhân sự theo ngày config
     */
    @Transactional
    @Scheduled(cron = "${finance.employeePackage}")
    public void createEmployeePackage() {
        logger.info("---------start auto create employeePackage---------");
        portBean.checkPortForCronjob();
        List<School> schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        LocalDate nowDate = LocalDate.now();
        int dateOfMonth = nowDate.getDayOfMonth();
        int month = nowDate.getMonthValue();
        int year = nowDate.getYear();
        schoolList.forEach(x -> {
            SchoolConfig schoolConfig = x.getSchoolConfig();
            if (schoolConfig.isAutoSignSalaryEmployee()) {
                Integer integer = schoolConfig.getAutoNexMonthFeesDate();
                if (integer != null && integer == dateOfMonth) {
                    List<InfoEmployeeSchool> dataList = infoEmployeeSchoolRepository.findBySchoolIdAndAppTypeAndEmployeeStatusAndDelActiveTrue(x.getId(), AppTypeConstant.TEACHER, EmployeeConstant.STATUS_WORKING);
                    dataList.forEach(y -> employeeSalaryService.generateEmployeeSalaryCommon(y, month, year, 1L, FinanceConstant.GENERATE_AUTO));
                }
            }
        });
        logger.info("----------end auto create employeePackage----------");
    }

    /**
     * tự động tạo order cho nhân sự khi config được tạo ngày đầu tháng
     */
    @Transactional
    @Scheduled(cron = "${finance.orderEmployee}")
    public void createOrderEmployee() {
        logger.info("---------start auto create orderEmployee---------");
        portBean.checkPortForCronjob();
        List<School> schoolList = schoolRepository.findAllBySchoolActiveTrueAndDelActiveTrue();
        schoolList.forEach(x -> {
            if (x.getSchoolConfig().isAutoBillSalaryEmployee()) {
                List<InfoEmployeeSchool> dataList = infoEmployeeSchoolRepository.findBySchoolIdAndAppTypeAndEmployeeStatusAndDelActiveTrue(x.getId(), AppTypeConstant.TEACHER, EmployeeConstant.STATUS_WORKING);
                employeeSalaryService.generateBillAuto(dataList);
            }
        });
        logger.info("----------end auto create orderEmployee----------");
    }


}
