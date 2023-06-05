package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.user.Api;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.Role;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.ApiRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.RoleRepository;
import com.example.onekids_project.request.schoolconfig.RoleCreateConfigRequest;
import com.example.onekids_project.request.schoolconfig.RoleUpdateConfigRequest;
import com.example.onekids_project.request.user.AppTypeRequest;
import com.example.onekids_project.request.user.SearchRoleRequest;
import com.example.onekids_project.response.schoolconfig.RoleConfigResponse;
import com.example.onekids_project.response.user.ApiResponse;
import com.example.onekids_project.response.user.RoleForUserResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Override
    public List<RoleConfigResponse> searchRole(UserPrincipal principal, SearchRoleRequest request) {
        List<Role> roleList = roleRepository.searchRole(principal.getIdSchoolLogin(), request);
        return listMapper.mapList(roleList, RoleConfigResponse.class);
    }

    @Override
    public RoleConfigResponse findRoleForUser(UserPrincipal principal, Long id) {
        Role role = roleRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("Lỗi tìm kiếm role theo id"));
        RoleConfigResponse roleConfigResponse = modelMapper.map(role, RoleConfigResponse.class);
        String type = AppTypeConstant.TEACHER.equals(role.getType()) ? AppTypeConstant.SCHOOL : role.getType();
        List<Api> apiList = apiRepository.findByTypeAndDelActiveTrueOrderByRanks(type);
        if (!CollectionUtils.isEmpty(apiList)) {
            List<ApiResponse> apiResponseList = listMapper.mapList(apiList, ApiResponse.class);
            List<Long> longList = role.getApiList().stream().map(Api::getId).collect(Collectors.toList());
            longList.forEach(x -> apiResponseList.forEach(y -> {
                if (x.equals(y.getId())) {
                    y.setUsed(AppConstant.APP_TRUE);
                }
            }));
            roleConfigResponse.setApiList(apiResponseList);
        }
        return roleConfigResponse;
    }

    @Transactional
    @Override
    public boolean createRole(UserPrincipal principal, RoleCreateConfigRequest request) {
        Role role = modelMapper.map(request, Role.class);
        role.setIdSchool(principal.getIdSchoolLogin());
        Role roleSaved = roleRepository.save(role);
        request.getIdApiList().forEach(x -> roleRepository.insertRoleAPi(roleSaved.getId(), x));
        return true;
    }

    @Transactional
    @Override
    public boolean updateRole(UserPrincipal principal, RoleUpdateConfigRequest request) {
        Role role = roleRepository.findByIdAndIdSchoolAndDelActiveTrue(request.getId(), principal.getIdSchoolLogin()).orElseThrow(() -> new NotFoundException("Lỗi tìm kiếm role theo id"));
        if (!role.isDefaultStatus()) {
            role.setRoleName(request.getRoleName());
        }
        role.setDescription(request.getDescription());
        roleRepository.save(role);
        roleRepository.deleteRoleAPi(request.getId());
        request.getIdApiList().forEach(x -> roleRepository.insertRoleAPi(request.getId(), x));
        return true;
    }

    @Override
    public boolean deleteRoleOne(UserPrincipal principal, Long id) {
        Role role = roleRepository.findByIdAndIdSchoolAndDelActiveTrue(id, principal.getIdSchoolLogin()).orElseThrow(() -> new NoSuchElementException("Lỗi tìm kiếm role theo id"));
        this.checkBeforeDeleteRole(role);
        role.setDelActive(AppConstant.APP_FALSE);
        roleRepository.save(role);
        return true;
    }

    private void checkBeforeDeleteRole(Role role) {
        if (role.isDefaultStatus()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đây là vai trò mặc định");
        }
        long count = role.getMaUsersList().stream().filter(BaseEntity::isDelActive).count();
        if (count > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vai trò này đang được áp dụng cho " + count + " người dùng");
        }
    }

    @Override
    public List<RoleForUserResponse> findRoleOfUser(UserPrincipal principal, Long idUser, AppTypeRequest request) {
        maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow(() -> new NotFoundException("not found maUser in role service by id"));
        Long idSchool = principal.getIdSchoolLogin();
        List<Role> roleList = roleRepository.findByIdSchoolAndTypeAndDelActiveTrue(idSchool, request.getType());
        List<Long> roleOfUserList = roleRepository.findByIdSchoolAndMaUsersListIdAndDelActiveTrue(idSchool, idUser).stream().map(BaseEntity::getId).collect(Collectors.toList());
        List<RoleForUserResponse> responseList = listMapper.mapList(roleList, RoleForUserResponse.class);
        responseList.stream().filter(x -> roleOfUserList.stream().anyMatch(y -> x.getId().equals(y))).forEach(a -> a.setUsed(AppConstant.APP_TRUE));
        return responseList;
    }

    @Transactional
    @Override
    public boolean updateRoleOfUser(UserPrincipal principal, Long idUser, List<Long> longList) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(idUser).orElseThrow(() -> new NotFoundException("not found maUser by id"));
        List<Long> idRoleList = maUser.getRoleList().stream().filter(x -> x.getIdSchool().equals(principal.getIdSchoolLogin())).map(BaseEntity::getId).collect(Collectors.toList());
        maUserRepository.deleteUserRole(idUser, idRoleList);
        longList.forEach(x -> maUserRepository.insertUserRole(idUser, x));
        return true;
    }

    @Override
    public void createRoleForSchool(School school) {
        Long idSchool = school.getId();
        Optional<Role> roleConfig = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(idSchool, AppConstant.ROLE_CONFIG);
        if (roleConfig.isEmpty()) {
            Role role = new Role();
            role.setRoleName(AppConstant.ROLE_CONFIG);
            role.setType(AppTypeConstant.SCHOOL);
            role.setIdSchool(idSchool);
            role.setDefaultStatus(AppConstant.APP_TRUE);
            role.setDescription("Có các quyền cấu hình nhà trường");
            roleRepository.save(role);
        }
        Optional<Role> roleCommon = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(idSchool, AppConstant.ROLE_COMMON);
        if (roleCommon.isEmpty()) {
            Role role = new Role();
            role.setRoleName(AppConstant.ROLE_COMMON);
            role.setType(AppTypeConstant.SCHOOL);
            role.setIdSchool(idSchool);
            role.setDefaultStatus(AppConstant.APP_TRUE);
            role.setDescription("Có các quyền chung");
            roleRepository.save(role);
        }
        Optional<Role> roleFinance = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(idSchool, AppConstant.ROLE_FINANCE);
        if (roleFinance.isEmpty()) {
            Role role = new Role();
            role.setRoleName(AppConstant.ROLE_FINANCE);
            role.setType(AppTypeConstant.SCHOOL);
            role.setIdSchool(idSchool);
            role.setDefaultStatus(AppConstant.APP_TRUE);
            role.setDescription("Có các quyền về tài chính");
            roleRepository.save(role);
        }
        Optional<Role> roleTeacher = roleRepository.findByIdSchoolAndDelActiveTrueAndRoleName(idSchool, AppConstant.ROLE_TEACHER);
        if (roleTeacher.isEmpty()) {
            Role role = new Role();
            role.setRoleName(AppConstant.ROLE_TEACHER);
            role.setType(AppTypeConstant.TEACHER);
            role.setIdSchool(idSchool);
            role.setDefaultStatus(AppConstant.APP_TRUE);
            role.setDescription("Các vai trò chung của giáo viên");
            roleRepository.save(role);
        }
    }

    @Override
    public void addApiForRoleDefault(School school) {
        Long idSchool = school.getId();
        Role roleConfig = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(idSchool, AppConstant.ROLE_CONFIG).orElseThrow();
        if (CollectionUtils.isEmpty(roleConfig.getApiList())) {
            List<Api> apiList = apiRepository.getApiFromTo(45, 63);
            apiList.forEach(a -> roleRepository.insertRoleAPi(roleConfig.getId(), a.getId()));
        }
        Role roleCommon = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(idSchool, AppConstant.ROLE_COMMON).orElseThrow();
        if (CollectionUtils.isEmpty(roleCommon.getApiList())) {
            List<Api> apiList = apiRepository.getApiFromTo(64, 121);
            apiList.forEach(a -> roleRepository.insertRoleAPi(roleCommon.getId(), a.getId()));
        }
        Role roleFinance = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(idSchool, AppConstant.ROLE_FINANCE).orElseThrow();
        if (CollectionUtils.isEmpty(roleFinance.getApiList())) {
            List<Api> apiList = apiRepository.getApiFromTo(1, 44);
            apiList.forEach(a -> roleRepository.insertRoleAPi(roleFinance.getId(), a.getId()));
        }
        Role roleTeacher = roleRepository.findByIdSchoolAndRoleNameAndDelActiveTrue(idSchool, AppConstant.ROLE_TEACHER).orElseThrow();
        if (CollectionUtils.isEmpty(roleTeacher.getApiList())) {
            List<Long> idApiList = Arrays.asList(70L, 84L, 95L, 71L, 72L, 73L, 74L, 75L, 76L, 91L, 92L, 93L, 94L, 96L, 97L, 98L, 99L, 100L, 101L, 102L, 103L, 104L, 105L, 106L, 107L, 108L, 109L, 110L, 111L, 112L, 129L, 130L);
            List<Api> apiList = apiRepository.findByIdInAndDelActiveTrue(idApiList);
            apiList.forEach(a -> roleRepository.insertRoleAPi(roleTeacher.getId(), a.getId()));
        }
    }


}
