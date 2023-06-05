package com.example.onekids_project.repository.repositorycustom;

import com.example.onekids_project.entity.user.Receivers;
import com.example.onekids_project.mobile.plus.request.historyNotifiRequest.DetailUserRequest;
import com.example.onekids_project.mobile.teacher.request.notifyTeacher.SearchNotifyTeacherRequest;
import com.example.onekids_project.request.parentweb.NotifyParentWebRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReceiversRepositoryCustom {
    Receivers findReceiverByIdUserAndIdSend(Long idUser, Long IdSend);
    /**
     * tìm kiếm thông báo cho một học sinh có phần trang
     * @param idSchool
     * @param idKid
     * @param pageable
     * @return
     */
    List<Receivers> findNotifiKidsForMobile(Long idUser, Long idSchool, Long idKid, Pageable pageable);

    List<Receivers> findNotifyKidsForWeb(NotifyParentWebRequest request);

    Long countNotifyKidsForWeb(NotifyParentWebRequest request);

    /**
     * tìm kiếm thông báo cho một giáo viên có phần trang
     * @param idSchool
     * @param idTeacher
     * @param
     * @return
     */
    List<Receivers> findNotifyTeacherForMobile(Long idSchool, Long idTeacher, SearchNotifyTeacherRequest searchNotifyTeacherRequest);

    /**
     * tìm kiếm số thống báo chưa đọc
     * @param idSchool
     * @param idKid
     * @return
     */
    long findNotifiKidsForUnReadMobile(Long idSchool, Long idKid);

    /**
     * đếm số lượng thông báo
     * @param idSchool
     * @param idKid
     * @return
     */
    Long getCountNotifi(Long idSchool, Long idKid);
    Long getCountNotifiTeacher(Long idSchool, Long idKid);

    List<Receivers> findByIdAppSend(Long id);

    List<Receivers> findBirthdayStatus(Long idSchool,Long idKid);

    List<Receivers> findAllByAppSendIdc(Long id, DetailUserRequest request);
}
