package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.dto.SubjectDTO;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.Subject;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mapper.SubjectMapper;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.repository.SubjectRepository;
import com.example.onekids_project.request.base.IdObjectRequest;
import com.example.onekids_project.request.classes.ClassSearchCommonRequest;
import com.example.onekids_project.request.schoolconfig.SubjectCreateRequest;
import com.example.onekids_project.request.schoolconfig.SubjectUpdateRequest;
import com.example.onekids_project.request.subject.CreateSubjectRequest;
import com.example.onekids_project.request.subject.UpdateSubjectRequest;
import com.example.onekids_project.response.schoolconfig.SubjectConfigResponse;
import com.example.onekids_project.response.schoolconfig.SubjectForClassConfigResponse;
import com.example.onekids_project.response.schoolconfig.SubjectForClassResponse;
import com.example.onekids_project.response.subject.CreateSubjectResponse;
import com.example.onekids_project.response.subject.ListSubjectResponse;
import com.example.onekids_project.response.subject.SubjectSearchRequest;
import com.example.onekids_project.response.subject.UpdateSubjectResponse;
import com.example.onekids_project.service.servicecustom.SubjectService;
import com.example.onekids_project.util.GenerateCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Override
    public ListSubjectResponse findAllSubject(Pageable pageable, Long idSchool) {
        List<Subject> subjectList = subjectRepository.findAllSubject(idSchool, pageable);
        List<SubjectDTO> subjectDTOList = listMapper.mapList(subjectList, SubjectDTO.class);
        ListSubjectResponse listSubjectResponse = new ListSubjectResponse(subjectDTOList);
//      List<EmployeeDTO> employeeDTOList = employeeList.stream().map(item -> employeeMapper.mapperToDTO(item)).collect(Collectors.toList());
        return listSubjectResponse;
    }

    @Override
    public Optional<SubjectDTO> findByIdSubject(Long id, Long idSchool) {
        //lấy từ DB, nếu không có giá trị nào thì kết quả sẽ là Optional.empty
        //isEmpty và isPresent khác với null, nếu giá trị là null mà thực hiện .isEmpty hoặc .isPresent thì sẽ báo lỗi
        Optional<Subject> optionalSubject = subjectRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool);
        if (optionalSubject.isEmpty()) {
            return Optional.empty();
        }
//      Optional.empty có thể thực hiện mapper được, khi đó dối tượng được mapper sẽ có Optional.empty là false
//        đối tượng null khi mapper sẽ bị lỗi
//      nên cần đoạn check ở trên
        Optional<SubjectDTO> optionalSubjectDTO = Optional.ofNullable(subjectMapper.mapperToDTO(optionalSubject.get()));
        return optionalSubjectDTO;
    }

    @Override
    public CreateSubjectResponse saveSubject(CreateSubjectRequest createSubjectRequest, Long idSchool) {
//      sửa sau, lấy code tìm kiếm trường theo id của tuấn
        School school = schoolRepository.findById(idSchool).orElseThrow(() -> new RuntimeException("Fail! -> Không tìm thấy school có id=" + idSchool));

        Subject newSubject = modelMapper.map(createSubjectRequest, Subject.class);
        newSubject.setIdSchool(idSchool);
        newSubject.setSubjectCode(RandomStringUtils.random(5, true, false));
        newSubject = subjectRepository.save(newSubject);
        CreateSubjectResponse createSubjectResponse = modelMapper.map(newSubject, CreateSubjectResponse.class);

        return createSubjectResponse;
    }

    @Override
    public UpdateSubjectResponse updateSubject(UpdateSubjectRequest updateSubjectRequest, Long idSchool) {
        Optional<Subject> subjectOptional = subjectRepository.findByIdAndIdSchoolAndDelActiveTrue(updateSubjectRequest.getId(), idSchool);
        if (subjectOptional.isEmpty()) {
            return null;
        }

        Subject oldSubject = subjectOptional.get();
        modelMapper.map(updateSubjectRequest, oldSubject);
        Subject newSubject = subjectRepository.save(oldSubject);
        UpdateSubjectResponse updateSubjectResponse = modelMapper.map(newSubject, UpdateSubjectResponse.class);

        return updateSubjectResponse;
    }

    @Override
    public boolean deleteSubject(Long idSchool, Long id) {
        Optional<Subject> subjectOptional = subjectRepository.findByIdAndIdSchoolAndDelActiveTrue(id, idSchool);
        if (subjectOptional.isEmpty()) {
            return false;
        }
        Subject deleteSubject = subjectOptional.get();
        deleteSubject.setDelActive(false);
        subjectRepository.save(deleteSubject);

        return true;
    }

    @Override
    public List<SubjectForClassConfigResponse> findAllSubjectManegeConfig(Long idSchool, ClassSearchCommonRequest classSearchCommonRequest) {
        List<MaClass> maClassList = maClassRepository.searchClassCommon(idSchool, classSearchCommonRequest);
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        List<SubjectForClassConfigResponse> subjectForClassConfigResponseList = listMapper.mapList(maClassList, SubjectForClassConfigResponse.class);
        return subjectForClassConfigResponseList;
    }

    @Override
    public List<SubjectForClassResponse> findSubjectForClass(Long idSchool, Long idClass) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndDelActiveTrue(idClass);
        if (maClassOptional.isEmpty()) {
            return null;
        }
        List<Subject> cameraList = subjectRepository.findByIdSchoolAndDelActiveTrueOrderBySubjectNameAsc(idSchool);
        if (CollectionUtils.isEmpty(cameraList)) {
            return null;
        }
        List<Long> subjectOfClassList = subjectRepository.findByMaClassList_Id(idClass).stream().map(x -> x.getId()).collect(Collectors.toList());
        List<SubjectForClassResponse> subjectForClassResponseList = listMapper.mapList(cameraList, SubjectForClassResponse.class);
        subjectForClassResponseList.forEach(y -> {
            subjectOfClassList.forEach(z -> {
                if (y.getId().equals(z)) {
                    y.setUsed(AppConstant.APP_TRUE);
                }
            });
        });
        return subjectForClassResponseList;
    }

    @Transactional
    @Override
    public boolean updateSubjectForClass(Long idClass, List<IdObjectRequest> idObjectRequestList) {
        Optional<MaClass> maClassOptional = maClassRepository.findByIdAndDelActiveTrue(idClass);
        if (maClassOptional.isEmpty()) {
            return false;
        }
        subjectRepository.deleteSubjectForClass(idClass);
        idObjectRequestList.forEach(x -> {
            subjectRepository.insertSubjectForClass(idClass, x.getId());
        });
        return true;
    }

    @Override
    public List<SubjectConfigResponse> searchSubjectConfig(Long idSchool, SubjectSearchRequest subjectSearchRequest) {
        List<Subject> subjectList = subjectRepository.searchSubject(idSchool, subjectSearchRequest);
        if (CollectionUtils.isEmpty(subjectList)) {
            return null;
        }
        List<SubjectConfigResponse> subjectConfigResponseList = listMapper.mapList(subjectList, SubjectConfigResponse.class);
        return subjectConfigResponseList;
    }

    @Override
    public SubjectConfigResponse createSubject(Long idSchool, SubjectCreateRequest subjectCreateRequest) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        Subject subject = modelMapper.map(subjectCreateRequest, Subject.class);
        subject.setSubjectCode(GenerateCode.codeSubject());
        subject.setIdSchool(idSchool);
        Subject subjectSaved = subjectRepository.save(subject);
        SubjectConfigResponse subjectConfigResponse = modelMapper.map(subjectSaved, SubjectConfigResponse.class);
        return subjectConfigResponse;
    }

    @Override
    public SubjectConfigResponse updateSubject(SubjectUpdateRequest subjectUpdateRequest) {
        Optional<Subject> subjectOptional = subjectRepository.findByIdAndDelActiveTrue(subjectUpdateRequest.getId());
        if (subjectOptional.isEmpty()) {
            return null;
        }
        Subject subject = subjectOptional.get();
        modelMapper.map(subjectUpdateRequest, subject);
        Subject subjectUpdate = subjectRepository.save(subject);
        SubjectConfigResponse subjectConfigResponse = modelMapper.map(subjectUpdate, SubjectConfigResponse.class);
        return subjectConfigResponse;
    }

    @Override
    public boolean deleteSubjectOne(Long id) {
        Subject subject = subjectRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        long countNumber = subject.getMaClassList().size();
        if (countNumber > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Có " + countNumber + " lớp đang áp dụng môn học này");
        }
        subject.setDelActive(AppConstant.APP_FALSE);
        subjectRepository.save(subject);
        return true;
    }

    @Transactional
    @Override
    public int deleteSubjectMany(List<IdObjectRequest> idObjectRequestList) {
        int number = 0;
        for (IdObjectRequest idObjectRequest : idObjectRequestList) {
            Subject subject = subjectRepository.findByIdAndDelActiveTrue(idObjectRequest.getId()).orElseThrow();
            long countNumber = subject.getMaClassList().stream().filter(BaseEntity::isDelActive).count();
            if (countNumber == 0) {
                subject.setDelActive(AppConstant.APP_FALSE);
                subjectRepository.save(subject);
                number++;
            }
        }
        return number;
    }
}
