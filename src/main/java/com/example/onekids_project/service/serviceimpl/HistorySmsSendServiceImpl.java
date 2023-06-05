package com.example.onekids_project.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.MessageWebConstant;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.entity.user.SmsReceivers;
import com.example.onekids_project.entity.user.SmsSend;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.teacher.response.historynotifi.HistorySmsSendNotifiResponse;
import com.example.onekids_project.repository.KidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SmsReiceiverRepository;
import com.example.onekids_project.repository.SmsSendRepository;
import com.example.onekids_project.request.notifihistory.SearchHistorySmsSendNewtRequest;
import com.example.onekids_project.response.excel.ExcelData;
import com.example.onekids_project.response.excel.ExcelResponse;
import com.example.onekids_project.response.notifihistory.ListHistorySmsSendResponse;
import com.example.onekids_project.response.notifihistory.historySmsSendNewResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.service.servicecustom.HistorySmsSendService;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.ExportExcelUtils;
import com.example.onekids_project.util.PrincipalUtils;
import com.example.onekids_project.util.SchoolUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Service
public class HistorySmsSendServiceImpl implements HistorySmsSendService {

    @Autowired
    private SmsReiceiverRepository smsReiceiverRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private ListMapper listMapper;

    @Override
    public ListHistorySmsSendResponse searchHistorySmsSend(UserPrincipal principal, SearchHistorySmsSendNewtRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        ListHistorySmsSendResponse response = new ListHistorySmsSendResponse();
        List<SmsSend> smsSendList = smsSendRepository.searchHistorySmsSend(idSchool, request);
        List<historySmsSendNewResponse> responseList = new ArrayList<>();
        smsSendList.forEach(x -> {
            historySmsSendNewResponse model = new historySmsSendNewResponse();
            MaUser maUser = maUserRepository.findById(x.getIdCreated()).orElseThrow();
            if (maUser != null) {
                model.setCreatedName(maUser.getFullName());
            }
            int coutSuccess;
            int coutFail;
            int coutFail2;
            if (x.getSmsReceiversList().size() > 0) {
                List<SmsReceivers> smsReceiversList = smsReiceiverRepository.findAllByIdSmsSend(x.getId());
                coutSuccess = (int) smsReceiversList.stream().filter(a -> a.getSmsCode() != null).filter(a -> a.getSmsCode().getId() == 1).count();
                coutFail = (int) smsReceiversList.stream().filter(b -> b.getSmsCode() != null).filter(b -> b.getSmsCode().getId() != 1).count();
                coutFail2 = (int) smsReceiversList.stream().filter(c -> c.getSmsCode() == null).count();
                model.setCoutFail(coutFail + coutFail2);
                model.setCoutSuccess(coutSuccess);
                model.setCoutAll(x.getSmsReceiversList().size());
            }
            model.setId(x.getId());
            model.setTimeAlarm(x.getCreatedDate());
            model.setSendType(x.getSendType());
            model.setSmsSendTotal(x.getSmsTotal());
            responseList.add(model);
        });
        long total = smsSendRepository.coutSearchSmsSendHistory(idSchool, request);
        List<historySmsSendNewResponse> historySmsSendNewResponseList = listMapper.mapList(responseList, historySmsSendNewResponse.class);
        response.setTotal(total);
        response.setResponseList(historySmsSendNewResponseList);
        return response;
    }

    @Override
    public List<HistorySmsSendNotifiResponse> findDetailHistory(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        SmsSend smsSend = smsSendRepository.findById(id).orElseThrow();
        Long idSmsSend = smsSend.getId();
        List<SmsReceivers> smsReceiversList = smsReiceiverRepository.findByIdSmsSend(idSmsSend);
        List<HistorySmsSendNotifiResponse> responseList = new ArrayList<>();
        smsReceiversList.forEach(x -> {
            HistorySmsSendNotifiResponse model = new HistorySmsSendNotifiResponse();
            MaUser maUser = maUserRepository.findById(x.getIdUserReceiver()).orElseThrow();
            if (maUser != null) {
                model.setName(maUser.getFullName());
                model.setPhone(maUser.getPhone());
                model.setType(maUser.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT) ? MessageWebConstant.PARENT : MessageWebConstant.EMPLOYEE);
            }
            model.setSendContent(x.getSmsSend().getSendContent());
            model.setNumberSend(smsReceiversList.size());
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                model.setClassName(kids.getFullName() + " - " + kids.getMaClass().getClassName());
            }
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<HistorySmsSendNotifiResponse> findDetailHistoryFail(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        SmsSend smsSend = smsSendRepository.findById(id).orElseThrow();
        Long idSmsSend = smsSend.getId();
        List<SmsReceivers> smsReceiversList = smsReiceiverRepository.findByIdSmsSendFail(idSmsSend);
        List<HistorySmsSendNotifiResponse> responseList = new ArrayList<>();
        smsReceiversList.forEach(x -> {
            HistorySmsSendNotifiResponse model = new HistorySmsSendNotifiResponse();
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(x.getIdUserReceiver()).orElseThrow();
            model.setName(maUser.getFullName());
            model.setPhone(maUser.getPhone());
            model.setSendContent(x.getSmsSend().getSendContent());
            model.setNumberSend(smsReceiversList.size());
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                model.setClassName(kids.getFullName() + " - " + kids.getMaClass().getClassName());
            }
            model.setType(maUser.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT) ? MessageWebConstant.PARENT : MessageWebConstant.EMPLOYEE);
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<HistorySmsSendNotifiResponse> findDetailHistoryAll(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        SmsSend smsSend = smsSendRepository.findById(id).orElseThrow();
        Long idSmsSend = smsSend.getId();
        List<SmsReceivers> smsReceiversList = smsReiceiverRepository.findByIdSmsSendAll(idSmsSend);
        List<HistorySmsSendNotifiResponse> responseList = new ArrayList<>();
        smsReceiversList.forEach(x -> {
            HistorySmsSendNotifiResponse model = new HistorySmsSendNotifiResponse();
            MaUser maUser = maUserRepository.findById(x.getIdUserReceiver()).orElseThrow();
            model.setName(maUser.getFullName());
            model.setPhone(maUser.getPhone());
            model.setSendContent(x.getSmsSend().getSendContent());
            model.setNumberSend(smsReceiversList.size());
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                model.setClassName(kids.getFullName() + " - " + kids.getMaClass().getClassName());
            } else {
                model.setClassName("");
            }
            model.setType(maUser.getAppType().equalsIgnoreCase(AppTypeConstant.PARENT) ? MessageWebConstant.PARENT : MessageWebConstant.EMPLOYEE);
            if (x.getSmsCode() != null) {
                if (x.getSmsCode().getId() == 1) {
                    model.setFail(AppConstant.APP_TRUE);
                } else {
                    model.setFail(AppConstant.APP_FALSE);
                }
            } else {
                model.setFail(AppConstant.APP_FALSE);
            }
            responseList.add(model);
        });
        return responseList;
    }

    @Override
    public List<HistorySmsSendNotifiResponse> viewContent(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        SmsSend smsSend = smsSendRepository.findById(id).orElseThrow();
        List<HistorySmsSendNotifiResponse> responseList = new ArrayList<>();
        HistorySmsSendNotifiResponse model = new HistorySmsSendNotifiResponse();
        model.setSendContent(smsSend.getSendContent());
        model.setId(smsSend.getId());
        model.setName(smsSend.getTitleContent() != null ? smsSend.getTitleContent() : "Không có tiêu đề");
        responseList.add(model);
        return responseList;
    }

    @Override
    public List<ExcelResponse> exportExcelSms(List<Long> idList) {
        List<ExcelResponse> responseList = new ArrayList<>();
        ExcelResponse response = new ExcelResponse();
        List<ExcelData> bodyList = new ArrayList<>();
        List<String> headerStringList = Collections.singletonList("LỊCH SỬ GỬI SMS");
        List<ExcelData> headerList = ExportExcelUtils.setHeaderExcel(headerStringList);
        response.setSheetName("SMS");
        Long idSchool = SchoolUtils.getIdSchool();
//        ListHistorySmsSendResponse response = new ListHistorySmsSendResponse();
        List<SmsSend> smsSendList = smsSendRepository.findByIdInAndDelActiveTrueOrderByIdDesc(idList);
//        List<historySmsSendNewResponse> responseList = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        smsSendList.forEach(x -> {
            i.getAndIncrement();
            historySmsSendNewResponse model = new historySmsSendNewResponse();
            MaUser maUser = maUserRepository.findById(x.getIdCreated()).orElseThrow();
            String userCreate = maUser == null ? "" : maUser.getFullName();

            List<HistorySmsSendNotifiResponse> detailList = this.findDetailHistoryAll(PrincipalUtils.getUserPrincipal(), x.getId());
            List<String> receiveList = detailList.stream().map(HistorySmsSendNotifiResponse::getName).collect(Collectors.toList());
            List<String> phoneList = detailList.stream().map(HistorySmsSendNotifiResponse::getPhone).collect(Collectors.toList());
            List<String> typeList = detailList.stream().map(HistorySmsSendNotifiResponse::getType).collect(Collectors.toList());
            List<String> kidsList = detailList.stream().map(HistorySmsSendNotifiResponse::getClassName).collect(Collectors.toList());
            List<Boolean> statusList = detailList.stream().map(HistorySmsSendNotifiResponse::isFail).collect(Collectors.toList());
            List<String> statusStringList = new ArrayList<>();
            statusList.forEach(a -> {
                statusStringList.add(a ? "Thành công" : "Thất bại");
            });
            List<String> bodyStringList = Arrays.asList(String.valueOf(i), userCreate, ConvertData.convertDatettotimeDDMMYY(x.getCreatedDate()),
                    String.join("\n", receiveList), String.join("\n", phoneList), String.join("\n", typeList), String.join("\n", kidsList),
                    String.join("\n", statusStringList), x.getSendContent());
            ExcelData modelData = ExportExcelUtils.setBodyExcel(bodyStringList);
            bodyList.add(modelData);
            response.setBodyList(bodyList);

        });
        response.setHeaderList(headerList);
        response.setBodyList(bodyList);
        responseList.add(response);
        return responseList;
    }
}




