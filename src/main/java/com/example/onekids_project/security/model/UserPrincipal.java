package com.example.onekids_project.security.model;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.entity.school.SchoolConfig;
import com.example.onekids_project.entity.system.SysConfig;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.response.school.SchoolResponse;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.response.system.SysConfigResponse;
import com.example.onekids_project.util.EmployeeUtil;
import com.example.onekids_project.util.PermissionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private Long id;

    private String fullName;

    private String username;

    @JsonIgnore
    private String passwordShow;

    @JsonIgnore
    private String passwordHash;

    private String appType;

    private Collection<? extends GrantedAuthority> authorities;

    private List<Long> idSchoolList;

    private Long idSchoolLogin;

    private List<Long> idClassList;

    private Long idClassLogin;

    private List<Long> idKidList;

    private Long idKidLogin;

    private SchoolConfigResponse schoolConfigResponse;

    private SysConfigResponse sysConfigResponse;

    private SchoolResponse schoolResponse;
//    private SysConfigResponse sysConfigResponse1;

    private SysInfor sysInfor;


    public UserPrincipal(Long id, String fullName, String username, String passwordShow, String passwordHash, String appType, Collection<? extends GrantedAuthority> authorities,
                         List<Long> idSchoolList, Long idSchoolLogin, List<Long> idClassList, Long idClassLogin, List<Long> idKidList, Long idKidLogin, SchoolConfigResponse schoolConfigResponse,
                         SysConfigResponse sysConfigResponse, SchoolResponse schoolResponse, SysInfor sysInfor
    ) {
        this.id = id;
        this.fullName = fullName;
        this.username = username;
        this.passwordShow = passwordShow;
        this.passwordHash = passwordHash;
        this.appType = appType;
        this.authorities = authorities;
        this.idSchoolList = idSchoolList;
        this.idSchoolLogin = idSchoolLogin;
        this.idClassList = idClassList;
        this.idClassLogin = idClassLogin;
        this.idKidList = idKidList;
        this.idKidLogin = idKidLogin;
        this.schoolConfigResponse = schoolConfigResponse;
        this.sysConfigResponse = sysConfigResponse;
        this.schoolResponse = schoolResponse;
        this.sysInfor = sysInfor;
    }

    public static UserPrincipal create(Boolean loginStatus, Long id, MaUser maUser, ModelMapper modelMapper, SchoolConfigRepository schoolConfigRepository, SysConfigRepository sysConfigRepository, SchoolRepository schoolRepository, MaClassRepository maClassRepository, KidsRepository kidsRepository, SysInforRepository sysInforRepository, ParentRepository parentRepository) {
        List<Long> idSchoolList = null;
        Long idSchoolLogin = null;
        List<Long> idClassList = null;
        Long idClassLogin = null;
        List<Long> idKidList = null;
        Long idKidLogin = null;
        SchoolConfig schoolConfig = null;
        SysConfig sysConfig = null;

        if (AppTypeConstant.PARENT.equals(maUser.getAppType())) {
            Parent parent = maUser.getParent();
            Kids kids;
            if (loginStatus) {
                kids = getKidsActive(parent, parentRepository);
            } else {
                Optional<Kids> kidsOptional = kidsRepository.getKidsDelActiveAndActivateCustom(id);
                if (kidsOptional.isEmpty()) {
                    kids = getKidsActive(parent, parentRepository);
                } else {
                    kids = kidsOptional.get();
                }
            }
            idKidLogin = loginStatus ? kids.getId() : id;
            idSchoolLogin = kids.getIdSchool();

        } else if (AppTypeConstant.TEACHER.equals(maUser.getAppType())) {
            //-------code moi-----------------
            Employee employee = maUser.getEmployee();
            if (loginStatus) {
                Long idClassInitial = employee.getIdClassLogin();
                List<InfoEmployeeSchool> infoIdClassList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.getExEmployeeClassList().stream().anyMatch(a -> a.getMaClass().getId().equals(idClassInitial))).collect(Collectors.toList());
                infoIdClassList = infoIdClassList.stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(infoIdClassList)) {
                    List<InfoEmployeeSchool> infoEmployeeSchoolList = EmployeeUtil.getInfoEmployeeList(employee);
                    if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_EMPLOYEE_EMPTY);
                    }
                    InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolList.get(0);
                    List<MaClass> maClassList = EmployeeUtil.getClassFromInfoEmployee(infoEmployeeSchool);
                    if (CollectionUtils.isEmpty(maClassList)) {
                        idClassLogin = 0L;
                    } else {
                        idClassLogin = maClassList.get(0).getId();
                    }
                    employee.setIdClassLogin(idClassLogin);
                    employee.setIdSchoolLogin(infoEmployeeSchool.getSchool().getId());
                }
                idClassLogin = employee.getIdClassLogin();
            } else {
                idClassLogin = id;
            }
//            idClassLogin = loginStatus ? employee.getIdClassLogin() : id;
            idSchoolLogin = employee.getIdSchoolLogin();
        } else if (AppTypeConstant.SCHOOL.equals(maUser.getAppType())) {
            if (loginStatus) {
                Employee employee = maUser.getEmployee();
                List<InfoEmployeeSchool> infoEmployeeSchoolIdSchoolList = employee.getInfoEmployeeSchoolList().stream().filter(x -> x.getSchool().getId().equals(employee.getIdSchoolLogin()) && x.isDelActive() && x.isActivated()).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(infoEmployeeSchoolIdSchoolList)) {
                    List<InfoEmployeeSchool> infoEmployeeSchoolList = EmployeeUtil.getInfoEmployeeList(employee);
                    if (CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_EMPLOYEE_EMPTY);
                    }
                    InfoEmployeeSchool infoEmployeeSchool = infoEmployeeSchoolList.get(0);
                    idSchoolLogin = infoEmployeeSchool.getSchool().getId();
                    employee.setIdSchoolLogin(idSchoolLogin);
                }
                idSchoolLogin = employee.getIdSchoolLogin();
            } else {
                idSchoolLogin = id;
            }
//            idSchoolLogin = loginStatus ? maUser.getEmployee().getIdSchoolLogin() : id;
        } else if (AppTypeConstant.SUPPER_SCHOOL.equals(maUser.getAppType())) {
            idSchoolLogin = maUser.getSchoolMaster().getSchool().getId();
        }
        Optional<SchoolConfig> schoolConfigOptional = schoolConfigRepository.findBySchoolIdAndDelActive(idSchoolLogin, AppConstant.APP_TRUE);
        Optional<SysConfig> sysConfigOptional = sysConfigRepository.findBySchoolIdAndDelActive(idSchoolLogin, AppConstant.APP_TRUE);
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActiveTrue(idSchoolLogin);
        Optional<SysInfor> sysInforOptional = sysInforRepository.findFirstByDelActiveTrue();
        SchoolConfigResponse schoolConfigResponse = null;
        SysConfigResponse sysConfigResponse = null;
        SchoolResponse schoolResponse = null;
        SysInfor sysInfor = null;
        if (schoolConfigOptional.isPresent()) {
            schoolConfigResponse = modelMapper.map(schoolConfigOptional.get(), SchoolConfigResponse.class);
        }
        if (sysConfigOptional.isPresent()) {
            sysConfigResponse = modelMapper.map(sysConfigOptional.get(), SysConfigResponse.class);
        }
        if (schoolOptional.isPresent()) {
            schoolResponse = modelMapper.map(schoolOptional.get(), SchoolResponse.class);
        }
        if (sysInforOptional.isPresent()) {
            sysInfor = sysInforOptional.get();
        }

        Set<String> apiSet = PermissionUtils.getApiOfUser(maUser, idSchoolLogin);
        List<GrantedAuthority> authorities = apiSet.stream().map(api ->
                new SimpleGrantedAuthority("ROLE_" + api)
        ).collect(Collectors.toList());

        return new UserPrincipal(
                maUser.getId(),
                maUser.getFullName(),
                maUser.getUsername(),
                maUser.getPasswordShow(),
                maUser.getPasswordHash(),
                maUser.getAppType(),
                authorities,
                idSchoolList,
                idSchoolLogin,
                idClassList,
                idClassLogin,
                idKidList,
                idKidLogin,
                schoolConfigResponse,
                sysConfigResponse,
                schoolResponse,
                sysInfor
        );
    }

    /**
     * hàm chuyển đổi chuỗi String thành chuỗi Long
     *
     * @param var
     * @return
     */
    private static List<Long> convertStringToLong(String var) {
        if (StringUtils.isBlank(var)) {
            return null;
        }
        String var1 = var.trim();
        boolean valid = var1.matches("[0-9,]*");
        if (!valid) {
            return null;
        }
        List<String> stringList = List.of(var1.split(","));
        List<String> filterString = stringList.stream().filter(x -> StringUtils.isNotBlank(x)).collect(Collectors.toList());
        List<Long> longList = filterString.stream().map(x -> Long.parseLong(x)).collect(Collectors.toList());
        return longList;
    }

    private static Kids getKidsActive(Parent parent, ParentRepository parentRepository) {
        Kids kids;
        List<Kids> kidsList = parent.getKidsList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kidsList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_KID_EMPTY);
        }
        List<Kids> kidsActiveList = kidsList.stream().filter(Kids::isActivated).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kidsActiveList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.ACCOUNT_KID_ACTIVE);
        }
        List<Kids> kidsLoginList = kidsActiveList.stream().filter(x -> x.getId().equals(parent.getIdKidLogin())).collect(Collectors.toList());
        //lấy hs khác trường
        if (CollectionUtils.isEmpty(kidsLoginList)) {
            kids = kidsActiveList.get(0);
        } else {
            //lấy hs cùng trường trước
            kids = kidsLoginList.get(0);
        }
        parent.setIdKidLogin(kids.getId());
        parentRepository.save(parent);
        return kids;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getAppType() {
        return appType;
    }

    public List<Long> getIdSchoolList() {
        return idSchoolList;
    }

    public Long getIdSchoolLogin() {
        return idSchoolLogin;
    }

    public List<Long> getIdClassList() {
        return idClassList;
    }

    public Long getIdClassLogin() {
        return idClassLogin;
    }

    public List<Long> getIdKidList() {
        return idKidList;
    }

    public Long getIdKidLogin() {
        return idKidLogin;
    }

    public SysInfor getSysInfor() {
        return sysInfor;
    }

    public SchoolConfigResponse getSchoolConfig() {
        return schoolConfigResponse;
    }

    public SysConfigResponse getSysConfig() {
        return sysConfigResponse;
    }

    public SchoolResponse getSchool() {
        return schoolResponse;
    }

    public String getPasswordShow() {
        return passwordShow;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
