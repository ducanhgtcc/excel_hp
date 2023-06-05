package com.example.onekids_project.service.serviceimpl.cashbook;

import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.entity.school.FnCashBook;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.FnCashBookRepository;
import com.example.onekids_project.request.cashbook.CashBookLockedRequest;
import com.example.onekids_project.request.cashbook.CashBookUpdateMoneyStartRequest;
import com.example.onekids_project.response.cashbook.CashBookResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.cashbook.FnCashBookService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * date 2021-03-01 12:01
 *
 * @author lavanviet
 */
@Service
public class FnCashBookServiceImpl implements FnCashBookService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FnCashBookRepository fnCashBookRepository;

    @Override
    public void createCashBook(School school) {
        LocalDate nowDate = LocalDate.now();
        int year = nowDate.getYear();
        Optional<FnCashBook> fnCashBookOptional = fnCashBookRepository.findBySchoolIdAndYear(school.getId(), year);
        if (fnCashBookOptional.isEmpty()) {
            FnCashBook fnCashBook = new FnCashBook();
            fnCashBook.setSchool(school);
            fnCashBook.setYear(year);
            fnCashBook.setStartDate(LocalDate.now());
            fnCashBook.setEndDate(LocalDate.of(year, 12, 31));
            fnCashBookRepository.save(fnCashBook);
        }
    }

    @Override
    public void saveMoneyToCashBook(FnCashBook fnCashBook, String type, double money) {
        if (money > 0) {
            if (type.equals(FinanceConstant.CATEGORY_IN)) {
                fnCashBook.setMoneyIn(fnCashBook.getMoneyIn() + money);
            } else if (type.equals(FinanceConstant.CATEGORY_OUT)) {
                double balance = fnCashBook.getMoneyStart() + (fnCashBook.getMoneyIn() - fnCashBook.getMoneyOut());
                if (balance < money) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CASH_BOOK_NOT_ENOUGHT);
                }
                fnCashBook.setMoneyOut(fnCashBook.getMoneyOut() + money);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CASH_BOOK_TYPE);
            }
            fnCashBookRepository.save(fnCashBook);
        }
    }

    @Override
    public List<CashBookResponse> findCashBook(UserPrincipal principal) {
        List<FnCashBook> fnCashBookList = fnCashBookRepository.findBySchoolIdOrderByYear(principal.getIdSchoolLogin());
        return listMapper.mapList(fnCashBookList, CashBookResponse.class);
    }

    @Override
    public boolean updateMoneyStart(UserPrincipal principal, CashBookUpdateMoneyStartRequest request) {
        FnCashBook fnCashBook = fnCashBookRepository.findByIdAndSchoolId(request.getId(), principal.getIdSchoolLogin()).orElseThrow();
        this.checkUpdateMoneyStart(fnCashBook, request.getMoneyStart());
        fnCashBook.setMoneyStart(request.getMoneyStart());
        fnCashBookRepository.save(fnCashBook);
        return true;
    }

    @Override
    public boolean updateLocked(UserPrincipal principal, CashBookLockedRequest request) {
        FnCashBook fnCashBook = fnCashBookRepository.findByIdAndSchoolId(request.getId(), principal.getIdSchoolLogin()).orElseThrow();
        fnCashBook.setLocked(request.getLocked());
        if (request.getLocked()) {
            fnCashBook.setIdLocked(principal.getId());
            fnCashBook.setTimeLocked(LocalDateTime.now());
        }
        fnCashBookRepository.save(fnCashBook);
        return true;
    }

    private void checkUpdateMoneyStart(FnCashBook fnCashBook, double moneyInput) {
        if (moneyInput < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Số dư đầu kỳ không thể nhỏ hơn 0");
        }
        if (fnCashBook.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.CASH_BOOK_LOCKED);
        }
        double moneyMin = fnCashBook.getMoneyIn() - fnCashBook.getMoneyOut();
        if (moneyMin < 0) {
            double moneyMinPositive = Math.abs(moneyMin);
            if (moneyInput < moneyMinPositive) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể cập nhật số dư đầu kỳ nhỏ hơn " + Math.abs(moneyMin));
            }
        }

    }
}
