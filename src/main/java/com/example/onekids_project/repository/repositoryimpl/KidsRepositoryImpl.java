package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.SampleConstant;
import com.example.onekids_project.common.SystemConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.master.request.kids.KidsSearchAdminRequest;
import com.example.onekids_project.mobile.plus.request.birthday.SearchKidsBirthdayPlusRequest;
import com.example.onekids_project.mobile.plus.request.kidsQuality.SearchKidsQualityPlusRequest;
import com.example.onekids_project.repository.repositorycustom.KidsRepositoryCustom;
import com.example.onekids_project.request.birthdaymanagement.SearchKidsBirthDayRequest;
import com.example.onekids_project.request.birthdaymanagement.SearchParentBirthDayRequest;
import com.example.onekids_project.request.evaluatekids.EvaluatePeriodicSearchRequest;
import com.example.onekids_project.request.finance.statistical.FinanceSearchKidsRequest;
import com.example.onekids_project.request.kids.*;
import com.example.onekids_project.request.kids.transfer.SearchKidsTransferRequest;
import com.example.onekids_project.request.kidsheightweight.SearchKidsHeightWeightRequest;
import com.example.onekids_project.request.studentgroup.SearchStudentGroupRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class KidsRepositoryImpl extends BaseRepositoryimpl<Kids> implements KidsRepositoryCustom {
    @Override
    public List<Kids> findAllKids(Long idSchool, Pageable pageable) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        queryStr.append("order by first_name asc ");
        return findAll(queryStr.toString(), mapParams, pageable);
    }

    @Override
    public Optional<Kids> findByIdKid(Long idSchool, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Kids> kidsList = findAllNoPaging(queryStr.toString(), mapParams);
        if (kidsList.size() > 0) {
            return Optional.ofNullable(kidsList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Kids> searchKids(Long idSchool, SearchKidsRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchKids(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<Kids> searchKidsTransfer(Long idSchool, SearchKidsTransferRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchKidsTransfer(queryStr, mapParams, idSchool, request);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public List<Kids> searchKidsGroupOut(Long idSchool, SearchKidsGroupOutRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getGroupOutKids(queryStr, mapParams, idSchool, request);
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), AppConstant.APP_FALSE);
    }

    @Override
    public List<Kids> searchKidsGroupOutExcel(Long idSchool, ExcelGroupOutRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getGroupOutKidsExcel(queryStr, mapParams, idSchool, request);
        return findAllNoPagingDeleteOrNot(queryStr.toString(), mapParams, AppConstant.APP_FALSE);
    }

    @Override
    public long countSearchKids(Long idSchool, SearchKidsRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchKids(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public long countSearchKidsTransfer(Long idSchool, SearchKidsTransferRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchKidsTransfer(queryStr, mapParams, idSchool, request);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public long countSearchKidsGroupOut(Long idSchool, SearchKidsGroupOutRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.getGroupOutKids(queryStr, mapParams, idSchool, request);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, AppConstant.APP_FALSE);
    }

    @Override
    public long countSearchKidsChart(Long idSchool, Long idGrade, Long idClass) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (idGrade != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", idGrade);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("and kid_status=:kidStatus ");
        mapParams.put("kidStatus", AppConstant.STUDYING);
        return countAll(queryStr.toString(), mapParams);
    }


    @Override
    public List<Kids> searchKidsAdmin(KidsSearchAdminRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSchoolAdmin(queryStr, mapParams, request, idSchoolList);
        queryStr.append("order by id_parent is null desc, father_phone !='' desc, mother_phone !='' desc, id_school, first_name asc ");
        return findAllWebPagingDeleteOrNot(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem(), request.isDeleteStatus());
    }


    @Override
    public long countSearchAdminKids(KidsSearchAdminRequest request, List<Long> idSchoolList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchSchoolAdmin(queryStr, mapParams, request, idSchoolList);
        return countAllDeleteOrNot(queryStr.toString(), mapParams, request.isDeleteStatus());
    }

    @Override
    public List<Kids> searchKidsByGradeClass(Long idSchool, SearchKidsExportRequest searchKidsExportRequest) {

        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (searchKidsExportRequest != null) {

            if (idSchool != null) {
                queryStr.append("and id_school=:idSchool ");
                mapParams.put("idSchool", idSchool);
            }
            if (searchKidsExportRequest.getIdGrade() != null) {
                queryStr.append("and id_grade=:idGrade ");
                mapParams.put("idGrade", searchKidsExportRequest.getIdGrade());
            }
            if (searchKidsExportRequest.getIdClass() != null) {
                queryStr.append("and id_class=:idClass ");
                mapParams.put("idClass", searchKidsExportRequest.getIdClass());
            }
            if (StringUtils.isNotBlank(searchKidsExportRequest.getStatus())) {
                queryStr.append("and kid_status=:kidStatus ");
                mapParams.put("kidStatus", searchKidsExportRequest.getStatus());
            }
            queryStr.append("order by id_class,full_name asc ");
        }

        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchKidsHeightWeight(Long idSchool, SearchKidsHeightWeightRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", request.getIdClass());

        if (StringUtils.isNotBlank(request.getStatus())) {
            queryStr.append("and kid_status=:kidStatus ");
            mapParams.put("kidStatus", request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getCodeOrName())) {
            queryStr.append("and full_name like :fullName ");
            mapParams.put("fullName", "%" + request.getCodeOrName() + "%");
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    /* List danh sách cân nặng chiều cao tất cả học sinh theo lựa chọn
     *  @Param searchKidsHeightWeightRequest
     */

    @Override
    public List<Kids> searchKidsByEvaluatePeriodic(Long idSchool, EvaluatePeriodicSearchRequest evaluatePeriodicSearchRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (evaluatePeriodicSearchRequest != null) {
            if (idSchool != null) {
                queryStr.append("and id_school=:idSchool ");
                mapParams.put("idSchool", idSchool);
            }
            if (evaluatePeriodicSearchRequest.getIdGrade() != null) {
                queryStr.append("and id_grade=:idGrade ");
                mapParams.put("idGrade", evaluatePeriodicSearchRequest.getIdGrade());
            }
            if (evaluatePeriodicSearchRequest.getIdClass() != null) {
                queryStr.append("and id_class=:idClass ");
                mapParams.put("idClass", evaluatePeriodicSearchRequest.getIdClass());
            }
            queryStr.append("and kid_status='STUDYING' ");
            queryStr.append("order by first_name asc ");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }


    @Override
    public List<Kids> searchKidsBirthday(Long idSchool, SearchKidsBirthDayRequest searchKidsBirthDayRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (searchKidsBirthDayRequest != null) {
            if (searchKidsBirthDayRequest.getDate() != null) {
                queryStr.append("and birth_day=:birthDay ");
                mapParams.put("birthDay", searchKidsBirthDayRequest.getDate());
            }
            if (searchKidsBirthDayRequest.getWeek() != null) {
                queryStr.append("and birth_day >=:monday ");
                mapParams.put("monday", searchKidsBirthDayRequest.getWeek().toString());
                queryStr.append(" and birth_day <=:sunday ");
                mapParams.put("sunday", searchKidsBirthDayRequest.getWeek().plusDays(6).toString());
            }

            if (searchKidsBirthDayRequest.getMonth() != null) {
                queryStr.append("and month(birth_day) =:month ");
                mapParams.put("month", searchKidsBirthDayRequest.getMonth().getMonthValue());
            }
            if (StringUtils.isNotBlank(searchKidsBirthDayRequest.getName())) {
                queryStr.append("and full_name like :fullName ");
                mapParams.put("fullName", "%" + searchKidsBirthDayRequest.getName() + "%");
            }
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
            queryStr.append("and kid_status='STUDYING' ");

        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchKidsClass(Long idSchool, SearchKidsClassRequest searchKidsClassRequest) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            if (idSchool != null) {
                queryStr.append("and id_school=:idSchool ");
                mapParams.put("idSchool", idSchool);
            }
            if (searchKidsClassRequest.getIdClass() != null) {
                queryStr.append("and id_class=:idClass ");
                mapParams.put("idClass", searchKidsClassRequest.getIdClass());
            }
            queryStr.append("and kid_status='STUDYING' ");
            queryStr.append("order by first_name asc ");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidsAlbum(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        queryStr.append("and kid_status =:status ");
        mapParams.put("status", AppConstant.STUDYING);
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by first_name asc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidsAlbumClass(Long idSchool, Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by first_name asc ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidsA(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAlbumClass(Long idSchool, Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (idSchool != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append(" and del_active =:delActive ");
        mapParams.put("delActive", AppConstant.APP_TRUE);
        queryStr.append("order by first_name asc ");
        return findAllNoPaging(queryStr.toString(), mapParams);

    }

    @Override
    public List<Kids> searchKidsBirthday(UserPrincipal principal, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());
        }
        if (principal.getIdClassLogin() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", principal.getIdClassLogin());
        }
        if (localDateTime != null) {
            queryStr.append("and day(birth_day) =:day ");
            mapParams.put("day", localDateTime.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month ");
            mapParams.put("month", localDateTime.getMonthValue());
        }
        queryStr.append("and kid_status =:status ");
        mapParams.put("status", AppConstant.STUDYING);
        queryStr.append("order by created_date desc");

        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> searchKidsBirthdayNoSchool(LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and day(birth_day) =:day ");
        mapParams.put("day", localDate.getDayOfMonth());
        queryStr.append("and month(birth_day) =:month ");
        mapParams.put("month", localDate.getMonthValue());
        queryStr.append("and kid_status =:status ");
        mapParams.put("status", AppConstant.STUDYING);
        queryStr.append("and is_activated =true ");
        queryStr.append("and id_parent is not null ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchParentBirthdayNoSchool(LocalDate localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and (case when father_birthday is not null then day(father_birthday)=:day and month(father_birthday)=:month else false end) or (case when mother_birthday is not null then day(mother_birthday)=:day and month(mother_birthday)=:month else false end) ");
        mapParams.put("day", localDate.getDayOfMonth());
        mapParams.put("month", localDate.getMonthValue());
        queryStr.append("and kid_status =:status ");
        mapParams.put("status", AppConstant.STUDYING);
        queryStr.append("and is_activated =true ");
        queryStr.append("and id_parent is not null ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchKidsBirthdayPlus(UserPrincipal principal, LocalDateTime localDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
//        LocalDateTime datePlus = localDate.plusDays(5);
        if (principal.getIdSchoolLogin() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());
        }
        if (principal.getIdClassLogin() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", principal.getIdClassLogin());
        }
        if (localDate != null) {
            queryStr.append("and  (birth_day =:birthDay) ");
//                mapParams.put("birthDayPlus", datePlus);
            mapParams.put("birthDay", localDate);
        }
//            queryStr.append("and account_type=:teacher ");
//            mapParams.put("teacher", FeedBackConstant.TEACHER);
        queryStr.append("order by created_date desc");
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> searchKidsBirthMonth(UserPrincipal principal, LocalDateTime localDateTime) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());
        }
        if (principal.getIdClassLogin() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", principal.getIdClassLogin());
        }
        if (localDateTime != null) {
            if (localDateTime.getMonth() != null) {
                queryStr.append("and month(birth_day) =:month ");
                mapParams.put("month", localDateTime.getMonth().getValue());
            }
//            queryStr.append("and account_type=:teacher ");
//            mapParams.put("teacher", FeedBackConstant.TEACHER);
            queryStr.append("order by created_date desc");
        }
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }


    @Override
    public List<Kids> finQualityKidsOfClass(UserPrincipal principal) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());
        }
        if (principal.getIdClassLogin() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", principal.getIdClassLogin());
        }
        queryStr.append("and kid_status=:status ");
        mapParams.put("status", KidsStatusConstant.STUDYING);
//        queryStr.append("and account_type=:teacher ");
//        mapParams.put("teacher", FeedBackConstant.TEACHER);
        queryStr.append("order by created_date desc");

        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> findKidsOfClass(Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and kid_status=:kidStatus ");
        mapParams.put("kidStatus", KidsStatusConstant.STUDYING);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidGroup(Long idSchool, SearchStudentGroupRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        queryStr.append("and kid_status=:kidStatus ");
        mapParams.put("kidStatus", KidsStatusConstant.STUDYING);
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchParentBirthdayNew(Long idSchool, SearchParentBirthDayRequest searchParentBirthDayRequest) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and kid_status='STUDYING' ");
        if (searchParentBirthDayRequest.getDate() != null) {
            queryStr.append(" and ((representation LIKE 'Bố' and father_birthday =:birthday) || (representation LIKE 'Mẹ' and mother_birthday=:birthday)) ");
            mapParams.put("birthday", searchParentBirthDayRequest.getDate());
        }
        if (searchParentBirthDayRequest.getWeek() != null) {
            queryStr.append(" and ((representation LIKE 'Bố' and father_birthday>=:monday) || (representation LIKE 'Mẹ' and mother_birthday>=:monday)) ");
            mapParams.put("monday", searchParentBirthDayRequest.getWeek());
            queryStr.append(" and ((representation LIKE 'Bố' and father_birthday<=:sunday) || (representation LIKE 'Mẹ' and mother_birthday<=:sunday)) ");
            mapParams.put("sunday", searchParentBirthDayRequest.getWeek().plusDays(6));
        }
        if (searchParentBirthDayRequest.getMonth() != null) {
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month)) ");
            mapParams.put("month", searchParentBirthDayRequest.getMonth().getMonthValue());
        }
        if (StringUtils.isNotBlank(searchParentBirthDayRequest.getName())) {
            queryStr.append(" and ((representation LIKE 'Bố' and father_name like :fatherName) || (representation LIKE 'Mẹ' and mother_name like :motherName)) ");
            mapParams.put("motherName", "%" + searchParentBirthDayRequest.getName() + "%");
            mapParams.put("fatherName", "%" + searchParentBirthDayRequest.getName() + "%");
        }
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidsGrade(UserPrincipal principal, List<Long> dataGradeNotifyList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (principal.getIdSchoolLogin() != null) {
            quertStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());

            if (!CollectionUtils.isEmpty(dataGradeNotifyList)) {
                quertStr.append(" and model.id_grade in :idGradeList ");
                mapParams.put("idGradeList", dataGradeNotifyList);

                quertStr.append(" and model.kid_status =:status ");
                mapParams.put("status", AppConstant.STUDYING);
            }
        }

        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidsClass(UserPrincipal principal, List<Long> idClassList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            quertStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());

            if (!CollectionUtils.isEmpty(idClassList)) {
                quertStr.append(" and model.id_class in :idClassList ");
                mapParams.put("idClassList", idClassList);

                quertStr.append(" and model.kid_status =:status ");
                mapParams.put("status", AppConstant.STUDYING);
            }
        }

        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKidsGroup(UserPrincipal principal, List<Long> idGroupList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            quertStr.append("and model.id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());

            quertStr.append(" and model.kid_status =:status ");
            mapParams.put("status", AppConstant.STUDYING);

            if (!CollectionUtils.isEmpty(idGroupList)) {
                quertStr.append(" and exists(select*from ex_kids_group as ekg where model.id = ekg.id_kids and ekg.id_kids_group in :idGroupList) ");
                mapParams.put("idGroupList", idGroupList);
            }
        }

        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findAllKids(UserPrincipal principal, List<Long> idKidList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (principal.getIdSchoolLogin() != null) {
            quertStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", principal.getIdSchoolLogin());

            if (!CollectionUtils.isEmpty(idKidList)) {
                quertStr.append(" and model.id in :idKidList ");
                mapParams.put("idKidList", idKidList);

                quertStr.append(" and model.kid_status =:status ");
                mapParams.put("status", AppConstant.STUDYING);
            }
        }

        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findKidsWithPhoneRepresentationIndSchool(Long idSchool, String phone) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and case when representation='Bố' then father_phone=:phone and father_name!='' and father_name is not null when representation='Mẹ' then mother_phone=:phone and mother_name!='' and mother_name is not null else false end ");
        mapParams.put("phone", phone);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchKidsBirthdayNew(Long idSchool, SearchKidsBirthDayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchKidsBirthday(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countSearchKidsBirthday(Long idSchool, SearchKidsBirthDayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchKidsBirthday(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> searchParentBirthdayNewa(Long idSchool, SearchParentBirthDayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchParentBirthday(idSchool, request, queryStr, mapParams);
        return findAllWebPaging(queryStr.toString(), mapParams, request.getPageNumber(), request.getMaxPageItem());
    }

    @Override
    public long countsearchParentBirthdayNewa(Long idSchool, SearchParentBirthDayRequest request) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        this.setSearchParentBirthday(idSchool, request, queryStr, mapParams);
        return countAll(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findByKidsClassWithStatus(Long idClass, String status) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and kid_status=:status ");
        mapParams.put("status", status);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findByKidsIdSchoolWithClassWithStatus(Long idSchool, Long idClass, String status) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        if (Objects.nonNull(idClass)) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        if (StringUtils.isNotBlank(status)) {
            queryStr.append("and kid_status=:status ");
            mapParams.put("status", status);
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findByKidsClassWithStatusName(Long idClass, String status, String fullName, List<Long>... idKidList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (idKidList.length > 0) {
            List<Long> idList = new ArrayList<>();
            for (List<Long> longList : idKidList) {
                idList = longList;
            }
            if (CollectionUtils.isNotEmpty(idList)) {
                queryStr.append("and id in :idKidList ");
                mapParams.put("idKidList", idList);
            }
        }
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and kid_status=:status ");
        mapParams.put("status", status);
        if (StringUtils.isNotBlank(fullName)) {
            queryStr.append("and lower(full_name) like lower(:fullName) ");
            mapParams.put("fullName", "%" + fullName.trim() + "%");
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findByKidsClassWithStatusNameExcel(Long idClass, String status) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (StringUtils.isNotBlank(status)) {
            queryStr.append("and kid_status=:status ");
            mapParams.put("status", status);
        }
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findKidInClassAndStatusWithDate(LocalDate date, List<Long> idClassList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idClassList)) {
            queryStr.append("and id_class in (:idClassList)  ");
            mapParams.put("idClassList", idClassList);
        }
        queryStr.append("and exists(select * from kids_status_timeline as model1 where model.id =  model1.id_kid and model1.status =:status  and model1.start_date <=:date and case when end_date is not null then end_date>=:date else true end) ");
        mapParams.put("status", KidsStatusConstant.STUDYING);
        mapParams.put("date", date);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findKidOneClassAndStatusWithDate(LocalDate date, Long idClass) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class =:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and exists(select * from kids_status_timeline as model1 where model.id =  model1.id_kid and model1.status =:status  and model1.start_date <=:date and case when end_date is not null then end_date>=:date else true end) ");
        mapParams.put("status", KidsStatusConstant.STUDYING);
        mapParams.put("date", date);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findKidInSchoolAndStatusWithDate(LocalDate date, Long idSchool) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school =:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and exists(select * from kids_status_timeline as model1 where model.id =  model1.id_kid and model1.status =:status  and model1.start_date <=:date and case when end_date is not null then end_date>=:date else true end) ");
        mapParams.put("status", KidsStatusConstant.STUDYING);
        mapParams.put("date", date);
        return findAllNoPagingNoDelActive(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> finQualityKidsforPlus(Long idSchool, SearchKidsQualityPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (StringUtils.isNotBlank(request.getKidName())) {
            queryStr.append("and full_name like :fullName ");
            mapParams.put("fullName", "%" + request.getKidName() + "%");
        }
        queryStr.append("and kid_status=:status ");
        mapParams.put("status", KidsStatusConstant.STUDYING);
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllMobilePaging(queryStr.toString(), mapParams, request.getPageNumber());
    }

    @Override
    public List<Kids> findKidsByIdList(List<Long> idKidList) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id in(:idKidList) ");
        mapParams.put("idKidList", idKidList);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> findKidByIdSchool(Long idSchool, SearchKidsBirthdayPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);

        queryStr.append("and day(birth_day) =:day ");
        mapParams.put("day", LocalDate.now().getDayOfMonth());
        queryStr.append("and month(birth_day) =:month ");
        mapParams.put("month", LocalDate.now().getMonthValue());
        queryStr.append("and kid_status =:status ");
        mapParams.put("status", AppConstant.STUDYING);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> searchKidsBirthWeekforplus(Long idSchool, SearchKidsBirthdayPlusRequest request, LocalDate toLocalDate) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (toLocalDate != null) {
            LocalDate day1 = toLocalDate;
            LocalDate day2 = day1.plusDays(1);
            LocalDate day3 = day1.plusDays(2);
            LocalDate day4 = day1.plusDays(3);
            LocalDate day5 = day1.plusDays(4);
            LocalDate day6 = day1.plusDays(5);
            LocalDate day7 = day1.plusDays(6);

            queryStr.append("and ((day(birth_day) =:day1 ");
            mapParams.put("day1", day1.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month1) ");
            mapParams.put("month1", day1.getMonthValue());

            queryStr.append("or (day(birth_day) =:day2 ");
            mapParams.put("day2", day2.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month2) ");
            mapParams.put("month2", day2.getMonthValue());

            queryStr.append("or (day(birth_day) =:day3 ");
            mapParams.put("day3", day3.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month3) ");
            mapParams.put("month3", day3.getMonthValue());

            queryStr.append("or (day(birth_day) =:day4 ");
            mapParams.put("day4", day4.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month4) ");
            mapParams.put("month4", day4.getMonthValue());

            queryStr.append("or (day(birth_day) =:day5 ");
            mapParams.put("day5", day5.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month5) ");
            mapParams.put("month5", day5.getMonthValue());

            queryStr.append("or (day(birth_day) =:day6 ");
            mapParams.put("day6", day6.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month6) ");
            mapParams.put("month6", day6.getMonthValue());

            queryStr.append("or ( day(birth_day) =:day7 ");
            mapParams.put("day7", day7.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month7)) ");
            mapParams.put("month7", day7.getMonthValue());
        }
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and kid_status='STUDYING' ");
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");

        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> searchMonthBirthday(Long idSchool, SearchKidsBirthdayPlusRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and kid_status='STUDYING' ");
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        queryStr.append("and month(birth_day) =:month ");
        mapParams.put("month", LocalDate.now().getMonthValue());
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> findKidByIdSchoolcout(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and day(birth_day) =:day ");
        mapParams.put("day", LocalDate.now().getDayOfMonth());
        queryStr.append("and month(birth_day) =:month ");
        mapParams.put("month", LocalDate.now().getMonthValue());
        queryStr.append("and kid_status =:status ");
        mapParams.put("status", AppConstant.STUDYING);
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> searchKidsBirthWeekforplusnew(Long idSchool, LocalDate monday) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (monday != null) {
            LocalDate day1 = monday;
            LocalDate day2 = day1.plusDays(1);
            LocalDate day3 = day1.plusDays(2);
            LocalDate day4 = day1.plusDays(3);
            LocalDate day5 = day1.plusDays(4);
            LocalDate day6 = day1.plusDays(5);
            LocalDate day7 = day1.plusDays(6);

            queryStr.append("and ((day(birth_day) =:day1 ");
            mapParams.put("day1", day1.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month1) ");
            mapParams.put("month1", day1.getMonthValue());

            queryStr.append("or (day(birth_day) =:day2 ");
            mapParams.put("day2", day2.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month2) ");
            mapParams.put("month2", day2.getMonthValue());

            queryStr.append("or (day(birth_day) =:day3 ");
            mapParams.put("day3", day3.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month3) ");
            mapParams.put("month3", day3.getMonthValue());

            queryStr.append("or (day(birth_day) =:day4 ");
            mapParams.put("day4", day4.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month4) ");
            mapParams.put("month4", day4.getMonthValue());

            queryStr.append("or (day(birth_day) =:day5 ");
            mapParams.put("day5", day5.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month5) ");
            mapParams.put("month5", day5.getMonthValue());

            queryStr.append("or (day(birth_day) =:day6 ");
            mapParams.put("day6", day6.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month6) ");
            mapParams.put("month6", day6.getMonthValue());

            queryStr.append("or ( day(birth_day) =:day7 ");
            mapParams.put("day7", day7.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month7)) ");
            mapParams.put("month7", day7.getMonthValue());
        }
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and kid_status='STUDYING' ");

        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

    @Override
    public List<Kids> searchMonthBirthdaycout(Long idSchool) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and kid_status='STUDYING' ");
        queryStr.append("and month(birth_day) =:month ");
        mapParams.put("month", LocalDate.now().getMonthValue());
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }

//    @Override
//    public List<Kids> findKidsWithStatus(Long idClass) {
//        StringBuilder queryStr = new StringBuilder();
//        Map<String, Object> mapParams = new HashMap<>();
//        LocalDate nowDate = LocalDate.now();
//        LocalDate date1 = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue() + 1, 1);//sai do thang 13
//        LocalDate date2 = LocalDate.of(nowDate.getYear(), nowDate.getMonthValue(), 1);
//        queryStr.append("and id_class=:idClass ");
//        mapParams.put("idClass", idClass);
//        queryStr.append("and exists(select * from kids_status_timeline as model1 where model1.id_kid=model.id and status!=:kidStatus and start_date<:date1 and case when end_date is not null then end_date>=:date2 else true end) ");
//        mapParams.put("kidStatus", KidsStatusConstant.LEAVE_SCHOOL);
//        mapParams.put("date1", date1);
//        mapParams.put("date2", date2);
//        return findAllNoPaging(queryStr.toString(), mapParams);
//    }

    @Override
    public List<Kids> searchByStatusOrderByName(Long idClass, FinanceSearchKidsRequest request) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryStr.append("and kid_status=:kidStatus ");
            mapParams.put("kidStatus", request.getStatus());
        }
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> getKidsInClassAndStatusAndName(Long idClass, String kidsStatus, String fullName) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id_class=:idClass ");
        mapParams.put("idClass", idClass);
        queryStr.append("and id_parent is not null ");
        if (StringUtils.isNotBlank(kidsStatus)) {
            queryStr.append("and kid_status=:kidStatus ");
            mapParams.put("kidStatus", kidsStatus);
        }
        if (StringUtils.isNotBlank(fullName)) {
            queryStr.append("and full_name like :fullName ");
            mapParams.put("fullName", "%" + fullName + "%");
        }
        queryStr.append("order by first_name collate utf8_vietnamese_ci ");
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public List<Kids> getKidsListForCelebrateAuto(Long idSchool, String gender, String type) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (!idSchool.equals(SystemConstant.ID_SYSTEM)) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", idSchool);
        }
        if (type.equals(SampleConstant.KIDS) && !gender.equals(AppConstant.ALL)) {
            queryStr.append("and gender=:gender ");
            mapParams.put("gender", gender);
        }
        queryStr.append("and is_activated=true ");
        queryStr.append("and kid_status=:status ");
        mapParams.put("status", KidsStatusConstant.STUDYING);
        return findAllNoPaging(queryStr.toString(), mapParams);
    }

    @Override
    public Optional<Kids> getKidsDelActiveAndActivateCustom(Long id) {
        StringBuilder queryStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        queryStr.append("and id=:id ");
        mapParams.put("id", id);
        queryStr.append("and is_activated=true ");
        List<Kids> kidsList = findAllNoPaging(queryStr.toString(), mapParams);
        return CollectionUtils.isNotEmpty(kidsList) ? Optional.of(kidsList.get(0)) : Optional.empty();
    }


    @Override
    public List<Kids> searchKidsBirthWeek(UserPrincipal principal, LocalDate monday) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

        if (principal.getIdClassLogin() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", principal.getIdClassLogin());
            if (monday != null) {
                LocalDate day1 = monday;
                LocalDate day2 = day1.plusDays(1);
                LocalDate day3 = day1.plusDays(2);
                LocalDate day4 = day1.plusDays(3);
                LocalDate day5 = day1.plusDays(4);
                LocalDate day6 = day1.plusDays(5);
                LocalDate day7 = day1.plusDays(6);

                queryStr.append("and ((day(birth_day) =:day1 ");
                mapParams.put("day1", day1.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month1) ");
                mapParams.put("month1", day1.getMonthValue());

                queryStr.append("or (day(birth_day) =:day2 ");
                mapParams.put("day2", day2.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month2) ");
                mapParams.put("month2", day2.getMonthValue());

                queryStr.append("or (day(birth_day) =:day3 ");
                mapParams.put("day3", day3.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month3) ");
                mapParams.put("month3", day3.getMonthValue());

                queryStr.append("or (day(birth_day) =:day4 ");
                mapParams.put("day4", day4.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month4) ");
                mapParams.put("month4", day4.getMonthValue());

                queryStr.append("or (day(birth_day) =:day5 ");
                mapParams.put("day5", day5.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month5) ");
                mapParams.put("month5", day5.getMonthValue());

                queryStr.append("or (day(birth_day) =:day6 ");
                mapParams.put("day6", day6.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month6) ");
                mapParams.put("month6", day6.getMonthValue());

                queryStr.append("or ( day(birth_day) =:day7 ");
                mapParams.put("day7", day7.getDayOfMonth());
                queryStr.append("and month(birth_day) =:month7)) ");
                mapParams.put("month7", day7.getMonthValue());
            }
        }

        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList;
    }


    @Override
    public int getKidsBirthdayInClass(Long idClass) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (idClass != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", idClass);
        }
        queryStr.append("and birth_day >=:startDate ");
        mapParams.put("startDate", LocalDate.now());
        queryStr.append("and birth_day <=:endDate ");
        mapParams.put("endDate", LocalDate.now().plusDays(5));
        queryStr.append("and kid_status=:kidStatus ");
        mapParams.put("kidStatus", KidsStatusConstant.STUDYING);
        List<Kids> dataList = findAllNoPaging(queryStr.toString(), mapParams);
        return dataList.size();
    }

    private void setSearchSchoolAdmin(StringBuilder queryStr, Map<String, Object> mapParams, KidsSearchAdminRequest request, List<Long> idSchoolList) {
        queryStr.append("and id_school in (:idSchoolList) ");
        mapParams.put("idSchoolList", idSchoolList);
        if (request.getIdSchool() != null) {
            queryStr.append("and id_school=:idSchool ");
            mapParams.put("idSchool", request.getIdSchool());
        }
        String NameOrPhone = request.getNameOrPhone();
        if (StringUtils.isNotBlank(NameOrPhone)) {
            NameOrPhone = NameOrPhone.trim();
            queryStr.append("and (lower(full_name) like lower(:fullName) ");
            mapParams.put("fullName", "%" + NameOrPhone + "%");
            queryStr.append("or (exists (select * from ma_parent as model1 where model1.id =  model.id_parent and  exists (select * from ma_user as model2 where model2.id =  model1.id_ma_user and model2.phone like :phone)))) ");
            mapParams.put("phone", "%" + NameOrPhone + "%");
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryStr.append("and kid_status=:kidStatus ");
            mapParams.put("kidStatus", request.getStatus());
        }
    }

    private void setSearchKidsBirthday(Long idSchool, SearchKidsBirthDayRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and kid_status='STUDYING' ");
        if (request.getDate() != null) {
            queryStr.append("and day(birth_day) =:day ");
            mapParams.put("day", request.getDate().getDayOfMonth());
            queryStr.append("and month(birth_day) =:month ");
            mapParams.put("month", request.getDate().getMonthValue());
        }
        if (request.getWeek() != null) {
            LocalDate day1 = request.getWeek();
            LocalDate day2 = day1.plusDays(1);
            LocalDate day3 = day1.plusDays(2);
            LocalDate day4 = day1.plusDays(3);
            LocalDate day5 = day1.plusDays(4);
            LocalDate day6 = day1.plusDays(5);
            LocalDate day7 = day1.plusDays(6);

            queryStr.append("and ((day(birth_day) =:day1 ");
            mapParams.put("day1", day1.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month1) ");
            mapParams.put("month1", day1.getMonthValue());

            queryStr.append("or (day(birth_day) =:day2 ");
            mapParams.put("day2", day2.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month2) ");
            mapParams.put("month2", day2.getMonthValue());

            queryStr.append("or (day(birth_day) =:day3 ");
            mapParams.put("day3", day3.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month3) ");
            mapParams.put("month3", day3.getMonthValue());

            queryStr.append("or (day(birth_day) =:day4 ");
            mapParams.put("day4", day4.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month4) ");
            mapParams.put("month4", day4.getMonthValue());

            queryStr.append("or (day(birth_day) =:day5 ");
            mapParams.put("day5", day5.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month5) ");
            mapParams.put("month5", day5.getMonthValue());

            queryStr.append("or (day(birth_day) =:day6 ");
            mapParams.put("day6", day6.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month6) ");
            mapParams.put("month6", day6.getMonthValue());

            queryStr.append("or (day(birth_day) =:day7 ");
            mapParams.put("day7", day7.getDayOfMonth());
            queryStr.append("and month(birth_day) =:month7)) ");
            mapParams.put("month7", day7.getMonthValue());

        }
        if (request.getMonth() != null) {
            queryStr.append("and month(birth_day) =:month ");
            mapParams.put("month", request.getMonth().getMonthValue());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append("and full_name like :fullName ");
            mapParams.put("fullName", "%" + request.getName() + "%");
        }
        queryStr.append("order by first_name asc ");
    }

    private void setSearchParentBirthday(Long idSchool, SearchParentBirthDayRequest request, StringBuilder queryStr, Map<String, Object> mapParams) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and kid_status='STUDYING' ");
        if (request.getDate() != null) {
            queryStr.append(" and ((representation LIKE 'Bố' and day(father_birthday) =:day) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month)) ");
            mapParams.put("day", request.getDate().getDayOfMonth());
            mapParams.put("month", request.getDate().getMonthValue());
        }
        if (request.getWeek() != null) {
            LocalDate day1 = request.getWeek();
            LocalDate day2 = day1.plusDays(1);
            LocalDate day3 = day1.plusDays(2);
            LocalDate day4 = day1.plusDays(3);
            LocalDate day5 = day1.plusDays(4);
            LocalDate day6 = day1.plusDays(5);
            LocalDate day7 = day1.plusDays(6);
            queryStr.append(" and ((representation LIKE 'Bố' and day(father_birthday) =:day1) || (representation LIKE 'Mẹ' or day(mother_birthday) =:day1)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month1) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month1)) ");
            mapParams.put("day1", day1.getDayOfMonth());
            mapParams.put("month1", day1.getMonthValue());

            queryStr.append(" or ((representation LIKE 'Bố' and day(father_birthday) =:day2) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day2)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month2) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month2)) ");
            mapParams.put("day2", day2.getDayOfMonth());
            mapParams.put("month2", day2.getMonthValue());

            queryStr.append(" or ((representation LIKE 'Bố' and day(father_birthday) =:day3) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day3)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month3) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month3)) ");
            mapParams.put("day3", day3.getDayOfMonth());
            mapParams.put("month3", day3.getMonthValue());

            queryStr.append(" or ((representation LIKE 'Bố' and day(father_birthday) =:day4) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day4)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month4) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month4)) ");
            mapParams.put("day4", day4.getDayOfMonth());
            mapParams.put("month4", day4.getMonthValue());

            queryStr.append(" or ((representation LIKE 'Bố' and day(father_birthday) =:day5) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day5)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month5) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month5)) ");
            mapParams.put("day5", day5.getDayOfMonth());
            mapParams.put("month5", day5.getMonthValue());

            queryStr.append(" or ((representation LIKE 'Bố' and day(father_birthday) =:day6) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day6)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month6) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month6)) ");
            mapParams.put("day6", day6.getDayOfMonth());
            mapParams.put("month6", day6.getMonthValue());

            queryStr.append(" or ((representation LIKE 'Bố' and day(father_birthday) =:day7) || (representation LIKE 'Mẹ' and day(mother_birthday) =:day7)) ");
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month7) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month7)) ");
            mapParams.put("day7", day7.getDayOfMonth());
            mapParams.put("month7", day7.getMonthValue());
        }
        if (request.getMonth() != null) {
            queryStr.append(" and ((representation LIKE 'Bố' and month(father_birthday) =:month) || (representation LIKE 'Mẹ' and month(mother_birthday) =:month)) ");
            mapParams.put("month", request.getMonth().getMonthValue());
        }
        if (StringUtils.isNotBlank(request.getName())) {
            queryStr.append(" and ((representation LIKE 'Bố' and father_name like :fatherName) || (representation LIKE 'Mẹ' and mother_name like :motherName)) ");
            mapParams.put("motherName", "%" + request.getName() + "%");
            mapParams.put("fatherName", "%" + request.getName() + "%");
        }
    }

    private void setSearchKids(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchKidsRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        String NameOrPhone = request.getNameOrPhone();
        if (request.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", request.getIdGrade());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryStr.append("and kid_status=:kidStatus ");
            mapParams.put("kidStatus", request.getStatus());
        }
        if (StringUtils.isNotBlank(NameOrPhone)) {
            NameOrPhone = NameOrPhone.trim();
            queryStr.append("and (lower(full_name) like lower(:fullName) ");
            mapParams.put("fullName", "%" + NameOrPhone + "%");
            queryStr.append("or lower(nick_name) like lower(:nickName) ");
            mapParams.put("nickName", "%" + NameOrPhone + "%");
            queryStr.append("or (exists (select * from ma_parent as model1 where model1.id =  model.id_parent and  exists (select * from ma_user as model2 where model2.id =  model1.id_ma_user and model2.phone like :phone)))) ");
            mapParams.put("phone", "%" + NameOrPhone + "%");
        }
        if (CollectionUtils.isNotEmpty(request.getDateList())) {
            if (AppConstant.KID_SEARCH_START_DATE.equals(request.getType())) {
                queryStr.append("and date_start>=:start and date_start<=:end ");
                mapParams.put("start", request.getDateList().get(0));
                mapParams.put("end", request.getDateList().get(1));
            } else if (AppConstant.KID_SEARCH_BIRTHDAY.equals(request.getType())) {
                queryStr.append("and birth_day>=:start and birth_day<=:end ");
                mapParams.put("start", request.getDateList().get(0));
                mapParams.put("end", request.getDateList().get(1));
            }
        }
        queryStr.append("order by id_parent is null desc, first_name collate utf8_vietnamese_ci ");
    }

    private void setSearchKidsTransfer(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchKidsTransferRequest request) {
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        String NameOrPhone = request.getNameOrPhone();
        if (request.getIdGrade() != null) {
            queryStr.append("and id_grade=:idGrade ");
            mapParams.put("idGrade", request.getIdGrade());
        }
        if (request.getIdClass() != null) {
            queryStr.append("and id_class=:idClass ");
            mapParams.put("idClass", request.getIdClass());
        }
        if (StringUtils.isNotBlank(request.getStatus())) {
            queryStr.append("and kid_status=:kidStatus ");
            mapParams.put("kidStatus", request.getStatus());
        }
        if (StringUtils.isNotBlank(NameOrPhone)) {
            NameOrPhone = NameOrPhone.trim();
            queryStr.append("and (lower(full_name) like lower(:fullName) ");
            mapParams.put("fullName", "%" + NameOrPhone + "%");
            queryStr.append("or (exists (select * from ma_parent as model1 where model1.id =  model.id_parent and  exists (select * from ma_user as model2 where model2.id =  model1.id_ma_user and model2.phone like :phone)))) ");
            mapParams.put("phone", "%" + NameOrPhone + "%");
        }
        queryStr.append("order by id_parent is null desc, first_name collate utf8_vietnamese_ci ");
    }

    private void getGroupOutKids(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, SearchKidsGroupOutRequest request) {
        LocalDate startYear = LocalDate.of(request.getYearOut().getYear(), 1, 1);
        LocalDate endYear = LocalDate.of(request.getYearOut().getYear(), 12, 31);
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and id_group_out_kids=:idGroupOut ");
        mapParams.put("idGroupOut", request.getIdGroupOut());
        queryStr.append("and out_date >= :startYear and out_date <= :endYear ");
        mapParams.put("startYear", startYear);
        mapParams.put("endYear", endYear);
        if (CollectionUtils.isNotEmpty(request.getBirthdayList())) {
            LocalDate startBirthday = request.getBirthdayList().get(0);
            LocalDate endBirthday = request.getBirthdayList().get(1);
            queryStr.append("and birth_day>=:startBirthday and birth_day<=:endBirthday ");
            mapParams.put("startBirthday", startBirthday);
            mapParams.put("endBirthday", endBirthday);
        }
        if (CollectionUtils.isNotEmpty(request.getDateInList())) {
            LocalDate startDate = request.getDateInList().get(0);
            LocalDate endDate = request.getDateInList().get(1);
            queryStr.append("and date_start>=:startDate and date_start<=:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        if (StringUtils.isNotBlank(request.getFullName())) {
            queryStr.append("and full_name like :fullName ");
            mapParams.put("fullName", "%" + request.getFullName().trim() + "%");
        }
        queryStr.append("order by out_date desc ");
    }

    private void getGroupOutKidsExcel(StringBuilder queryStr, Map<String, Object> mapParams, Long idSchool, ExcelGroupOutRequest request) {
        LocalDate startYear = LocalDate.of(request.getYearOut().getYear(), 1, 1);
        LocalDate endYear = LocalDate.of(request.getYearOut().getYear(), 12, 31);
        queryStr.append("and id_school=:idSchool ");
        mapParams.put("idSchool", idSchool);
        queryStr.append("and out_date >= :startYear and out_date <= :endYear ");
        mapParams.put("startYear", startYear);
        mapParams.put("endYear", endYear);
        if (CollectionUtils.isNotEmpty(request.getBirthdayList())) {
            LocalDate startBirthday = request.getBirthdayList().get(0);
            LocalDate endBirthday = request.getBirthdayList().get(1);
            queryStr.append("and birth_day>=:startBirthday and birth_day<=:endBirthday ");
            mapParams.put("startBirthday", startBirthday);
            mapParams.put("endBirthday", endBirthday);
        }
        if (CollectionUtils.isNotEmpty(request.getDateInList())) {
            LocalDate startDate = request.getDateInList().get(0);
            LocalDate endDate = request.getDateInList().get(1);
            queryStr.append("and date_start>=:startDate and date_start<=:endDate ");
            mapParams.put("startDate", startDate);
            mapParams.put("endDate", endDate);
        }
        queryStr.append("order by out_date desc ");
    }
}
