package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.finance.CashInternal.FnCashInternal;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashBookPlusRequest;
import com.example.onekids_project.mobile.plus.request.cashinternal.CashInternalPlusRequest;
import com.example.onekids_project.mobile.plus.response.cashinternal.CashBookPlusResponse;
import com.example.onekids_project.mobile.plus.response.cashinternal.NumberCashInternalResponse;
import com.example.onekids_project.mobile.plus.service.servicecustom.CashInternalPlusService;
import com.example.onekids_project.mobile.request.IdAndStatusRequest;
import com.example.onekids_project.mobile.response.CashInternalPlusResponse;
import com.example.onekids_project.mobile.response.ListCashInternalPlusResponse;
import com.example.onekids_project.repository.CashBookHistoryRepository;
import com.example.onekids_project.repository.FnCashBookRepository;
import com.example.onekids_project.repository.FnCashInternalSchoolRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-06-18 14:15
 *
 * @author lavanviet
 */
@Service
public class CashInternalPlusServiceImpl implements CashInternalPlusService {

    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FnCashInternalSchoolRepository fnCashInternalSchoolRepository;
    @Autowired
    private FnCashBookRepository fnCashBookRepository;
    @Autowired
    private CashBookHistoryRepository cashBookHistoryRepository;

    @Override
    public ListCashInternalPlusResponse getCashInternalPlus(UserPrincipal principal, CashInternalPlusRequest request, String category) {
        ListCashInternalPlusResponse response = new ListCashInternalPlusResponse();
        List<FnCashInternal> fnCashInternalList = fnCashInternalSchoolRepository.getCashInternalPlus(principal.getIdSchoolLogin(), request, category);
        List<CashInternalPlusResponse> dataList = new ArrayList<>();
        fnCashInternalList.forEach(x -> {
            CashInternalPlusResponse model = modelMapper.map(x, CashInternalPlusResponse.class);
            model.setContent(StringUtils.isNotBlank(x.getContent()) ? x.getContent() : "");
            model.setDate(ConvertData.formartDateDash(x.getDate()));
            dataList.add(model);
        });
        boolean lastPage = fnCashInternalList.size() < MobileConstant.MAX_PAGE_ITEM;
        response.setLastPage(lastPage);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public boolean approvedCashInternalPlus(UserPrincipal principal, List<IdAndStatusRequest> request) {
        request.forEach(x -> {
            FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndIdSchoolAndPaymentFalseAndDelActiveTrue(x.getId(), principal.getIdSchoolLogin()).orElseThrow();
            if (fnCashInternal.isApproved() != x.getStatus()) {
                fnCashInternal.setApproved(x.getStatus());
                fnCashInternal.setIdApproved(principal.getId());
                fnCashInternal.setTimeApproved(LocalDateTime.now());
                fnCashInternalSchoolRepository.save(fnCashInternal);
            }
        });
        return true;
    }

    @Override
    public boolean canceledCashInternalPlus(UserPrincipal principal, Long id) {
        FnCashInternal fnCashInternal = fnCashInternalSchoolRepository.findByIdAndIdSchoolAndCanceledFalseAndPaymentFalseAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        fnCashInternal.setCanceled(AppConstant.APP_TRUE);
        fnCashInternalSchoolRepository.save(fnCashInternal);
        return true;
    }

    @Override
    public List<CashBookPlusResponse> getCashBookPlus(UserPrincipal principal, CashBookPlusRequest request) {
        List<CashBookPlusResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        if (request.getStartDate() == null && request.getEndDate() == null) {
            List<FnCashBook> fnCashBookList = fnCashBookRepository.findBySchoolIdOrderByYear(idSchool);
            fnCashBookList.forEach(x -> {
                CashBookPlusResponse model = new CashBookPlusResponse();
                model.setTime(ConvertData.convertTwoDate(x.getStartDate(), x.getEndDate()));
                model.setMoneyIn((long) x.getMoneyIn());
                model.setMoneyOut((long) x.getMoneyOut());
                model.setMoneyInOut(model.getMoneyIn() - model.getMoneyOut());
                model.setMoneyStart((long) x.getMoneyStart());
                model.setMoneyEnd(model.getMoneyStart() + model.getMoneyInOut());
                responseList.add(model);
            });
        } else {
            if (request.getEndDate().getYear() != request.getStartDate().getYear()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vui lòng chọn cùng năm");
            }
            CashBookPlusResponse model = new CashBookPlusResponse();
            FnCashBook fnCashBook = fnCashBookRepository.findBySchoolIdAndYear(idSchool, request.getEndDate().getYear()).orElseThrow();
            List<CashBookHistory> cashBookHistoryCalculateList = cashBookHistoryRepository.findCashBookHistoryPlus(fnCashBook.getId(), request.getStartDate(), request.getEndDate(), request.getType());
            List<CashBookHistory> cashBookHistoryBeforeList = cashBookHistoryRepository.findCashBookHistoryBeforePlus(fnCashBook.getId(), request.getStartDate(), request.getType());

            double moneyIn = cashBookHistoryCalculateList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
            double moneyOut = cashBookHistoryCalculateList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();

            double moneyInBefore = cashBookHistoryBeforeList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
            double moneyOutBefore = cashBookHistoryBeforeList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();
            double moneyStart = fnCashBook.getMoneyStart() + (moneyInBefore - moneyOutBefore);

            model.setTime(ConvertData.convertTwoDate(request.getStartDate(), request.getEndDate()));
            model.setMoneyIn((long) moneyIn);
            model.setMoneyOut((long) moneyOut);
            model.setMoneyInOut(model.getMoneyIn() - model.getMoneyOut());
            model.setMoneyStart((long) moneyStart);
            model.setMoneyEnd(model.getMoneyStart() + model.getMoneyInOut());
            responseList.add(model);
        }
        return responseList;
    }

    @Override
    public NumberCashInternalResponse getShowNumber(UserPrincipal principal) {
        NumberCashInternalResponse response = new NumberCashInternalResponse();
        Long idSchool = principal.getIdSchoolLogin();
        int inNumber = fnCashInternalSchoolRepository.countByIdSchoolAndCategoryAndApprovedFalseAndCanceledFalseAndDelActiveTrue(idSchool, FinanceConstant.CATEGORY_IN);
        int outNumber = fnCashInternalSchoolRepository.countByIdSchoolAndCategoryAndApprovedFalseAndCanceledFalseAndDelActiveTrue(idSchool, FinanceConstant.CATEGORY_OUT);
        response.setInNumber(inNumber);
        response.setOutNumber(outNumber);
        return response;
    }
}
