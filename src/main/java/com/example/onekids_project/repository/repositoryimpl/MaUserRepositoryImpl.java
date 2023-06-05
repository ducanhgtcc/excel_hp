package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AccountTypeConstant;
import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.dto.MaUserDTO;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.master.request.school.SearchAccountRequest;
import com.example.onekids_project.repository.repositorycustom.MaUserRepositoryCustom;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchTeacherBirthDayRequest;
import com.example.onekids_project.security.payload.LoginMobileRequest;
import com.example.onekids_project.security.payload.LoginRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MaUserRepositoryImpl extends BaseRepositoryimpl<MaUser> implements MaUserRepositoryCustom {

    @Override
    public List<MaUser> findAllMaUser(MaUserDTO maUser, Pageable pageable) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (maUser != null) {
            if (StringUtils.isNotBlank(maUser.getUsername())) {
                queryStr.append("and username like :username");
                mapParams.put("username", maUser.getUsername());
            }
        }
        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public List<MaUser> getMaUsersUsername(String username) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isBlank(username)) {
            return null;
        } else {
            queryStr.append("and username=:username ");
            mapParams.put("username", username);
        }

        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaUser> findMaUsersLogin(LoginRequest loginRequest) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (loginRequest.equals(null)) {
            return null;
        }
        if (StringUtils.isNotBlank(loginRequest.getUsername())) {
            queryStr.append("and username=:username ");
            mapParams.put("username", loginRequest.getUsername());
        }
        if (StringUtils.isNotBlank(loginRequest.getAppType())) {
            queryStr.append("and app_type=:appType ");
            mapParams.put("appType", loginRequest.getAppType());
        }
        queryStr.append("and activated=:activated ");
        mapParams.put("activated", AppConstant.APP_TRUE);

        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaUser> findMaUsersLoginMobile(LoginMobileRequest loginMobileRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (StringUtils.isNotBlank(loginMobileRequest.getUsername())) {
            queryStr.append("and username=:username ");
            mapParams.put("username", loginMobileRequest.getUsername());
        }
        if (StringUtils.isNotBlank(loginMobileRequest.getAppType())) {
            queryStr.append("and app_type=:appType ");
            mapParams.put("appType", loginMobileRequest.getAppType());
        }
        queryStr.append("and activated=:activated ");
        mapParams.put("activated", AppConstant.APP_TRUE);

        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaUser> searchTeacherBirthday(Long idSchoolLogin, SearchTeacherBirthDayRequest searchTeacherBirthDayRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchTeacherBirthDayRequest != null) {
            queryStr.append(" and exists(select*from ma_employee as a, info_employee_school as b where model.app_type=:appType and model.id = a.id_ma_user and a.id = b.id_employee )");
            mapParams.put("appType", AppTypeConstant.TEACHER);
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchoolLogin);
            if (searchTeacherBirthDayRequest.getDate() != null) {
                queryStr.append("and b.birthday=:birthday ");
                mapParams.put("birthday", searchTeacherBirthDayRequest.getDate());
            }
            if (searchTeacherBirthDayRequest.getWeek() != null) {
                queryStr.append("and b.birthday >=:monday ");
                mapParams.put("monday", searchTeacherBirthDayRequest.getWeek().toString());
                queryStr.append(" and b.birthday <=:sunday ");
                mapParams.put("sunday", searchTeacherBirthDayRequest.getWeek().plusDays(6).toString());
            }
            if (searchTeacherBirthDayRequest.getMonth() != null) {
                queryStr.append("and month(b.birthday) =:month ");
                mapParams.put("month", searchTeacherBirthDayRequest.getMonth().getMonthValue());
            }
            if (StringUtils.isNotBlank(searchTeacherBirthDayRequest.getName())) {
                queryStr.append("and b.full_name like :fullName ");
                mapParams.put("fullName", "%" + searchTeacherBirthDayRequest.getName() + "%");
            }


        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaUser> searchParentBirthday(Long idSchool, SearchParentBirthDayRequest searchParentBirthDayRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchParentBirthDayRequest != null) {
//            queryStr.append("and id_school=:idSchool ");
//            mapParams.put("idSchool", idSchool);
            queryStr.append(" and exists(select*from ma_parent as a, ma_kids as b where model.app_type=:appType and model.id = a.id_ma_user and a.id = b.id_parent ");
            mapParams.put("appType", AppTypeConstant.PARENT);

            if (searchParentBirthDayRequest.getDate() != null) {
                queryStr.append(" and ((representation LIKE 'Bố' and father_birthday =:birthday) || (representation LIKE 'Mẹ' and mother_birthday=:birthday))");
                mapParams.put("birthday", searchParentBirthDayRequest.getDate());
            }
            if (searchParentBirthDayRequest.getWeek() != null) {
                queryStr.append(" and ((representation LIKE 'Bố' and father_birthday>=:monday) || (representation LIKE 'Mẹ' and mother_birthday>=:monday))");
                mapParams.put("monday", searchParentBirthDayRequest.getWeek());

                queryStr.append(" and ((representation LIKE 'Bố' and b.father_birthday<=:sunday) || (representation LIKE 'Mẹ' and mother_birthday<=:sunday)) ");
                mapParams.put("sunday", searchParentBirthDayRequest.getWeek().plusDays(6));
            }
            if (searchParentBirthDayRequest.getMonth() != null) {
                queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month)) ");
                mapParams.put("month", searchParentBirthDayRequest.getMonth().getMonthValue());
            }
            if (StringUtils.isNotBlank(searchParentBirthDayRequest.getName())) {
                queryStr.append(" and ((representation LIKE 'Bố' and mother_name like :fatherName) || (representation LIKE 'Mẹ' and father_name like :motherName)) ");
                mapParams.put("motherName", "%" + searchParentBirthDayRequest.getName() + "%");
                mapParams.put("fatherName", "%" + searchParentBirthDayRequest.getName() + "%");
            }
            queryStr.append(")");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaUser> findAccount(SearchAccountRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAccount(queryStr, mapParams, request);
        boolean checkDelete = request.getDeleteStatus().equals(AppConstant.DELETE_TRUE) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE;
        queryStr.append("order by id desc ");
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), checkDelete);
    }

    @Override
    public Long countTotalAccount(SearchAccountRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchAccount(queryStr, mapParams, request);
        boolean checkDelete = request.getDeleteStatus().equals(AppConstant.DELETE_TRUE) ? AppConstant.APP_TRUE : AppConstant.APP_FALSE;
        return countAllDeleteOrNot(queryStr.toString(), mapParams, checkDelete);
    }

    @Override
    public List<MaUser> findAccountHasExtendUsername(String username) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and username like :username ");
        mapParams.put("username", username + "_%");
        return findAllNoPagingDeleteOrNot(queryStr.toString(), mapParams, AppConstant.APP_FALSE);
    }

    @Override
    public List<MaUser> getUserExpired(int number) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and activated=true ");
        queryStr.append("and case when trial_status=true and demo_status=true and to_demo_date is not null then datediff(date(to_demo_date), current_date)>=0 and datediff(date(to_demo_date), current_date)<=:number " +
                "when trial_status=false and unlimit_time=true and to_date is not null then datediff(date(to_date), current_date)>=0 and datediff(date(to_date), current_date)<=:number else false end ");
        mapParams.put("number", number);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<MaUser> getUserExpiredHandle() {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and activated=true ");
        queryStr.append("and case when trial_status=true and demo_status=true and to_demo_date is not null then datediff(date(to_demo_date), current_date)<0 when trial_status=false and unlimit_time=true and to_date is not null then datediff(date(to_date), current_date)<0 else false end ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    private void setSearchAccount(StringBuilder queryStr, Map<String, Object> mapParams, SearchAccountRequest request) {
        if (StringUtils.isNotBlank(request.getAppType())) {
            queryStr.append("and app_type=:appType ");
            mapParams.put("appType", request.getAppType());
        }
        if (StringUtils.isNotBlank(request.getNameOrPhone())) {
            String nameOrPhone = request.getNameOrPhone().trim();
            queryStr.append("and (lower(full_name) like lower(:fullName) ");
            mapParams.put("fullName", "%" + nameOrPhone + "%");
            queryStr.append("or lower(phone) like lower(:phone)) ");
            mapParams.put("phone", "%" + nameOrPhone + "%");
        }
        if (request.getActive() != null) {
            queryStr.append("and activated=:activated ");
            mapParams.put("activated", request.getActive());
        }
        if (request.getDeleteStatus().equals(AppConstant.DELETE_COMPLETE)) {
            //tài khoản đã có đuổi ở đằng sau
            queryStr.append("and username like '%#___%' ");
            if (StringUtils.isNotBlank(request.getTypeDelete())) {
                queryStr.append("and type_delete=:typeDelete ");
                mapParams.put("typeDelete", request.getTypeDelete());
            }
        } else {
            //tài khoản của 3 đối tượng: plus, teacher, parent
            queryStr.append("and username like '%#__' ");
            if (StringUtils.isNotBlank(request.getTypeChildren())) {
                if (request.getTypeChildren().equals(AccountTypeConstant.ACCOUNT_YES)) {
                    queryStr.append("and start_date_delete is null ");
                } else {
                    queryStr.append("and start_date_delete is not null ");
                }
            }
        }

    }
}
