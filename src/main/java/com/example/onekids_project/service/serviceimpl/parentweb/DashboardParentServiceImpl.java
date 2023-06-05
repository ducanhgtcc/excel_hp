package com.example.onekids_project.service.serviceimpl.parentweb;

import com.example.onekids_project.entity.kids.AttendanceArriveKids;
import com.example.onekids_project.entity.kids.AttendanceKids;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.repository.AttendanceKidsRepository;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.response.chart.ChartAttendanceResponse;
import com.example.onekids_project.response.parentweb.ChangeKidsResponse;
import com.example.onekids_project.security.controller.AuthController;
import com.example.onekids_project.security.model.UserPrincipal;
import com.example.onekids_project.security.payload.LoginRequest;
import com.example.onekids_project.service.servicecustom.parentweb.DashboardParentService;
import com.example.onekids_project.util.AttendanceKidsUtil;
import com.example.onekids_project.util.ConvertData;
import com.example.onekids_project.util.PrincipalUtils;
import com.example.onekids_project.util.UserInforUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lavanviet
 */
@Service
public class DashboardParentServiceImpl implements DashboardParentService {
    @Autowired
    private AttendanceKidsRepository attendanceKidsRepository;

    @Autowired
    private MaUserRepository maUserRepository;
    @Autowired
    private AuthController authController;
    @Autowired
    private ListMapper listMapper;

    @Override
    public List<ChartAttendanceResponse> getAttendanceChart() {
        UserPrincipal principal = PrincipalUtils.getUserPrincipal();
        Long idKid = principal.getIdKidLogin();
        List<ChartAttendanceResponse> responseList = new ArrayList<>();
        List<AttendanceKids> attendanceKidsList = attendanceKidsRepository.findByKidsIdAndAttendanceDateGreaterThanEqualAndAttendanceDateLessThanEqual(idKid, LocalDate.now().minusMonths(1), LocalDate.now());
        attendanceKidsList.forEach(x -> {
            ChartAttendanceResponse response = new ChartAttendanceResponse();
            AttendanceArriveKids arriveKids = x.getAttendanceArriveKids();
            response.setName(ConvertData.convertLocalDateToString(x.getAttendanceDate()));
            if (AttendanceKidsUtil.checkArrive(arriveKids)) {
                response.setAttendance(1);
            } else if (AttendanceKidsUtil.checkArriveYes(arriveKids)) {
                response.setAttendanceYes(1);
            } else if (AttendanceKidsUtil.checkArriveNo(arriveKids)) {
                response.setAttendanceNo(1);
            } else {
                response.setAttendanceUn(1);
            }
            responseList.add(response);
        });
        return responseList;
    }

    @Override
    public List<ChangeKidsResponse> getKidsOfParent() {
        Long idKid = PrincipalUtils.getUserPrincipal().getIdKidLogin();
        MaUser maUser = UserInforUtils.getMaUser(PrincipalUtils.getUserPrincipal().getId());
        List<Kids> kidsList = maUser.getParent().getKidsList().stream().filter(x -> x.isDelActive() && x.isActivated()).collect(Collectors.toList());
        List<ChangeKidsResponse> responseList = listMapper.mapList(kidsList, ChangeKidsResponse.class);
        responseList.stream().filter(x -> x.getId().equals(idKid)).forEach(a -> a.setStatus(true));
        return responseList;
    }

    public ResponseEntity changeKidsOfParent(Long idKid) {
        MaUser maUser = UserInforUtils.getMaUser(PrincipalUtils.getUserPrincipal().getId());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setAppType(maUser.getAppType());
        loginRequest.setUsername(ConvertData.getUsernameNoExtend(maUser.getUsername()));
        loginRequest.setPassword(maUser.getPasswordShow());
        maUser.getParent().setIdKidLogin(idKid);
        maUserRepository.save(maUser);
        ResponseEntity responseEntity = authController.authenticateUser(loginRequest);
        return responseEntity;
    }
}
