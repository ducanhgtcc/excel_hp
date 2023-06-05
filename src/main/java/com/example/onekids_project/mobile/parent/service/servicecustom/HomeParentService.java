package com.example.onekids_project.mobile.parent.service.servicecustom;

import com.example.onekids_project.mobile.parent.response.home.*;
import com.example.onekids_project.mobile.response.ChangeTokenResponse;
import com.example.onekids_project.response.parentweb.ListNotifyParentResponse;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.JwtParentResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeParentService {
    /**
     * tìm kiếm thông tin cho màn home parent
     * @param userPrincipal
     * @return
     */
    HomeParentResponse findHomeParent(UserPrincipal userPrincipal);
    /**
     * lấy danh bạ của phụ huynh
     * @param principal
     * @return
     */
    List<PhoneBookOfParentResponse> findPhoneOfParent(UserPrincipal principal);

    /**
     * thay đổi học sinh
     * @param principal
     * @param idKid
     * @return
     */
    ChangeTokenResponse changeKids(UserPrincipal principal, Long idKid);

    /**
     * tìm kiếm thông tin chung trước khi đăng nhập
     * @param principal
     * @return
     */
    JwtParentResponse findHomeFirstParent(UserPrincipal principal);

    /**
     * tìm kiếm thông báo cho app parent
     * @param principal
     * @param pageable
     * @return
     */
    ListMobileNotifiParentResponse findNotifiKidsForMobile(UserPrincipal principal, Pageable pageable);

    /**
     * tìm kiếm chi tiết thông báo cho phụ huynh
     * @param id
     * @return
     */
    MobileNotifiDetailParentResponse findNotifiByIdForMobile(Long id);

    /**
     * tìm kiếm những thông báo chưa đọc
     * @param principal
     * @return
     */
    long findNotifiUnRead(UserPrincipal principal);
}
