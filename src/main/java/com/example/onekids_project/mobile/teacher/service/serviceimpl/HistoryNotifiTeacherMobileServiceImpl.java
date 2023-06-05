package com.example.onekids_project.mobile.teacher.service.serviceimpl;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.common.MessageConstant;
import com.example.onekids_project.common.MobileConstant;
import com.example.onekids_project.entity.classes.MaClass;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.entity.user.UrlFileAppSend;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.ListFileNotifi;
import com.example.onekids_project.mobile.teacher.request.historynotifi.SearchHistoryNotifiTeacherRequest;
import com.example.onekids_project.mobile.teacher.response.historynotifi.*;
import com.example.onekids_project.mobile.teacher.service.servicecustom.HistoryNotifiTeacherMobileService;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.validate.CommonValidate;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryNotifiTeacherMobileServiceImpl implements HistoryNotifiTeacherMobileService {

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private UrlFileAppSendRepository urlFileAppSendRepository;

    @Autowired
    private AppSendRepository appSendRepository;

    @Autowired
    private ReceiversRepository receiversRepository;

    @Autowired
    ListMapper listMapper;


    @Override
    public ListHistoryNotifiTeacherResponse searchHistoryNotifi(UserPrincipal principal, SearchHistoryNotifiTeacherRequest searchHistoryNotifiTeacherRequest) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        List<AppSend> appSendList = appSendRepository.findAppSendListByIdClass(idClass, searchHistoryNotifiTeacherRequest, principal);
        List<Receivers> receiversList = receiversRepository.findByIdClass(idClass);
        ListHistoryNotifiTeacherResponse listHistoryNotifiTeacherResponse = new ListHistoryNotifiTeacherResponse();
        List<HistoryNotifiTeacherResponse> historyNotifiTeacherResponseList = new ArrayList<>();
        appSendList.forEach(x -> {
            if (x.isSendDel() == AppConstant.APP_FALSE) {
                HistoryNotifiTeacherResponse model = new HistoryNotifiTeacherResponse();
                model.setId(x.getId());
                if (x.getReceiversList().size() > 1) {
                    model.setFullName("Gửi nhiều người" + "(" + x.getReceiversList().size() + ")");
                    model.setAvatar(AvatarDefaultConstant.AVATAR_GROUP);
                } else if (x.getReceiversList().size() == 1) {
                    Optional<Kids> kids = kidsRepository.findById(x.getReceiversList().get(0).getIdKids());
                    String avatarkid = StringUtils.isNotBlank(kids.get().getAvatarKid()) ? kids.get().getAvatarKid() : AvatarDefaultConstant.AVATAR_KIDS;
                    model.setFullName(kids.get().getFullName());
                    model.setAvatar(avatarkid);
                } else {
                    return;
                }
                List<UrlFileAppSend> urlFileAppSendList = urlFileAppSendRepository.findUrlFileAppSendByThanh(x.getId());
                model.setCoutFile((int) urlFileAppSendList.stream().filter(a -> StringUtils.isNotBlank(a.getAttachFile())).count());
                model.setCoutPicture((int) urlFileAppSendList.stream().filter(a -> StringUtils.isNotBlank(a.getAttachPicture())).count());
                model.setSendContent(x.getSendContent().length() < 100 ? x.getSendContent() : x.getSendContent().substring(0, 100));
                model.setApprove(x.getReceiversList().stream().anyMatch(Receivers::isApproved));
                model.setCreatedDate(ConvertData.convertDatettoStringHhMMDD(x.getCreatedDate()));
                historyNotifiTeacherResponseList.add(model);
            }
        });
        boolean lastPage = appSendList.size() < MobileConstant.MAX_PAGE_ITEM;
        listHistoryNotifiTeacherResponse.setDataList(historyNotifiTeacherResponseList);
        listHistoryNotifiTeacherResponse.setLastPage(lastPage);
        return listHistoryNotifiTeacherResponse;
    }

    @Override
    public boolean historyTeacherReovke(UserPrincipal principal, Long id) {
        AppSend appSend = appSendRepository.findByIdAndDelActive(id, AppConstant.APP_TRUE).orElseThrow(() -> new NotFoundException("not found messageParent by id"));
        appSend.setSendDel(AppConstant.APP_TRUE);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(id);
        receiversList.forEach(x -> {
            x.setSendDel(AppConstant.APP_TRUE);
        });
        receiversList.forEach(x -> {
            if (x.getAppSend().isSendDel() == AppConstant.APP_TRUE) {
                return;
            }
        });
        appSendRepository.save(appSend);
        return true;
    }

    @Override
    public HistoryNotifiTeacherDetailResponse viewDetailNotifi(UserPrincipal principal, Long id) {
        AppSend appSend = appSendRepository.findByIdAndDelActive(id, true).orElseThrow(() -> new NotFoundException("not found album by id"));
        HistoryNotifiTeacherDetailResponse model = new HistoryNotifiTeacherDetailResponse();
        if (appSend.isSendDel() == AppConstant.APP_TRUE) {
            model.setSendContent(MessageConstant.REVOKE_NOTIFI);
        } else if (appSend.isSendDel() == AppConstant.APP_FALSE) {
            model.setSendContent(appSend.getSendContent());
        }
        model.setId(appSend.getId());
        Long idAppsend = appSend.getId();
        Long idClass = appSend.getReceiversList().get(0).getIdClass();
        Optional<MaClass> maClassOptional = maClassRepository.findById(idClass);
        model.setClassName(maClassOptional.get().getClassName());
        List<UrlFileAppSend> urlFileAppSendListt = urlFileAppSendRepository.findUrlFileAppSendByThanh(idAppsend);
        List<UrlFileAppSend> urlFileAppSendListhasagi =  urlFileAppSendListt.stream().filter(x ->x.getAttachPicture() != null).collect(Collectors.toList());
        model.setPictureList(appSend.getUrlFileAppSendList().stream().filter(x ->x.getAttachPicture()!=null).map(UrlFileAppSend::getAttachPicture).collect(Collectors.toList()));
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(idAppsend);
        if (receiversList.size() > 1) {
            int coutR = receiversList.size();
            model.setFullName("Gửi nhiều người" + "(" + coutR + ")");
            String avatarSchool = StringUtils.isNotBlank(principal.getSchool().getSchoolAvatar()) ? principal.getSchool().getSchoolAvatar() : AvatarDefaultConstant.AVATAR_SCHOOL;
            model.setAvatarkid(avatarSchool);
            model.setCoutNumberReceivers(coutR);
            model.setStatusSend("");
        } else if (receiversList.size() == 1) {
            Optional<Kids> kidsOptional = kidsRepository.findById(receiversList.get(0).getIdKids());
            model.setFullName(kidsOptional.get().getFullName());
            String avatarkid = StringUtils.isNotBlank(kidsOptional.get().getAvatarKid()) ? kidsOptional.get().getAvatarKid() : AvatarDefaultConstant.AVATAR_KIDS;
            model.setAvatarkid(avatarkid);
            model.setCoutNumberReceivers(1);
            if (principal.getSysConfig().isAppSendParent() == AppConstant.APP_TRUE) {
                receiversList.forEach(z -> {
                    if (z.isApproved() == AppConstant.APP_TRUE && z.isUserUnread() == AppConstant.APP_TRUE) {
                        model.setStatusSend("Read");
                    } else if (z.isSendDel() == AppConstant.APP_TRUE) {
                        model.setStatusSend("Revoke");
                    } else if (z.isApproved() == AppConstant.APP_FALSE) {
                        model.setStatusSend("IsApprove");
                    } else {
                        model.setStatusSend("Sent");
                    }
                });
            } else if (principal.getSysConfig().isAppSendParent() == AppConstant.APP_FALSE ) {
                receiversList.forEach(g -> {
                    if(g.isApproved() == AppConstant.APP_FALSE){
                        model.setStatusSend("IsApprove");
                    }
                    else {
                        model.setStatusSend("Sent");
                    }
                });
            }
        }
        List<UrlFileAppSend> urlFileAppSendList = urlFileAppSendRepository.findUrlFileAppSendByThanh(idAppsend);
        List<ListFileNotifi> fileNotifis = new ArrayList<>();
        List<UrlFileAppSend> urlFileAppSendList1 =  urlFileAppSendList.stream().filter(x ->x.getAttachFile() != null).collect(Collectors.toList());
        urlFileAppSendList1.forEach(x -> {
            ListFileNotifi model2 = new ListFileNotifi();
            model2.setName(x.getName());
            model2.setId(x.getId());
            if (x.getAttachFile() == null) {
                model2.setUrl("");
            } else {
                model2.setUrl(x.getAttachFile());
            }
            fileNotifis.add(model2);
        });
        model.setSendTitle(appSend.getSendTitle());
        model.setFileList(fileNotifis);
        model.setTimeSend(ConvertData.convertDatettotimeDDMMYY(appSend.getCreatedDate()));
        return model;
    }

    @Override
    public ListViewDetailUserNotifiTeacherResponse viewDetailNotifiUser(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Long idClass = principal.getIdClassLogin();
        Long idAppsend = id;
        List<AppSend> appSendList = appSendRepository.findAllById(idClass);
        List<Receivers> receiversList = receiversRepository.findAllByAppSendId(idAppsend);
        ListViewDetailUserNotifiTeacherResponse listViewDetailUserNotifiTeacherResponse = new ListViewDetailUserNotifiTeacherResponse();
        List<HistoryNotifiUserResponse> historyNotifiTeacherResponseList = new ArrayList<>();
        receiversList.forEach(x -> {
            if (x.isSendDel() == AppConstant.APP_FALSE) {
                HistoryNotifiUserResponse model = new HistoryNotifiUserResponse();
                Kids kids = kidsRepository.findById(x.getIdKids()).orElseThrow();
                model.setKidName(kids.getFullName());
                model.setAvatarkid(ConvertData.getAvatarKid(kids));
                model.setIdRevoke(x.getId());
                if (principal.getSysConfig().isAppSendParent() == AppConstant.APP_TRUE) {
                    if (x.isApproved() == AppConstant.APP_FALSE) {
                        model.setStatusSend("IsApprove");
                    } else if (x.isSendDel() == AppConstant.APP_TRUE) {
                        model.setStatusSend("Revoke");
                    }else if (x.isApproved() == AppConstant.APP_TRUE && x.isUserUnread() == AppConstant.APP_FALSE) {
                        model.setStatusSend("Sent");
                    }  else if (x.isUserUnread() == AppConstant.APP_TRUE && x.isApproved() == AppConstant.APP_TRUE) {
                        model.setStatusSend("Read");
                    }
                } else {
                    if (x.isApproved() == AppConstant.APP_FALSE) {
                        model.setStatusSend("IsApprove");
                    } else if (x.isSendDel() == AppConstant.APP_TRUE) {
                        model.setStatusSend("Revoke");
                    } else if (x.isApproved() == AppConstant.APP_TRUE && x.isUserUnread() == AppConstant.APP_FALSE) {
                        model.setStatusSend("Sent");
                    } else if (x.isUserUnread() == AppConstant.APP_TRUE && x.isApproved() == AppConstant.APP_TRUE && x.isSendDel() == AppConstant.APP_FALSE) {
                        model.setStatusSend("Sent");
                    }
                }
                historyNotifiTeacherResponseList.add(model);
            }

        });
        listViewDetailUserNotifiTeacherResponse.setDataList(historyNotifiTeacherResponseList);
        return listViewDetailUserNotifiTeacherResponse;
    }

    @Override
    public boolean revokeUserNotifi(UserPrincipal principal, Long id) {
        CommonValidate.checkDataTeacher(principal);
        Receivers receivers = receiversRepository.findByIdAndDelActiveTrue(id).orElseThrow();
        receivers.setSendDel(AppConstant.APP_TRUE);
        receiversRepository.save(receivers);
        return true;
    }

}
