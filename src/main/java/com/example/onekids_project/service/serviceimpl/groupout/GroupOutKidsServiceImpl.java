package com.example.onekids_project.service.serviceimpl.groupout;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.entity.kids.GroupOutKids;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.GroupOutKidsRepository;
import com.example.onekids_project.request.groupout.GroupOutRequest;
import com.example.onekids_project.response.groupout.GroupOutResponse;
import com.example.onekids_project.response.groupout.GroupOutNameResponse;
import com.example.onekids_project.response.groupout.ListGroupOutResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.groupout.GroupOutKidsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * date 2021-07-12 2:25 PM
 *
 * @author nguyễn văn thụ
 */
@Service
public class GroupOutKidsServiceImpl implements GroupOutKidsService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GroupOutKidsRepository groupOutKidsRepository;

    @Override
    public ListGroupOutResponse searchGroupOutKids(UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        ListGroupOutResponse response = new ListGroupOutResponse();
        List<GroupOutResponse> responseList = new ArrayList<>();
        List<GroupOutKids> groupOutKidsList = groupOutKidsRepository.findAllByIdSchoolAndDelActiveTrue(idSchool);
        groupOutKidsList.forEach(x -> {
            GroupOutResponse groupOutResponse = modelMapper.map(x, GroupOutResponse.class);
            groupOutResponse.setNumber(x.getKidsList().size());
            responseList.add(groupOutResponse);
        });
        response.setGroupOutResponseList(responseList);
        return response;
    }

    @Override
    public List<GroupOutNameResponse> findAllGroupName(UserPrincipal principal) {
        List<GroupOutKids> groupOutKidsList = groupOutKidsRepository.findAllByIdSchoolAndDelActiveTrue(principal.getIdSchoolLogin());
        return listMapper.mapList(groupOutKidsList, GroupOutNameResponse.class);
    }

    @Override
    public boolean createGroupOutKids(UserPrincipal principal, GroupOutRequest request) {
        if (AppConstant.GROUP_OUT_ABSENT.equals(request.getName()) || AppConstant.GROUP_OUT_LEAVE.equals(request.getName()) || AppConstant.GROUP_OUT_OTHER.equals(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trùng với tên thư mục mặc định");
        }
        GroupOutKids groupOutKids = modelMapper.map(request, GroupOutKids.class);
        groupOutKids.setIdSchool(principal.getIdSchoolLogin());
        groupOutKidsRepository.save(groupOutKids);
        return true;
    }

    @Override
    public boolean updateGroupOutKids(UserPrincipal principal, Long id, GroupOutRequest request) {
        GroupOutKids groupOutKidsOld = groupOutKidsRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        if (groupOutKidsOld.isDefaultStatus()) {
            request.setName(groupOutKidsOld.getName());
        }
        modelMapper.map(request, groupOutKidsOld);
        groupOutKidsRepository.save(groupOutKidsOld);
        return true;
    }

    @Override
    public boolean deleteGroupOutKids(UserPrincipal principal, Long id) {
        GroupOutKids groupOutKids = groupOutKidsRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow();
        if (groupOutKids.getKidsList().size() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thư mục có chứa học sinh");
        }
        if (!groupOutKids.isDefaultStatus()) {
            groupOutKids.setDelActive(AppConstant.APP_FALSE);
            groupOutKidsRepository.save(groupOutKids);
        }
        return true;
    }
}
