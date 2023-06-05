package com.example.onekids_project.service.serviceimpl.parentweb;

import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.repository.ReceiversRepository;
import com.example.onekids_project.request.parentweb.NotifyParentWebRequest;
import com.example.onekids_project.response.common.FileResponse;
import com.example.onekids_project.response.parentweb.ListNotifyParentResponse;
import com.example.onekids_project.response.parentweb.NotifyParentResponse;
import com.example.onekids_project.service.servicecustom.parentweb.ParentNotifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lavanviet
 */
@Service
public class ParentNotifyServiceImpl implements ParentNotifyService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Override
    public ListNotifyParentResponse findNotifyWeb(NotifyParentWebRequest request) {
        ListNotifyParentResponse response = new ListNotifyParentResponse();
        List<Receivers> receiversList = receiversRepository.findNotifyKidsForWeb(request);
        long count = receiversRepository.countNotifyKidsForWeb(request);
        List<NotifyParentResponse> dataList = new ArrayList<>();
        receiversList.forEach(x -> {
            NotifyParentResponse model = modelMapper.map(x, NotifyParentResponse.class);
            model.setTitle(x.getAppSend().getSendTitle());
            model.setContent(x.getAppSend().getSendContent());
            if (CollectionUtils.isNotEmpty(x.getAppSend().getUrlFileAppSendList())) {
                List<FileResponse> fileList = new ArrayList<>();
                x.getAppSend().getUrlFileAppSendList().forEach(a -> {
                    FileResponse file = new FileResponse();
                    file.setId(a.getId());
                    file.setName(a.getName());
                    file.setUrl(StringUtils.isNoneBlank(a.getAttachPicture()) ? a.getAttachPicture() : a.getAttachFile());
                    fileList.add(file);
                });
                model.setFileList(fileList);
            }
            dataList.add(model);
        });

        response.setTotal(count);
        response.setDataList(dataList);
        return response;
    }

    @Override
    public void viewNotifyWeb(List<Long> idList) {
        List<Receivers> receiversList = receiversRepository.findByIdIn(idList);
        receiversList.forEach(x -> x.setUserUnread(true));
        receiversRepository.saveAll(receiversList);
    }
}
