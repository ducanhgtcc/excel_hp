package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.dto.ExEmployeeClassDTO;
import com.example.onekids_project.dto.KidsDTO;
import com.example.onekids_project.dto.MaClassDTO;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.DayOffClass;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.employee.InfoEmployeeSchool;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.enums.StudentStatusEnum;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.DayOffClassRepository;
import com.example.onekids_project.repository.GradeRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.classes.ClassSearchNewRequest;
import com.example.onekids_project.request.classes.CreateMaClassRequest;
import com.example.onekids_project.request.classes.SearchMaClassRequest;
import com.example.onekids_project.request.classes.UpdateMaClassRequest;
import com.example.onekids_project.response.classes.*;
import com.example.onekids_project.response.schoolconfig.SchoolConfigResponse;
import com.example.onekids_project.response.teacher.ClassTeacherResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.MaClassService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamSettingService;
import com.example.onekids_project.util.UserPrincipleToUserUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaClassServiceImpl implements MaClassService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private DayOffClassRepository dayOffClassRepository;

    @Autowired
    private OneCamSettingService oneCamSettingService;


    @Override
    public List<MaClassOtherResponse> findClassInGrade(UserPrincipal principal, Long idGrade) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        List<MaClass> maClassList = maClassRepository.findClassInGrade(principal.getIdSchoolLogin(), idGrade);
        List<MaClassOtherResponse> responseList = listMapper.mapList(maClassList, MaClassOtherResponse.class);
        return responseList;
    }

    @Override
    public List<MaClassOtherResponse> findAllMaClassOther(Long idSchool) {
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        return listMapper.mapList(maClassList, MaClassOtherResponse.class);
    }

    @Override
    public List<GradeClassNameResponse> findAllMaClassOtherNew(Long idSchool) {
        List<MaClass> maClassList = maClassRepository.findByIdSchoolAndDelActiveTrue(idSchool);
        List<GradeClassNameResponse> responseList = new ArrayList<>();
        maClassList.forEach(x -> {
            GradeClassNameResponse model = new GradeClassNameResponse();
            model.setId(x.getId());
            model.setClassName(x.getClassName());
            model.setGradeName(x.getGrade().getGradeName());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<ClassTeacherResponse> findClassTeacher(UserPrincipal principal) {
        List<Long> idClassList = UserPrincipleToUserUtils.getIdClassListTeacher(principal);
        List<MaClass> maClassList = maClassRepository.findClassTeacher(principal.getIdSchoolLogin(), idClassList);
        List<ClassTeacherResponse> responseList = listMapper.mapList(maClassList, ClassTeacherResponse.class);
        responseList.forEach(x -> {
            if (x.getId().equals(principal.getIdClassLogin())) {
                x.setChecked(AppConstant.APP_TRUE);
            }
        });
        return responseList;
    }

    @Override
    public ListClassNewResponse searchClassNew(UserPrincipal principal, ClassSearchNewRequest request) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        ListClassNewResponse response = new ListClassNewResponse();
        List<ClassNewResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        List<MaClass> maClassList = maClassRepository.searchClassNew(idSchool, request);
        long total = maClassRepository.countSearchClassNew(idSchool, request);
        maClassList.forEach(x -> {
            ClassNewResponse model = modelMapper.map(x, ClassNewResponse.class);
            List<ExEmployeeClass> exEmployeeClassList = x.getExEmployeeClassList();

            //số giáo viên chủ nhiệm
            List<ExEmployeeClass> masterList = exEmployeeClassList.stream().filter(ExEmployeeClass::isMaster).collect(Collectors.toList());
            List<InfoEmployeeSchool> infoEmployeeSchoolList = this.getEmployeeNumberFromExEmployeeClass(masterList);
            List<String> masterName = infoEmployeeSchoolList.stream().map(InfoEmployeeSchool::getFullName).collect(Collectors.toList());
            model.setMasterNameList(masterName);

            //số giáo viên: giáo viên chủ nhiệm hoặc dạy ít nhất 1 môn
            List<ExEmployeeClass> teacherList = exEmployeeClassList.stream().filter(a -> a.isMaster() || !CollectionUtils.isEmpty(a.getSubjectSet())).collect(Collectors.toList());
            model.setTeacherNumber(this.getEmployeeNumberFromExEmployeeClass(teacherList).size());

            //số nhân sự: người được add vào lớp nhưng ko phải là giáo viên chủ nhiệm và ko dạy môn nào
            List<ExEmployeeClass> employeeList = exEmployeeClassList.stream().filter(a -> !a.isMaster() && CollectionUtils.isEmpty(a.getSubjectSet())).collect(Collectors.toList());
            model.setEmployeeNumber(this.getEmployeeNumberFromExEmployeeClass(employeeList).size());

            //tổng số học sinh
            model.setStudentTotalNumber((int) x.getKidsList().stream().filter(BaseEntity::isDelActive).count());

            //số học sinh ở trạng thái đang học
            List<Kids> studyKidList = x.getKidsList().stream().filter(a -> a.isDelActive() && a.getKidStatus().equals(KidsStatusConstant.STUDYING)).collect(Collectors.toList());
            model.setStudentStudyNumber(studyKidList.size());
            responseList.add(model);
        });
        response.setResponseList(responseList);
        response.setTotal(total);
        return response;
//        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "gg gg");
    }


    @Override
    public MaClassNewResponse findClassNewById(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        MaClassNewResponse response = modelMapper.map(maClass, MaClassNewResponse.class);
        return response;
    }

    @Override
    public List<TeacherInClassResponse> findTeacherInClass(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        List<ExEmployeeClass> exEmployeeClassList = maClass.getExEmployeeClassList().stream().filter(x -> x.getInfoEmployeeSchool().isDelActive() && (x.isMaster() || !CollectionUtils.isEmpty(x.getSubjectSet()))).collect(Collectors.toList());
        return listMapper.mapList(exEmployeeClassList, TeacherInClassResponse.class);
    }

    @Override
    public List<EmployeeInClassResponse> findEmployeeInClass(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = maClass.getExEmployeeClassList().stream().filter(x -> x.getInfoEmployeeSchool().isDelActive() && !x.isMaster() && CollectionUtils.isEmpty(x.getSubjectSet())).map(ExEmployeeClass::getInfoEmployeeSchool).collect(Collectors.toList());
        return listMapper.mapList(infoEmployeeSchoolList, EmployeeInClassResponse.class);
    }


    @Override
    public Optional<MaClassDTO> findByIdMaClass(Long idSchool, Long id) {
        Optional<MaClass> optionalMaClass = maClassRepository.findByIdMaClass(idSchool, id);
        if (optionalMaClass.isEmpty()) {
            return Optional.empty();
        }
        Optional<MaClassDTO> optionalMaClassDTO = Optional.ofNullable(modelMapper.map(optionalMaClass.get(), MaClassDTO.class));
        return optionalMaClassDTO;
    }

    @Override
    public ListMaClassResponse searchMaClass(Long idSchool, Pageable pageable, SearchMaClassRequest searchMaClassRequest) {
        List<MaClass> maClassList = maClassRepository.searchMaClass(idSchool, pageable, searchMaClassRequest);
        if (CollectionUtils.isEmpty(maClassList)) {
            return null;
        }
        ListMaClassResponse listMaClassResponse = convertEntityTODTO(maClassList);
        return listMaClassResponse;
    }

    @Transactional
    @Override
    public MaClassResponse createMaClass(UserPrincipal principal, CreateMaClassRequest createMaClassRequest) {
        Long idSchool = principal.getIdSchoolLogin();
        SchoolConfigResponse schoolConfigResponse = principal.getSchoolConfig();
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return null;
        }
        MaClass newMaClass = modelMapper.map(createMaClassRequest, MaClass.class);
        if (createMaClassRequest.getIdGrade() != null) {
            Optional<Grade> gradeOptional = gradeRepository.findByIdGrade(idSchool, createMaClassRequest.getIdGrade());
            if (gradeOptional.isPresent()) {
                newMaClass.setGrade(gradeOptional.get());
            }
        }

        newMaClass.setIdSchool(idSchool);
        newMaClass.setClassCode(RandomStringUtils.random(8, true, true));
        newMaClass.setMorningSaturday(schoolConfigResponse.isMorningSaturday());
        newMaClass.setAfternoonSaturday(schoolConfigResponse.isAfternoonSaturday());
        newMaClass.setEveningSaturday(schoolConfigResponse.isEveningSaturday());
        newMaClass.setSunday(schoolConfigResponse.isSunday());
        MaClass savedMaClass = maClassRepository.save(newMaClass);
        this.createExtraClass(savedMaClass);
        return modelMapper.map(savedMaClass, MaClassResponse.class);
    }

    @Override
    public boolean updateMaClass(UserPrincipal principal, UpdateMaClassRequest updateMaClassRequest) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(updateMaClassRequest.getId()).orElseThrow();
        modelMapper.map(updateMaClassRequest, maClass);
        maClassRepository.save(maClass);
        return true;
    }

    @Override
    public boolean deleteMaClass(UserPrincipal principal, Long id) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        MaClass maClass = maClassRepository.findByIdMaClass(idSchool, id).orElseThrow();
        List<ExEmployeeClass> exEmployeeClassList = maClass.getExEmployeeClassList();
        List<InfoEmployeeSchool> infoEmployeeSchoolList = this.getEmployeeNumberFromExEmployeeClass(exEmployeeClassList);
        if (!CollectionUtils.isEmpty(infoEmployeeSchoolList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.DELETE_CLASS_EXIST_EMPLOYEE);
        }
        List<Kids> kidsList = maClass.getKidsList().stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(kidsList)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageWebConstant.DELETE_CLASS_EXIST_KIDS);
        }
        maClass.setDelActive(false);
        maClassRepository.save(maClass);
        return true;
    }

    /**
     * tìm kiếm danh sách các lớp, chuyển entity thành dto
     *
     * @param maClassList
     * @return
     */
    private ListMaClassResponse convertEntityTODTO(List<MaClass> maClassList) {
        ListMaClassResponse listMaClassResponse = new ListMaClassResponse();
        List<MaClassDTO> maClassDTOList = listMapper.mapList(maClassList, MaClassDTO.class);
        maClassDTOList.forEach(x -> {
            if (CollectionUtils.isEmpty(x.getKidsList())) {
                x.setKidsNumber(0);
            } else {
                //lấy số học sinh trong lớp
                List<KidsDTO> kidsDTOList = x.getKidsList().stream().filter(y -> y.isDelActive() && StudentStatusEnum.STUDYING.toString().equals(y.getKidStatus())).collect(Collectors.toList());
                x.setKidsNumber(kidsDTOList.size());
            }

            if (CollectionUtils.isEmpty(x.getExEmployeeClassList())) {
                x.setTeacherNumber(0);
            } else {
                //lấy số giáo viên: isMaster=true hoặc listIdSubject!=null
                List<ExEmployeeClassDTO> exEmployeeClassDTOList = x.getExEmployeeClassList().stream().filter(y -> y.isDelActive() && (StringUtils.isNotBlank(y.getListIdSubject()) || y.isMaster())).collect(Collectors.toList());
                x.setTeacherNumber(exEmployeeClassDTOList.size());

                //lấy tên giáo viên chủ nhiệm
                List<String> masterNameList = exEmployeeClassDTOList.stream().filter(z -> z.isDelActive() && z.isMaster()).map(z -> z.getInfoEmployeeSchool().getFullName()).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(masterNameList)) {
                    String teacherList = StringUtils.join(masterNameList, "\n- ");
                    teacherList = "- " + teacherList;

                    x.setMasterTeacherList(teacherList);
                }
            }
        });
        listMaClassResponse.setMaClassList(maClassDTOList);
        return listMaClassResponse;
    }

    private List<InfoEmployeeSchool> getEmployeeNumberFromExEmployeeClass(List<ExEmployeeClass> exEmployeeClassList) {
        List<InfoEmployeeSchool> infoEmployeeSchoolList = exEmployeeClassList.stream().map(ExEmployeeClass::getInfoEmployeeSchool).collect(Collectors.toList());
        infoEmployeeSchoolList = infoEmployeeSchoolList.stream().filter(BaseEntity::isDelActive).collect(Collectors.toList());
        return infoEmployeeSchoolList;
    }

    private void createExtraClass(MaClass maClass) {
        LocalDate date = LocalDate.now();
        while (date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        do {
            DayOffClass dayOffClass = new DayOffClass();
            dayOffClass.setMaClass(maClass);
            dayOffClass.setDate(date);
            dayOffClass.setNote(AppConstant.DAY_OFF);
            dayOffClassRepository.save(dayOffClass);
            date = date.plusDays(7);
        } while (date.getYear() <= 2024);
        oneCamSettingService.createOneCamSettingDefault(maClass);
    }
}
