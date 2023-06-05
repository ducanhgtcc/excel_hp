package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.AppSend;
import com.example.onekids_project.master.request.SearchAppSendRequest;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.NotifiSysRequest;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.SearchHistoryNotifiPlusRequest;
import com.example.onekids_project.mobile.teacher.request.historynotifi.SearchHistoryNotifiTeacherRequest;
import com.example.onekids_project.request.AppSend.SearchContentRequest;
import com.example.onekids_project.request.notifihistory.SearchSmsAppRequest;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AppSendRepositoryCustom {

    List<AppSend> findAllNotif(Long idSchool, Pageable pageable);

    Optional<AppSend> findByIdNotifi(Long idSchool, Long id);

    List<AppSend> searchNotifi(Long idSchool,Long idUserReceiver, SearchContentRequest request);

    List<AppSend> searchNotifyAdmin(SearchAppSendRequest request, List<Long> idSchoolList);

    long countSearchNotifyAdmin(SearchAppSendRequest request, 	List<Long> idSchoolList);

    List<AppSend> findAppSendListByIdClass(Long idClass, SearchHistoryNotifiTeacherRequest searchHistoryNotifiTeacherRequest, UserPrincipal principal);

    long countTotalAccount(Long idSchool,Long idUserReceiver,SearchContentRequest request);

    List<AppSend> searchSmsAppnew(Long idSchool,Long idUser, SearchSmsAppRequest request);

    long coutSearchSmsAppnew(Long idSchool,Long idUser, SearchSmsAppRequest request);

    List<AppSend> searchSmsAppTeachernew(Long idSchool, SearchSmsAppRequest request);

    long coutSearchSmsAppTeachernew(Long idSchool, SearchSmsAppRequest request);

    long countByIdSchool(Long idSchool);

    List<AppSend> searchHistoryNotifi(Long idSchool, SearchHistoryNotifiPlusRequest request);

    List<AppSend> findNotifiSys(Long idSchool,Long idReceiver, NotifiSysRequest request);
}
