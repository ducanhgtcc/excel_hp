package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.KidsStatusConstant;
import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.employee.ExEmployeeClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.kids.KidsGroup;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.mobile.plus.request.student.GroupPlusRequest;
import com.example.onekids_project.mobile.plus.response.student.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.StudentPlusService;
import com.example.onekids_project.repository.GradeRepository;
import com.example.onekids_project.repository.KidsGroupRepository;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.StudentUtil;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentPlusServiceImpl implements StudentPlusService {

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KidsGroupRepository kidsGroupRepository;


    @Override
    public StudentYearPlusResponse searchStudentYear(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        StudentYearPlusResponse response = new StudentYearPlusResponse();
        LocalDate date = LocalDate.now();
        Long idSchool = principal.getIdSchoolLogin();
        List<Kids> kidsList = kidsRepository.findByDelActiveTrueAndIdSchool(idSchool);
        AtomicInteger leaveSchool = new AtomicInteger();
        AtomicInteger reserve = new AtomicInteger();
        AtomicInteger studyWait = new AtomicInteger();
        kidsList.forEach(x -> {
            if (x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.LEAVE_SCHOOL)) {
                leaveSchool.addAndGet(1);
            } else if (x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.RESERVE)) {
                reserve.addAndGet(1);
            } else if (x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDY_WAIT)) {
                studyWait.addAndGet(1);
            }
        });
        response.setNumberLeaveSchool(leaveSchool.get());
        response.setNumberReserve(reserve.get());
        response.setNumberWaitStudy(studyWait.get());
        response.setYear(date.getYear());
        return response;
    }

    @Override
    public List<GradePlusResponse> searchGrade(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<GradePlusResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        List<Grade> gradeList = gradeRepository.findByDelActiveTrueAndSchool_Id(idSchool);
        gradeList.forEach(x -> {
            GradePlusResponse response = new GradePlusResponse();
            response.setId(x.getId());
            response.setName(x.getGradeName());
            response.setNumberClass((int) x.getMaClassList().stream().filter(BaseEntity::isDelActive).count());
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ClassPlusResponse> searchClass(UserPrincipal principal, Long idGrade) {
        CommonValidate.checkDataPlus(principal);
        List<ClassPlusResponse> responseList = new ArrayList<>();
        Long idSchool = principal.getIdSchoolLogin();
        List<MaClass> maClassList = maClassRepository.findClassBySchool(idSchool, idGrade);
        maClassList.forEach(x -> {
            ClassPlusResponse response = new ClassPlusResponse();
            response.setNameClass(x.getClassName());
            response.setId(x.getId());
            response.setNumberKid((int) x.getKidsList().stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDYING) && y.isDelActive()).count());
            List<ExEmployeeClass> employeeClassList = x.getExEmployeeClassList().stream().filter(k -> k.getInfoEmployeeSchool().isDelActive() && k.getInfoEmployeeSchool().getEmployeeStatus().equals(AppConstant.EMPLOYEE_STATUS_WORKING)).collect(Collectors.toList());
            List<String> namerList = new ArrayList<>();
            employeeClassList.forEach(z -> {
                String name = z.getInfoEmployeeSchool().getFullName();
                namerList.add(name);
            });
            response.setTeacherList(namerList);
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<KidPlusResponse> searchKid(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataPlus(principal);
        List<KidPlusResponse> responseList = new ArrayList<>();
        MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(idClass).orElseThrow();
        List<Kids> kidsList = maClass.getKidsList().stream().filter(x -> x.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDYING) && x.isDelActive()).collect(Collectors.toList());
        kidsList = kidsList.stream().sorted(Comparator.comparing(Kids::getFirstName)).collect(Collectors.toList());
        kidsList.forEach(y -> {
            KidPlusResponse response = new KidPlusResponse();
            response.setAvatar(ConvertData.getAvatarKid(y));
            response.setKidName(y.getFullName());
            response.setNickName(StringUtils.isNotBlank(y.getNickName()) ? y.getNickName() : "");
            response.setId(y.getId());
            response.setNameParent(y.getRepresentation().equals(AppConstant.MOTHER) ? y.getMotherName() : y.getFatherName());
            response.setLoginStatus(StudentUtil.setLoginStatus(y));
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public InfoKidResponse searchDeatailKid(UserPrincipal principal, Long idKid) {
        CommonValidate.checkDataPlus(principal);
        InfoKidResponse response = new InfoKidResponse();
        Kids kids = kidsRepository.findById(idKid).orElseThrow();
        modelMapper.map(kids, response);
        response.setBirthDay(ConvertData.convertDateString(kids.getBirthDay()));
        response.setAvatarKid(ConvertData.getAvatarKid(kids));
        if (kids.getParent() != null) {
            response.setPhoneParent(kids.getParent().getMaUser().getPhone());
        }
        return response;
    }

    @Override
    public List<GroupPlusResponse> searchGroup(UserPrincipal principal) {
        CommonValidate.checkDataPlus(principal);
        List<GroupPlusResponse> responseList = new ArrayList<>();
        List<KidsGroup> kidsGroupList = kidsGroupRepository.findByDelActiveTrueAndIdSchool(principal.getIdSchoolLogin());
        kidsGroupList.forEach(x -> {
            GroupPlusResponse response = new GroupPlusResponse();
            response.setId(x.getId());
            response.setName(x.getGroupName());
            List<Kids> kidsList = x.getKidsList().stream().filter(y -> y.isDelActive() && y.getKidStatus().equals(KidsStatusConstant.STUDYING)).collect(Collectors.toList());
            response.setNumberClass(kidsList.size());
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<FeatureKidPlusResponse> searchKidGroup(UserPrincipal principal, GroupPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<FeatureKidPlusResponse> responseList = new ArrayList<>();
        KidsGroup kidsGroup = kidsGroupRepository.findById(request.getIdGroup()).orElseThrow();
        List<Kids> kidsList = kidsGroup.getKidsList().stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDYING) && y.isDelActive()).collect(Collectors.toList());
        if (request.getIdClass() != null) {
            kidsList = kidsGroup.getKidsList().stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDYING) && y.getMaClass().getId().equals(request.getIdClass())).collect(Collectors.toList());
        }
        kidsList.forEach(x -> {
//            FeatureKidPlusResponse response = new FeatureKidPlusResponse();
//            response.setFeatureKid(this.setFearetureKid(x));
//            response.setTitle(kidsGroup.getGroupName());
            responseList.add(this.setFearetureKid(x));
        });
        return responseList;
    }

    @Override
    public List<FeatureKidPlusResponse> searchKidWait(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataPlus(principal);
        List<FeatureKidPlusResponse> responseList = new ArrayList<>();
//        List<MaClass> maClassList = maClassRepository.searchMaClassWithSchool(principal.getIdSchoolLogin(), idClass);
//        List<Kids> kidsList = new ArrayList<>();
//        for (MaClass maClass : maClassList) {
//            kidsList.addAll(maClass.getKidsList());
//        }
        List<Kids> kidsList = kidsRepository.findAlbumClass(principal.getIdSchoolLogin(), idClass);
        kidsList = kidsList.stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.STUDY_WAIT)).collect(Collectors.toList());
        kidsList.forEach(x -> {
            responseList.add(this.setFearetureKid(x));
        });
        return responseList;
    }

    @Override
    public List<FeatureKidPlusResponse> searchKidReserve(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataPlus(principal);
        List<FeatureKidPlusResponse> responseList = new ArrayList<>();

        List<Kids> kidsList = kidsRepository.findAlbumClass(principal.getIdSchoolLogin(), idClass);
        kidsList = kidsList.stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.RESERVE)).collect(Collectors.toList());
        kidsList.forEach(x -> {
            responseList.add(this.setFearetureKid(x));
        });
        return responseList;
    }

    @Override
    public List<FeatureKidPlusResponse> searchKidOff(UserPrincipal principal, Long idClass) {
        CommonValidate.checkDataPlus(principal);
        List<FeatureKidPlusResponse> responseList = new ArrayList<>();
        List<Kids> kidsList = kidsRepository.findAlbumClass(principal.getIdSchoolLogin(), idClass);
        kidsList = kidsList.stream().filter(y -> y.getKidStatus().equalsIgnoreCase(KidsStatusConstant.LEAVE_SCHOOL)).collect(Collectors.toList());
        kidsList.forEach(x -> {
            responseList.add(this.setFearetureKid(x));
        });
        return responseList;
    }


    /**
     * set đặc trưng cho học sinh
     *
     * @param x
     * @return
     */
    private FeatureKidPlusResponse setFearetureKid(Kids x) {
        FeatureKidPlusResponse response = new FeatureKidPlusResponse();
        response.setAvatarKid(ConvertData.getAvatarKid(x));
        response.setLoginStatus(StudentUtil.setLoginStatus(x));
        response.setNameClass(x.getMaClass().getClassName());
        response.setNameKid(x.getFullName());
        response.setId(x.getId());
        response.setNameParent(x.getRepresentation().equals(AppConstant.MOTHER) ? x.getMotherName() : x.getFatherName());
        return response;
    }
}
