package com.example.onekids_project.security.model;

import com.example.onekids_project.common.AppConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.*;
import com.example.onekids_project.util.LoginRequestBean;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    MaUserRepository userRepository;

    @Autowired
    LoginRequestBean loginRequestBean;

    @Autowired
    private SchoolConfigRepository schoolConfigRepository;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private MaClassRepository maClassRepository;

    @Autowired
    private KidsRepository kidsRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SysInforRepository sysInforRepository;

    @Autowired
    private ParentRepository parentRepository;


    /**
     * khi login bằng username và password
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<MaUser> maUserOptional = maUserRepository.findByUsername(username);
        if (maUserOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstant.BAD_CREDENTIALS);
        }
        MaUser maUser = maUserOptional.get();
        return UserPrincipal.create(AppConstant.APP_TRUE, null, maUser, modelMapper, schoolConfigRepository, sysConfigRepository, schoolRepository, maClassRepository, kidsRepository, sysInforRepository, parentRepository);
    }

    /**
     * khi login bằng token
     *
     * @param id
     * @return
     */
    @Transactional
    public UserDetails loadUserById(Long userId, Long id) {
        MaUser maUser = userRepository.findByIdAndDelActiveTrueAndActivatedTrue(userId).orElseThrow(() -> new NotFoundException("Không tìm thấy tài khoản theo id chưa bị xóa và còn kích hoạt"));
        return UserPrincipal.create(AppConstant.APP_FALSE, id, maUser, modelMapper, schoolConfigRepository, sysConfigRepository, schoolRepository, maClassRepository, kidsRepository, sysInforRepository, parentRepository);
    }

}
