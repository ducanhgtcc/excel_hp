package com.example.onekids_project.mobile.service.serviceimpl;

import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.mobile.service.servicecustom.ChangeInforService;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.security.controller.AuthController;
import com.example.onekids_project.security.jwt.JwtTokenProvider;
import com.example.onekids_project.security.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
public class ChangeInforServiceImpl implements ChangeInforService {

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public String findNewToken(UserPrincipal principal) {
        MaUser maUser = maUserRepository.findByIdAndDelActiveTrue(principal.getId()).orElseThrow(() -> new NotFoundException("not found maUser by id"));
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        maUser.getUsername(),
                        maUser.getPasswordShow()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return jwt;
    }
}
