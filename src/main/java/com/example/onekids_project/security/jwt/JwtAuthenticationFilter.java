package com.example.onekids_project.security.jwt;

import com.example.onekids_project.common.AppTypeConstant;
import com.example.onekids_project.common.ErrorsConstant;
import com.example.onekids_project.entity.user.MaUser;
import com.example.onekids_project.repository.MaUserRepository;
import com.example.onekids_project.response.common.NewDataResponse;
import com.example.onekids_project.security.model.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MaUserRepository maUserRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if ("172.21.16.1".equals(InetAddress.getLocalHost().getHostAddress())) {
                String api = request.getRequestURI();
                System.out.println("Call api request: " + api);
            }
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                //lấy id từ token
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                /**
                 * id tùy theo đối tượng
                 * plus: id=idSchool đang login
                 * teacher: id=idClass đang login
                 * kids: id=idKid đang login
                 */
//                String dd = request.getHeader("x-header");
                Long id = tokenProvider.getIdCustom(jwt);
                Claims claims = tokenProvider.getClaims(jwt);
//                this.checkInfoUserInToken(claims, userId);
                UserDetails userDetails = customUserDetailsService.loadUserById(userId, id);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        } catch (ResponseStatusException ex) {
            logger.error("Error client filter: {}", ex.getReason());
        } catch (Exception ex) {
            logger.error("Lỗi thiết lập user vào trong context", ex.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    private void checkInfoUserInToken(Claims claims, Long idUser) {
        MaUser maUser = maUserRepository.findById(idUser).orElseThrow();
        String username = claims.get("username").toString();
        String passwordHash = claims.get("passwordHash").toString();
        if (!maUser.getAppType().equals(AppTypeConstant.PARENT)) {
            if (!maUser.getPasswordHash().equals(passwordHash)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password đã thay đổi");
            }
            if (!maUser.getUsername().equals(username)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username đã thay đổi");
            }
        }
    }
}
