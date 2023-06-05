package com.example.onekids_project.service.serviceimpl.cashinternal;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.FinanceConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.finance.CashInternal.FnPeopleType;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.PeopleTypeRepository;
import com.example.onekids_project.request.cashinternal.CreatePeopleTypeRequest;
import com.example.onekids_project.request.cashinternal.SeacrhCashInternalRequest;
import com.example.onekids_project.request.cashinternal.UpdatePeopleTypeRequest;
import com.example.onekids_project.response.caskinternal.CashInternalCreateResponse;
import com.example.onekids_project.response.caskinternal.ListPeopleTypeReponseResponse;
import com.example.onekids_project.response.caskinternal.PeopleTypeOtherResponse;
import com.example.onekids_project.response.caskinternal.PeopleTypeResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.cashinternal.PeopleTypeService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PeopleTypeServiceImpl implements PeopleTypeService {
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PeopleTypeRepository peopleTypeRepository;

    /**
     * @param school
     * @return
     */
    @Override
    public boolean createPeopleTypeSchool(School school) {
        Optional<FnPeopleType> fnPeopleTypeOptional = peopleTypeRepository.findByIdSchoolAndDefaultStatusTrue(school.getId());
        if (fnPeopleTypeOptional.isEmpty()) {
            FnPeopleType fnPeopleType = new FnPeopleType();
            fnPeopleType.setIdSchool(school.getId());
            fnPeopleType.setName(AppConstant.SCHOOL_NAME);
            fnPeopleType.setDefaultStatus(AppConstant.APP_TRUE);
            fnPeopleType.setType(FinanceConstant.INTERNAL);
            fnPeopleType.setAddress(school.getSchoolAddress());
            fnPeopleType.setDescription(school.getSchoolDescription());
            fnPeopleType.setEmail(school.getSchoolEmail());
            fnPeopleType.setPhone(school.getSchoolPhone());
            peopleTypeRepository.save(fnPeopleType);
        }
        return true;
    }

    /**
     * @param principal
     * @param request
     * @return
     */
    @Override
    public ListPeopleTypeReponseResponse searchPeopleType(UserPrincipal principal, SeacrhCashInternalRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListPeopleTypeReponseResponse response = new ListPeopleTypeReponseResponse();
        List<FnPeopleType> fnPeopleTypeList = peopleTypeRepository.searchPeopleType(idSchool, request);
        long total = peopleTypeRepository.countSearch(idSchool, request);
        List<PeopleTypeResponse> responseList = listMapper.mapList(fnPeopleTypeList, PeopleTypeResponse.class);
        response.setTotal(total);
        response.setResponseList(responseList);
        return response;
    }

    /**
     * @param principal
     * @return
     */
    @Override
    public CashInternalCreateResponse searchPeopleInternal(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        CashInternalCreateResponse response = new CashInternalCreateResponse();
        List<FnPeopleType> peopleTypeList = peopleTypeRepository.searchPeopleTypeOther(idSchool);
        List<PeopleTypeOtherResponse> peopleTypeAllList = listMapper.mapList(peopleTypeList, PeopleTypeOtherResponse.class);
        peopleTypeAllList.forEach(x -> x.setName(x.isDefaultStatus() == AppConstant.APP_TRUE ? x.getName() + " * " : x.getName()));
        List<PeopleTypeOtherResponse> internalList = peopleTypeAllList.stream().filter(a -> a.getType().equals(FinanceConstant.INTERNAL)).collect(Collectors.toList());
        response.setPeopleTypeInternalList(internalList);
        response.setPeopleTypeOtherList(peopleTypeAllList);
        response.setPaymentNote(principal.getSchoolConfig().isPaymentNote());
        response.setReceiptNote(principal.getSchoolConfig().isReceiptNote());
        return response;
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @Override
    public PeopleTypeResponse findDetailPeopleType(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        return modelMapper.map(fnPeopleType, PeopleTypeResponse.class);
    }

    /**
     * @param principal
     * @param request
     * @return
     */
    @Override
    public boolean createPeopleType(UserPrincipal principal, CreatePeopleTypeRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnPeopleType newData = modelMapper.map(request, FnPeopleType.class);
        newData.setIdSchool(principal.getIdSchoolLogin());
        peopleTypeRepository.save(newData);
        return true;
    }

    /**
     * @param principal
     * @param request
     * @return
     */
    @Override
    public boolean updatePeopleType(UserPrincipal principal, UpdatePeopleTypeRequest request) {
        CommonValidate.checkDataPlus(principal);
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(request.getId()).orElseThrow();
        modelMapper.map(request, fnPeopleType);
        fnPeopleType.setName(fnPeopleType.isDefaultStatus() ? AppConstant.SCHOOL_NAME : request.getName());
        peopleTypeRepository.save(fnPeopleType);
        return true;
    }

    /**
     * @param principal
     * @param id
     * @return
     */
    @Override
    public boolean deletePeopleTypeById(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        FnPeopleType fnPeopleType = peopleTypeRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        long count = fnPeopleType.getFnCashInternalList().size();
        long coutOther = fnPeopleType.getFnCashOtherList().size();
        if (count + coutOther > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.DELETE_PEOPLE_TYPE);
        }
        peopleTypeRepository.deleteById(id);
        return true;
    }
}
