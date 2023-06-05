package com.example.onekids_project.service.serviceimpl.employeesaralyImpl;

import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.finance.employeesalary.FnEmployeeSalary;
import com.example.onekids_project.entity.finance.employeesalary.FnOrderEmployee;
import com.example.onekids_project.repository.FnEmployeeSalaryRepository;
import com.example.onekids_project.repository.FnOrderEmployeeRepository;
import com.example.onekids_project.request.common.DescriptionRequest;
import com.example.onekids_project.response.employeesalary.ListOrderSalaryDetailResponse;
import com.example.onekids_project.response.employeesalary.SalaryPackagePaymentDetailResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.employeesaraly.OrderSalaryService;
import com.example.onekids_project.util.FinanceUltils;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * date 2021-03-04 2:21 CH
 *
 * @author ADMIN
 */
@Service
public class OrderSalaryServiceImpl implements OrderSalaryService {

    @Autowired
    private FnOrderEmployeeRepository fnOrderEmployeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FnEmployeeSalaryRepository fnEmployeeSalaryRepository;

    @Override
    public ListOrderSalaryDetailResponse findSalaryPackagePaymentDetail(UserPrincipal principal, Long idOrder) {
        CommonValidate.checkDataPlus(principal);
        FnOrderEmployee fnOrderEmployee = fnOrderEmployeeRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment"));

        ListOrderSalaryDetailResponse response = modelMapper.map(fnOrderEmployee, ListOrderSalaryDetailResponse.class);
        List<FnEmployeeSalary> fnEmployeeSalaryList = fnEmployeeSalaryRepository.findByInfoEmployeeSchoolIdAndMonthAndYearAndApprovedTrueAndDelActiveTrue(fnOrderEmployee.getInfoEmployeeSchool().getId(), fnOrderEmployee.getMonth(), fnOrderEmployee.getYear());
        List<SalaryPackagePaymentDetailResponse> dataList = new ArrayList<>();
        for (FnEmployeeSalary x : fnEmployeeSalaryList) {
            SalaryPackagePaymentDetailResponse model = modelMapper.map(x, SalaryPackagePaymentDetailResponse.class);
            model.setMoney(FinanceUltils.getMoneySalary(x));
            dataList.add(model);
        }
        response.setDataList(dataList);
        return response;
    }

    @Override
    public boolean saveOrderSalaryDescription(UserPrincipal principal, Long idOrder, DescriptionRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnOrderEmployee fnOrderEmployee = fnOrderEmployeeRepository.findById(idOrder).orElseThrow(() -> new NoSuchElementException("not found order by id in payment"));
        checkLocked(fnOrderEmployee);
        fnOrderEmployee.setDescription(request.getDescription());
        fnOrderEmployeeRepository.save(fnOrderEmployee);
        return true;
    }

    private void checkLocked(FnOrderEmployee fnOrderEmployee) {
        if (fnOrderEmployee.isLocked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ORDER_LOCKED);
        }
    }
}
