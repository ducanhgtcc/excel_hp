package com.example.onekids_project.service.serviceimpl.groupout;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.employee.GroupOutEmployee;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.GroupOutEmployeeRepository;
import com.example.onekids_project.request.groupout.GroupOutRequest;
import com.example.onekids_project.response.groupout.GroupOutNameResponse;
import com.example.onekids_project.response.groupout.GroupOutResponse;
import com.example.onekids_project.response.groupout.ListGroupOutResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.groupout.GroupOutEmployeeService;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-07-14 8:32 AM
 *
 * @author nguyễn văn thụ
 */
@Getter
@Setter
@Service
public class GroupOutEmployeeImpl implements GroupOutEmployeeService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ListMapper listMapper;
    @Autowired
    private GroupOutEmployeeRepository groupOutEmployeeRepository;

    @Override
    public ListGroupOutResponse searchGroupOutEmployee(UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        ListGroupOutResponse response = new ListGroupOutResponse();
        List<GroupOutResponse> responseList = new ArrayList<>();
        List<GroupOutEmployee> groupOutEmployeeList = groupOutEmployeeRepository.findAllByIdSchoolAndDelActiveTrue(idSchool);
        groupOutEmployeeList.forEach(x -> {
            GroupOutResponse groupOutResponse = modelMapper.map(x, GroupOutResponse.class);
            groupOutResponse.setNumber(x.getInfoEmployeeSchoolList().size());
            responseList.add(groupOutResponse);
        });
        response.setGroupOutResponseList(responseList);
        return response;
    }

    @Override
    public List<GroupOutNameResponse> findAllGroupNameEmployee(UserPrincipal principal) {
        List<GroupOutEmployee> groupOutEmployeeList = groupOutEmployeeRepository.findAllByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        return listMapper.mapList(groupOutEmployeeList, GroupOutNameResponse.class);
    }

    @Override
    @Transactional
    public boolean createGroupOutEmployee(UserPrincipal principal, GroupOutRequest request) {
        if (AppConstant.GROUP_OUT_ABSENT.equals(request.getName()) || AppConstant.GROUP_OUT_OTHER.equals(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trùng với tên thư mục mặc định");
        }
        GroupOutEmployee groupOutEmployee = modelMapper.map(request, GroupOutEmployee.class);
        groupOutEmployee.setIdSchool(principal.getIdSchoolLogin());
        groupOutEmployeeRepository.save(groupOutEmployee);
        return true;
    }

    @Override
    @Transactional
    public boolean updateGroupOutEmployee(UserPrincipal principal, Long id, GroupOutRequest request) {
        GroupOutEmployee groupOutEmployeeOld = groupOutEmployeeRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        if (groupOutEmployeeOld.isDefaultStatus()) {
            request.setName(groupOutEmployeeOld.getName());
        }
        modelMapper.map(request, groupOutEmployeeOld);
        groupOutEmployeeRepository.save(groupOutEmployeeOld);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteGroupOutEmployee(UserPrincipal principal, Long id) {
        GroupOutEmployee groupOutEmployee = groupOutEmployeeRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        if (groupOutEmployee.getInfoEmployeeSchoolList().size() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thư mục có chứa nhân sự");
        }
        if (!groupOutEmployee.isDefaultStatus()) {
            groupOutEmployee.setDelActive(AppConstant.APP_FALSE);
            groupOutEmployeeRepository.save(groupOutEmployee);
        }
        return true;
    }
}
