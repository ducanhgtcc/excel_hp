package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.parent.WalletParent;
import com.example.onekids_project.entity.parent.WalletParentHistory;
import com.example.onekids_project.mobile.plus.service.servicecustom.WalletPlusService;
import com.example.onekids_project.mobile.response.wallet.*;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.WalletParentHistoryRepository;
import com.example.onekids_project.repository.WalletParentRepository;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.FinanceUltils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-10-22 08:35
 *
 * @author lavanviet
 */
@Service
public class WalletPlusServiceImpl implements WalletPlusService {
    @Autowired
    private WalletParentRepository walletParentRepository;
    @Autowired
    private WalletParentHistoryRepository walletParentHistoryRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private KidsRepository kidsRepository;

    @Override
    public List<WalletClassPlusResponse> getWalletClass(Long idSchool) {
        List<WalletClassPlusResponse> responseList = new ArrayList<>();
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        maClassList.forEach(x -> {
            WalletClassPlusResponse model = new WalletClassPlusResponse();
            model.setId(x.getId());
            model.setClassName(x.getClassName());
            List<Kids> kidsList = kidsRepository.findByMaClassIdAndParentIsNotNullAndDelActiveTrue(x.getId());
            int schoolNoConfirm = 0;
            int parentNoConfirm = 0;
            for (Kids y : kidsList) {
                WalletParent walletParent = FinanceUltils.getWalletParentFromKids(y);
                schoolNoConfirm += walletParentHistoryRepository.countByWalletParentIdAndTypeAndConfirmFalse(walletParent.getId(), FinanceConstant.WALLET_TYPE_PARENT);
                parentNoConfirm += walletParentHistoryRepository.countByWalletParentIdAndTypeAndConfirmFalse(walletParent.getId(), FinanceConstant.WALLET_TYPE_SCHOOL);
            }
            model.setSchoolNoConfirm(schoolNoConfirm);
            model.setParentNoConfirm(parentNoConfirm);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<WalletKidsPlusResponse> getWalletKids(Long idClass, String kidsStatus, String fullName) {
        List<Kids> kidsList = kidsRepository.getKidsInClassAndStatusAndName(idClass, kidsStatus, fullName);
        return setWalletKidsPlus(kidsList, false);
    }

    @Override
    public WalletInfoResponse getWalletInfoByKid(Long idKid) {
        Kids kids = kidsRepository.findByIdAndDelActiveTrue(idKid).orElseThrow();
        WalletInfoResponse response = new WalletInfoResponse();
        WalletInfoKids walletInfoKids = new WalletInfoKids();
        WalletInfoParent walletInfoParent = new WalletInfoParent();
        WalletParent walletParent = FinanceUltils.getWalletParentFromKids(kids);
        walletInfoKids.setCode(walletParent.getCode());
        walletInfoKids.setMoneyIn((long) walletParent.getMoneyIn());
        walletInfoKids.setMoneyOut((long) walletParent.getMoneyOut());
        walletInfoKids.setMoneyRemain(walletInfoKids.getMoneyIn() - walletInfoKids.getMoneyOut());
        Parent parent = kids.getParent();
        if (parent != null) {
            walletInfoParent.setFullName(parent.getMaUser().getFullName());
            walletInfoParent.setAddress(kids.getAddress());
            walletInfoParent.setPhone(parent.getMaUser().getPhone());
        }
        response.setWalletInfoKids(walletInfoKids);
        response.setWalletInfoParent(walletInfoParent);
        return response;
    }

    @Override
    public ListWalletHistoryPlusResponse getWalletHistoryKids(Long idWallet, String status, int pageNumber) {
        ListWalletHistoryPlusResponse response = new ListWalletHistoryPlusResponse();
        List<WalletHistoryPlusResponse> responseList = new ArrayList<>();
        List<WalletParentHistory> walletParentHistoryList = walletParentHistoryRepository.searchWalletForPlus(idWallet, status, pageNumber);
        walletParentHistoryList.forEach(x -> {
            WalletHistoryPlusResponse model = new WalletHistoryPlusResponse();
            model.setId(x.getId());
            model.setDate(ConvertData.convertDateString(x.getDate()));
            model.setCategory(x.getCategory());
            model.setMoney((long) x.getMoney());
            model.setDescription(x.getDescription());
            model.setPicture(StringUtils.isNotBlank(x.getPicture()) ? x.getPicture() : "");
            if (!x.isConfirm()) {
                if (x.getCategory().equals(FinanceConstant.CATEGORY_IN) && x.getType().equals(FinanceConstant.WALLET_TYPE_PARENT)) {
                    model.setSchoolUnConfirm(true);
                } else if (x.getCategory().equals(FinanceConstant.CATEGORY_OUT) && x.getType().equals(FinanceConstant.WALLET_TYPE_SCHOOL)) {
                    model.setParentUnConfirm(true);
                }
            }
            responseList.add(model);
        });
        response.setDataList(responseList);
        response.setLastPage(responseList.size() < MobileConstant.MAX_PAGE_ITEM);
        return response;
    }

    @Override
    public List<WalletKidsPlusResponse> getWalletUnConfirm(Long idSchool) {
        List<Kids> kidsList = kidsRepository.findByIdSchoolAndParentIsNotNullAndDelActiveTrue(idSchool);
        return setWalletKidsPlus(kidsList, true);
    }

    @Override
    public WalletHistoryDetailResponse getWalletHistoryDetail(Long id) {
        WalletParentHistory walletParentHistory = walletParentHistoryRepository.findById(id).orElseThrow();
        WalletHistoryDetailResponse response = new WalletHistoryDetailResponse();
        response.setId(walletParentHistory.getId());
        response.setDescription(walletParentHistory.getDescription());
        response.setMoney((long) walletParentHistory.getMoney());
        response.setDate(ConvertData.convertDateString(walletParentHistory.getDate()));
        response.setPicture(StringUtils.isNotBlank(walletParentHistory.getPicture()) ? walletParentHistory.getPicture() : "");
        if (!walletParentHistory.isConfirm()) {
            if (walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_IN) && walletParentHistory.getType().equals(FinanceConstant.WALLET_TYPE_PARENT)) {
                response.setSchoolUnConfirm(true);
            } else if (walletParentHistory.getCategory().equals(FinanceConstant.CATEGORY_OUT) && walletParentHistory.getType().equals(FinanceConstant.WALLET_TYPE_SCHOOL)) {
                response.setParentUnConfirm(true);
            }
        }
        return response;
    }


    private List<WalletKidsPlusResponse> setWalletKidsPlus(List<Kids> kidsList, boolean unConfirm) {
        List<WalletKidsPlusResponse> responseList = new ArrayList<>();
        kidsList.forEach(x -> {
            WalletKidsPlusResponse model = new WalletKidsPlusResponse();
            model.setId(x.getId());
            model.setFullName(x.getFullName());
            WalletParent walletParent = FinanceUltils.getWalletParentFromKids(x);
            int schoolNoConfirm = walletParentHistoryRepository.countByWalletParentIdAndTypeAndConfirmFalse(walletParent.getId(), FinanceConstant.WALLET_TYPE_PARENT);
            int parentNoConfirm = walletParentHistoryRepository.countByWalletParentIdAndTypeAndConfirmFalse(walletParent.getId(), FinanceConstant.WALLET_TYPE_SCHOOL);
            model.setIdWallet(walletParent.getId());
            model.setSchoolNoConfirm(schoolNoConfirm);
            model.setParentNoConfirm(parentNoConfirm);
            model.setMoney((long) (walletParent.getMoneyIn() - walletParent.getMoneyOut()));
            model.setAvatar(AvatarUtils.getAvatarKids(x));
            model.setClassName(x.getMaClass().getClassName());
            model.setCodeWallet(walletParent.getCode());
            if (unConfirm) {
                if (schoolNoConfirm + parentNoConfirm > 0) {
                    responseList.add(model);
                }
            } else {
                responseList.add(model);
            }

        });
        return responseList;
    }
}
