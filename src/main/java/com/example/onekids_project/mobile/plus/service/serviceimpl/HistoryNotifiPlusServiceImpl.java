package com.example.onekids_project.mobile.plus.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.*;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.*;
import com.example.onekids_project.mobile.plus.response.historyNotifiResponse.*;
import com.example.onekids_project.mobile.plus.service.servicecustom.HistoryNotifPlusService;
import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.AvatarUtils;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.StringDataUtils;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryNotifiPlusServiceImpl implements HistoryNotifPlusService {

    @Autowired
    ListMapper listMapper;
    @Autowired
    private AppSendRepository appSendRepository;
    @Autowired
    private SmsSendRepository smsSendRepository;
    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private ReceiversRepository receiversRepository;
    @Autowired
    private SmsSendCustomRepository smsSendCustomRepository;
    @Autowired
    private KidsRepository kidsRepository;
    @Autowired
    private MaClassRepository maClassRepository;
    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;
    @Autowired
    private SmsReiceiversRepository smsReiceiversRepository;
    @Autowired
    private SmsReceiversCustomRepository smsReceiversCustomRepository;
    @Autowired
    private SmsReiceiverRepository smsReiceiverRepository;
    @Autowired
    private smsReceiverCusstomRepository aRepository;

    @Override
    public ListHistoryNotifiPlusResponse searchHistoryNotifi(UserPrincipal principal, SearchHistoryNotifiPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<AppSend> appSendList = appSendRepository.searchHistoryNotifi(idSchool, request);
        ListHistoryNotifiPlusResponse listHistoryNotifiPlusResponse = new ListHistoryNotifiPlusResponse();
        List<HistoryNotifiPlusResponse> responseList = new ArrayList<>();
        appSendList.forEach(x -> {
            if (x.isSendDel() == AppConstant.APP_FALSE && x.getReceiversList().size() > 0) {
                HistoryNotifiPlusResponse model = new HistoryNotifiPlusResponse();
                model.setId(x.getId());
                if (x.getReceiversList().size() > 1) {
                    model.setFullName(AppConstant.SEND_MULTI + "(" + x.getReceiversList().size() + ")");
                    model.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                } else {
                    if (x.getReceiversList().get(0).getIdKids() != null) {
                        Kids kids = kidsRepository.findById(x.getReceiversList().get(0).getIdKids()).orElseThrow();
                        model.setFullName(kids.getFullName());
                        model.setAvatar(ConvertData.getAvatarKid(kids));
                    } else {
                        MaUser maUser = maUserRepository.findById(x.getReceiversList().get(0).getIdUserReceiver()).orElseThrow();
                        model.setFullName(maUser.getFullName());
                        model.setAvatar(ConvertData.getAvatarUserSchool(maUser));
                    }
                }
                int count = (int) x.getReceiversList().stream().filter(a -> a.isApproved() == AppConstant.APP_FALSE).count();
                model.setApprove(count > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                model.setContent(StringDataUtils.getSubStringLarge(x.getSendTitle()));
                model.setType(AppConstant.APP_SEND);
                model.setNumberFile((int) x.getUrlFileAppSendList().stream().filter(a -> StringUtils.isNotBlank(a.getAttachFile())).count());
                model.setNumberPicture((int) x.getUrlFileAppSendList().stream().filter(a -> StringUtils.isNotBlank(a.getAttachPicture())).count());
                model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
                responseList.add(model);
            }
        });
        boolean lastPage = responseList.size() < MobileConstant.MAX_PAGE_ITEM;
        listHistoryNotifiPlusResponse.setDataList(responseList);
        listHistoryNotifiPlusResponse.setLastPage(lastPage);
        return listHistoryNotifiPlusResponse;
    }

    @Override
    public boolean deleteNotifi(Long id) {
        AppSend appSend = appSendRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE).orElseThrow();
        List<Receivers> receiversList = appSend.getReceiversList();
        receiversList.forEach(x -> receiversRepository.deleteById(x.getId()));
        appSendRepository.deleteById(id);
        return true;
    }

    @Override
    public ListSmsPlusResponse searchSmsPlus(UserPrincipal principal, SearchSmsPlusRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        List<SmsSend> smsSendList = smsSendRepository.searchSmsSendPlus(idSchool, request);
        List<SmsSendCustom> smsSendCustomList = smsSendCustomRepository.searchSmsSendCustom(idSchool, request);
        ListSmsPlusResponse listSmsPlusResponse = new ListSmsPlusResponse();
        List<SmsPlusResponse> responseList = new ArrayList<>();
        if (request.getSmsType().equals(AppConstant.SMS)) {
            smsSendList.forEach(x -> {
                if (x.getSmsReceiversList().size() > 0) {
                    SmsPlusResponse model = new SmsPlusResponse();
                    model.setId(x.getId());
                    model.setContent(StringDataUtils.getSubStringLarge(x.getSendContent()));
                    if (x.getSmsReceiversList().size() > 1) {
                        model.setFullName(AppConstant.SEND_MULTI + "(" + x.getSmsReceiversList().size() + ")");
                        model.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                    } else {
                        if (x.getSmsReceiversList().get(0).getIdKid() != null) {
                            Kids kids = kidsRepository.findById(x.getSmsReceiversList().get(0).getIdKid()).orElseThrow();
                            model.setAvatar(ConvertData.getAvatarKid(kids));
                            model.setFullName(kids.getFullName());
                        } else {
                            if (x.getSmsReceiversList().get(0).getIdUserReceiver() != null) {
                                MaUser maUser = maUserRepository.findById(x.getSmsReceiversList().get(0).getIdUserReceiver()).orElseThrow();
                                model.setFullName(maUser.getFullName());
                                model.setAvatar(maUser.getAppType().equals(AppTypeConstant.SCHOOL) && maUser.getAppType().equals(AppTypeConstant.TEACHER) ? AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()) : AvatarDefaultConstant.AVATAR_TEACHER);
                            }
                        }
                    }
                    int coutFail = 0;
                    int coutFail2 = 0;
                    List<SmsReceivers> smsReceiversList = smsReiceiverRepository.findAllByIdSmsSend(x.getId());
                    coutFail = (int) smsReceiversList.stream().filter(b -> b.getSmsCode() != null).filter(b -> b.getSmsCode().getId() != 1).count();
                    coutFail2 = (int) smsReceiversList.stream().filter(c -> c.getSmsCode() == null).count();
                    model.setFail(coutFail + coutFail2 > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                    model.setType(AppConstant.SMS);
                    model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
                    responseList.add(model);
                }
            });
        }
        if (request.getSmsType().equals(AppConstant.CUSTOM)) {
            smsSendCustomList.forEach(x -> {
                if (x.getSmsReceiversCustomList().size() > 0) {
                    SmsPlusResponse model1 = new SmsPlusResponse();
                    model1.setId(x.getId());
                    model1.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
                    if (x.getSmsReceiversCustomList().size() > 1) {
                        model1.setContent(x.getSendType().equalsIgnoreCase(AppConstant.TYPE_ACCOUNT) ? AppConstant.SMS_ACCOUNT : AppConstant.SMS_VERY);
                        model1.setFullName(AppConstant.SEND_MULTI + "(" + x.getSmsReceiversCustomList().size() + ")");
                        model1.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                    } else {
                        model1.setContent(x.getSmsReceiversCustomList().get(0).getSendContent());
                        if (x.getSmsReceiversCustomList().get(0).getIdKid() != null) {
                            Kids kids = kidsRepository.findById(x.getSmsReceiversCustomList().get(0).getIdKid()).orElseThrow();
                            model1.setAvatar(ConvertData.getAvatarKid(kids));
                            model1.setFullName(kids.getFullName());
                        } else {
                            model1.setFullName(x.getSmsReceiversCustomList().get(0).getNameUserReceiver());
                            model1.setAvatar(AvatarDefaultConstant.AVATAR_TEACHER);
                        }
                    }
                    int coutFail = 0;
                    int coutFail2 = 0;
                    List<SmsReceiversCustom> smsReceiversCustomList = smsReceiversCustomRepository.findSmsReceiverCustom(x.getId());
                    coutFail = (int) smsReceiversCustomList.stream().filter(b -> b.getSmsCode() != null).filter(b -> b.getSmsCode().getId() != 1).count();
                    coutFail2 = (int) smsReceiversCustomList.stream().filter(c -> c.getSmsCode() == null).count();
                    model1.setFail(coutFail + coutFail2 > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                    model1.setType(AppConstant.CUSTOM);
                    responseList.add(model1);
                }
            });
        }
        if (request.getSmsType().equals(AppConstant.SMS_ALL)) {
            smsSendList.forEach(x -> {
                if (x.getSmsReceiversList().size() > 0) {
                    SmsPlusResponse model = new SmsPlusResponse();
                    model.setId(x.getId());
                    model.setContent(StringDataUtils.getSubStringLarge(x.getSendContent()));
                    if (x.getSmsReceiversList().size() > 1) {
                        model.setFullName(AppConstant.SEND_MULTI + "(" + x.getSmsReceiversList().size() + ")");
                        model.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                    } else if (x.getSmsReceiversList().size() == 1) {
                        if (x.getSmsReceiversList().get(0).getIdKid() != null) {
                            Kids kids = kidsRepository.findById(x.getSmsReceiversList().get(0).getIdKid()).orElseThrow();
                            model.setAvatar(ConvertData.getAvatarKid(kids));
                            model.setFullName(kids.getFullName());
                        } else {
                            if (x.getSmsReceiversList().get(0).getIdUserReceiver() != null) {
                                MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(x.getSmsReceiversList().get(0).getIdUserReceiver()).orElseThrow();
                                model.setFullName(maUser.getFullName());
                                model.setAvatar(maUser.getAppType().equals(AppTypeConstant.SCHOOL) && maUser.getAppType().equals(AppTypeConstant.TEACHER) ? AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()) : AvatarDefaultConstant.AVATAR_TEACHER);
                            }
                        }
                    }
                    int coutFail = 0;
                    int coutFail2 = 0;
                    List<SmsReceivers> smsReceiversList = smsReiceiverRepository.findAllByIdSmsSend(x.getId());
                    coutFail = (int) smsReceiversList.stream().filter(b -> b.getSmsCode() != null).filter(b -> b.getSmsCode().getId() != 1).count();
                    coutFail2 = (int) smsReceiversList.stream().filter(c -> c.getSmsCode() == null).count();
                    model.setFail(coutFail + coutFail2 > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                    model.setType(AppConstant.SMS);
                    model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
                    responseList.add(model);
                }
            });
            smsSendCustomList.forEach(x -> {
                if (x.getSmsReceiversCustomList().size() > 0) {
                    SmsPlusResponse model1 = new SmsPlusResponse();
                    model1.setId(x.getId());
                    model1.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
                    if (x.getSmsReceiversCustomList().size() > 1) {
                        model1.setContent(x.getSendType().equalsIgnoreCase(AppConstant.TYPE_ACCOUNT) ? AppConstant.SMS_ACCOUNT : AppConstant.SMS_VERY);
                        model1.setFullName(AppConstant.SEND_MULTI + "(" + x.getSmsReceiversCustomList().size() + ")");
                        model1.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                    } else {
                        model1.setContent(x.getSmsReceiversCustomList().get(0).getSendContent());
                        if (x.getSmsReceiversCustomList().get(0).getIdKid() != null) {
                            Kids kids = kidsRepository.findById(x.getSmsReceiversCustomList().get(0).getIdKid()).orElseThrow();
                            model1.setAvatar(ConvertData.getAvatarKid(kids));
                            model1.setFullName(kids.getFullName());
                        } else {
                            model1.setFullName(x.getSmsReceiversCustomList().get(0).getNameUserReceiver());
                            model1.setAvatar(AvatarDefaultConstant.AVATAR_TEACHER);
                        }
                    }
                    int coutFail;
                    int coutFail2;
                    List<SmsReceiversCustom> smsReceiversCustomList = smsReceiversCustomRepository.findSmsReceiverCustom(x.getId());
                    coutFail = (int) smsReceiversCustomList.stream().filter(b -> b.getSmsCode() != null).filter(b -> b.getSmsCode().getId() != 1).count();
                    coutFail2 = (int) smsReceiversCustomList.stream().filter(c -> c.getSmsCode() == null).count();
                    model1.setFail(coutFail + coutFail2 > 0 ? AppConstant.APP_TRUE : AppConstant.APP_FALSE);
                    model1.setType(AppConstant.CUSTOM);
                    responseList.add(model1);
                }
            });
        }
        boolean lastPage = responseList.size() < MobileConstant.MAX_PAGE_ITEM;
        listSmsPlusResponse.setResponseList(responseList);
        listSmsPlusResponse.setLastPage(lastPage);
        return listSmsPlusResponse;
    }

    @Override
    public DetailHistoryNotifiAppResponse findDeTailNotifi(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        AppSend appSend = appSendRepository.findById(id).orElseThrow();
        Long idSchool = principal.getIdSchoolLogin();
        MaUser maUser1 = maUserRepository.findByIdAndDelActiveTrue(appSend.getIdCreated()).orElseThrow();
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(appSend.getId());
        DetailHistoryNotifiAppResponse model = new DetailHistoryNotifiAppResponse();
        model.setId(appSend.getId());
        model.setCoutNumberReceivers(receiversList.size());
        if (receiversList.size() > 0) {
            if (receiversList.size() > 1) {
                model.setFullName(AppConstant.SEND_MULTI + "(" + receiversList.size() + ")");
                model.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                model.setClassName("");
                model.setStatusSend("");
            } else {
                if (receiversList.get(0).getIdKids() != null) {
                    Kids kids = kidsRepository.findById(receiversList.get(0).getIdKids()).orElseThrow();
                    MaClass maClass = maClassRepository.findByIdAndDelActiveTrue(kids.getMaClass().getId()).orElseThrow();
                    model.setFullName(kids.getFullName());
                    model.setAvatar(ConvertData.getAvatarKid(kids));
                    model.setClassName(maClass.getClassName());
                } else {
                    MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(receiversList.get(0).getIdUserReceiver()).orElseThrow();
                    model.setFullName(maUser.getFullName());
                    model.setAvatar(maUser.getAppType().equals(AppTypeConstant.SCHOOL) && maUser.getAppType().equals(AppTypeConstant.TEACHER) ? AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()) : AvatarDefaultConstant.AVATAR_TEACHER);
                    model.setClassName("");
                }
                receiversList.forEach(z -> {
                    if (z.isApproved() == AppConstant.APP_TRUE && z.isUserUnread() == AppConstant.APP_TRUE) {
                        model.setStatusSend(AppConstant.READ);
                    } else if (z.isSendDel() == AppConstant.APP_TRUE) {
                        model.setStatusSend(AppConstant.REVOKE);
                    } else if (z.isApproved() == AppConstant.APP_FALSE) {
                        model.setStatusSend(AppConstant.ISAPPROVE);
                    } else {
                        model.setStatusSend(AppConstant.SENT);
                    }
                });
            }
        }

        List<UrlFileAppSend> urlFileAppSendList = urlFileAppSendRepository.findUrlFileAppSendByThanh(appSend.getId());
        List<ListFileNotifi> fileNotifis = new ArrayList<>();
        List<UrlFileAppSend> urlFileAppSendList1 = urlFileAppSendList.stream().filter(x -> x.getAttachFile() != null).collect(Collectors.toList());
        urlFileAppSendList1.forEach(x -> {
            ListFileNotifi model2 = new ListFileNotifi();
            model2.setName(x.getName() != null ? x.getName() : "");
            model2.setId(x.getId());
            model2.setUrl(x.getAttachFile() != null ? x.getAttachFile() : "");
            fileNotifis.add(model2);
        });
        model.setCreatedName(maUser1.getFullName());
        model.setPhone(maUser1.getPhone());
        model.setCreatedDate(ConvertData.convertDatettotimeDDMMYY(appSend.getCreatedDate()));
        model.setTitle(appSend.getSendTitle());
        model.setContent(appSend.getSendContent());
        model.setPictureList(appSend.getUrlFileAppSendList().stream().filter(x -> x.getAttachPicture() != null).map(UrlFileAppSend::getAttachPicture).collect(Collectors.toList()));
        model.setFileList(fileNotifis);
        return model;
    }

    @Override
    public ListViewDetailUserNotifiPlusResponse viewDetailNotifiUserPlus(UserPrincipal principal, Long id, DetailUserRequest request) {
        CommonValidate.checkDataPlus(principal);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendIdc(id, request);
        Long idSchool = principal.getIdSchoolLogin();
        ListViewDetailUserNotifiPlusResponse listViewDetailUserNotifiPlusResponse = new ListViewDetailUserNotifiPlusResponse();
        List<DetailUserHistoryNotifiAppResponse> responseList = new ArrayList<>();
        AppSend appSend = appSendRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE).orElseThrow();
        listViewDetailUserNotifiPlusResponse.setCheck(appSend.getAppType().equals(AppTypeConstant.SCHOOL) ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
        receiversList.forEach(x -> {
            DetailUserHistoryNotifiAppResponse model = new DetailUserHistoryNotifiAppResponse();
            if (x.getIdKids() != null) {
                Kids kids = kidsRepository.findById(x.getIdKids()).orElseThrow();
                model.setId(x.getIdUserReceiver());
                model.setFullName(kids.getFullName());
                model.setAvatar(ConvertData.getAvatarKid(kids));
                if (principal.getSysConfig().isAppSendParent() == AppConstant.APP_TRUE) {
                    if (x.isApproved() == AppConstant.APP_FALSE) {
                        model.setStatus(AppConstant.ISAPPROVE);
                    } else if (x.isSendDel() == AppConstant.APP_TRUE) {
                        model.setStatus(AppConstant.REVOKE);
                    } else if (x.isApproved() == AppConstant.APP_TRUE && x.isUserUnread() == AppConstant.APP_FALSE) {
                        model.setStatus(AppConstant.SENT);
                    } else if (x.isUserUnread() == AppConstant.APP_TRUE && x.isApproved() == AppConstant.APP_TRUE) {
                        model.setStatus(AppConstant.READ);
                    }
                } else {
                    if (x.isApproved() == AppConstant.APP_FALSE) {
                        model.setStatus(AppConstant.ISAPPROVE);
                    } else if (x.isSendDel() == AppConstant.APP_TRUE) {
                        model.setStatus(AppConstant.REVOKE);
                    } else if (x.isApproved() == AppConstant.APP_TRUE && x.isUserUnread() == AppConstant.APP_FALSE) {
                        model.setStatus(AppConstant.SENT);
                    } else if (x.isUserUnread() == AppConstant.APP_TRUE && x.isApproved() == AppConstant.APP_TRUE && x.isSendDel() == AppConstant.APP_FALSE) {
                        model.setStatus(AppConstant.SENT);
                    }
                }
                responseList.add(model);
            } else {
                MaUser maUser = maUserRepository.findById(x.getIdUserReceiver()).orElseThrow();
                model.setId(x.getIdUserReceiver());
                model.setFullName(maUser.getFullName());
                model.setAvatar(maUser.getAppType().equals(AppTypeConstant.SCHOOL) && maUser.getAppType().equals(AppTypeConstant.TEACHER) ? AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()) : AvatarDefaultConstant.AVATAR_TEACHER);
                if (x.isApproved() == AppConstant.APP_FALSE) {
                    model.setStatus(AppConstant.ISAPPROVE);
                } else if (x.isSendDel() == AppConstant.APP_TRUE) {
                    model.setStatus(AppConstant.REVOKE);
                } else if (x.isApproved() == AppConstant.APP_TRUE && x.isUserUnread() == AppConstant.APP_FALSE) {
                    model.setStatus(AppConstant.SENT);
                } else if (x.isUserUnread() == AppConstant.APP_TRUE && x.isApproved() == AppConstant.APP_TRUE) {
                    model.setStatus(AppConstant.READ);
                }
                responseList.add(model);
            }
        });
        boolean lastPage = responseList.size() < MobileConstant.MAX_PAGE_ITEM;
        listViewDetailUserNotifiPlusResponse.setResponseList(responseList);
        listViewDetailUserNotifiPlusResponse.setLastPage(lastPage);
        return listViewDetailUserNotifiPlusResponse;
    }


    @Transactional
    @Override
    public boolean approveMultiNotifi(UserPrincipal principal, ApproveNotifiRequest request) {
        Long idAppsend = request.getId();
        List<Receivers> receiversApprovedList = receiversRepository.findByAppSendIdAndIdUserReceiverInAndDelActiveTrue(idAppsend, request.getIdUserReceiverList());
        receiversApprovedList.forEach(x -> x.setApproved(AppConstant.APP_TRUE));
        receiversRepository.saveAll(receiversApprovedList);

        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(idAppsend);
        List<Receivers> receiversList1 = receiversList.stream().filter(Receivers::isApproved).collect(Collectors.toList());
        if (receiversList.size() == receiversList1.size()) {
            AppSend appSend = appSendRepository.findByIdAndDelActive(idAppsend, true).orElseThrow();
            appSend.setApproved(AppConstant.APP_TRUE);
            appSendRepository.save(appSend);
        }
        return true;
    }

    @Override
    public boolean revokeMultiNotifi(UserPrincipal principal, DeleteNotifiRequest request) {
        Long idAppsend = request.getId();
        Receivers receivers = receiversRepository.findByAppSendIdAndIdUserReceiverAndDelActiveTrue(idAppsend, request.getIdUserReceiver()).orElseThrow();
        receivers.setSendDel(AppConstant.APP_TRUE);
        receiversRepository.save(receivers);
        return true;
    }

    @Override
    public DetailSmsResponse findDeTailSms(UserPrincipal principal, Long id, DetailSmsRequest request) {
        CommonValidate.checkDataPlus(principal);
        Long idSchool = principal.getIdSchoolLogin();
        DetailSmsResponse model = new DetailSmsResponse();
        model.setId(id);
        if (request.getKey().equals(AppConstant.SMS)) {
            SmsSend smsSend = smsSendRepository.findById(id).orElseThrow();
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(smsSend.getIdCreated()).orElseThrow();
            model.setPhone(maUser.getPhone());
            model.setContent(smsSend.getSendContent());
            model.setKey(AppConstant.SMS);
            this.setReply(idSchool, smsSend, model, request);
        }
        if (request.getKey().equals(AppConstant.CUSTOM)) {
            SmsSendCustom smsSendCustom = smsSendCustomRepository.findById(id).orElseThrow();
            MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(smsSendCustom.getIdCreated()).orElseThrow();
            model.setPhone(maUser.getPhone());
            model.setContent(smsSendCustom.getSmsReceiversCustomList().get(0).getSendContent());
            model.setKey(AppConstant.CUSTOM);
            this.setReplySmscustom(idSchool, smsSendCustom, model, request);
        }
        return model;
    }

    @Override
    public boolean deletenotifi(UserPrincipal principal, DeleteNotifiRequest request) {
        Receivers receivers = receiversRepository.findByAppSendIdAndIdUserReceiverAndDelActiveTrue(request.getId(), request.getIdUserReceiver()).orElseThrow();
        receiversRepository.deleteById(receivers.getId());
        return true;
    }

    @Override
    public DetailSmsCustomResponse findDeTailSmsCustom(UserPrincipal principal, Long id) {
        CommonValidate.checkDataPlus(principal);
        DetailSmsCustomResponse model = new DetailSmsCustomResponse();
        SmsReceiversCustom smsReceiversCustom = smsReceiversCustomRepository.findById(id).orElseThrow();
        model.setContent(smsReceiversCustom.getSendContent());
        return model;
    }


    private void setReply(Long idSchool, SmsSend smsSend, DetailSmsResponse model, DetailSmsRequest request) {
        List<DetailsmsPlusObject> detailsmsPlusObjectList = new ArrayList<>();
        Long idSend = smsSend.getId();
        List<SmsReceivers> smsReceiversList = smsReiceiversRepository.findByIdSmsSendNew(idSchool, idSend, request);
        smsReceiversList.forEach(x -> {
            DetailsmsPlusObject detailsmsPlusObject = new DetailsmsPlusObject();
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                detailsmsPlusObject.setFullName(kids.getFullName());
                detailsmsPlusObject.setAvatar(ConvertData.getAvatarKid(kids));
            } else {
                MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(x.getIdUserReceiver()).orElseThrow();
                detailsmsPlusObject.setFullName(maUser.getFullName());
                detailsmsPlusObject.setAvatar(maUser.getAppType().equals(AppTypeConstant.SCHOOL) && maUser.getAppType().equals(AppTypeConstant.TEACHER) ? AvatarUtils.getAvatarEmployeeWithSchool(idSchool, maUser.getId()) : AvatarDefaultConstant.AVATAR_TEACHER);
            }
            detailsmsPlusObject.setId(x.getId());
            detailsmsPlusObject.setStatus(x.getSmsCode() == null || x.getSmsCode().getId() != 1 ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            detailsmsPlusObjectList.add(detailsmsPlusObject);
        });
        model.setDataList(detailsmsPlusObjectList);
        boolean lastPage = detailsmsPlusObjectList.size() < MobileConstant.MAX_PAGE_ITEM;
        model.setLastPage(lastPage);
    }

    private void setReplySmscustom(Long idSchool, SmsSendCustom smsSendCustom, DetailSmsResponse model, DetailSmsRequest request) {
        List<DetailsmsPlusObject> detailsmsPlusObjectList = new ArrayList<>();
        Long idSmsSendCustom = smsSendCustom.getId();
        List<SmsReceiversCustom> smsReceiversCustomList = aRepository.searchSmsCustomnew(idSchool, idSmsSendCustom, request);
        smsReceiversCustomList.forEach(x -> {
            DetailsmsPlusObject detailsmsPlusObject = new DetailsmsPlusObject();
            if (x.getIdKid() != null) {
                Kids kids = kidsRepository.findById(x.getIdKid()).orElseThrow();
                detailsmsPlusObject.setFullName(kids.getFullName());
                detailsmsPlusObject.setAvatar(ConvertData.getAvatarKid(kids));
            } else {
                detailsmsPlusObject.setFullName(x.getNameUserReceiver());
                detailsmsPlusObject.setAvatar(AvatarDefaultConstant.AVATAR_TEACHER);
            }
            detailsmsPlusObject.setId(x.getId());
            detailsmsPlusObject.setStatus(x.getSmsCode() == null || x.getSmsCode().getId() != 1 ? AppConstant.APP_FALSE : AppConstant.APP_TRUE);
            detailsmsPlusObjectList.add(detailsmsPlusObject);
        });
        model.setDataList(detailsmsPlusObjectList);
        boolean lastPage = detailsmsPlusObjectList.size() < MobileConstant.MAX_PAGE_ITEM;
        model.setLastPage(lastPage);
    }
}
