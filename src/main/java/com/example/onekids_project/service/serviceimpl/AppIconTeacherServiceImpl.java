package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppIconName;
import com.example.onekids_project.entity.school.AppIconTeacher;
import com.example.onekids_project.entity.school.School;
import com.example.onekids_project.repository.AppIconTeacherRepository;
import com.example.onekids_project.repository.SchoolRepository;
import com.example.onekids_project.request.system.IconTeacherConfigRequest;
import com.example.onekids_project.response.school.AppIconTeacherResponse;
import com.example.onekids_project.response.school.ListAppIconTeacherResponse;
import com.example.onekids_project.response.system.IconTeacherConfigResponse;
import com.example.onekids_project.service.servicecustom.AppIconTeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppIconTeacherServiceImpl implements AppIconTeacherService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppIconTeacherRepository appIconTeacherRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public ListAppIconTeacherResponse getAppIconTeacher(Long idSchool) {
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findAppIconTeacher(idSchool);
        ListAppIconTeacherResponse response=new ListAppIconTeacherResponse();
        List<AppIconTeacherResponse> allList = new ArrayList<>();
        allList.add(this.getAppIcon(appIconTeacher.getMessageName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getMedicineName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getAbsentName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getHealthName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getAttendanceName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getAlbumName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getEvaluateName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getStudentFeesName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getVideoName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getLearnName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getMenuName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getBirthdayName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getCameraName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getUtilityName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getSalaryName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getFeedbackName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(appIconTeacher.getFacebookName(), AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        allList.add(this.getAppIcon(AppIconName.NEWS_NAME, AppConstant.APP_TRUE, AppConstant.APP_TRUE));
        if (allList.size() <= 10) {
            response.setAppIconTeacherResponseList1(allList);
        } else {
            response.setAppIconTeacherResponseList1(allList.subList(0, 10));
            response.setAppIconTeacherResponseList2(allList.subList(10, allList.size()));
        }
        return response;
    }

    private AppIconTeacherResponse getAppIcon(String name, boolean status, boolean statusShow) {
        AppIconTeacherResponse icon = new AppIconTeacherResponse();
        icon.setIconName(name);
        icon.setStatus(status);
        icon.setStatusShow(statusShow);
        return icon;
    }


    @Override
    public boolean createAppIconTeacher(Long idSchool) {
        Optional<School> schoolOptional = schoolRepository.findByIdAndDelActive(idSchool, AppConstant.APP_TRUE);
        if (schoolOptional.isEmpty()) {
            return false;
        }
        Optional<AppIconTeacher> appIconTeacherOptional = appIconTeacherRepository.findBySchoolId(idSchool);
        if (appIconTeacherOptional.isPresent()) {
            return false;
        }
        School school = schoolOptional.get();
        AppIconTeacher appIconTeacher = new AppIconTeacher();
        appIconTeacher.setSchool(school);
        appIconTeacherRepository.save(appIconTeacher);
        return true;
    }

    @Override
    public IconTeacherConfigResponse findIconByIdSchoolConfig(Long idSchool) {
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findBySchoolId(idSchool).orElseThrow(() -> new NotFoundException("not found icon teacher by id"));
        IconTeacherConfigResponse iconTeacherConfigResponse = modelMapper.map(appIconTeacher, IconTeacherConfigResponse.class);
        return iconTeacherConfigResponse;
    }

    @Override
    public IconTeacherConfigResponse updateIconSchool(Long id, IconTeacherConfigRequest iconTeacherConfigRequest) {
        AppIconTeacher appIconTeacher = appIconTeacherRepository.findBySchoolId(id).orElseThrow(() -> new NotFoundException("not found icon teacher by id"));
        modelMapper.map(iconTeacherConfigRequest, appIconTeacher);
        AppIconTeacher appIconParentSaved = appIconTeacherRepository.save(appIconTeacher);
        IconTeacherConfigResponse iconTeacherConfigResponse = modelMapper.map(appIconParentSaved, IconTeacherConfigResponse.class);
        return iconTeacherConfigResponse;
    }
}
