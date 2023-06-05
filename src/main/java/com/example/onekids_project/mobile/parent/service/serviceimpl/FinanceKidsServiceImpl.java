package com.example.onekids_project.mobile.parent.service.serviceimpl;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.finance.fees.ExOrderHistoryKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.school.SchoolInfo;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.parent.request.finance.KidsPackageParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.OrderKidsParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.WalletParentHistoryCreateParentRequest;
import com.example.onekids_project.mobile.parent.request.finance.WalletParentHistoryParentRequest;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageDetailParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.kidspackage.KidsPackageWrapperParentResponse;
import com.example.onekids_project.mobile.parent.response.finance.order.*;
import com.example.onekids_project.mobile.parent.response.finance.walletparent.*;
import com.example.onekids_project.mobile.parent.service.servicecustom.FinanceKidsService;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.common.InforRepresentationResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.util.*;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * date 2021-03-16 09:24
 *
 * @author lavanviet
 */
@Service
public class FinanceKidsServiceImpl implements FinanceKidsService {

    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;

    @Autowired
    private FnKidsPackageRepository fnKidsPackageRepository;

    @Autowired
    private OrderKidsHistoryRepository orderKidsHistoryRepository;

    @Autowired
    private WalletParentRepository walletParentRepository;

    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private FnBankRepository fnBankRepository;

    @Autowired
    private SchoolInfoRepository schoolInfoRepository;

    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private AppSendService appSendService;

    @Override
    public List<OrderKidsParentResponse> searchOrderKids(UserPrincipal principal, Long idKid, OrderKidsParentRequest request) {
        List<OrderKidsParentResponse> responseList = new ArrayList<>();
        String paidStatus = request.getPaidStatus();
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.searchOrderKidsYearParent(idKid, request.getYear());
        if (principal.getAppType().equals(AppTypeConstant.PARENT)) {
            fnOrderKidsList = fnOrderKidsList.stream().filter(FnOrderKids::isView).collect(Collectors.toList());
        }
        fnOrderKidsList.forEach(x -> {
            List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idKid, x.getMonth(), x.getYear());
            OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
            if (StringUtils.isEmpty(paidStatus)) {
                responseList.add(this.getOrderKids(x, fnKidsPackageList));
            } else if (FinanceMobileConstant.PAID_EMPTY.equals(paidStatus)) {
                if (orderNumberModel.getTotalNumber() == 0) {
                    responseList.add(this.getOrderKids(x, fnKidsPackageList));
                }
            } else if (FinanceMobileConstant.PAID_PART.equals(paidStatus)) {
                if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getTotalNumber() > orderNumberModel.getEnoughNumber()) {
                    responseList.add(this.getOrderKids(x, fnKidsPackageList));
                }
            } else if (FinanceMobileConstant.PAID_FULL.equals(paidStatus)) {
                if (orderNumberModel.getTotalNumber() > 0 && orderNumberModel.getTotalNumber() == orderNumberModel.getEnoughNumber()) {
                    responseList.add(this.getOrderKids(x, fnKidsPackageList));
                }
            }
        });
        return responseList;
    }

    @Override
    public List<OrderKidsHistoryParentResponse> searchOrderKidsHistory(Long idOrderKids) {
        List<OrderKidsHistory> orderKidsHistoryList = orderKidsHistoryRepository.findByFnOrderKidsIdOrderByIdDesc(idOrderKids);
        List<OrderKidsHistoryParentResponse> responseList = new ArrayList<>();
        orderKidsHistoryList.forEach(x -> {
            OrderKidsHistoryParentResponse model = new OrderKidsHistoryParentResponse();
            model.setDate(ConvertData.fomaterLocalDate(x.getDate()));
            model.setCategory(x.getCategory());
            double money = x.getExOrderHistoryKidsPackageList().stream().mapToDouble(ExOrderHistoryKidsPackage::getMoney).sum();
            model.setMoney((long) money);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public OrderKidsStatisticalResponse statisticalOrderKids(Long idKid) {
        OrderKidsStatisticalResponse response = new OrderKidsStatisticalResponse();
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.findOrderKidsParent(idKid);
        long moneyInTotal = 0;
        long moneyPaidInTotal = 0;
        long moneyOutTotal = 0;
        long moneyPaidOutTotal = 0;
        for (FnOrderKids x : fnOrderKidsList) {
            List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idKid, x.getMonth(), x.getYear());
            List<FnKidsPackage> fnKidsPackageInList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_IN)).collect(Collectors.toList());
            List<FnKidsPackage> fnKidsPackageOutList = fnKidsPackageList.stream().filter(a -> a.getFnPackage().getCategory().equals(FinanceConstant.CATEGORY_OUT)).collect(Collectors.toList());
            long moneyIn = fnKidsPackageInList.stream().mapToLong(FinanceUltils::getMoneyCalculate).sum();
            long moneyPaidIn = fnKidsPackageInList.stream().mapToLong(a -> (long) a.getPaid()).sum();
            long moneyOut = fnKidsPackageOutList.stream().mapToLong(FinanceUltils::getMoneyCalculate).sum();
            long moneyPaidOut = fnKidsPackageOutList.stream().mapToLong(a -> (long) a.getPaid()).sum();
            moneyInTotal += moneyIn;
            moneyPaidInTotal += moneyPaidIn;
            moneyOutTotal += moneyOut;
            moneyPaidOutTotal += moneyPaidOut;
        }
        response.setMoneyInTotal(moneyInTotal);
        response.setMoneyPaidInTotal(moneyPaidInTotal);
        response.setMoneyRemainInTotal(moneyInTotal - moneyPaidInTotal);
        response.setMoneyOutTotal(moneyOutTotal);
        response.setMoneyPaidOutTotal(moneyPaidOutTotal);
        response.setMoneyRemainOutTotal(moneyOutTotal - moneyPaidOutTotal);
        return response;
    }

    @Override
    public OrderKidsPackageParentResponse findOrderKidsPackage(Long idKidsPackage) {
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(idKidsPackage).orElseThrow();
        OrderKidsPackageParentResponse response = new OrderKidsPackageParentResponse();
        response.setName(fnKidsPackage.getFnPackage().getName());
        response.setCategory(fnKidsPackage.getFnPackage().getCategory());
        response.setUnit(fnKidsPackage.getFnPackage().getUnit());
        response.setPrice((long) fnKidsPackage.getPrice());
        response.setDiscountType(fnKidsPackage.getDiscountType());
        response.setDiscountNumber(fnKidsPackage.isDiscount() ? (long) fnKidsPackage.getDiscountNumber() : 0);
        response.setDiscountPrice((long) (fnKidsPackage.isDiscount() ? fnKidsPackage.getDiscountPrice() : fnKidsPackage.getPrice()));
        response.setUseNumber(fnKidsPackage.getUsedNumber());
        response.setMoney(FinanceUltils.getMoneyCalculate(fnKidsPackage));
        response.setMoneyPaid((long) fnKidsPackage.getPaid());
        response.setMoneyRemain(response.getMoney() - response.getMoneyPaid());
        response.setPaidStatus(this.getPaidStatus(response.getMoneyPaid(), response.getMoneyRemain()));
        return response;
    }

    @Override
    public List<KidsPackageWrapperParentResponse> searchKidsPackageYear(UserPrincipal principal, Long idKid, KidsPackageParentRequest request) {
        List<KidsPackageWrapperParentResponse> responseList = new ArrayList<>();
        if (principal.getAppType().equals(AppTypeConstant.PARENT) && !principal.getSchoolConfig().isAutoShowFeesFutureKids()) {
            return responseList;
        }
        List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.searchKidsPackageParent(idKid, request.getYear());
        List<Integer> monthList = fnKidsPackageList.stream().map(FnKidsPackage::getMonth).distinct().collect(Collectors.toList());
        String discountStatus = request.getDiscountStatus();
        int year = request.getYear();
        monthList.forEach(x -> {
            KidsPackageWrapperParentResponse response = new KidsPackageWrapperParentResponse();
            List<KidsPackageParentResponse> dataList = new ArrayList<>();
            response.setMonth(FinanceMobileConstant.MONTH_STRING + ConvertData.formatMonthAndYear(x, year));
            List<FnKidsPackage> fnKidsPackageMonthList = fnKidsPackageList.stream().filter(a -> a.getMonth() == x).collect(Collectors.toList());
            if (StringUtils.isNotBlank(discountStatus)) {
                if (discountStatus.equals(FinanceMobileConstant.DISCOUNT_YES)) {
                    fnKidsPackageMonthList = fnKidsPackageMonthList.stream().filter(FnKidsPackage::isDiscount).collect(Collectors.toList());
                } else if (discountStatus.equals(FinanceMobileConstant.DISCOUNT_NO)) {
                    fnKidsPackageMonthList = fnKidsPackageMonthList.stream().filter(b -> !b.isDiscount()).collect(Collectors.toList());
                }
            }
            fnKidsPackageMonthList.forEach(y -> {
                KidsPackageParentResponse model = new KidsPackageParentResponse();
                model.setId(y.getId());
                model.setName(y.getFnPackage().getName());
                model.setCategory(y.getFnPackage().getCategory());
                model.setUnit(y.getFnPackage().getUnit());
                model.setPrice((long) y.getPrice());
                model.setDiscountNumber(y.isDiscount() ? (long) y.getDiscountNumber() : 0);
                model.setDiscountType(y.getDiscountType());
                dataList.add(model);
            });
            response.setDataList(dataList);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public KidsPackageDetailParentResponse getKidsPackageById(Long idKidsPackage) {
        KidsPackageDetailParentResponse response = new KidsPackageDetailParentResponse();
        FnKidsPackage fnKidsPackage = fnKidsPackageRepository.findByIdAndDelActiveTrue(idKidsPackage).orElseThrow();
        response.setName(fnKidsPackage.getFnPackage().getName());
        response.setCategory(fnKidsPackage.getFnPackage().getCategory());
        response.setUnit(fnKidsPackage.getFnPackage().getUnit());
        response.setNumber(fnKidsPackage.getNumber());
        response.setPrice((long) fnKidsPackage.getPrice());
        response.setDiscountNumber(fnKidsPackage.isDiscount() ? (long) fnKidsPackage.getDiscountNumber() : 0);
        response.setDiscountPrice(fnKidsPackage.isDiscount() ? (long) fnKidsPackage.getDiscountPrice() : (long) fnKidsPackage.getPrice());
        response.setDiscountType(fnKidsPackage.getDiscountType());
        response.setNote(response.getCategory().equals(FinanceConstant.CATEGORY_IN) ? FinanceMobileConstant.CATEGORY_STRING_IN : FinanceMobileConstant.CATEGORY_STRING_OUT);
        response.setExpireDate(fnKidsPackage.isExpired() ? ConvertData.fomaterLocalDate(FinanceUltils.getExpireDate(fnKidsPackage)) : "");
        return response;
    }

    @Override
    public List<WalletParentHistoryWrapperParentResponse> searchWalletParentHistoryYear(UserPrincipal principal, WalletParentHistoryParentRequest request) {
        CommonValidate.checkDataParent(principal);
        List<WalletParentHistoryWrapperParentResponse> responseList = new ArrayList<>();
        //cấu hình mục 61 ko cho phép truy cập
        if (!principal.getSchoolConfig().isShowWalletParent()) {
            return responseList;
        }
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow();
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        List<WalletParentHistory> walletParentHistoryList = walletParentHistoryRepository.searchWalletParentHistory(walletParent.getId(), request.getYear(), request.getDescription());
        List<Integer> monthList = walletParentHistoryList.stream().map(x -> x.getDate().getMonthValue()).distinct().collect(Collectors.toList());
        int year = request.getYear();
        monthList.forEach(x -> {
            WalletParentHistoryWrapperParentResponse response = new WalletParentHistoryWrapperParentResponse();
            List<WalletParentHistoryParentResponse> dataList = new ArrayList<>();
            response.setMonth(FinanceMobileConstant.MONTH_STRING + ConvertData.formatMonthAndYear(x, year));
            List<WalletParentHistory> walletParentHistoryMonthList = walletParentHistoryList.stream().filter(a -> a.getDate().getMonthValue() == x).collect(Collectors.toList());
            walletParentHistoryMonthList.forEach(y -> {
                WalletParentHistoryParentResponse model = new WalletParentHistoryParentResponse();
                model.setId(y.getId());
                model.setDate(ConvertData.fomaterLocalDate(y.getDate()));
                model.setCategory(y.getCategory());
                model.setMoney((long) y.getMoney());
                model.setDescription(y.getDescription());
                model.setPictureLink(StringUtils.isNotBlank(y.getPicture()) ? y.getPicture() : "");
                if (y.getType().equals(FinanceConstant.WALLET_TYPE_PARENT) && y.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
                    model.setStatus(y.isConfirm());
                }
                if (y.getType().equals(FinanceConstant.WALLET_TYPE_SCHOOL) && y.getCategory().equals(FinanceConstant.CATEGORY_OUT) && !y.isConfirm()) {
                    model.setConfirm(AppConstant.APP_TRUE);
                }
                dataList.add(model);
            });
            long moneyInTotal = (long) walletParentHistoryMonthList.stream().filter(a -> a.isConfirm() && a.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(WalletParentHistory::getMoney).sum();
            long moneyOutTotal = (long) walletParentHistoryMonthList.stream().filter(a -> a.isConfirm() && a.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(WalletParentHistory::getMoney).sum();
            response.setMoneyInTotal(moneyInTotal);
            response.setMoneyOutTotal(moneyOutTotal);
            response.setDataList(dataList);
            responseList.add(response);
        });
        return responseList;
    }

    @Transactional
    @Override
    public boolean confirmWalletParent(UserPrincipal principal, Long idWalletHistory) {
        CommonValidate.checkDataParent(principal);
        WalletParentHistory walletParentHistory = walletParentHistoryRepository.findById(idWalletHistory).orElseThrow();
        if (walletParentHistory.getType().equals(FinanceConstant.WALLET_TYPE_SCHOOL) && walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_OUT) && !walletParentHistory.isConfirm()) {
            WalletParent walletParent = walletParentHistory.getWalletParent();
            FinanceUltils.checkBalanceWalletParent(walletParent, walletParentHistory.getMoney());
            walletParentHistory.setConfirm(AppConstant.APP_TRUE);
            walletParentHistory.setIdConfirm(principal.getId());
            walletParentHistory.setTimeConfirm(LocalDateTime.now());
            walletParent.setMoneyOut(walletParent.getMoneyOut() + walletParentHistory.getMoney());
            walletParentRepository.save(walletParent);
            walletParentHistoryRepository.save(walletParentHistory);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CONFIRM_FAIL);
        }
        return true;
    }

    @Override
    public WalletParentParentResponse getWalletParent(UserPrincipal principal) {
        CommonValidate.checkDataParent(principal);
        WalletParentParentResponse response = new WalletParentParentResponse();
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow();
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        InforRepresentationResponse inforRepresentationResponse = ConvertData.getInforRepresent(kids);
        response.setAvatar(AvatarUtils.getAvatarParentFromObject(kids.getParent()));
        response.setFullName(StringUtils.isNotBlank(inforRepresentationResponse.getFullName()) ? inforRepresentationResponse.getFullName() : "");
        response.setPhone(StringUtils.isNotBlank(inforRepresentationResponse.getPhone()) ? inforRepresentationResponse.getPhone() : "");
        response.setAddress(kids.getAddress());
        response.setCode(walletParent.getCode());
        response.setMoneyIn((long) walletParent.getMoneyIn());
        response.setMoneyOut((long) walletParent.getMoneyOut());
        response.setMoneyRemain(response.getMoneyIn() - response.getMoneyOut());
        return response;
    }

    @Override
    public ListBankParentResponse findBankInSchool(UserPrincipal principal) {
        CommonValidate.checkDataParent(principal);
        ListBankParentResponse response = new ListBankParentResponse();
        List<FnBank> fnBankList = fnBankRepository.findBySchoolIdAndDelActiveTrue(principal.getIdSchoolLogin());
        List<BankParentResponse> dataList = new ArrayList<>();
        fnBankList.forEach(x -> {
            BankParentResponse model = new BankParentResponse();
            model.setId(x.getId());
            model.setContent(x.getFullName() + " - " + x.getAccountNumber() + " - " + x.getBankName());
            dataList.add(model);
        });
        SchoolInfo schoolInfo = schoolInfoRepository.findBySchoolId(principal.getIdSchoolLogin()).orElseThrow();
        response.setInformation(schoolInfo.getBankInfo());
        response.setDataList(dataList);
        return response;
    }

    @Transactional
    @Override
    public boolean createWalletParentHistory(UserPrincipal principal, WalletParentHistoryCreateParentRequest request) throws IOException, FirebaseMessagingException {
        CommonValidate.checkDataParent(principal);
        DateCommonUtils.checkDateWithCurrentDate(request.getDate());
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(principal.getIdKidLogin()).orElseThrow();
        WalletParentHistory walletParentHistory = new WalletParentHistory();
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        walletParentHistory.setWalletParent(walletParent);
        walletParentHistory.setType(FinanceConstant.WALLET_TYPE_PARENT);
        walletParentHistory.setCategory(FinanceConstant.CATEGORY_IN);
        walletParentHistory.setMoney(request.getMoney());
        walletParentHistory.setName(principal.getFullName());
        walletParentHistory.setDate(request.getDate() != null ? request.getDate() : LocalDate.now());
        walletParentHistory.setKids(kids);
        walletParentHistory.setDescription(StringUtils.isNotBlank(request.getDescription()) ? request.getDescription() : FinanceMobileConstant.WALLET_PARENT_DESCRIPTION);
        if (request.getIdBank() != null) {
            FnBank fnBank = fnBankRepository.findByIdAndDelActiveTrue(request.getIdBank()).orElseThrow();
            walletParentHistory.setFnBank(fnBank);
        }
        if (request.getMultipartFile() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getMultipartFile(), principal.getIdSchoolLogin(), UploadDownloadConstant.TAI_CHINH);
            walletParentHistory.setPicture(handleFileResponse.getUrlWeb());
            walletParentHistory.setPictureLocal(handleFileResponse.getUrlLocal());
        }
        walletParentHistoryRepository.save(walletParentHistory);
        //send firebase
        List<InfoEmployeeSchool> infoEmployeeSchoolList = UserPrincipleToUserUtils.getInfoEmployeeInSchoolHasAccount(kids.getIdSchool());
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findById(79L).orElseThrow();
        String title = webSystemTitle.getTitle();
        String content = webSystemTitle.getContent().replace("{kid_name}", kids.getFullName()).replace("{lop}", kids.getMaClass().getClassName()).replace("{money}", FinanceUltils.formatMoney(request.getMoney()));
        firebaseFunctionService.sendPlusCommon(infoEmployeeSchoolList, title, content, kids.getIdSchool(), FirebaseConstant.CATEGORY_NOTIFY);
        infoEmployeeSchoolList.forEach(x -> appSendService.saveToAppSendEmployee(principal, x, title, content, AppSendConstant.TYPE_FINANCE));
        return true;
    }

    @Override
    public StatisticalOrderKidsResponse statisticalNumber(UserPrincipal principal) {
        StatisticalOrderKidsResponse response = new StatisticalOrderKidsResponse();
        Long idKid = principal.getIdKidLogin();
        int year = LocalDate.now().getYear();
        response.setOrderNumber(this.getNumberOrderKidsNoCompleteYear(idKid, year));
        response.setWalletNumber(this.getNumberWalletNoConfirm(idKid, year));
        return response;
    }

    /**
     * lấy số hóa đơn chưa hoàn thành trong một năm
     *
     * @param idKid
     * @param year
     * @return
     */
    @Override
    public int getNumberOrderKidsNoCompleteYear(Long idKid, int year) {
        int number = 0;
        List<FnOrderKids> fnOrderKidsList = fnOrderKidsRepository.searchOrderKidsYearParent(idKid, year);
        for (FnOrderKids x : fnOrderKidsList) {
            List<FnKidsPackage> fnKidsPackageList = fnKidsPackageRepository.findByKidsIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(idKid, x.getMonth(), x.getYear());
            long count = fnKidsPackageList.stream().filter(a -> a.getPaid() < FinanceUltils.getMoneyCalculate(a)).count();
            if (count > 0) {
                number++;
            }
        }
        return number;
    }

    private int getNumberWalletNoConfirm(Long idKid, int year) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        List<WalletParentHistory> walletParentHistoryList = walletParentHistoryRepository.getWalletParentHistory(walletParent.getId(), year);
        return (int) walletParentHistoryList.stream().filter(x -> !x.isConfirm() && x.getType().equals(FinanceConstant.WALLET_TYPE_SCHOOL) && x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).count();
    }

    private String getPaidStatus(long moneyPaid, long moneyRemain) {
        if (moneyPaid == 0) {
            return FinanceMobileConstant.PAID_EMPTY;
        } else if (moneyRemain > 0) {
            return FinanceMobileConstant.PAID_PART;
        }
        return FinanceMobileConstant.PAID_FULL;
    }

    private OrderKidsParentResponse getOrderKids(FnOrderKids x, List<FnKidsPackage> fnKidsPackageList) {
        ListOrderKidsCustom dataIn = new ListOrderKidsCustom();
        ListOrderKidsCustom dataOut = new ListOrderKidsCustom();
        List<OrderKidsParentCustom1> inList = new ArrayList<>();
        List<OrderKidsParentCustom1> outList = new ArrayList<>();
        OrderKidsParentResponse response = new OrderKidsParentResponse();
        long moneyIn = 0;
        long moneyPaidIn = 0;
        long moneyRemainIn = 0;
        long moneyOut = 0;
        long moneyPaidOut = 0;
        long moneyRemainOut = 0;
        for (FnKidsPackage y : fnKidsPackageList) {
            OrderKidsParentCustom1 model = new OrderKidsParentCustom1();
            model.setId(y.getId());
            model.setName(y.getFnPackage().getName());
            model.setCategory(y.getFnPackage().getCategory());
            model.setMoney(FinanceUltils.getMoneyCalculate(y));
            model.setMoneyPaid((long) y.getPaid());
            model.setMoneyRemain(model.getMoney() - model.getMoneyPaid());
            model.setPaidStatus(this.getPaidStatus(model.getMoneyPaid(), model.getMoneyRemain()));
            if (FinanceConstant.CATEGORY_IN.equals(model.getCategory())) {
                moneyIn += model.getMoney();
                moneyPaidIn += model.getMoneyPaid();
                moneyRemainIn += model.getMoneyRemain();
                inList.add(model);
            } else if (FinanceConstant.CATEGORY_OUT.equals(model.getCategory())) {
                moneyOut += model.getMoney();
                moneyPaidOut += model.getMoneyPaid();
                moneyRemainOut += model.getMoneyRemain();
                outList.add(model);
            }
        }
        long moneyTotal = moneyIn - moneyOut;
        long moneyRemainTotal = moneyRemainIn - moneyRemainOut;
        dataIn.setMoney(moneyIn);
        dataIn.setMoneyPaid(moneyPaidIn);
        dataIn.setMoneyRemain(moneyRemainIn);
        dataOut.setMoney(moneyOut);
        dataOut.setMoneyPaid(moneyPaidOut);
        dataOut.setMoneyRemain(moneyRemainOut);
        dataIn.setDataList(inList);
        dataOut.setDataList(outList);

        response.setId(x.getId());
        response.setMonth(x.getMonth());
        response.setParentRead(x.isParentRead());
        response.setMoneyTotal(moneyTotal);
        response.setMoneyRemainTotal(moneyRemainTotal);
        response.setDataIn(dataIn);
        response.setDataOut(dataOut);
        return response;
    }
}
