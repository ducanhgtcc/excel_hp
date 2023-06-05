package com.example.onekids_project.supperpluscontroller;

import com.example.onekids_project.request.onecam.OneCamConfigRequest;
import com.example.onekids_project.request.schoolconfig.MediaSettingSearchRequest;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.response.onecam.OneCamConfigResponse;
import com.example.onekids_project.response.onecam.OneCamNewsResponse;
import com.example.onekids_project.response.onecam.OneCameSettingResponse;
import com.example.onekids_project.service.servicecustom.onecam.OneCamConfigService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamNewsService;
import com.example.onekids_project.service.servicecustom.onecam.OneCamSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author lavanviet
 */
@RestController
@RequestMapping("/web/onecam")
public class OneCamWebController {
    @Autowired
    private OneCamNewsService oneCamNewsService;
    @Autowired
    private OneCamSettingService oneCamSettingService;

    @RequestMapping(method = RequestMethod.GET, value = "/news")
    public ResponseEntity searchOneCameNews() {
        OneCamNewsResponse response = oneCamNewsService.getOneCamNews();
        return NewDataResponse.setDataSearch(response);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/news")
    public ResponseEntity updateOneCameNews(@RequestBody OneCamNewsResponse request) {
        oneCamNewsService.updateOneCamNews(request);
        return NewDataResponse.setDataSave(true);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/setting")
    public ResponseEntity searchOneCamSetting(@Valid MediaSettingSearchRequest request) {
        List<OneCameSettingResponse> responseList = oneCamSettingService.searchOneCameSettingService(request);
        return NewDataResponse.setDataSearch(responseList);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/setting")
    public ResponseEntity updateOneCamSetting(@Valid @RequestBody OneCameSettingResponse request) {
        oneCamSettingService.updateOneCameSettingService(request);
        return NewDataResponse.setDataUpdate(true);
    }

}
