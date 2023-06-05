package com.example.onekids_project.service.serviceimpl.cashinternal;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.school.FnBank;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.FnBankRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.cashinternal.CreateBankInforRequest;
import com.example.onekids_project.request.cashinternal.UpdateBankInfoRequest;
import com.example.onekids_project.request.finance.wallet.BankBriefResponse;
import com.example.onekids_project.response.caskinternal.BankResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.cashinternal.FnBankService;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FnBankServiceImpl implements FnBankService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private FnBankRepository fnBankRepository;
    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public List<BankBriefResponse> getBankBrief(UserPrincipal principal) {
        List<FnBank> fnBankList = fnBankRepository.findBySchoolIdAndDelActiveTrue(principal.getIdSchoolLogin());
        return listMapper.mapList(fnBankList, BankBriefResponse.class);
    }

    @Override
    public List<BankResponse> searchBankInfo(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<FnBank> fnBankList = fnBankRepository.searchBank(principal.getIdSchoolLogin());
        return listMapper.mapList(fnBankList, BankResponse.class);
    }

    @Override
    public BankResponse findByIdBankInfo(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnBank fnBank = fnBankRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        return modelMapper.map(fnBank, BankResponse.class);
    }

    @Override
    public boolean updateBankInfo(UserPrincipal principal, UpdateBankInfoRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnBank fnBank = fnBankRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        modelMapper.map(request, fnBank);
        fnBankRepository.save(fnBank);
        return true;
    }

    @Override
    public boolean deleteBankInFo(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnBank fnBank = fnBankRepository.findByIdAndSchoolIdAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        if (fnBank.isChecked()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.BANK_CHECKED);
        }
        fnBank.setDelActive(AppConstant.APP_FALSE);
        fnBankRepository.save(fnBank);
        return true;
    }

    @Override
    public boolean createBankInfo(UserPrincipal principal, CreateBankInforRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<FnBank> fnBankList = fnBankRepository.findBySchoolIdAndDelActiveTrue(idSchool);
        FnBank fnBank = modelMapper.map(request, FnBank.class);
        School school = schoolRepository.findByIdAndDelActiveTrue(idSchool).orElseThrow();
        fnBank.setSchool(school);
        //nếu là tạo tài khoản đầu tiên thi set cho tài khoản chinh
        if (CollectionUtils.isEmpty(fnBankList)) {
            fnBank.setChecked(AppConstant.APP_TRUE);
        }
        fnBankRepository.save(fnBank);
        return true;
    }

    @Transactional
    @Override
    public boolean updateCheckedBank(UserPrincipal principal, IdObjectRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<FnBank> fnBankList = fnBankRepository.findBySchoolIdAndCheckedTrue(idSchool);
        fnBankList.forEach(x -> x.setChecked(AppConstant.APP_FALSE));
        FnBank fnBank = fnBankRepository.findByIdAndSchoolIdAndDelActiveTrue(request.getId(), idSchool).orElseThrow(() -> new NoSuchElementException("not found bank by id in school"));
        fnBank.setChecked(AppConstant.APP_TRUE);
        fnBankRepository.saveAll(fnBankList);
        fnBankRepository.save(fnBank);
        return true;
    }

}
