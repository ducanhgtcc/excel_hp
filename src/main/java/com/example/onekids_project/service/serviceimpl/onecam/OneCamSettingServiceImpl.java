package com.example.onekids_project.service.serviceimpl.onecam;

import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.onecam.OneCamSetting;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.MaClassRepository;
import com.example.onekids_project.repository.OneCamSettingRepository;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.response.onecam.OneCameSettingResponse;
import com.example.onekids_project.service.servicecustom.onecam.OneCamSettingService;
import com.example.onekids_project.util.SchoolUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lavanviet
 */
@Service
public class OneCamSettingServiceImpl implements OneCamSettingService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;
    @Autowired
    private OneCamSettingRepository oneCamSettingRepository;
    @Autowired
    private MaClassRepository maClassRepository;

    private final String arriveViewText = "Học sinh chưa được điểm danh đến, Vui lòng quay lại sau!";
    private final String leaveViewText = "Học sinh đã được điểm danh về, Vui lòng quay lại sau!";
    private final String viewLimitText = "Bạn đã xem online liên tục quá lâu, Vui lòng quay lại sau!";
    private final String timeViewText = "Bạn không thể xem camera trong thời gian này, Vui lòng quay lại sau!";

    @Override
    public void createOneCamSettingDefault(MaClass maClass) {
        OneCamSetting entity = new OneCamSetting();
        entity.setArriveViewStatus(true);
        entity.setArriveViewText(arriveViewText);

        entity.setLeaveNoViewStatus(true);
        entity.setLeaveNoViewText(leaveViewText);

        entity.setViewLimitStatus(true);
        entity.setViewLimitText(viewLimitText);
        entity.setViewLimitNumber(10);

        entity.setTimeViewStatus(true);
        entity.setTimeViewText(timeViewText);
        entity.setStartTime1(LocalTime.of(5, 0, 0));
        entity.setEndTime1(LocalTime.of(19, 0, 0));
        entity.setMaClass(maClass);
        oneCamSettingRepository.save(entity);
    }

    @Override
    public List<OneCameSettingResponse> searchOneCameSettingService(MediaSettingSearchRequest request) {
        Long idSchool = SchoolUtils.getIdSchool();
        List<MaClass> maClassList = maClassRepository.searchMaclassForMediaSetting(idSchool, request);
        List<OneCameSettingResponse> responseList = new ArrayList<>();
        maClassList.forEach(x -> {
            OneCamSetting oneCamSetting = x.getOneCamSetting();
            OneCameSettingResponse model = modelMapper.map(oneCamSetting, OneCameSettingResponse.class);
            model.setClassName(x.getClassName());
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public void updateOneCameSettingService(OneCameSettingResponse request) {
        this.checkRangeTimeValid(request.getStartTime1(), request.getEndTime1());
        this.checkRangeTimeValid(request.getStartTime2(), request.getEndTime2());
        this.checkRangeTimeValid(request.getStartTime3(), request.getEndTime3());
        this.checkRangeTimeValid(request.getStartTime4(), request.getEndTime4());
        this.checkRangeTimeValid(request.getStartTime5(), request.getEndTime5());
        OneCamSetting entity = oneCamSettingRepository.findById(request.getId()).orElseThrow();
        modelMapper.map(request, entity);
        oneCamSettingRepository.save(entity);
    }

    private void checkRangeTimeValid(LocalTime start, LocalTime end) {
        LocalDate dd = LocalDate.now();
        if (Objects.nonNull(start) || Objects.nonNull(end)) {
            if (start.isAfter(end)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu không được lớn hơn thời gian kết thúc");
            } else if (start.equals(end)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thời gian bắt đầu và kết thúc không được bằng nhau");
            }
        }
    }
}
