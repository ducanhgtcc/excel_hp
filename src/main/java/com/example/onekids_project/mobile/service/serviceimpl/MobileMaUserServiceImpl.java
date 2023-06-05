package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.common.AvatarDefaultConstant;
import com.example.onekids_project.entity.parent.Parent;
import com.example.onekids_project.entity.system.SysInfor;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mapper.ListMapper;
import com.example.onekids_project.mobile.response.MobileMaUserResponse;
import com.example.onekids_project.mobile.service.servicecustom.MobileMaUserService;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.repository.SysInforRepository;
import com.example.onekids_project.security.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class MobileMaUserServiceImpl implements MobileMaUserService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private SysInforRepository sysInforRepository;


    @Override
    public MobileMaUserResponse getMaUserByIdOfFather(UserPrincipal principal) {
        SysInfor sysInfor = sysInforRepository.findFirstByDelActiveTrue().orElseThrow();
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found idUser by id in mobile"));
        MobileMaUserResponse model = new MobileMaUserResponse();
        Parent parent = maUser.getParent();

        model.setGuideLink(sysInfor.getGuideLink());
        model.setFullName(maUser.getFullName());
        model.setPhone(maUser.getPhone());
        String mail = StringUtils.isNotBlank(parent.getEmail()) ? parent.getEmail() : "";
        model.setEmail(mail);
        String avatar = StringUtils.isNotBlank(parent.getAvatar()) ? parent.getAvatar() : AvatarDefaultConstant.AVATAR_PARENT;
        model.setAvatar(avatar);
        return model;
    }
}
