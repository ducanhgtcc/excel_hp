package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.dto.GradeDTO;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.school.Grade;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.GradeRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.base.PageNumberWebRequest;
import com.example.onekids_project.request.grade.CreateGradeRequest;
import com.example.onekids_project.request.grade.SearchGradeRequest;
import com.example.onekids_project.request.grade.UpdateGradeRequest;
import com.example.onekids_project.response.grade.GradeOtherResponse;
import com.example.onekids_project.response.grade.GradeResponse;
import com.example.onekids_project.response.grade.GradeUniqueResponse;
import com.example.onekids_project.response.grade.ListGradeResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.GradeService;
import com.example.onekids_project.validate.CommonValidate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public List<GradeUniqueResponse> findGradeInSchool(UserPrincipal principal) {
        CommonValidate.checkExistIdSchoolInPrinciple(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<Grade> gradeList = gradeRepository.findGradeInSchool(idSchool);
        List<GradeUniqueResponse> responseList = listMapper.mapList(gradeList, GradeUniqueResponse.class);
        return responseList;
    }

    @Override
    public ListGradeResponse findAllGrade(Long idSchool, PageNumberWebRequest request) {
        List<Grade> gradeList = gradeRepository.findAllGrade(idSchool, request);
        if (CollectionUtils.isEmpty(gradeList)) {
            return null;
        }
//        mapper for list
        List<GradeDTO> gradeDTOList = listMapper.mapList(gradeList, GradeDTO.class);
        gradeDTOList.forEach(x -> {
            if (CollectionUtils.isEmpty(x.getMaClassList())) {
                x.setClassNumber(0);
            } else {
                x.setClassNumber(x.getMaClassList().stream().filter(a -> a.isDelActive()).collect(Collectors.toList()).size());
            }
        });

        ListGradeResponse listGradeResponse = new ListGradeResponse();
        listGradeResponse.setGradeList(gradeDTOList);
        long total = gradeRepository.countGrade(idSchool);
        listGradeResponse.setTotal(total);

        return listGradeResponse;
    }

    @Override
    public Optional<GradeDTO> findByIdGrade(Long idSchool, Long id) {
        //lấy từ DB, nếu không có giá trị nào thì kết quả sẽ là Optional.empty
        //isEmpty và isPresent khác với null, nếu giá trị là null mà thực hiện .isEmpty hoặc .isPresent thì sẽ báo lỗi
        Optional<Grade> optionalGrade = gradeRepository.findByIdGrade(idSchool, id);
        if (optionalGrade.isEmpty()) {
            return Optional.empty();
        }
//      Optional.empty có thể thực hiện mapper được, khi đó dối tượng được mapper sẽ có Optional.empty là false
//        đối tượng null khi mapper sẽ bị lỗi
//      nên cần đoạn check ở trên
        Optional<GradeDTO> optionalGradeDTO = Optional.ofNullable(modelMapper.map(optionalGrade.get(), GradeDTO.class));
        return optionalGradeDTO;
    }

    @Override
    public ListGradeResponse searchGrade(Long idSchool, Pageable pageable, SearchGradeRequest searchGradeRequest) {
        List<Grade> gradeList = gradeRepository.searchGrade(idSchool, pageable, searchGradeRequest);
        if (CollectionUtils.isEmpty(gradeList)) {
            return null;
        }
        List<GradeDTO> gradeDTOList = listMapper.mapList(gradeList, GradeDTO.class);
        ListGradeResponse listGradeResponse = new ListGradeResponse();
        listGradeResponse.setGradeList(gradeDTOList);

        return listGradeResponse;
    }

    @Override
    public GradeResponse createGrade(UserPrincipal principal, Long idSchool, CreateGradeRequest gradeAddRequest) {
        CommonValidate.checkDataPlus(principal);
        // sửa sau, lấy code tìm kiếm trường theo id của tuấn
        School school = schoolRepository.findById(idSchool).orElseThrow(() -> new RuntimeException("Fail! -> Không tìm thấy school có id=" + idSchool));
        Grade newGrade = modelMapper.map(gradeAddRequest, Grade.class);
        newGrade.setSchool(school);
        Grade savedGrade = gradeRepository.save(newGrade);
        GradeResponse gradeAddResponse = modelMapper.map(savedGrade, GradeResponse.class);
        return gradeAddResponse;
    }

    @Override
    public GradeResponse updateGrade(UserPrincipal principal, Long idSchool, UpdateGradeRequest updateGradeRequest) {
        CommonValidate.checkDataPlus(principal);
        Optional<Grade> gradeOptional = gradeRepository.findByIdGrade(idSchool, updateGradeRequest.getId());
        if (gradeOptional.isEmpty()) {
            return null;
        }

        Grade odlGrade = gradeOptional.get();
        modelMapper.map(updateGradeRequest, odlGrade);
        Grade newGrade = gradeRepository.save(odlGrade);
        GradeResponse gradeEditResponse = modelMapper.map(newGrade, GradeResponse.class);
        return gradeEditResponse;
    }

    @Override
    public boolean deleteGrade(UserPrincipal principal, Long idSchool, Long id) {
        CommonValidate.checkDataPlus(principal);
        Optional<Grade> gradeOptional = gradeRepository.findByIdGrade(idSchool, id);
        if (gradeOptional.isEmpty()) {
            return false;
        }
        Grade deleteGrade = gradeOptional.get();
        List<MaClass> maClassList = deleteGrade.getMaClassList();
        if (!CollectionUtils.isEmpty(maClassList)) {
            List<MaClass> classList = maClassList.stream().filter(x -> x.isDelActive()).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(classList)) {
                return false;
            }
        }
        deleteGrade.setDelActive(false);
        gradeRepository.save(deleteGrade);
        return true;
    }

}
