package com.example.onekids_project.service.serviceimpl.cashbook;

import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.school.CashBookHistory;
import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.CashBookHistoryRepository;
import com.example.onekids_project.repository.FnCashBookRepository;
import com.example.onekids_project.request.cashbook.SearchCashBookHistoryRequest;
import com.example.onekids_project.response.cashbook.CashBookHistoryResponse;
import com.example.onekids_project.response.cashbook.CashbookMoneyResponse;
import com.example.onekids_project.response.cashbook.ListCashBookHistoryResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.cashbook.CashBookHistoryService;
import com.example.onekids_project.service.servicecustom.cashbook.FnCashBookService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * date 2021-03-01 10:34
 *
 * @author lavanviet
 */
@Service
public class CashBookHistoryServiceImpl implements CashBookHistoryService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CashBookHistoryRepository cashBookHistoryRepository;

    @Autowired
    private FnCashBookRepository fnCashBookRepository;

    @Autowired
    private FnCashBookService fnCashBookService;


    @Transactional
    @Override
    public CashBookHistory saveCashBookHistory(Long idSchool, String category, String type, LocalDate date, double money, String origin, String identify) {
        int year = date.getYear();
        FnCashBook fnCashBook = fnCashBookRepository.findBySchoolIdAndYear(idSchool, year).orElseThrow(() -> new NoSuchElementException("not found cashbook by idSchool=" + idSchool + " and year=" + year));
        this.checkCashbook(fnCashBook, category, type, date);
        CashBookHistory cashBookHistory = new CashBookHistory();
        cashBookHistory.setCategory(category);
        cashBookHistory.setType(type);
        cashBookHistory.setDate(date);
        cashBookHistory.setMoney(money);
        cashBookHistory.setOrigin(origin);
        cashBookHistory.setIdentify(identify);
        cashBookHistory.setFnCashBook(fnCashBook);
        fnCashBookService.saveMoneyToCashBook(fnCashBook, category, money);
        return cashBookHistoryRepository.save(cashBookHistory);
    }

    @Override
    public void updateCashBookHistory(CashBookHistory cashBookHistory) {
        cashBookHistoryRepository.save(cashBookHistory);
        fnCashBookService.saveMoneyToCashBook(cashBookHistory.getFnCashBook(), cashBookHistory.getCategory(), cashBookHistory.getMoney());
    }

    @Override
    public ListCashBookHistoryResponse searchCashBookHistory(UserPrincipal principal, SearchCashBookHistoryRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnCashBook fnCashBook = fnCashBookRepository.findByIdAndSchoolId(request.getIdCashBook(), principal.getIdSchoolLogin()).orElseThrow();
        return this.getCashBookHistory(fnCashBook, request);
    }

    @Override
    public ListCashBookHistoryResponse searchCashBookHistoryNow(UserPrincipal principal, SearchCashBookHistoryRequest request) {
        int year = LocalDate.now().getYear();
        Optional<FnCashBook> fnCashBook = fnCashBookRepository.findBySchoolIdAndYearAndDelActiveTrue(principal.getIdSchoolLogin(), year);
        if (fnCashBook.isPresent()) {
            request.setIdCashBook(fnCashBook.get().getId());
            return this.getCashBookHistory(fnCashBook.get(), request);
        } else {
            return null;
        }
    }

    @Override
    public CashbookMoneyResponse searchCashBookHistoryYear(UserPrincipal principal, Integer year, SearchCashBookHistoryRequest request) {
        CashbookMoneyResponse data = new CashbookMoneyResponse();
        FnCashBook fnCashBook = fnCashBookRepository.findBySchoolIdAndYearAndDelActiveTrue(principal.getIdSchoolLogin(), year).orElseThrow();
        request.setIdCashBook(fnCashBook.getId());
        List<CashBookHistory> cashBookHistoryList = cashBookHistoryRepository.searchCashBookHistory(year, request);
        long moneyIn = (long) cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
        long moneyOut = (long) cashBookHistoryList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();
        data.setMoneyIn(moneyIn);
        data.setMoneyOut(moneyOut);
        data.setMoneyInOut(moneyIn - moneyOut);
        return data;
    }

    private ListCashBookHistoryResponse getCashBookHistory(FnCashBook fnCashBook, SearchCashBookHistoryRequest request) {
        List<CashBookHistory> cashBookHistoryList = cashBookHistoryRepository.searchCashBookHistoryPaging(fnCashBook.getYear(), request);
        List<CashBookHistory> cashBookHistoryCalculateList = cashBookHistoryRepository.searchCashBookHistory(fnCashBook.getYear(), request);
        List<CashBookHistory> cashBookHistoryBeforeList = cashBookHistoryRepository.findCashBookHistoryBefore(fnCashBook.getYear(), request);
        ListCashBookHistoryResponse response = new ListCashBookHistoryResponse();
        double moneyIn = cashBookHistoryCalculateList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
        double moneyOut = cashBookHistoryCalculateList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();

        double moneyInBefore = cashBookHistoryBeforeList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_IN)).mapToDouble(CashBookHistory::getMoney).sum();
        double moneyOutBefore = cashBookHistoryBeforeList.stream().filter(x -> x.getCategory().equals(FinanceConstant.CATEGORY_OUT)).mapToDouble(CashBookHistory::getMoney).sum();
        double moneyStart = fnCashBook.getMoneyStart() + (moneyInBefore - moneyOutBefore);
        double moneyEnd = moneyStart + (moneyIn - moneyOut);

        response.setMoneyIn(moneyIn);
        response.setMoneyOut(moneyOut);
        response.setMoneyStart(moneyStart);
        response.setMoneyEnd(moneyEnd);
        response.setTotal(cashBookHistoryCalculateList.size());
        List<CashBookHistoryResponse> dataList = new ArrayList<>();
        cashBookHistoryList.forEach(x -> {
            CashBookHistoryResponse model = modelMapper.map(x, CashBookHistoryResponse.class);
            model.setCode(this.getCodeHistory(x));
            dataList.add(model);
        });
        response.setDataList(dataList);
        return response;
    }

    /**
     * kiểm tra thông tin trước khi tạo lịch sử
     *
     * @param fnCashBook
     * @param category
     * @param type
     * @param date
     */
    private void checkCashbook(FnCashBook fnCashBook, String category, String type, LocalDate date) {
        if (fnCashBook.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CASH_BOOK_LOCKED);
        }
        if (date.isBefore(fnCashBook.getStartDate()) || date.isAfter(fnCashBook.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CASH_BOOK_DATE);
        }
        if (!category.equals(FinanceConstant.CATEGORY_IN) && !category.equals(FinanceConstant.CATEGORY_OUT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CATEGORY_NOT_FOUND);
        }
        if (!type.equals(FinanceConstant.CASH_BOOK_KID) && !type.equals(FinanceConstant.CASH_BOOK_EMP) && !type.equals(FinanceConstant.CASH_BOOK_SCH) && !type.equals(FinanceConstant.CASH_BOOK_OTHER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CASH_BOOK_TYPE);
        }
    }

    /**
     * lấy mã tương ứng với loại thu, chi
     *
     * @param cashBookHistory
     * @return
     */
    private String getCodeHistory(CashBookHistory cashBookHistory) {
        String code = "";
        if (cashBookHistory.getType().equals(FinanceConstant.CASH_BOOK_KID)) {
            code = cashBookHistory.getOrderKidsHistory().getFnOrderKids().getCode();
        } else if (cashBookHistory.getType().equals(FinanceConstant.CASH_BOOK_EMP)) {
            code = cashBookHistory.getOrderEmployeeHistory().getFnOrderEmployee().getCode();
        } else if (cashBookHistory.getType().equals(FinanceConstant.CASH_BOOK_SCH)) {
            code = cashBookHistory.getFnCashInternal().getCode();
        } else if (cashBookHistory.getType().equals(FinanceConstant.CASH_BOOK_OTHER)) {
            code = cashBookHistory.getIdentify();
        }
        return code;
    }
}
