package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppSendConstant;
import com.example.onekids_project.common.FirebaseConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.finance.fees.FnKidsPackage;
import com.example.onekids_project.entity.finance.fees.FnOrderKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mobile.plus.response.fees.FeesClassResponse;
import com.example.onekids_project.mobile.plus.response.fees.KidsFeesResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.FeesPlusService;
import com.example.onekids_project.mobile.request.IdAndDateRequest;
import com.example.onekids_project.model.finance.OrderMoneyModel;
import com.example.onekids_project.model.finance.OrderNumberModel;
import com.example.onekids_project.repository.FnOrderKidsRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.WebSystemTitleRepository;
import com.example.onekids_project.request.common.StatusRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.finance.FnOrderKidsService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.StudentUtil;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * date 2021-06-07 09:14
 *
 * @author lavanviet
 */
@Service
public class FeesPlusServiceImpl implements FeesPlusService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private FnOrderKidsRepository fnOrderKidsRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private SmsDataService smsDataService;
    @Autowired
    private AppSendService appSendService;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private FnOrderKidsService fnOrderKidsService;


    @Override
    public List<FeesClassResponse> getFeesClass(UserPrincipal principal, LocalDate date) {
        List<MaClass> maClassList = maClassRepository.findClassCommon(principal.getIdSchoolLogin());
        List<FeesClassResponse> responseList = new ArrayList<>();
        maClassList.forEach(x -> {
            FeesClassResponse model = new FeesClassResponse();
            model.setId(x.getId());
            model.setClassName(x.getClassName());
            List<Kids> kidsList = StudentUtil.getKidInClassWithStudying(x);
            model.setStudentNumber(kidsList.size());
            int noOrderNumber = 0;
            int noPackageNumber = 0;
            int successYesNumber = 0;
            int successNoNumber = 0;
            for (Kids y : kidsList) {
                Optional<FnOrderKids> fnOrderKidsOptional = fnOrderKidsRepository.findByKidsIdAndMonthAndYear(y.getId(), date.getMonthValue(), date.getYear());
                if (fnOrderKidsOptional.isEmpty()) {
                    noOrderNumber++;
                } else {
                    List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(y, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                    OrderNumberModel orderNumberModel = FinanceUltils.getNumberOrderModel(fnKidsPackageList);
                    if (orderNumberModel.getTotalNumber() == 0) {
                        noPackageNumber++;
                    } else {
                        if (orderNumberModel.getEnoughNumber() == orderNumberModel.getTotalNumber()) {
                            successYesNumber++;
                        } else {
                            successNoNumber++;
                        }
                    }
                }
            }
            model.setNoOrderNumber(noOrderNumber);
            model.setNoPackageNumber(noPackageNumber);
            model.setSuccessYesNumber(successYesNumber);
            model.setSuccessNoNumber(successNoNumber);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<KidsFeesResponse> getKidsInClass(UserPrincipal principal, IdAndDateRequest request) {
        List<KidsFeesResponse> responseList = new ArrayList<>();
        List<Kids> kidsList = kidsRepository.findKidsOfClass(request.getId());
        kidsList.forEach(x -> {
            KidsFeesResponse model = new KidsFeesResponse();
            model.setId(x.getId());
            model.setFullName(x.getFullName());
            model.setClassName(x.getMaClass().getClassName());
            model.setAvatar(AvatarUtils.getAvatarKids(x));
            Optional<FnOrderKids> fnOrderKidsOptional = fnOrderKidsRepository.findByKidsIdAndMonthAndYear(x.getId(), request.getDate().getMonthValue(), request.getDate().getYear());
            if (fnOrderKidsOptional.isPresent()) {
                model.setIdOrder(fnOrderKidsOptional.get().isLocked() ? null : fnOrderKidsOptional.get().getId());
                model.setOrderShow(fnOrderKidsOptional.get().isView());
            }
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public void sendSms(UserPrincipal principal, List<Long> idList, LocalDate date) throws ExecutionException, InterruptedException {
//        FinanceUltils.checkDateGenerateOrder(date);
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(71L).orElseThrow();
        if (!webSystemTitle.isSms()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cấu hình không cho phép gửi Sms học phí");
        }
        String content = webSystemTitle.getContent();
        for (Long x : idList) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<FnOrderKids> fnOrderKidsList = kids.getFnOrderKidsList().stream().filter(a -> a.getMonth() == date.getMonthValue() && a.getYear() == date.getYear()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fnOrderKidsList)) {
                FnOrderKids fnOrderKids = fnOrderKidsList.get(0);
                List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(kids, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
                String totalMoneyString = orderMoneyModel.getMoneyTotalInOut() >= 0 ? "phải đóng" : "được nhận";
                String totalRemainString = orderMoneyModel.getMoneyTotalInOutRemain() >= 0 ? "phải đóng" : "được nhận";
                content = content.replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()).replace("{order_code}", fnOrderKids.getCode()).replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderKids.getMonth(), fnOrderKids.getYear())).replace("{in_out_fees}", totalMoneyString).replace("{money_total}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOut()))).replace("{in_out}", totalRemainString).replace("{money_pay}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOutRemain())));
                SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
                smsNotifyDataRequest.setSendContent(content);
                smsDataService.sendSmsKid(Collections.singletonList(kids), kids.getIdSchool(), smsNotifyDataRequest);
            }
        }
    }

    @Override
    public void sendApp(UserPrincipal principal, List<Long> idList, LocalDate date) throws FirebaseMessagingException {
//        FinanceUltils.checkDateGenerateOrder(date);
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(71L).orElseThrow();
        String title = webSystemTitle.getTitle();
        String content = webSystemTitle.getContent();
        for (Long x : idList) {
            Kids kids = kidsRepository.findByIdAndDelActiveTrue(x).orElseThrow();
            List<FnOrderKids> fnOrderKidsList = kids.getFnOrderKidsList().stream().filter(a -> a.getMonth() == date.getMonthValue() && a.getYear() == date.getYear()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fnOrderKidsList)) {
                FnOrderKids fnOrderKids = fnOrderKidsList.get(0);
                List<FnKidsPackage> fnKidsPackageList = FinanceUltils.getKidsPackageListFromKid(kids, date, AppConstant.APP_TRUE, AppConstant.APP_TRUE);
                OrderMoneyModel orderMoneyModel = FinanceUltils.getOrderMoneyKidsModel(fnKidsPackageList);
                String totalMoneyString = orderMoneyModel.getMoneyTotalInOut() >= 0 ? "phải đóng" : "được nhận";
                String totalRemainString = orderMoneyModel.getMoneyTotalInOutRemain() >= 0 ? "phải đóng" : "được nhận";
                content = content.replace(FirebaseConstant.REPLACE_KID_NAME, kids.getFullName()).replace("{order_code}", fnOrderKids.getCode()).replace("{month_year}", ConvertData.formatMonthAndYear(fnOrderKids.getMonth(), fnOrderKids.getYear())).replace("{in_out_fees}", totalMoneyString).replace("{money_total}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOut()))).replace("{in_out}", totalRemainString).replace("{money_pay}", FinanceUltils.formatMoney((long) Math.abs(orderMoneyModel.getMoneyTotalInOutRemain())));
                appSendService.saveToAppSendParent(principal, kids, title, content, AppSendConstant.TYPE_FINANCE);
                firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, content, kids.getIdSchool(), FirebaseConstant.CATEGORY_ORDER_NOTIFY);
            }
        }
    }

    @Override
    public boolean setOrderShow(UserPrincipal principal, List<StatusRequest> request) throws ExecutionException, FirebaseMessagingException, InterruptedException {
        for (StatusRequest x : request) {
            fnOrderKidsService.setViewOrder(principal, x);
        }
        return true;
    }
}
