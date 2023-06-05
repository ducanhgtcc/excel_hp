package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.EmployeeDTO;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.repository.EmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.UpdateReiceiversRequest;
import com.example.onekids_project.response.birthdaymanagement.KidBirthdayResponse;
import com.example.onekids_project.response.birthdaymanagement.ListTeacherBirthDayResponse;
import com.example.onekids_project.response.birthdaymanagement.TeacherBirthdayResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.TeacherBirthdayService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class TeacherBirthdayServiceImpl implements TeacherBirthdayService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Optional<EmployeeDTO> findByIdEmployee(UserPrincipal principal, Long idSchoolLogin, Long id) {
        CommonValidate.checkDataPlus(principal);
        Optional<Employee> employeeOptional = employeeRepository.findByIdEmployee(idSchoolLogin, id);
        if (employeeOptional.isEmpty()) {
            return Optional.empty();
        }
        Optional<EmployeeDTO> employeeDTOOptional = Optional.ofNullable(modelMapper.map(employeeOptional.get(), EmployeeDTO.class));
        return employeeDTOOptional;
    }

    @Override
    public KidBirthdayResponse updateApprove(Long idSchoolLogin, UserPrincipal principal, UpdateReiceiversRequest updateReiceiversEditRequest) {
        return null;
    }

    @Override
    public ListTeacherBirthDayResponse searchTeacherBirthdayNewa(UserPrincipal principal, SearchTeacherBirthDayRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListTeacherBirthDayResponse response = new ListTeacherBirthDayResponse();
        List<InfoEmployeeSchool> employeeSchoolList = infoEmployeeSchoolRepository.searchEmplyeenewa(idSchool, request);
        List<TeacherBirthdayResponse> responseList = new ArrayList<>();
        employeeSchoolList.forEach(x -> {
            TeacherBirthdayResponse model = new TeacherBirthdayResponse();
            model.setId(x.getId());
            model.setBirthday(x.getBirthday());
            model.setFullName(x.getFullName());
            model.setGender(x.getGender());
            model.setPhone(x.getPhone());
            responseList.add(model);
        });
        long total = infoEmployeeSchoolRepository.countSearchTeacherBirthday(idSchool, request);
        response.setTotal(total);
        response.setResponseList(responseList);
        return response;
    }
}





