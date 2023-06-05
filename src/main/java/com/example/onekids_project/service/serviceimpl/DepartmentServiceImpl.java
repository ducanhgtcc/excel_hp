package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.DepartmentDTO;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.school.Department;
import com.example.onekids_project.entity.school.ExDepartmentEmployee;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ExDepartmentEmployeeMapper;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.DepartmentRepository;
import com.example.onekids_project.repository.ExDepartmentEmployeeRepository;
import com.example.onekids_project.repository.InfoEmployeeSchoolRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.department.CreateDepartmentRequest;
import com.example.onekids_project.request.department.UpdateDepartmentRequest;
import com.example.onekids_project.request.employee.TransferEmployeeDepartmentRequest;
import com.example.onekids_project.response.department.CreateDepartmentResponse;
import com.example.onekids_project.response.department.DepartmentOtherResponse;
import com.example.onekids_project.response.department.DepartmentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private InfoEmployeeSchoolRepository infoEmployeeSchoolRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private ExDepartmentEmployeeRepository exDepartmentEmployeeRepository;

    @Autowired
    private ExDepartmentEmployeeMapper exDepartmentEmployeeMapper;


    @Override
    public List<DepartmentOtherResponse> findDepartmentCommon(UserPrincipal principal) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Department> departmentList = departmentRepository.findDepartmentCommon(idSchool);
        return listMapper.mapList(departmentList, DepartmentOtherResponse.class);
    }

    @Override
    public List<DepartmentResponse> findAllDepartment(UserPrincipal principal, PageNumberWebRequest request) {
        Long idSchool = principal.getIdSchoolLogin();
        List<Department> departmentList = departmentRepository.findAllDepartment(idSchool, request);
        List<DepartmentResponse> responseList = new ArrayList<>();
        departmentList.forEach(x -> {
            DepartmentResponse model = modelMapper.map(x, DepartmentResponse.class);
            model.setEmployeeNumber((int) x.getDepartmentEmployeeList().stream().filter(a -> a.getInfoEmployeeSchool().isDelActive()).count());
            responseList.add(model);
        });
        return responseList;

//        if (CollectionUtils.isEmpty(departmentList)) {
//            return null;
//        }
////        mapper for list
//        List<DepartmentDTO> departmentDTOList = listMapper.mapList(departmentList, DepartmentDTO.class);
//        departmentDTOList.forEach(x -> x.setEmployeeNumber(x.getDepartmentEmployeeList().stream()
//                .filter(a ->a.isDelActive()).collect(Collectors.toList()).size()));
//        ListDepartmentResponse listDepartmentResponse = new ListDepartmentResponse(departmentDTOList);
//
//        return listDepartmentResponse;


    }

    @Override
    public Optional<DepartmentDTO> findByIdDepartment(Long idSchool, Long id) {
        //lấy từ DB, nếu không có giá trị nào thì kết quả sẽ là Optional.empty
        //isEmpty và isPresent khác với null, nếu giá trị là null mà thực hiện .isEmpty hoặc .isPresent thì sẽ báo lỗi
        Optional<Department> optionalDepartment = departmentRepository.findByIdAndSchoolIdAndDelActive(id, idSchool, AppConstant.APP_TRUE);

        if (optionalDepartment.isEmpty()) {
            return Optional.empty();
        }
//      Optional.empty có thể thực hiện mapper được, khi đó dối tượng được mapper sẽ có Optional.empty là false
//        đối tượng null khi mapper sẽ bị lỗi
//      nên cần đoạn check ở trên
        Optional<DepartmentDTO> optionalDepartmentDTO = Optional.ofNullable(modelMapper.map(optionalDepartment.get(), DepartmentDTO.class));
        List<InfoEmployeeSchool> employeeInDepartmentList = optionalDepartment.get().getDepartmentEmployeeList().stream().map(ExDepartmentEmployee::getInfoEmployeeSchool).collect(Collectors.toList());
        List<Long> idEmployeeInDepartmentList = employeeInDepartmentList.stream().map(InfoEmployeeSchool::getId).collect(Collectors.toList());
        optionalDepartmentDTO.get().setIdEmployeeInDepartmentList(idEmployeeInDepartmentList);
        return optionalDepartmentDTO;
    }

    @Override
    public Optional<Department> findOne(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public CreateDepartmentResponse saveDeparment(Long idSchool, CreateDepartmentRequest createDepartmentRequest) {
        // sửa sau, lấy code tìm kiếm trường theo id của tuấn
        School school = schoolRepository.findById(idSchool).orElseThrow(() -> new RuntimeException("Fail! -> Không tìm thấy school có id=" + idSchool));

        Department department = modelMapper.map(createDepartmentRequest, Department.class);

        department.setSchool(school);

        department = departmentRepository.save(department);

        CreateDepartmentResponse createDepartmentResponse = modelMapper.map(department, CreateDepartmentResponse.class);

        return createDepartmentResponse;
    }

    @Override
    public boolean updateDeparment(Long idSchool, UpdateDepartmentRequest updateDepartmentRequest) {
        Department department = departmentRepository.findByIdAndSchoolIdAndDelActive(updateDepartmentRequest.getId(), idSchool, AppConstant.APP_TRUE).orElseThrow();
        modelMapper.map(updateDepartmentRequest, department);
        departmentRepository.save(department);
        return true;
    }

    @Override
    public boolean deleteDepartment(Long idSchool, Long id) {
        Department department = departmentRepository.findByIdAndSchoolIdAndDelActive(id, idSchool, AppConstant.APP_TRUE).orElseThrow();
        if (department.getDepartmentEmployeeList().stream().anyMatch(x -> x.getInfoEmployeeSchool().isDelActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.ERROR_DELETE_DEPARTMENT);
        }
        department.setDelActive(AppConstant.APP_FALSE);
        departmentRepository.save(department);
        return true;
    }


    @Override
    @Transactional
    public boolean insertTransferEmployeeInDepartment(Long idSchool, TransferEmployeeDepartmentRequest transferEmployeeDepartmentRequest) {
        Department department = departmentRepository.findByIdAndSchoolIdAndDelActive(transferEmployeeDepartmentRequest.getId(), idSchool, AppConstant.APP_TRUE).orElseThrow();
        Long idDepartment = transferEmployeeDepartmentRequest.getId();
        List<Long> idEmployeeInDepartmentList = transferEmployeeDepartmentRequest.getIdEmployeeInDepartmentList();
        exDepartmentEmployeeRepository.deleteByDepartmentId(idDepartment);
        idEmployeeInDepartmentList.forEach(idEmployee -> {
            ExDepartmentEmployee exDepartmentEmployee = new ExDepartmentEmployee();
            exDepartmentEmployee.setInfoEmployeeSchool(infoEmployeeSchoolRepository.findById(idEmployee).get());
            exDepartmentEmployee.setDepartment(department);
            exDepartmentEmployeeRepository.save(exDepartmentEmployee);
        });
        return true;
    }
}
