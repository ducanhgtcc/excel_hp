package com.example.onekids_project.repository.repositoryimpl;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.entity.employee.Employee;
import com.example.onekids_project.repository.repositorycustom.EmployeeRepositoryCustom;
import com.example.onekids_project.request.employee.SearchEmployeeRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmployeeRepositoryImpl extends BaseRepositoryimpl<Employee> implements EmployeeRepositoryCustom {

    @Override
    public List<Employee> findAllEmployee(SearchEmployeeRequest searchEmployeeRequest, Pageable pageable, Long idSchool) {

        return findAll(null, null, pageable);
    }


    @Override
    public Optional<Employee> findByIdEmployee(Long idSchoolLogin, Long id) {
        StringBuilder queryStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();

//        if (idSchoolLogin != null) {
//            queryStr.append("and id_school=:idSchool ");
//            mapParams.put("idSchool", idSchoolLogin);
//            mapParams.put("idSchool", idSchool);
//        }
        if (id != null) {
            queryStr.append("and id=:id");
            mapParams.put("id", id);
        }
        List<Employee> employeeList = findAllNoPaging(queryStr.toString(), mapParams);
        if (employeeList.size() > 0) {
            return Optional.ofNullable(employeeList.get(0));
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> findAllEmployeeAppsend(List<Long> idSchoolList, String appTypeReceive) throws IOException {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idSchoolList)) {
            quertStr.append(" and exists(select*from info_employee_school where model.id=info_employee_school.id_employee and info_employee_school.app_type = :appType  and info_employee_school.id_school in :idSchoolList) " );
            mapParams.put("idSchoolList", idSchoolList);
            mapParams.put("appType",appTypeReceive);
        }
//        if(StringUtils.isNotBlank(appTypeReceive)){
//            quertStr.append(" and exists(select*from info_employee_school where info_employee_school.app_type = :appType ) ");
//            mapParams.put("appType",appTypeReceive);
//        }

        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Employee> findAllEmployeeList(List<Long> idEmployeeList) {
        StringBuilder quertStr = new StringBuilder();
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idEmployeeList)) {
            quertStr.append(" and exists(select*from info_employee_school as ies where model.id = ies.id_employee and ies.id in :idEmployeeList)");
            mapParams.put("idEmployeeList", idEmployeeList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Employee> findAllDeparmentList(List<Long> idDeparmentList) {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idDeparmentList)) {
            quertStr.append(" and exists(select*from ex_department_employee as ede, info_employee_school as ies where model.id=ies.id_employee and ies.id = ede.id_info_employee and ede.id_department in :idDeparmentList)");
            mapParams.put("idDeparmentList", idDeparmentList);
        }
        return findAllNoPaging(quertStr.toString(), mapParams);
    }

    @Override
    public List<Employee> findAllEmployeeAAppsend(List<Long> idSchoolList, String teacher) throws IOException {
        StringBuilder quertStr = new StringBuilder("");
        Map<String, Object> mapParams = new HashMap<>();
        if (!CollectionUtils.isEmpty(idSchoolList)) {
            quertStr.append(" and exists(select*from info_employee_school where model.id=info_employee_school.id_employee and info_employee_school.app_type = :appType  and info_employee_school.id_school in :idSchoolList) ");
            mapParams.put("idSchoolList", idSchoolList);
            mapParams.put("appType", AppTypeConstant.TEACHER);
        }
//        if(StringUtils.isNotBlank(appTypeReceive)){
//            quertStr.append(" and exists(select*from info_employee_school where info_employee_school.app_type = :appType ) ");
//            mapParams.put("appType",appTypeReceive);
//        }

        return findAllNoPaging(quertStr.toString(), mapParams);
    }
}
