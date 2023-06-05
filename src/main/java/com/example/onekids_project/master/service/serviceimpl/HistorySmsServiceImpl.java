package com.example.onekids_project.master.service.serviceimpl;

import com.example.onekids_project.master.request.SearchHistorySmsRequest;
import com.example.onekids_project.master.response.HistorySmsResponse;
import com.example.onekids_project.master.response.HistorySmsResponseByStatus;
import com.example.onekids_project.master.service.HistorySmsService;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SmsSendCustomRepository;
import com.example.onekids_project.repository.SmsSendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistorySmsServiceImpl implements HistorySmsService {

    @Autowired
    private SmsSendRepository smsSendRepository;

    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Override
    public List<HistorySmsResponse> findAllHistorySms(SearchHistorySmsRequest searchHistorySmsRequest) {

        List<HistorySmsResponse> historySmsResponseList = new ArrayList<>();
//        if (searchHistorySmsRequest.getTypeSend().equalsIgnoreCase(AppConstant.AUTO_SEND) || StringUtils.isBlank(searchHistorySmsRequest.getTypeSend())) {
//            List<SmsSendCustom> smsSendCustomList = smsSendCustomRepository.findAllSmsSendCustom(searchHistorySmsRequest);
//            if (!CollectionUtils.isEmpty(smsSendCustomList)) {
//                smsSendCustomList.forEach(smsSendCustom -> {
//                    HistorySmsResponse historySmsResponse = new HistorySmsResponse();
//                    historySmsResponse.setCreatedDate(smsSendCustom.getCreatedDate().toString());
//                    historySmsResponse.setId(smsSendCustom.getId());
//                    MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(smsSendCustom.getIdCreated()).get();
//                    historySmsResponse.setCreatedBy(maUser.getFullName());
//                    historySmsResponse.setReceiversNumber(smsSendCustom.getReceivedCount());
//
//                    if (CollectionUtils.isEmpty(smsSendCustom.getSmsReceiversCustomList())) {
//                        int countSucess = 0;
//                        int countFailure = 0;
//                        int totalSms = 0;
//                        for (SmsReceiversCustom smsReceiversCustom : smsSendCustom.getSmsReceiversCustomList()) {
//                            if (smsReceiversCustom.getCode().equalsIgnoreCase(AppConstant.CODE_SUCCESS)) {
//                                countSucess++;
//                            } else {
//                                countFailure++;
//                            }
//
//                            if (smsReceiversCustom.getNumberSms() != 0) {
//                                totalSms = totalSms + smsReceiversCustom.getNumberSms();
//                            }
//                        }
//                        historySmsResponse.setTotalSms(totalSms);
//                        historySmsResponse.setSuccessNumber(countSucess);
//                        historySmsResponse.setFailureNumber(countFailure);
//                    }
//                    historySmsResponse.setSendType(smsSendCustom.getSendType());
//                    historySmsResponse.setTypeSend(AppConstant.AUTO_SEND);
//                    historySmsResponseList.add(historySmsResponse);
//                });
//
//            }
//        } else if (searchHistorySmsRequest.getTypeSend().equalsIgnoreCase(AppConstant.USER_SEND) || StringUtils.isBlank(searchHistorySmsRequest.getTypeSend())) {
//            List<SmsSend> smsSendList = smsSendRepository.findAllSmsSend(searchHistorySmsRequest);
//            smsSendList.forEach(smsSend -> {
//                HistorySmsResponse historySmsResponse = new HistorySmsResponse();
//                historySmsResponse.setCreatedDate(smsSend.getCreatedDate().toString());
//                historySmsResponse.setId(smsSend.getId());
//                MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(smsSend.getIdCreated()).get();
//                historySmsResponse.setCreatedBy(maUser.getFullName());
//                historySmsResponse.setReceiversNumber(smsSend.getReceivedCount());
//
//                if (CollectionUtils.isEmpty(smsSend.getSmsReceiversList())) {
//                    int countSucess = 0;
//                    int countFailure = 0;
//                    for (SmsReceivers smsReceivers : smsSend.getSmsReceiversList()) {
//                        if (smsReceivers.getCode().equalsIgnoreCase(AppConstant.CODE_SUCCESS)) {
//                            countSucess++;
//                        } else {
//                            countFailure++;
//                        }
//
//                    }
//                    historySmsResponse.setSuccessNumber(countSucess);
//                    historySmsResponse.setFailureNumber(countFailure);
//                    historySmsResponse.setTotalSms(smsSend.getNumberSms() * smsSend.getSmsReceiversList().size());
//                }
//                historySmsResponse.setSendType(smsSend.getSendType());
//                historySmsResponse.setTypeSend(AppConstant.USER_SEND);
//                historySmsResponseList.add(historySmsResponse);
//            });
//        }
        return historySmsResponseList;
    }

    @Override
    public List<HistorySmsResponseByStatus> findByHistorySmsById(Long id, String typeSend) {
        List<HistorySmsResponseByStatus> historySmsResponseByStatusList=new ArrayList<>();
//        if(typeSend.equalsIgnoreCase(AppConstant.AUTO_SEND)){
//            Optional<SmsSendCustom> smsSendCustomOptional=smsSendCustomRepository.findById(id);
//            if(smsSendCustomOptional.isEmpty()){
//                return null;
//            }
//            SmsSendCustom smsSendCustom=smsSendCustomOptional.get();
//            if(!CollectionUtils.isEmpty(smsSendCustom.getSmsReceiversCustomList())){
//                smsSendCustom.getSmsReceiversCustomList().forEach(smsReceiversCustom -> {
//                    HistorySmsResponseByStatus historySmsResponseByStatus=new HistorySmsResponseByStatus();
//                    historySmsResponseByStatus.setFullName(smsReceiversCustom.getNameUserReceiver());
//                    historySmsResponseByStatus.setPhone(smsReceiversCustom.getPhoneUserReceiver());
//                    historySmsResponseByStatus.setAppType(smsReceiversCustom.getAppType());
//                    historySmsResponseByStatus.setContent(smsReceiversCustom.getSendContent());
//                    if(smsReceiversCustom.getCode().equalsIgnoreCase(AppConstant.CODE_SUCCESS)){
//                        historySmsResponseByStatus.setStatus("Thành công");
//                    }else{
//                        historySmsResponseByStatus.setStatus("Thất bại");
//                    }
//                    historySmsResponseByStatusList.add(historySmsResponseByStatus);
//                });
//            }
//        }else {
//            Optional<SmsSend> smsSendOptional=smsSendRepository.findById(id);
//            if(smsSendOptional.isEmpty()){
//                return null;
//            }
//            SmsSend smsSend=smsSendOptional.get();
//            if(!CollectionUtils.isEmpty(smsSend.getSmsReceiversList())){
//                smsSend.getSmsReceiversList().forEach(smsReceivers -> {
//                    HistorySmsResponseByStatus historySmsResponseByStatus=new HistorySmsResponseByStatus();
//                    if(smsReceivers.getIdUserReceiver()!=null){
//                        MaUser maUser=maUserRepository.findByIdAndDelActiveTrue(smsReceivers.getIdUserReceiver()).get();
//                        historySmsResponseByStatus.setFullName(maUser.getFullName());
//                    }
//                    historySmsResponseByStatus.setPhone(smsReceivers.getPhone());
//                    historySmsResponseByStatus.setAppType(smsReceivers.getAppType());
//                    historySmsResponseByStatus.setContent(smsReceivers.getSmsSend().getSendContent());
//                    if(smsReceivers.getCode().equalsIgnoreCase(AppConstant.CODE_SUCCESS)){
//                        historySmsResponseByStatus.setStatus("Thành công");
//                    }else{
//                        historySmsResponseByStatus.setStatus("Thất bại");
//                    }
//                    historySmsResponseByStatusList.add(historySmsResponseByStatus);
//                });
//            }
//        }
        return historySmsResponseByStatusList;
    }

}
