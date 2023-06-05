package com.example.onekids_project.service.serviceimpl.finance;

import com.example.onekids_project.common.*;
import com.example.onekids_project.entity.finance.fees.OrderKidsHistory;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.system.WebSystemTitle;
import com.example.onekids_project.firebase.servicecustom.FirebaseFunctionService;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.request.finance.wallet.SearchWalletParentHistoryRequest;
import com.example.onekids_project.request.finance.wallet.WalletParentHistoryCreateRequest;
import com.example.onekids_project.request.smsNotify.SmsNotifyDataRequest;
import com.example.onekids_project.response.common.HandleFileResponse;
import com.example.onekids_project.response.finance.wallet.ListWalletParentHistoryResponse;
import com.example.onekids_project.response.finance.wallet.WalletParentHistoryResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.AppSendService;
import com.example.onekids_project.service.servicecustom.finance.WalletParentHistoryService;
import com.example.onekids_project.service.servicecustom.sms.SmsDataService;
import com.example.onekids_project.util.DateCommonUtils;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.util.HandleFileUtils;
import com.example.onekids_project.validate.CommonValidate;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * date 2021-02-25 09:05
 *
 * @author lavanviet
 */
@Service
public class WalletParentHistoryServiceImpl implements WalletParentHistoryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private WalletParentRepository walletParentRepository;

    @Autowired
    private FnBankRepository fnBankRepository;

    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private WebSystemTitleRepository webSystemTitleRepository;
    @Autowired
    private FirebaseFunctionService firebaseFunctionService;
    @Autowired
    private AppSendService appSendService;
    @Autowired
    private SmsDataService smsDataService;


    @Transactional
    @Override
    public boolean createWalletParentHistory(UserPrincipal principal, WalletParentHistoryCreateRequest request) throws IOException, FirebaseMessagingException, ExecutionException, InterruptedException {
        CommonValidate.checkDataPlus(principal);
        DateCommonUtils.checkDateWithCurrentDate(request.getDate());
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(request.getIdKid()).orElseThrow();
        WalletParent walletParent = walletParentRepository.findById(request.getIdWalletParent()).orElseThrow();
        WalletParentHistory walletParentHistory = modelMapper.map(request, WalletParentHistory.class);
        walletParentHistory.setWalletParent(walletParent);
        walletParentHistory.setType(FinanceConstant.WALLET_TYPE_SCHOOL);
        boolean sendNotifyOut = principal.getSchoolConfig().isNotifyWalletOutStatus();
        if (request.getIdBank() != null) {
            FnBank fnBank = fnBankRepository.findByIdAndDelActiveTrue(request.getIdBank()).orElseThrow();
            walletParentHistory.setFnBank(fnBank);
        }
        this.saveWalletParent(principal, walletParent, walletParentHistory);
        if (request.getMultipartFile() != null) {
            HandleFileResponse handleFileResponse = HandleFileUtils.getUrlPictureSaved(request.getMultipartFile(), principal.getIdSchoolLogin(), UploadDownloadConstant.TAI_CHINH);
            walletParentHistory.setPicture(handleFileResponse.getUrlWeb());
            walletParentHistory.setPictureLocal(handleFileResponse.getUrlLocal());
        }
        WalletParentHistory walletParentHistorySaved = walletParentHistoryRepository.save(walletParentHistory);
        //send firebase
        if (walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_IN) || (walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_OUT) && sendNotifyOut)) {
            this.sendFirebaseAndSmsWallet(principal, kids, walletParentHistorySaved);
        }
        return true;
    }

    @Override
    public ListWalletParentHistoryResponse searchWalletParentHistory(UserPrincipal principal, SearchWalletParentHistoryRequest request) {
        CommonValidate.checkDataPlus(principal);
        ListWalletParentHistoryResponse response = new ListWalletParentHistoryResponse();
        List<WalletParentHistory> walletParentHistoryList = walletParentHistoryRepository.searchWalletParentHistory(request);
        long total = walletParentHistoryRepository.countWalletParentHistory(request);
        List<WalletParentHistoryResponse> dataList = listMapper.mapList(walletParentHistoryList, WalletParentHistoryResponse.class);
        response.setTotal(total);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public ListWalletParentHistoryResponse searchWalletParentHistoryFalse(UserPrincipal principal, Long idWalletParent) {
        CommonValidate.checkDataPlus(principal);
        ListWalletParentHistoryResponse response = new ListWalletParentHistoryResponse();
        List<WalletParentHistory> walletParentHistoryList = walletParentHistoryRepository.searchWalletParentHistoryFalse(idWalletParent);
        long total = walletParentHistoryRepository.countWalletParentHistoryFalse(idWalletParent);
        List<WalletParentHistoryResponse> dataList = listMapper.mapList(walletParentHistoryList, WalletParentHistoryResponse.class);
        response.setTotal(total);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public void saveMoneyWalletHistory(long idUser, String name, LocalDate date, String orderCode, String category, double money, WalletParent walletParent, OrderKidsHistory orderKidsHistory) {
        if (money > 0) {
            WalletParentHistory walletParentHistory = new WalletParentHistory();
            walletParentHistory.setWalletParent(walletParent);
            walletParentHistory.setType(FinanceConstant.WALLET_TYPE_SCHOOL);
            walletParentHistory.setConfirm(AppConstant.APP_TRUE);
            walletParentHistory.setIdConfirm(idUser);
            walletParentHistory.setTimeConfirm(LocalDateTime.now());
            walletParentHistory.setName(name);
            walletParentHistory.setDate(date);
            walletParentHistory.setMoney(money);
            walletParentHistory.setOrderKidsHistory(orderKidsHistory);
            if (category.equals(FinanceConstant.CATEGORY_IN)) {
                walletParentHistory.setCategory(FinanceConstant.CATEGORY_IN);
                walletParentHistory.setDescription(TextWebConstant.ORDER_PAYMENT_IN + orderCode);
                walletParent.setMoneyIn(walletParent.getMoneyIn() + walletParentHistory.getMoney());
            } else if (category.equals(FinanceConstant.CATEGORY_OUT)) {
                double balance = walletParent.getMoneyIn() - walletParent.getMoneyOut();
                if (money > balance) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.WALLET_BALANCE);
                }
                walletParentHistory.setCategory(FinanceConstant.CATEGORY_OUT);
                walletParentHistory.setDescription(TextWebConstant.ORDER_PAYMENT_OUT + orderCode);
                walletParent.setMoneyOut(walletParent.getMoneyOut() + walletParentHistory.getMoney());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CATEGORY_NOT_FOUND);
            }
            walletParentRepository.save(walletParent);
            walletParentHistoryRepository.save(walletParentHistory);
        }
    }

    @Override
    public boolean confirmWalletParentHistory(UserPrincipal principal, Long idWalletHistory) throws FirebaseMessagingException {
        CommonValidate.checkDataPlus(principal);
        WalletParentHistory walletParentHistory = walletParentHistoryRepository.findById(idWalletHistory).orElseThrow();
        if (!walletParentHistory.isConfirm() && walletParentHistory.getType().equals(FinanceConstant.WALLET_TYPE_PARENT) && walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
            WalletParent walletParent = walletParentHistory.getWalletParent();
            walletParentHistory.setConfirm(AppConstant.APP_TRUE);
            walletParentHistory.setIdConfirm(principal.getId());
            walletParentHistory.setTimeConfirm(LocalDateTime.now());
            walletParent.setMoneyIn(walletParent.getMoneyIn() + walletParentHistory.getMoney());
            walletParentRepository.save(walletParent);
            walletParentHistoryRepository.save(walletParentHistory);

            //send firebase
            if (walletParentHistory.getKids() != null) {
                WebSystemTitle webSystemTitle = webSystemTitleRepository.findById(80L).orElseThrow();
                String title = webSystemTitle.getTitle();
                String content = webSystemTitle.getContent();
                firebaseFunctionService.sendParentCommon(Collections.singletonList(walletParentHistory.getKids()), title, content, principal.getIdSchoolLogin(), FirebaseConstant.CATEGORY_NOTIFY);
                appSendService.saveToAppSendParent(principal, walletParentHistory.getKids(), title, content, AppSendConstant.TYPE_FINANCE);
            }

        }
        return true;
    }

    @Override
    public boolean deleteWalletParentHistory(UserPrincipal principal, Long idWalletHistory) {
        CommonValidate.checkDataPlus(principal);
        WalletParentHistory walletParentHistory = walletParentHistoryRepository.findById(idWalletHistory).orElseThrow();
        if ((!walletParentHistory.isConfirm() && walletParentHistory.getType().equals(FinanceConstant.WALLET_TYPE_SCHOOL) && walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_OUT))
                || (!walletParentHistory.isConfirm() && walletParentHistory.getType().equals(FinanceConstant.WALLET_TYPE_PARENT) && walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_IN))) {
            walletParentHistoryRepository.delete(walletParentHistory);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa lịch sử giao dịch này");
        }
        return true;
    }


    private void saveWalletParent(UserPrincipal principal, WalletParent walletParent, WalletParentHistory walletParentHistory) {
        if (walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_IN)) {
            walletParentHistory.setConfirm(AppConstant.APP_TRUE);
            walletParentHistory.setIdConfirm(principal.getId());
            walletParentHistory.setTimeConfirm(LocalDateTime.now());
            walletParent.setMoneyIn(walletParent.getMoneyIn() + walletParentHistory.getMoney());
            walletParentRepository.save(walletParent);
        } else if (walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_OUT)) {
            boolean sendNotifyOut = principal.getSchoolConfig().isNotifyWalletOutStatus();
            FinanceUltils.checkBalanceWalletParent(walletParent, walletParentHistory.getMoney());
            if (!sendNotifyOut) {
                walletParentHistory.setConfirm(AppConstant.APP_TRUE);
                walletParentHistory.setIdConfirm(principal.getId());
                walletParentHistory.setTimeConfirm(LocalDateTime.now());
                walletParent.setMoneyOut(walletParent.getMoneyOut() + walletParentHistory.getMoney());
                walletParentRepository.save(walletParent);
            }
        }
    }

    private void sendFirebaseAndSmsWallet(UserPrincipal principal, Kids kids, WalletParentHistory walletParentHistory) throws FirebaseMessagingException, ExecutionException, InterruptedException {
        WebSystemTitle webSystemTitle = webSystemTitleRepository.findByIdAndDelActiveTrue(70L).orElseThrow();
        String title = webSystemTitle.getTitle();
        String type = walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_IN) ? "nạp" : "rút";
        String content = webSystemTitle.getContent().replace("{in_out_wallet}", type).replace("{money}", FinanceUltils.formatMoney((long) walletParentHistory.getMoney())).replace("{wallet_code}", walletParentHistory.getWalletParent().getCode());

        appSendService.saveToAppSendParent(principal, kids, title, content, AppSendConstant.TYPE_FINANCE);
        //gửi firebase
        firebaseFunctionService.sendParentCommon(Collections.singletonList(kids), title, content, kids.getIdSchool(), FirebaseConstant.CATEGORY_WALLET);
        //gửi Sms
        if (webSystemTitle.isSms()) {
            SmsNotifyDataRequest smsNotifyDataRequest = new SmsNotifyDataRequest();
            smsNotifyDataRequest.setSendContent(content);
            smsDataService.sendSmsKid(Collections.singletonList(kids), kids.getIdSchool(), smsNotifyDataRequest);
        }
    }

}
