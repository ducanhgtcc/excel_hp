package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.*;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.CycleMoney;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseDataService;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.base.IdListRequest;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.common.DescriptionRequest;
import com.example.onekids_project.request.common.StatusListRequest;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.finance.GenerateOrderKidsRequest;
import com.example.onekids_project.request.finance.KidsPackageInKidsSearchRequest;
import com.example.onekids_project.request.finance.order.*;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.response.excel.ExcelDataNew;
import com.example.onekids_project.response.excel.ExcelDynamicResponse;
import com.example.onekids_project.response.excel.ExcelNewResponse;
import com.example.onekids_project.response.finance.order.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.cashbook.CashBookHistoryService;
import com.example.onekids_project.service.servicecustom.finance.FnKidsPackageService;
import com.example.onekids_project.service.servicecustom.finance.FnOrderKidsService;
import com.example.onekids_project.service.servicecustom.finance.OrderKidsHistoryService;
import com.example.onekids_project.service.servicecustom.finance.WalletParentHistoryService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * date 2021-02-23 15:23
 *
 * @author lavanviet
 */
@Service
public class FnOrderKidsServiceImpl implements FnOrderKidsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;

    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private CycleMoneyRepository cycleMoneyRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;

    @Autowired
    private WalletParentRepository walletParentRepository;

    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;

    @Autowired
    private ExOrderHistoryKidsPackageRepository exOrderHistoryKidsPackageRepository;

    @Autowired
    private CashBookHistoryService cashBookHistoryService;

    @Autowired
    private OrderKidsHistoryService orderKidsHistoryService;

    @Autowired
    private WalletParentHistoryService walletParentHistoryService;

    @Autowired
    private FirebaseDataService firebaseDataService;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FnBankRepository fnBankRepository;
    @Autowired
    private AppSendService appSendService;
    @Autowired
    private SmsDataService smsDataService;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FnPackageRepository fnPackageRepository;
    @Autowired
    private FnKidsPackageService fnKidsPackageService;

    @Override
    public ExcelDynamicResponse excelExportOrderService(OrderExcelRequest request) {
        ExcelDynamicResponse response = new ExcelDynamicResponse();
        List<ExcelNewResponse> dataList = new ArrayList<>();
        ExcelNewResponse data = new ExcelNewResponse();
        List<ExcelDataNew> bodyList = new ArrayList<>();
        LocalDate date = request.getDate();
        Long idSchool = SchoolUtils.getIdSchool();
        School school = schoolRepository.findById(idSchool).orElseThrow();
        MaClass maClass = maClassRepository.findById(request.getIdClass()).orElseThrow();
        String monthYearString = ConvertData.formatMonthYear(date);

        response.setFileName("HOC_PHI_THANG_" + monthYearString);
        data.setSheetName(maClass.getClassName());
        List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC PHÍ CỦA HỌC SINH", "Trường: " + school.getSchoolName(), "Lớp: " + maClass.getClassName(), "Tháng: " + monthYearString);
        List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
        List<Kids> kidsList = kidsRepository.findKidsByIdList(request.getIdKidList());
        List<FnPackage> packageInClassList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderByCategory(idSchool, request.getIdClass());
        List<FnPackage> packageOtherAllKidList = new ArrayList<>();
        kidsList.forEach(x -> {
            List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
            fnKidsPackageList = fnKidsPackageList.stream().filter(a -> packageInClassList.stream().noneMatch(b -> a.getFnPackage().getId().equals(b.getId()))).collect(Collectors.toList());
            packageOtherAllKidList.addAll(fnKidsPackageList.stream().map(FnKidsPackage::getFnPackage).collect(Collectors.toList()));
        });
        List<FnPackage> packageOtherList = packageOtherAllKidList.stream().filter(FilterDataUtils.distinctBy(BaseEntity::getId)).collect(Collectors.toList());
        List<String> titleHeaderList = new ArrayList<>();
        List<Integer> sizeColumn = new ArrayList<>();
        titleHeaderList.add("STT");
        titleHeaderList.add("MÃ HS");
        titleHeaderList.add("Tên học sinh");
        titleHeaderList.add("Ngày sinh");
        titleHeaderList.add("PHHS còn phải trả các tháng trước");
        sizeColumn.add(5);
        sizeColumn.add(10);
        sizeColumn.add(20);
        sizeColumn.add(20);
        sizeColumn.add(20);
        packageInClassList.forEach(x -> {
            titleHeaderList.add(x.getName());
            sizeColumn.add(20);
        });
        packageOtherList.forEach(x -> {
            titleHeaderList.add(x.getName());
            sizeColumn.add(20);
        });
        titleHeaderList.add("PHHS phải trả tháng này");
        titleHeaderList.add("PHHS đã thanh toán");
        titleHeaderList.add("Tiền mặt");
        titleHeaderList.add("Chuyển khoản");
        titleHeaderList.add("Tồn tháng này");
        titleHeaderList.add("Tồn bao gồm tháng này");
        sizeColumn.add(20);
        sizeColumn.add(20);
        sizeColumn.add(20);
        sizeColumn.add(20);
        sizeColumn.add(20);
        sizeColumn.add(20);
        response.setTitleHeaderList(titleHeaderList);
        List<String> proList = new ArrayList<>();
        AtomicInteger count = new AtomicInteger(1);
        titleHeaderList.forEach(x -> {
            proList.add("pro" + count.getAndIncrement());
        });
        response.setProList(proList);
        response.setSizeColumnList(sizeColumn);
        int sizeHeader = titleHeaderList.size() - 4;
        List<Long> longList = new ArrayList<>();
        for (int i = 0; i < sizeHeader; i++) {
            longList.add(0L);
        }
        int i = 1;
        for (Kids x : kidsList) {
            List<String> bodyStringList = new ArrayList<>();
            bodyStringList.add(String.valueOf(i));
            bodyStringList.add(x.getKidCode());
            bodyStringList.add(x.getFullName());
            bodyStringList.add(ConvertData.formartDateDash(x.getBirthDay()));
            long moneyTotalPayMonthBefore = 0;
            long moneyTotalPayedMonthBefore = 0;
            List<FnKidsPackage> fnKidsPackageBeforeList = FinanceUltils.getKidsPackageListFromKidBefore(x, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
            for (FnKidsPackage a : fnKidsPackageBeforeList) {
                long moneyPackage = FinanceUltils.getMoneyCalculate(a);
                long moneyPackagePayed = ((long) a.getPaid());
                if (a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                    moneyPackage = moneyPackage * (-1);
                    moneyPackagePayed = moneyPackagePayed * (-1);
                }
                moneyTotalPayedMonthBefore += moneyPackagePayed;
                moneyTotalPayMonthBefore += moneyPackage;
            }
            long moneyTotalBefore = moneyTotalPayMonthBefore - moneyTotalPayedMonthBefore;
            bodyStringList.add(FinanceUltils.formatMoney(moneyTotalBefore));
            longList.set(0, longList.get(0) + moneyTotalBefore);

            long moneyTotalPayMonth = 0;
            long moneyTotalPayedMonth = 0;
            List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
            int count1 = 1;
            for (FnPackage a : packageInClassList) {
                List<FnKidsPackage> packageList = fnKidsPackageList.stream().filter(b -> b.getFnPackage().getId().equals(a.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(packageList)) {
                    FnKidsPackage fnKidsPackage = packageList.get(0);
                    long moneyPackage = FinanceUltils.getMoneyCalculate(fnKidsPackage);
                    long moneyPackagePayed = ((long) fnKidsPackage.getPaid());
                    if (fnKidsPackage.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                        moneyPackage = moneyPackage * (-1);
                        moneyPackagePayed = moneyPackagePayed * (-1);
                    }
                    moneyTotalPayedMonth += moneyPackagePayed;
                    moneyTotalPayMonth += moneyPackage;
                    bodyStringList.add(FinanceUltils.formatMoney(moneyPackage));
                    longList.set(count1, longList.get(count1) + moneyPackage);
                } else {
                    bodyStringList.add("");
                }
                count1++;
            }
            for (FnPackage a : packageOtherList) {
                List<FnKidsPackage> packageList = fnKidsPackageList.stream().filter(b -> b.getFnPackage().getId().equals(a.getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(packageList)) {
                    FnKidsPackage fnKidsPackage = packageList.get(0);
                    long moneyPackage = FinanceUltils.getMoneyCalculate(fnKidsPackage);
                    long moneyPackagePayed = ((long) fnKidsPackage.getPaid());
                    if (fnKidsPackage.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                        moneyPackage = moneyPackage * (-1);
                        moneyPackagePayed = moneyPackagePayed * (-1);
                    }
                    moneyTotalPayedMonth += moneyPackagePayed;
                    moneyTotalPayMonth += moneyPackage;
                    bodyStringList.add(FinanceUltils.formatMoney(moneyPackage));
                    longList.set(count1, longList.get(count1) + moneyPackage);
                } else {
                    bodyStringList.add("");
                }
                count1++;
            }

            bodyStringList.add(FinanceUltils.formatMoney(moneyTotalPayMonth));
            bodyStringList.add(FinanceUltils.formatMoney(moneyTotalPayedMonth));
            longList.set(count1, longList.get(count1) + moneyTotalPayMonth);
            longList.set(++count1, longList.get(count1) + moneyTotalPayedMonth);

            Optional<FnOrderKids> fnOrderKidsOptional = fnOrderKidsRepository.findByKidsIdAndMonthAndYear(x.getId(), date.getMonthValue(), date.getYear());
            if (fnOrderKidsOptional.isPresent()) {
                List<OrderKidsHistory> orderKidsHistoryList = fnOrderKidsOptional.get().getOrderKidsHistoryList();
                long moneyCash = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyCash() != null ? a.getMoneyCash() : 0).sum();
                long moneyTransfer = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyTransfer() != null ? a.getMoneyTransfer() : 0).sum();
                bodyStringList.add(FinanceUltils.formatMoney(moneyCash));
                bodyStringList.add(FinanceUltils.formatMoney(moneyTransfer));
                longList.set(++count1, longList.get(count1) + moneyCash);
                longList.set(++count1, longList.get(count1) + moneyTransfer);
            } else {
                bodyStringList.add("");
                bodyStringList.add("");
            }
            long moneyTotalMonth = moneyTotalPayMonth - moneyTotalPayedMonth;
            bodyStringList.add(FinanceUltils.formatMoney(moneyTotalMonth));
            bodyStringList.add(FinanceUltils.formatMoney(moneyTotalBefore + moneyTotalMonth));
            longList.set(++count1, longList.get(count1) + moneyTotalMonth);
            longList.set(++count1, longList.get(count1) + moneyTotalBefore + moneyTotalMonth);
            ExcelDataNew bodyData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
            bodyList.add(bodyData);
            i++;
        }

        List<String> bodyStringFinalList = new ArrayList<>();
        bodyStringFinalList.add("Tổng");
        bodyStringFinalList.add("");
        bodyStringFinalList.add("");
        bodyStringFinalList.add("");
        longList.forEach(y -> bodyStringFinalList.add(FinanceUltils.formatMoney(y)));
        ExcelDataNew bodyData = ExportExcelUtils.setBodyExcelNew(bodyStringFinalList);
        bodyList.add(bodyData);

        data.setHeaderList(headerList);
        data.setBodyList(bodyList);
        dataList.add(data);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public ExcelDynamicResponse excelExportNowOrderService(OrderExcelRequest request) {
        {
            ExcelDynamicResponse response = new ExcelDynamicResponse();
            List<ExcelNewResponse> dataList = new ArrayList<>();
            ExcelNewResponse data = new ExcelNewResponse();
            List<ExcelDataNew> bodyList = new ArrayList<>();
            LocalDate date = request.getDate();
            Long idSchool = SchoolUtils.getIdSchool();
            School school = schoolRepository.findById(idSchool).orElseThrow();
            MaClass maClass = maClassRepository.findById(request.getIdClass()).orElseThrow();
            String monthYearString = ConvertData.formatMonthYear(date);

            response.setFileName("HOC_PHI_THUC_TE_THANG_" + monthYearString);
            data.setSheetName(maClass.getClassName());
            List<String> headerStringList = Arrays.asList("DANH SÁCH HỌC PHÍ CỦA HỌC SINH SỐ THỰC TẾ", "Trường: " + school.getSchoolName(), "Lớp: " + maClass.getClassName(), "Tháng: " + monthYearString);
            List<ExcelDataNew> headerList = ExportExcelUtils.setHeaderExcelNew(headerStringList);
            List<Kids> kidsList = kidsRepository.findKidsByIdList(request.getIdKidList());
            List<FnPackage> packageInClassList = fnPackageRepository.findByIdSchoolAndMaClassSetIdAndDelActiveTrueOrderByCategory(idSchool, request.getIdClass());
            List<FnPackage> packageOtherAllKidList = new ArrayList<>();
            kidsList.forEach(x -> {
                List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                fnKidsPackageList = fnKidsPackageList.stream().filter(a -> packageInClassList.stream().noneMatch(b -> a.getFnPackage().getId().equals(b.getId()))).collect(Collectors.toList());
                packageOtherAllKidList.addAll(fnKidsPackageList.stream().map(FnKidsPackage::getFnPackage).collect(Collectors.toList()));
            });
            List<FnPackage> packageOtherList = packageOtherAllKidList.stream().filter(FilterDataUtils.distinctBy(BaseEntity::getId)).collect(Collectors.toList());
            List<String> titleHeaderList = new ArrayList<>();
            List<Integer> sizeColumn = new ArrayList<>();
            titleHeaderList.add("STT");
            titleHeaderList.add("MÃ HS");
            titleHeaderList.add("Tên học sinh");
            titleHeaderList.add("Ngày sinh");
            titleHeaderList.add("Tiền thừa(Số dư ví)");
            titleHeaderList.add("Tồn các tháng trước");
//            titleHeaderList.add("Số ngày học(T2-T6)");
//            titleHeaderList.add("Số ngày học(T7)");
            sizeColumn.add(5);
            sizeColumn.add(10);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            packageInClassList.forEach(x -> {
                titleHeaderList.add(x.getName());
                sizeColumn.add(20);
            });
            packageOtherList.forEach(x -> {
                titleHeaderList.add(x.getName());
                sizeColumn.add(20);
            });
            titleHeaderList.add("Tổng thực tế tháng này");
            titleHeaderList.add("Học phí thu trước");
            titleHeaderList.add("PHHS đã thanh toán");
            titleHeaderList.add("Tiền mặt");
            titleHeaderList.add("Chuyển khoản");
            titleHeaderList.add("Tồn tháng này");
            titleHeaderList.add("Tồn bao gồm tháng này");
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            sizeColumn.add(20);
            response.setTitleHeaderList(titleHeaderList);
            List<String> proList = new ArrayList<>();
            AtomicInteger count = new AtomicInteger(1);
            titleHeaderList.forEach(x -> {
                proList.add("pro" + count.getAndIncrement());
            });
            response.setProList(proList);
            response.setSizeColumnList(sizeColumn);
            int sizeHeader = titleHeaderList.size() - 4;
            List<Long> longList = new ArrayList<>();
            for (int i = 0; i < sizeHeader; i++) {
                longList.add(0L);
            }
            int i = 1;
            for (Kids x : kidsList) {
                int count1 = 0;
                List<String> bodyStringList = new ArrayList<>();
                bodyStringList.add(String.valueOf(i));
                bodyStringList.add(x.getKidCode());
                bodyStringList.add(x.getFullName());
                bodyStringList.add(ConvertData.formartDateDash(x.getBirthDay()));

//                số dư ví
                if (Objects.nonNull(x.getParent())) {
                    WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
                    Long moneyWallet = (long) (walletParent.getMoneyIn() - walletParent.getMoneyOut());
                    bodyStringList.add(FinanceUltils.formatMoney(moneyWallet));
                    longList.set(count1, longList.get(count1) + moneyWallet);
                } else {
                    bodyStringList.add("Chưa có ví");
                }

                long moneyTotalPayMonthBefore = 0;
                long moneyTotalPayedMonthBefore = 0;
                List<FnKidsPackage> fnKidsPackageBeforeList = FinanceUltils.getKidsPackageListFromKidBefore(x, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                for (FnKidsPackage a : fnKidsPackageBeforeList) {
                    long moneyPackage = FinanceUltils.getMoneyCalculate(a);
                    long moneyPackagePayed = ((long) a.getPaid());
                    if (a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                        moneyPackage = moneyPackage * (-1);
                        moneyPackagePayed = moneyPackagePayed * (-1);
                    }
                    moneyTotalPayedMonthBefore += moneyPackagePayed;
                    moneyTotalPayMonthBefore += moneyPackage;
                }
                long moneyTotalBefore = moneyTotalPayMonthBefore - moneyTotalPayedMonthBefore;
                bodyStringList.add(FinanceUltils.formatMoney(moneyTotalBefore));
                longList.set(++count1, longList.get(count1) + moneyTotalBefore);

//                bodyStringList.add("đi học T2-T6");
//                bodyStringList.add("đi học T7");
//                longList.set(2, longList.get(2) + 1);
//                longList.set(3, longList.get(3) + 1);

                long moneyTotalPayMonth = 0;
                long moneyTotalPayedMonth = 0;
                long moneyTotalPayDynamic = 0;
                List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(x, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                count1++;
                for (FnPackage a : packageInClassList) {
                    List<FnKidsPackage> packageList = fnKidsPackageList.stream().filter(b -> b.getFnPackage().getId().equals(a.getId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(packageList)) {
                        FnKidsPackage fnKidsPackage = packageList.get(0);
                        long moneyPackageDynamic = FinanceUltils.getMoneyCalculateDynamic(fnKidsPackage);
                        long moneyPackage = FinanceUltils.getMoneyCalculate(fnKidsPackage);
                        long moneyPackagePayed = ((long) fnKidsPackage.getPaid());
                        if (fnKidsPackage.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                            moneyPackage = moneyPackage * (-1);
                            moneyPackagePayed = moneyPackagePayed * (-1);
                            moneyPackageDynamic = moneyPackageDynamic * (-1);
                        }
                        moneyTotalPayedMonth += moneyPackagePayed;
                        moneyTotalPayMonth += moneyPackage;
                        moneyTotalPayDynamic += moneyPackageDynamic;
                        bodyStringList.add(FinanceUltils.formatMoney(moneyPackageDynamic));
                        longList.set(count1, longList.get(count1) + moneyPackageDynamic);
                    } else {
                        bodyStringList.add("");
                    }
                    count1++;
                }
                for (FnPackage a : packageOtherList) {
                    List<FnKidsPackage> packageList = fnKidsPackageList.stream().filter(b -> b.getFnPackage().getId().equals(a.getId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(packageList)) {
                        FnKidsPackage fnKidsPackage = packageList.get(0);
                        long moneyPackageDynamic = FinanceUltils.getMoneyCalculateDynamic(fnKidsPackage);
                        long moneyPackage = FinanceUltils.getMoneyCalculate(fnKidsPackage);
                        long moneyPackagePayed = ((long) fnKidsPackage.getPaid());
                        if (fnKidsPackage.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                            moneyPackage = moneyPackage * (-1);
                            moneyPackagePayed = moneyPackagePayed * (-1);
                            moneyPackageDynamic = moneyPackageDynamic * (-1);
                        }
                        moneyTotalPayedMonth += moneyPackagePayed;
                        moneyTotalPayMonth += moneyPackage;
                        moneyTotalPayDynamic += moneyPackageDynamic;
                        bodyStringList.add(FinanceUltils.formatMoney(moneyPackageDynamic));
                        longList.set(count1, longList.get(count1) + moneyPackageDynamic);
                    } else {
                        bodyStringList.add("");
                    }
                    count1++;
                }

//                bodyStringList.add()


                bodyStringList.add(FinanceUltils.formatMoney(moneyTotalPayDynamic));
                bodyStringList.add(FinanceUltils.formatMoney(moneyTotalPayMonth));
                bodyStringList.add(FinanceUltils.formatMoney(moneyTotalPayedMonth));
                longList.set(count1, longList.get(count1) + moneyTotalPayDynamic);
                longList.set(++count1, longList.get(count1) + moneyTotalPayMonth);
                longList.set(++count1, longList.get(count1) + moneyTotalPayedMonth);

                Optional<FnOrderKids> fnOrderKidsOptional = fnOrderKidsRepository.findByKidsIdAndMonthAndYear(x.getId(), date.getMonthValue(), date.getYear());
                if (fnOrderKidsOptional.isPresent()) {
                    List<OrderKidsHistory> orderKidsHistoryList = fnOrderKidsOptional.get().getOrderKidsHistoryList();
                    long moneyCashIn = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyCash() != null && a.getCategory().equals(FinanceConstant.CATEGORY_IN) ? a.getMoneyCash() : 0).sum();
                    long moneyCashOut = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyCash() != null && a.getCategory().equals(FinanceConstant.CATEGORY_OUT) ? a.getMoneyCash() : 0).sum();
                    long moneyTransferIn = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyCash() != null && a.getCategory().equals(FinanceConstant.CATEGORY_IN) ? a.getMoneyTransfer() : 0).sum();
                    long moneyTransferOut = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyCash() != null && a.getCategory().equals(FinanceConstant.CATEGORY_OUT) ? a.getMoneyTransfer() : 0).sum();
                    long moneyCash = moneyCashIn - moneyCashOut;
                    long moneyTransfer = moneyTransferIn - moneyTransferOut;
//                    long moneyCash = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyCash() != null ? a.getMoneyCash() : 0).sum();
//                    long moneyTransfer = (long) orderKidsHistoryList.stream().mapToDouble(a -> a.getMoneyTransfer() != null ? a.getMoneyTransfer() : 0).sum();
                    bodyStringList.add(FinanceUltils.formatMoney(moneyCash));
                    bodyStringList.add(FinanceUltils.formatMoney(moneyTransfer));
                    longList.set(++count1, longList.get(count1) + moneyCash);
                    longList.set(++count1, longList.get(count1) + moneyTransfer);
                } else {
                    bodyStringList.add("");
                    bodyStringList.add("");
                }
                long moneyTotalMonth = moneyTotalPayMonth - moneyTotalPayedMonth;
                bodyStringList.add(FinanceUltils.formatMoney(moneyTotalMonth));
                bodyStringList.add(FinanceUltils.formatMoney(moneyTotalBefore + moneyTotalMonth));
                longList.set(++count1, longList.get(count1) + moneyTotalMonth);
                longList.set(++count1, longList.get(count1) + moneyTotalBefore + moneyTotalMonth);
                ExcelDataNew bodyData = ExportExcelUtils.setBodyExcelNew(bodyStringList);
                bodyList.add(bodyData);
                i++;
            }

            List<String> bodyStringFinalList = new ArrayList<>();
            bodyStringFinalList.add("Tổng");
            bodyStringFinalList.add("");
            bodyStringFinalList.add("");
            bodyStringFinalList.add("");
            longList.forEach(y -> bodyStringFinalList.add(FinanceUltils.formatMoney(y)));
            ExcelDataNew bodyData = ExportExcelUtils.setBodyExcelNew(bodyStringFinalList);
            bodyList.add(bodyData);

            data.setHeaderList(headerList);
            data.setBodyList(bodyList);
            dataList.add(data);
            response.setDataList(dataList);
            return response;
        }
    }

    @Override
    public List<OrderKidsResponse> searchOrderKids(UserPrincipal principal, KidsPackageInKidsSearchRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<OrderKidsResponse> responseList = new ArrayList<>();
        int month = request.getDate().getMonthValue();
        int year = request.getDate().getYear();
        List<Kids> kidsList = kidsRepository.findByKidsClassWithStatusName(request.getIdClass(), request.getStatus(), request.getFullName());
        if (request.isWalletStatus()) {
            kidsList = kidsList.stream().filter(x -> x.getParent() != null).collect(Collectors.toList());
            kidsList = kidsList.stream().filter(x -> {
                WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
                double balance = FinanceUltils.getWalletParentBalance(walletParent);
                return x.getParent() != null && balance > 0;
            }).collect(Collectors.toList());
        }
        kidsList.forEach(x -> {
            OrderKidsResponse model = modelMapper.map(x, OrderKidsResponse.class);
            List<FnOrderKids> fnOrderKidsList = x.getFnOrderKidsList().stream().filter(a -> a.getMonth() == month && a.getYear() == year).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fnOrderKidsList)) {
                if (fnOrderKidsList.size() == 1) {
                    OrderKidsCustom1 orderKidsCustom1 = modelMapper.map(fnOrderKidsList.get(0), OrderKidsCustom1.class);
                    this.setProperties(x.getId(), month, year, orderKidsCustom1);
                    model.setOrderKids(orderKidsCustom1);
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Có nhiều hơn 1 khoản thu trong một tháng của học sinh");
                }
            }
            List<String> codeList = new ArrayList<>();
            LocalDate nowDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
            List<FnOrderKids> fnOrderKidsAllList = fnOrderKidsRepository.findByKidsId(x.getId());
            fnOrderKidsAllList = fnOrderKidsAllList.stream().filter(a -> {
                LocalDate dateOrder = LocalDate.of(a.getYear(), a.getMonth(), 1);
                return dateOrder.isBefore(nowDate);
            }).collect(Collectors.toList());
            fnOrderKidsAllList.forEach(y -> {
                List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(x.getId(), y.getMonth(), y.getYear());
                if (FinanceUltils.getOrderStatus(fnKidsPackageList)) {
                    codeList.add(y.getCode());
                }
            });
            model.setInCompleteOrderNumber(codeList.size());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<OrderKidsCustom1> searchOrderKidsMonth(UserPrincipal principal, SearchOrderKidsDetailRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idKid = request.getIdKid();
        int year = request.getYear();
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findByKidsIdAndYearOrderByMonthDesc(idKid, year);
        List<OrderKidsCustom1> responseList = new ArrayList<>();
        fnOrderKidsList.forEach(x -> {
            OrderKidsCustom1 orderKidsCustom1 = modelMapper.map(x, OrderKidsCustom1.class);
            this.setProperties(idKid, x.getMonth(), x.getYear(), orderKidsCustom1);
            responseList.add(orderKidsCustom1);
        });
        return responseList;
    }

    @Override
    public ListOrderKidsResponse searchOrderKidsForKids(UserPrincipal principal, SearchOrderKidsAllRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idKid = request.getIdKid();
        ListOrderKidsResponse response = new ListOrderKidsResponse();
        List<OrderKidsCustom1> dataList = new ArrayList<>();
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.searchOrderForKids(request);
        fnOrderKidsList.forEach(x -> {
            OrderKidsCustom1 orderKidsCustom1 = modelMapper.map(x, OrderKidsCustom1.class);
            this.setProperties(idKid, x.getMonth(), x.getYear(), orderKidsCustom1);
            dataList.add(orderKidsCustom1);
        });
        response.setMoneyTotalIn(dataList.stream().mapToLong(OrderKidsCustom1::getMoneyTotalIn).sum());
        response.setMoneyPaidIn(dataList.stream().mapToLong(OrderKidsCustom1::getMoneyPaidIn).sum());
        response.setMoneyTotalOut(dataList.stream().mapToLong(OrderKidsCustom1::getMoneyTotalOut).sum());
        response.setMoneyPaidOut(dataList.stream().mapToLong(OrderKidsCustom1::getMoneyPaidOut).sum());
        response.setDataList(dataList);
        return response;
    }


    @Transactional
    @Override
    public boolean generateOrderKids(UserPrincipal principal, GenerateOrderKidsRequest request) {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
//        FinanceUltils.checkDateGenerateOrder(date);
        for (IdObjectRequest x : request.getIdKidList()) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
            this.creatOrderKids(kids, date, FinanceConstant.GENERATE_MANUAL);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean sendNotifyOrderKids(UserPrincipal principal, GenerateOrderKidsRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        LocalDate date = request.getDate();
//        FinanceUltils.checkDateGenerateOrder(date);
        Long idSchool = principal.getIdSchoolLogin();
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(71L).orElseThrow();
        String title = webSystemTitle.getTitle();
        for (IdObjectRequest x : request.getIdKidList()) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x.getId()).orElseThrow();
            List<FnOrderKids> fnOrderKidsList = kids.getFnOrderKidsList().stream().filter(a -> a.getMonth() == date.getMonthValue() && a.getYear() == date.getYear()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fnOrderKidsList)) {
                FnOrderKids fnOrderKids = fnOrderKidsList.get(0);
                List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(kids, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
                String totalMoneyString = orderMoneyModel.getMoneyTotalInOut() >= 0 ? "phải đóng" : "được nhận";
                String totalRemainString = orderMoneyModel.getMoneyTotalInOutRemain() >= 0 ? "phải đóng" : "được nhận";
                String content = webSystemTitle.getContent().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()).replace("{order_code}", fnOrderKids.getCode()).replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderKids.getMonth(), fnOrderKids.getYear())).replace("{in_out_fees}", totalMoneyString).replace("{money_total}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOut()))).replace("{in_out}", totalRemainString).replace("{money_pay}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOutRemain())));
                appSendService.saveToAppSendParent(principal, kids, title, content, AppSendConstant.TYPE_FINANCE);
                firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, content, idSchool, FirebaseConstant.CATEGORY_ORDER_NOTIFY);
                //gửi Sms
                if (webSystemTitle.isSms()) {
                    SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                    smsNotifyDataRequest.setSendContent(content);
                    smsDataService.sendSmsKid(Collections.singletonList(kids), kids.getIdSchool(), smsNotifyDataRequest);
                }
            }
        }
        return true;
    }

    @Override
    public void generateOrderKidsAuto(List<Kids> kidsList) {
        LocalDate date = LocalDate.now();
        for (Kids x : kidsList) {
            this.creatOrderKids(x, date, FinanceConstant.GENERATE_AUTO);
        }
    }


    @Transactional
    @Override
    public boolean setViewOrder(UserPrincipal principal, StatusRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(request.getId()).orElseThrow();
        this.setView(principal, fnOrderKids, request.getStatus(), principal.getId(), LocalDateTime.now());
        return true;
    }


    @Override
    public boolean setLockedOrder(UserPrincipal principal, StatusRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(request.getId()).orElseThrow();
        this.setLocked(fnOrderKids, request.getStatus(), principal.getId(), LocalDateTime.now());
        return true;
    }

    @Transactional
    @Override
    public boolean setViewOrderMany(UserPrincipal principal, StatusListRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findAllById(request.getIdList());
        Long idUser = principal.getId();
        LocalDateTime dateTime = LocalDateTime.now();
        for (FnOrderKids x : fnOrderKidsList) {
            this.setView(principal, x, request.getStatus(), idUser, dateTime);
        }
        return true;
    }

    @Override
    public void sendViewOrderManyNoSMS(UserPrincipal principal, StatusListRequest request) throws FirebaseMessagingException {
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findAllById(request.getIdList());
        fnOrderKidsList = fnOrderKidsList.stream().filter(FnOrderKids::isView).collect(Collectors.toList());
        for (FnOrderKids x : fnOrderKidsList) {
            this.sendFirebaseOrderViewNoSMS(principal, x);
        }

    }

    @Override
    public boolean setLockedOrderMany(UserPrincipal principal, StatusListRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idUser = principal.getId();
        LocalDateTime timeNow = LocalDateTime.now();
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findAllById(request.getIdList());
        fnOrderKidsList.forEach(x -> this.setLocked(x, request.getStatus(), idUser, timeNow));
        return true;
    }

    @Override
    public ListKidsPackageForPaymentResponse searchKidsPackagePayment(UserPrincipal principal, Long idOrder, OrderRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment search"));
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdKid(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id kids in school"));
        CycleMoney cycleMoney = cycleMoneyRepository.findBySchoolId(SchoolUtils.getIdSchool()).orElseThrow();
        List<FnKidsPackage> fnKidsPackageList = request.getCategory().equals(FinanceConstant.CATEGOTY_BOTH) ? fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(kids.getId(), fnOrderKids.getMonth(), fnOrderKids.getYear()) : fnKidsPackageRepository.findByKidsIdAndFnPackageCategoryAndMonthAndYearAndApprovedTrueAndDelActiveTrue(kids.getId(), request.getCategory(), fnOrderKids.getMonth(), fnOrderKids.getYear());
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        ListKidsPackageForPaymentResponse response = new ListKidsPackageForPaymentResponse();
        List<KidsPackageForPaymentResponse> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnKidsPackage x : fnKidsPackageList) {
            KidsPackageForPaymentResponse model = modelMapper.map(x, KidsPackageForPaymentResponse.class);
            model.setMoney(FinanceUltils.getMoneyCalculate(x));
            if (request.getCategory().equals(FinanceConstant.CATEGOTY_BOTH) && x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                model.setMoney(model.getMoney() * (-1));
                model.setPaid(model.getPaid() * (-1));
            }
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        response.setMoneyWallet(walletParent.getMoneyIn() - walletParent.getMoneyOut());
        response.setMoneyTotal(moneyTotal);
        response.setMoneyTotalPaid(moneyTotalPaid);
        response.setDataList(dataList);
        response.setDateTime(fnOrderKids.getTimePayment());
        response.setDescription(fnOrderKids.getDescription());
        response.setTransferMoneyType(cycleMoney.getTransferMoneyType());
        return response;
    }

    @Override
    public OrderPrintResponse getPrintOrder(UserPrincipal principal, OrderRequest request, IdListRequest idList) throws IOException {
        OrderPrintResponse response = new OrderPrintResponse();
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdKid(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id kids in school"));
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndApprovedTrueAndDelActiveTrue(idList.getIdList(), principal.getIdSchoolLogin());
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        List<KidsPackageCustom2> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnKidsPackage x : fnKidsPackageList) {
            KidsPackageCustom2 model = new KidsPackageCustom2();
            model.setName(x.getFnPackage().getName());
            model.setMoney(FinanceUltils.getMoneyCalculate(x));
            model.setPaid(x.getPaid());
            model.setNumber(x.getUsedNumber());
            model.setPrice(FinanceUltils.getPriceData(x));
            if (request.getCategory().equals(FinanceConstant.CATEGOTY_BOTH) && x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
                model.setMoney(model.getMoney() * (-1));
                model.setPaid(model.getPaid() * (-1));
            }
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        double moneyTotalRemain = moneyTotal - moneyTotalPaid;

        KidsPackageCustom2 model = new KidsPackageCustom2();
        model.setName("Tổng cộng");
        model.setMoney(moneyTotal);
        model.setPaid(moneyTotalPaid);
        model.setRemain(moneyTotalRemain);
        dataList.add(model);

        KidsPackageCustom2 model1 = new KidsPackageCustom2();
        model1.setName("Số dư trong ví");
        model1.setMoney(walletParent.getMoneyIn() - walletParent.getMoneyOut());
        dataList.add(model1);

        KidsPackageCustom2 model2 = new KidsPackageCustom2();
        model2.setName("Số tiền đã trả");
        model2.setMoney(moneyTotalPaid);
        dataList.add(model2);

        KidsPackageCustom2 model3 = new KidsPackageCustom2();
        model3.setName("Số tiền còn thiếu");
        model3.setMoney(moneyTotalRemain);
        dataList.add(model3);

        response.setDataList(dataList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<FnBank> fnBankList = school.getFnBankList().stream().filter(x -> x.isDelActive() && x.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fnBankList)) {
            FnBank fnBank = fnBankList.get(0);
            response.setBankNameBank(fnBank.getBankName());
            response.setAccountNumberBank(fnBank.getAccountNumber());
            response.setFullNameBank(fnBank.getFullName());
        }
        response.setFullName(kids.getFullName());
        response.setClassName(kids.getMaClass().getClassName());
        response.setPhone(kids.getParent() != null ? kids.getParent().getMaUser().getPhone() : null);
        response.setBankInfo(school.getSchoolInfo().getExpired());
        response.setUserCreate(principal.getFullName());
        response.setSchoolName(school.getSchoolName());
        response.setSchoolAddress(school.getSchoolAddress());
        response.setAvatar(HandleFileUtils.getBase64EncodedImage(AvatarUtils.getAvatarSchool(school)));
        return response;
    }

    @Override
    public List<OrderPrintResponse> getPrintOrderMany(UserPrincipal principal, List<OrderManyRequest> request) throws IOException {
        List<OrderPrintResponse> responseList = new ArrayList<>();
        for (OrderManyRequest x : request) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x.getIdKid()).orElseThrow();
            if (kids.getParent() != null) {
                OrderRequest orderRequest = modelMapper.map(x, OrderRequest.class);
                List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getFnKidsPackageInOrder(x.getIdKid(), x.getIdOrder(), x.getCategory());
                if (CollectionUtils.isNotEmpty(fnKidsPackageList)) {
                    IdListRequest idListRequest = new IdListRequest();
                    idListRequest.setIdList(fnKidsPackageList.stream().map(BaseEntity::getId).collect(Collectors.toList()));
                    OrderPrintResponse response = null;
                    response = this.getPrintOrder(principal, orderRequest, idListRequest);
                    responseList.add(response);
                }
            }
        }
        return responseList;
    }

    @Override
    public OrderPrintResponse getPrintOrderOut(UserPrincipal principal, OrderRequest request, IdListRequest idList) throws IOException {
        OrderPrintResponse response = new OrderPrintResponse();
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdKid(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id kids in school"));
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndApprovedTrueAndDelActiveTrue(idList.getIdList(), principal.getIdSchoolLogin());
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        List<KidsPackageCustom2> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnKidsPackage x : fnKidsPackageList) {
            KidsPackageCustom2 model = new KidsPackageCustom2();
            model.setName(x.getFnPackage().getName());
            model.setMoney(FinanceUltils.getMoneyCalculate(x));
            model.setPaid(x.getPaid());
            model.setNumber(x.getUsedNumber());
            model.setPrice(FinanceUltils.getPriceData(x));
//            if (request.getCategory().equals(FinanceConstant.CATEGORY_OUT) && x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
//                model.setMoney(model.getMoney() * (-1));
//                model.setPaid(model.getPaid() * (-1));
//            }
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        double moneyTotalRemain = moneyTotal - moneyTotalPaid;

        KidsPackageCustom2 model = new KidsPackageCustom2();
        model.setName("Tổng cộng");
        model.setMoney(moneyTotal);
        model.setPaid(moneyTotalPaid);
        model.setRemain(moneyTotalRemain);
        dataList.add(model);

        KidsPackageCustom2 model1 = new KidsPackageCustom2();
        model1.setName("Số dư trong ví");
        model1.setMoney(walletParent.getMoneyIn() - walletParent.getMoneyOut());
        dataList.add(model1);

        KidsPackageCustom2 model2 = new KidsPackageCustom2();
        model2.setName("Số tiền đã trả");
        model2.setMoney(moneyTotalPaid);
        dataList.add(model2);

        KidsPackageCustom2 model3 = new KidsPackageCustom2();
        model3.setName("Số tiền còn thiếu");
        model3.setMoney(moneyTotalRemain);
        dataList.add(model3);

        response.setDataList(dataList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<FnBank> fnBankList = school.getFnBankList().stream().filter(x -> x.isDelActive() && x.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fnBankList)) {
            FnBank fnBank = fnBankList.get(0);
            response.setBankNameBank(fnBank.getBankName());
            response.setAccountNumberBank(fnBank.getAccountNumber());
            response.setFullNameBank(fnBank.getFullName());
        }
        response.setFullName(kids.getFullName());
        response.setClassName(kids.getMaClass().getClassName());
        response.setPhone(kids.getParent() != null ? kids.getParent().getMaUser().getPhone() : null);
        response.setBankInfo(school.getSchoolInfo().getExpired());
        response.setUserCreate(principal.getFullName());
        response.setSchoolName(school.getSchoolName());
        response.setSchoolAddress(school.getSchoolAddress());
        response.setAvatar(HandleFileUtils.getBase64EncodedImage(AvatarUtils.getAvatarSchool(school)));
        return response;
    }

    @Override
    public OrderPrintResponse getPrintOrderIn(UserPrincipal principal, OrderRequest request, IdListRequest idList) throws IOException {
        OrderPrintResponse response = new OrderPrintResponse();
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdKid(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found by id kids in school"));
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndApprovedTrueAndDelActiveTrue(idList.getIdList(), principal.getIdSchoolLogin());
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        List<KidsPackageCustom2> dataList = new ArrayList<>();
        double moneyTotal = 0;
        double moneyTotalPaid = 0;
        for (FnKidsPackage x : fnKidsPackageList) {
            KidsPackageCustom2 model = new KidsPackageCustom2();
            model.setName(x.getFnPackage().getName());
            model.setMoney(FinanceUltils.getMoneyCalculate(x));
            model.setPaid(x.getPaid());
            model.setNumber(x.getUsedNumber());
            model.setPrice(FinanceUltils.getPriceData(x));
//            if (request.getCategory().equals(FinanceConstant.CATEGORY_IN) && x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)) {
//                model.setMoney(model.getMoney() * (-1));
//                model.setPaid(model.getPaid() * (-1));
//            }
            model.setRemain(model.getMoney() - model.getPaid());
            moneyTotal = moneyTotal + model.getMoney();
            moneyTotalPaid = moneyTotalPaid + model.getPaid();
            dataList.add(model);
        }
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        double moneyTotalRemain = moneyTotal - moneyTotalPaid;

        KidsPackageCustom2 model = new KidsPackageCustom2();
        model.setName("Tổng cộng");
        model.setMoney(moneyTotal);
        model.setPaid(moneyTotalPaid);
        model.setRemain(moneyTotalRemain);
        dataList.add(model);

        KidsPackageCustom2 model1 = new KidsPackageCustom2();
        model1.setName("Số dư trong ví");
        model1.setMoney(walletParent.getMoneyIn() - walletParent.getMoneyOut());
        dataList.add(model1);

        KidsPackageCustom2 model2 = new KidsPackageCustom2();
        model2.setName("Số tiền đã trả");
        model2.setMoney(moneyTotalPaid);
        dataList.add(model2);

        KidsPackageCustom2 model3 = new KidsPackageCustom2();
        model3.setName("Số tiền còn thiếu");
        model3.setMoney(moneyTotalRemain);
        dataList.add(model3);

        response.setDataList(dataList);
        School school = schoolRepository.findById(principal.getIdSchoolLogin()).orElseThrow();
        List<FnBank> fnBankList = school.getFnBankList().stream().filter(x -> x.isDelActive() && x.isChecked()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(fnBankList)) {
            FnBank fnBank = fnBankList.get(0);
            response.setBankNameBank(fnBank.getBankName());
            response.setAccountNumberBank(fnBank.getAccountNumber());
            response.setFullNameBank(fnBank.getFullName());
        }
        response.setFullName(kids.getFullName());
        response.setClassName(kids.getMaClass().getClassName());
        response.setPhone(kids.getParent() != null ? kids.getParent().getMaUser().getPhone() : null);
        response.setBankInfo(school.getSchoolInfo().getExpired());
        response.setUserCreate(principal.getFullName());
        response.setSchoolName(school.getSchoolName());
        response.setSchoolAddress(school.getSchoolAddress());
        response.setAvatar(HandleFileUtils.getBase64EncodedImage(AvatarUtils.getAvatarSchool(school)));
        return response;
    }

    @Override
    public ListOrderKidsDetailResponse findKidsPackagePaymentDetail(UserPrincipal principal, Long idOrder) {
        CommonValidate.checkDataPlus(principal);
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment detail"));
        ListOrderKidsDetailResponse response = modelMapper.map(fnOrderKids, ListOrderKidsDetailResponse.class);
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(fnOrderKids.getKids().getId(), fnOrderKids.getMonth(), fnOrderKids.getYear());
        fnKidsPackageList = fnKidsPackageList.stream().sorted(Comparator.comparing(a -> a.getFnPackage().getCategory())).collect(Collectors.toList());
        List<KidsPackageForPaymentDetailResponse> dataList = new ArrayList<>();
        for (FnKidsPackage x : fnKidsPackageList) {
            KidsPackageForPaymentDetailResponse model = modelMapper.map(x, KidsPackageForPaymentDetailResponse.class);
            model.setMoney(FinanceUltils.getMoneyCalculate(x));
            dataList.add(model);
        }
        response.setDataList(dataList);
        return response;
    }

    @Override
    public boolean saveOrderKidsDescription(UserPrincipal principal, Long idOrder, DescriptionRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment detail"));
        checkLocked(fnOrderKids);
        fnOrderKids.setDescription(request.getDescription());
        fnOrderKidsRepository.save(fnOrderKids);
        return true;
    }

    @Transactional
    @Override
    public boolean orderKidsPayment(UserPrincipal principal, Long idOrder, OrderKidsPaymentRequest request) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        String category = request.getCategory();
        boolean showOrder = principal.getSchoolConfig().isAutoLockFeesSuccessKids();
        FnOrderKids fnOrderKids = fnOrderKidsRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment update"));
        LocalDate monthYearDate = LocalDate.of(fnOrderKids.getYear(), fnOrderKids.getMonth(), 1);
        this.checkLocked(fnOrderKids);
        this.checkOrderTimeEdit(fnOrderKids, request.getDateTime());
        this.checkInputInfo(request);

        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByIdInAndKidsIdSchoolAndDelActiveTrue(request.getIdKidsPackageList(), idSchool);
        long unApprovedNumber = fnKidsPackageList.stream().filter(x -> !x.isApproved()).count();
        if (unApprovedNumber > 0) {
            logger.warn("Có khoản chưa được duyệt");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ORDER_MODIFIED);
        }
        //check số khoản thu truyền vào có bằng số tìm kiếm được tron DB hay không
        if (fnKidsPackageList.size() < request.getIdKidsPackageList().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.NOT_ENOUGH_PACKAGE);
        }
        Kids kids = kidsRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getIdKid(), principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("not found kids in payment"));
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        //số tiền thanh toán trong mỗi lần thanh toán
        double moneyPaymentAll = 0;
        //với kiểu thanh toán tổng hợp có cả thu và chi
        if (category.equals(FinanceConstant.CATEGOTY_BOTH)) {
            List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
            List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
            long moneyOut = outList.stream().mapToLong(x -> (long) (FinanceUltils.getMoneyCalculate(x) - x.getPaid())).sum();
            long moneyIn = inList.stream().mapToLong(x -> (long) (FinanceUltils.getMoneyCalculate(x) - x.getPaid())).sum();
            if (moneyOut > moneyIn) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PAYMENT_NEGATIVE);
            }
            OrderKidsPaymentRequest requestOut = new OrderKidsPaymentRequest();
            OrderKidsPaymentRequest requestIn = new OrderKidsPaymentRequest();
            modelMapper.map(request, requestOut);
            modelMapper.map(request, requestIn);
            //thanh toán khoản chi trước, tiền sẽ được cho vào ví
            double moneyPayOut = 0;
            if (CollectionUtils.isNotEmpty(outList)) {
                requestOut.setCategory(FinanceConstant.CATEGORY_OUT);
                moneyPayOut = outList.stream().mapToDouble(x -> (FinanceUltils.getMoneyCalculate(x) - x.getPaid())).sum();
                requestOut.setMoneyInput(moneyPayOut);
                requestOut.setWalletStatus(AppConstant.APP_TRUE);
                moneyPaymentAll -= this.paymentOrderWithCategory(principal, outList, requestOut, walletParent, fnOrderKids, showOrder);
            }
            double moneyInOutFinal = 0;
            double moneyPayInTotal = 0;

            String transferMoneyType = request.getTransferMoneyType();
            //thanh toán khoản thu
            if (CollectionUtils.isNotEmpty(inList)) {
                requestIn.setCategory(FinanceConstant.CATEGORY_IN);
                double moneyPayIn = request.getMoneyWallet() + moneyPayOut;
                requestIn.setMoneyWallet(moneyPayIn);
                if (transferMoneyType.equals(CycleMoneyConstant.TRANSFER_MONEY_MONTH)) {
                    //thanh toán đủ cho các khoản thu
                    double moneyPayInFull = inList.stream().mapToDouble(x -> (FinanceUltils.getMoneyCalculate(x) - x.getPaid())).sum();
                    double moneyForWallet = moneyPayInFull - requestIn.getMoneyWallet();
                    requestIn.setMoneyInput(moneyForWallet);
                    moneyPayInTotal = moneyPayInFull;

                    //tổng số tiền nhập vào tính cả khoản chi
                    double moneyTotalInput = request.getMoneyInput() + request.getMoneyWallet() + moneyPayOut;
                    //hiệu của số tiền nhập vào với số tiền phải thanh toán
                    moneyInOutFinal = moneyTotalInput - moneyPayInFull;
                }
                moneyPaymentAll += this.paymentOrderWithCategory(principal, inList, requestIn, walletParent, fnOrderKids, showOrder);
            }

            if (transferMoneyType.equals(CycleMoneyConstant.TRANSFER_MONEY_MONTH)) {
                double moneyPayInOut = moneyPayInTotal - moneyPayOut;
                if (moneyPaymentAll != moneyPayInOut) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số tiền thanh toán không khớp");
                }
                FnKidsPackage fnKidsPackageCreateNew = this.transferMoneyRedundant(transferMoneyType, moneyInOutFinal, monthYearDate, fnOrderKids, walletParent);
                if (Objects.nonNull(fnKidsPackageCreateNew)) {
                    //tạo ra lịch sử cho các khoản truy thu được tạo tự động
                    double moneyRedundantAbs = Math.abs(moneyInOutFinal);
                    String categoryCreate = fnKidsPackageCreateNew.getFnPackage().getCategory();
                    Double payCash = 0d;
                    Double payTransfer = 0d;
                    if (request.getPaymentType().equals(FinanceConstant.PAYMENT_BOTH)) {
                        //thanh toán thiếu
                        if (moneyInOutFinal < 0) {
                            payCash = moneyRedundantAbs;
                        } else {
                            //thanh toán thừa
                            if (request.getMoneyCash() >= request.getMoneyTransfer()) {
                                payCash = moneyRedundantAbs;
                            } else {
                                payTransfer = moneyRedundantAbs;
                            }
                        }
                    }
                    CashBookHistory cashBookHistory = cashBookHistoryService.saveCashBookHistory(idSchool, categoryCreate, FinanceConstant.CASH_BOOK_KID, request.getDate(), moneyRedundantAbs, FinanceConstant.CASH_BOOK_ORIGIN_KID_PAYMENT, null);
                    OrderKidsHistory orderKidsHistory = orderKidsHistoryService.saveOrderKidsHistory(categoryCreate, "Phụ huynh học sinh", request.getDate(), moneyRedundantAbs, 0, "", cashBookHistory, fnOrderKids, request.getPaymentType(), payCash, payTransfer);
                    ExOrderHistoryKidsPackage exOrderHistoryKidsPackage = new ExOrderHistoryKidsPackage();
                    exOrderHistoryKidsPackage.setMoney(moneyRedundantAbs);
                    exOrderHistoryKidsPackage.setFnKidsPackage(fnKidsPackageCreateNew);
                    exOrderHistoryKidsPackage.setOrderKidsHistory(orderKidsHistory);
                    exOrderHistoryKidsPackageRepository.save(exOrderHistoryKidsPackage);
                }
            }
        } else {
            //trường hợp thanh toán thu, chi riêng
            moneyPaymentAll += this.paymentOrderWithCategory(principal, fnKidsPackageList, request, walletParent, fnOrderKids, showOrder);
        }
        //send firebase and sms
        this.sendFirebaseAndSmsOrderPayment(principal, kids, request.getCategory(), (long) moneyPaymentAll, fnOrderKids.getCode(), idSchool);
        return true;
    }

    /**
     * @param moneyRedundant = tổng số tiền nhập vào - tổng số tiền các khoản phải thanh toán: x>0 thanh toán thừa tiền, x<0 thanh toán thiếu tiền
     * @param date
     * @param fnOrderKids
     * @param walletParent
     */
    private FnKidsPackage transferMoneyRedundant(String transferMoneyType, double moneyRedundant, LocalDate date, FnOrderKids fnOrderKids, WalletParent walletParent) {
        if (transferMoneyType.equals(CycleMoneyConstant.TRANSFER_MONEY_MONTH)) {
            Long idSchool = SchoolUtils.getIdSchool();
            double moneyRedundantAbs = Math.abs(moneyRedundant);
            Kids kids = fnOrderKids.getKids();
            if (moneyRedundant != 0) {
                //thanh toán thiếu
                if (moneyRedundant < 0) {
                    //tạo ra 1 phiếu thu cho tháng tiếp theo
                    LocalDate date3 = date.plusMonths(1);
                    FnPackage fnPackage3 = fnPackageRepository.findByIdSchoolAndRootNumberAndRootStatusTrueAndDelActiveTrue(idSchool, 3).orElseThrow();
                    fnKidsPackageService.createKidsPackageRootFromPackage(kids, fnPackage3, date3.getMonthValue(), date3.getYear(), moneyRedundantAbs, 0);

                    //tạo ra 1 phiếu chi cho tháng hiện tại
                    FnPackage fnPackage1 = fnPackageRepository.findByIdSchoolAndRootNumberAndRootStatusTrueAndDelActiveTrue(idSchool, 1).orElseThrow();
                    return fnKidsPackageService.createKidsPackageRootFromPackage(kids, fnPackage1, date.getMonthValue(), date.getYear(), moneyRedundantAbs, moneyRedundantAbs);
                } else {
                    //tạo ra 1 phiếu chi cho tháng tiếp theo
                    LocalDate date4 = date.plusMonths(1);
                    FnPackage fnPackage4 = fnPackageRepository.findByIdSchoolAndRootNumberAndRootStatusTrueAndDelActiveTrue(idSchool, 4).orElseThrow();
                    fnKidsPackageService.createKidsPackageRootFromPackage(kids, fnPackage4, date4.getMonthValue(), date4.getYear(), moneyRedundantAbs, 0);

                    //tạo ra 1 phiếu thu cho tháng hiện tại
                    FnPackage fnPackage2 = fnPackageRepository.findByIdSchoolAndRootNumberAndRootStatusTrueAndDelActiveTrue(idSchool, 2).orElseThrow();
                    return fnKidsPackageService.createKidsPackageRootFromPackage(kids, fnPackage2, date.getMonthValue(), date.getYear(), moneyRedundantAbs, moneyRedundantAbs);
                }
            }
        }
        return null;
    }

    private void sendFirebaseAndSmsOrderPayment(UserPrincipal principal, Kids kids, String category, long money, String orderCode, Long idSchool) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(19L).orElseThrow();
        String title = webSystemTitle.getTitle();
        String categoryType = category.equals(FinanceConstant.CATEGORY_OUT) ? "nhận" : "đóng";
        String content = webSystemTitle.getContent().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()).replace("{in_out_fees}", categoryType).replace("{money}", FinanceUltils.formatMoney(money)).replace("{order_code}", orderCode);
        School school = schoolRepository.findById(idSchool).orElseThrow();
        String contentFirebase = content;
        String extendContent = school.getSchoolInfo().getBankInfo();
        if (StringUtils.isNotBlank(extendContent)) {
            contentFirebase = content.concat("\n").concat(extendContent);
        }
        appSendService.saveToAppSendParent(principal, kids, title, contentFirebase, AppSendConstant.TYPE_FINANCE);
        //gửi firebase
        firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, contentFirebase, idSchool, FirebaseConstant.CATEGORY_ORDER_PAYMENT);
        //gửi Sms
        if (webSystemTitle.isSms()) {
            SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
            smsNotifyDataRequest.setSendContent(content);
            smsDataService.sendSmsKid(Collections.singletonList(kids), kids.getIdSchool(), smsNotifyDataRequest);
        }
    }

    private void sendFirebaseOrderView(UserPrincipal principal, FnOrderKids fnOrderKids) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        Long idSchool = principal.getIdSchoolLogin();
        Kids kids = fnOrderKids.getKids();
        LocalDate date = LocalDate.of(fnOrderKids.getYear(), fnOrderKids.getMonth(), 1);
        List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(kids, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
//        long money = this.getMoneyTotalInMinusOut(fnKidsPackageList);
//        String inOutString = money >= 0 ? "phải đóng" : "được nhận";
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(18L).orElseThrow();
        School school = schoolRepository.findById(idSchool).orElseThrow();
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
        String totalMoneyString = orderMoneyModel.getMoneyTotalInOut() >= 0 ? "phải đóng" : "được nhận";
        String totalRemainString = orderMoneyModel.getMoneyTotalInOutRemain() >= 0 ? "phải đóng" : "được nhận";

        String title = webSystemTitle.getTitle();
        String content = webSystemTitle.getContent().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()).replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderKids.getMonth(), fnOrderKids.getYear())).replace("{in_out_fees}", totalMoneyString).replace("{money}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOut())))
                .replace("{in_out_pay}", totalRemainString).replace("{money_pay}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOutRemain())));
        String contentFirebase = content;
        String extendContent = school.getSchoolInfo().getFeesParent();
        if (StringUtils.isNotBlank(extendContent)) {
            contentFirebase = content.concat("\n").concat(extendContent);
        }
        appSendService.saveToAppSendParent(principal, kids, title, contentFirebase, AppSendConstant.TYPE_FINANCE);
        firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, contentFirebase, idSchool, FirebaseConstant.CATEGORY_ORDER_SHOW);
        //gửi Sms
        if (webSystemTitle.isSms()) {
            SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
            smsNotifyDataRequest.setSendContent(content);
            smsDataService.sendSmsKid(Collections.singletonList(kids), kids.getIdSchool(), smsNotifyDataRequest);
        }
    }

    /*
    làm y hệt click hiện thị
     */
    private void sendFirebaseOrderViewNoSMS(UserPrincipal principal, FnOrderKids fnOrderKids) throws FirebaseMessagingException {
        Long idSchool = principal.getIdSchoolLogin();
        Kids kids = fnOrderKids.getKids();
        LocalDate date = LocalDate.of(fnOrderKids.getYear(), fnOrderKids.getMonth(), 1);
        List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(kids, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
//        long money = this.getMoneyTotalInMinusOut(fnKidsPackageList);
//        String inOutString = money >= 0 ? "phải đóng" : "được nhận";
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(18L).orElseThrow();
        School school = schoolRepository.findById(idSchool).orElseThrow();
        OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
        String totalMoneyString = orderMoneyModel.getMoneyTotalInOut() >= 0 ? "phải đóng" : "được nhận";
        String totalRemainString = orderMoneyModel.getMoneyTotalInOutRemain() >= 0 ? "phải đóng" : "được nhận";
        String title = webSystemTitle.getTitle();
        String content = webSystemTitle.getContent().replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()).replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderKids.getMonth(), fnOrderKids.getYear())).replace("{in_out_fees}", totalMoneyString).replace("{money}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOut())))
                .replace("{in_out_pay}", totalRemainString).replace("{money_pay}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOutRemain())));
        ;
        String contentFirebase = content;
        String extendContent = school.getSchoolInfo().getFeesParent();
        if (StringUtils.isNotBlank(extendContent)) {
            contentFirebase = content.concat("\n").concat(extendContent);
        }
        appSendService.saveToAppSendParent(principal, kids, title, contentFirebase, AppSendConstant.TYPE_FINANCE);
        firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, contentFirebase, idSchool, FirebaseConstant.CATEGORY_ORDER_SHOW);
    }

    private double paymentOrderWithCategory(UserPrincipal principal, List<FnKidsPackage> fnKidsPackageList, OrderKidsPaymentRequest request, WalletParent walletParent, FnOrderKids fnOrderKids, boolean orderLocked) {
        Long idSchool = principal.getIdSchoolLogin();
        Long idUser = principal.getId();
        String name = request.getName();
        LocalDate date = request.getDate();
        String category = request.getCategory();
        if (category.equals(FinanceConstant.CATEGORY_IN)) {
            //check số tiền nhập lấy từ ví so với số dư ví
            double balanceWallet = walletParent.getMoneyIn() - walletParent.getMoneyOut();
            if (request.getMoneyWallet() > balanceWallet) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.WALLET_NOT_ENOUGH);
            }
        }
        /**
         *  (1) create cashbook history với money=0
         */
        CashBookHistory cashBookHistory = cashBookHistoryService.saveCashBookHistory(idSchool, category, FinanceConstant.CASH_BOOK_KID, date, 0, FinanceConstant.CASH_BOOK_ORIGIN_KID_PAYMENT, null);
        /**
         * (2) create order_kids_history
         */
        OrderKidsHistory orderKidsHistory = orderKidsHistoryService.saveOrderKidsHistory(category, name, date, request.getMoneyInput(), request.getMoneyWallet(), request.getDescription(), cashBookHistory, fnOrderKids, request.getPaymentType(), request.getMoneyCash(), request.getMoneyTransfer());
        if (category.equals(FinanceConstant.CATEGORY_IN)) {
            /**
             *  (3) nạp số tiền mặt vào lịch sử ví và cập nhật lại số tiền trong ví
             */
            walletParentHistoryService.saveMoneyWalletHistory(idUser, name, date, fnOrderKids.getCode(), FinanceConstant.CATEGORY_IN, request.getMoneyInput(), walletParent, orderKidsHistory);
        }

        double moneyInputTotal = category.equals(FinanceConstant.CATEGORY_IN) ? request.getMoneyInput() + request.getMoneyWallet() : request.getMoneyInput();
        double moneyInputTotalCheck = moneyInputTotal;
        //số tiền thanh toán trong mỗi lần thanh toán
        double moneyPaymentTotal = 0;
        /**
         * (4) tạo bảng ex_order_kids_package và update paid trong Kids_Package
         */
        for (FnKidsPackage x : fnKidsPackageList) {
            //số tiền còn thiếu của khoản thu
            double moneyRemain = FinanceUltils.getMoneyCalculate(x) - x.getPaid();
            //số tiền thanh toán cho khoản thu
            double moneyPayment = Math.min(moneyInputTotal, moneyRemain);
            if (moneyPayment == 0) {
                break;
            }
            ExOrderHistoryKidsPackage exOrderHistoryKidsPackage = new ExOrderHistoryKidsPackage();
            exOrderHistoryKidsPackage.setMoney(moneyPayment);
            exOrderHistoryKidsPackage.setFnKidsPackage(x);
            exOrderHistoryKidsPackage.setOrderKidsHistory(orderKidsHistory);
            //create ex_order_history_kids_package
            exOrderHistoryKidsPackageRepository.save(exOrderHistoryKidsPackage);
            moneyInputTotal = moneyInputTotal - moneyPayment;
            moneyPaymentTotal = moneyPaymentTotal + moneyPayment;
            x.setPaid(x.getPaid() + moneyPayment);
            //khi thanh toán đủ, khoản thu auto khóa
            if (moneyPayment == moneyRemain) {
                if (orderLocked) {
                    x.setLocked(AppConstant.APP_TRUE);
                    x.setIdLocked(idUser);
                    x.setTimeLocked(LocalDateTime.now());
                }
            }
            //update kids_package
            fnKidsPackageRepository.save(x);
        }
        /**
         * (5) cập nhật lại tiền trong walletparenthistory và walletparent
         */
        if (category.equals(FinanceConstant.CATEGORY_IN)) {
            walletParentHistoryService.saveMoneyWalletHistory(idUser, name, date, fnOrderKids.getCode(), FinanceConstant.CATEGORY_OUT, moneyPaymentTotal, walletParent, orderKidsHistory);
        } else if (category.equals(FinanceConstant.CATEGORY_OUT)) {
            //check số tiền nhập vào với số tiền phải thanh toán đối với khoản chi
            if (moneyInputTotalCheck > moneyPaymentTotal) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.MONEY_INPUT_INVALID);
            }
            //chọn chi tiền vào ví
            if (request.isWalletStatus()) {
                walletParentHistoryService.saveMoneyWalletHistory(idUser, name, date, fnOrderKids.getCode(), FinanceConstant.CATEGORY_IN, moneyPaymentTotal, walletParent, orderKidsHistory);
            }
        }

        /**
         * (6) update money trong fn_cash_book_history update cash_book
         */
        cashBookHistory.setMoney(moneyPaymentTotal);
        cashBookHistoryService.updateCashBookHistory(cashBookHistory);

        /**
         * (7) update lastModifiedDate in fnOrderKids
         */
        fnOrderKids.setTimePayment(LocalDateTime.now());
        fnOrderKidsRepository.save(fnOrderKids);
        return moneyPaymentTotal;
    }

    private void checkOrderTimeEdit(FnOrderKids fnOrderKids, LocalDateTime dateTime) {
        //check thời gian chỉnh sửa hóa đơn
        if (!dateTime.isEqual(fnOrderKids.getTimePayment())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ORDER_MODIFIED);
        }
    }

    /**
     * kiểm tra thông tin trước khi thanh toán
     *
     * @param request
     */
    private void checkInputInfo(OrderKidsPaymentRequest request) {
        if (request.getMoneyInput() + request.getMoneyWallet() <= 0) {
            if (request.getCategory().equals(FinanceConstant.CATEGOTY_BOTH) && request.getTransferMoneyType().equals(CycleMoneyConstant.TRANSFER_MONEY_MONTH)) {
                //no check money input
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.PAYMENT_ZERO);
            }
        }
        if (FinanceConstant.PAYMENT_BOTH.equals(request.getPaymentType())) {
            double moneyFirst = request.getMoneyInput() + request.getMoneyWallet();
            double moneySecond = request.getMoneyCash() + request.getMoneyTransfer();
            if (moneyFirst != moneySecond) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tổng tiền nhập ở TM & CK khác số tiền thanh toán");
            }
        }
    }

    private String getCodeOrder(LocalDate date) {
        String month = date.getMonthValue() < 10 ? "0" + date.getMonthValue() : String.valueOf(date.getMonthValue());
        String year = String.valueOf(date.getYear());
        String code = "K".concat(month).concat(year).concat("-");
        String index;
        Optional<FnOrderKids> orderKidsOptional = fnOrderKidsRepository.findFirstByOrderByIdDesc();
        if (orderKidsOptional.isEmpty()) {
            index = "1";
        } else {
            String codeBefore = orderKidsOptional.get().getCode();
            int indexBefore = Integer.parseInt(codeBefore.substring(codeBefore.lastIndexOf("-") + 1));
            index = String.valueOf(indexBefore + 1);
        }
        return code.concat(index);
    }

    private void setProperties(Long idKid, int month, int year, OrderKidsCustom1 orderKidsCustom1) {
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idKid, month, year);
        if (CollectionUtils.isNotEmpty(fnKidsPackageList)) {
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
            modelMapper.map(orderNumberModel, orderKidsCustom1);
            this.setTotalMoney(fnKidsPackageList, orderKidsCustom1);
        }
    }

    private void setTotalMoney(List<FnKidsPackage> fnKidsPackageList, OrderKidsCustom1 orderKidsCustom1) {
        List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
        List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
        long totalMoneyIn = inList.stream().mapToLong(FinanceUltils::getMoneyCalculate).sum();
        long totalPaidIn = inList.stream().mapToLong(x -> (long) x.getPaid()).sum();
        long totalMoneyOut = outList.stream().mapToLong(FinanceUltils::getMoneyCalculate).sum();
        long totalPaidOut = outList.stream().mapToLong(x -> (long) x.getPaid()).sum();
        long totalRemain = (totalMoneyIn - totalPaidIn) - (totalMoneyOut - totalPaidOut);

        if (totalRemain > 0) {
            orderKidsCustom1.setTotalMoneyRemainIn(totalRemain);
        } else {
            orderKidsCustom1.setTotalMoneyRemainOut(Math.abs(totalRemain));
        }
        orderKidsCustom1.setMoneyTotalIn(totalMoneyIn);
        orderKidsCustom1.setMoneyPaidIn(totalPaidIn);
        orderKidsCustom1.setMoneyTotalOut(totalMoneyOut);
        orderKidsCustom1.setMoneyPaidOut(totalPaidOut);
    }

    private void checkLocked(FnOrderKids fnOrderKids) {
        if (fnOrderKids.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ORDER_LOCKED);
        }
    }

    private void setView(UserPrincipal principal, FnOrderKids fnOrderKids, boolean status, Long idUser, LocalDateTime time) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        this.checkLocked(fnOrderKids);
        if (fnOrderKids.isView() != status) {
            boolean sendFirebase = status && fnOrderKids.getIdView() == null;
            fnOrderKids.setView(status);
            fnOrderKids.setIdView(idUser);
            fnOrderKids.setTimeView(time);
            fnOrderKidsRepository.save(fnOrderKids);
            if (sendFirebase) {
                this.sendFirebaseOrderView(principal, fnOrderKids);
            }
        }
    }

    private void setLocked(FnOrderKids fnOrderKids, boolean status, Long idUser, LocalDateTime time) {
        if (fnOrderKids.isLocked() != status) {
            fnOrderKids.setLocked(status);
            fnOrderKids.setIdLocked(idUser);
            fnOrderKids.setTimeLock(time);
            fnOrderKidsRepository.save(fnOrderKids);
        }
    }

    private void creatOrderKids(Kids kids, LocalDate date, String generateType) {
        int month = date.getMonthValue();
        int year = date.getYear();
        Optional<FnOrderKids> fnOrderKidsOptional = fnOrderKidsRepository.findByKidsIdAndMonthAndYear(kids.getId(), month, year);
        if (fnOrderKidsOptional.isEmpty()) {
            FnOrderKids fnOrderKids = new FnOrderKids();
            fnOrderKids.setKids(kids);
            fnOrderKids.setCode(this.getCodeOrder(date));
            fnOrderKids.setMonth(month);
            fnOrderKids.setYear(year);
            if (FinanceConstant.GENERATE_AUTO.equals(generateType)) {
                fnOrderKids.setIdCreated(1L);
                fnOrderKids.setCreatedDate(LocalDateTime.now());
                fnOrderKids.setIdModified(1L);
                fnOrderKids.setLastModifieDate(LocalDateTime.now());
            }
            fnOrderKidsRepository.save(fnOrderKids);
        }
    }

    /**
     * lấy tổng thu trừ tổng chi
     *
     * @param fnKidsPackageList
     * @return
     */
    private long getMoneyTotalInMinusOut(List<FnKidsPackage> fnKidsPackageList) {
        List<FnKidsPackage> inList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
        List<FnKidsPackage> outList = fnKidsPackageList.stream().filter(x -> x.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
        long moneyTotalIn = inList.stream().mapToLong(FinanceUltils::getMoneyCalculate).sum();
        long moneyTotalOut = outList.stream().mapToLong(FinanceUltils::getMoneyCalculate).sum();
        return moneyTotalIn - moneyTotalOut;
    }
}
