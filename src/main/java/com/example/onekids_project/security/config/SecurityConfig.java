package com.example.onekids_project.security.config;

import com.example.onekids_project.entity.user.Api;
import com.example.onekids_project.repository.ApiRepository;
import com.example.onekids_project.security.api.ApiData;
import com.example.onekids_project.security.jwt.JwtAuthenticationEntryPoint;
import com.example.onekids_project.security.jwt.JwtAuthenticationFilter;
import com.example.onekids_project.security.model.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * thực hiện config cho project
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private ApiRepository apiRepository;
    @Autowired
    private ApiData apiData;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * config đường dẫn
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //lấy danh sách api từ bảng ma_api
        List<String> apiList = apiRepository.findByDelActiveTrue().stream().map(Api::getApiUrl).collect(Collectors.toList());
        apiList.add(0, "insert index");
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")

                //cho phép truy cập không cần đăng nhập
                .permitAll()
                .antMatchers("/api/auth/**")
                .permitAll()
                .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                .permitAll()
                .antMatchers("/api/ok", "/api/test", "/api/createauto", "/web/common/**")
                .permitAll()
                .antMatchers("/web/test/**")
                .permitAll()

                //for mobile
                .antMatchers("/mob/app-version")
                .permitAll()

                .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**")
                .permitAll().antMatchers("/onekids/api-docs", "/onekids/swagger-ui.html", "/onekids/swagger-ui/index.html")
                .permitAll()


/*
                // cho phép truy cập tất cả GET,POST,PUT,DELETE api/ok1 với quyền get(0)
                .antMatchers("/api/ok1").access("hasAnyRole('" + apiList.get(1).getApiUrl() + "')")
                .antMatchers("/api/ok2").access("hasAnyRole('" + apiList.get(2).getApiUrl() + "')")

                //cho phép truy cập GET vào bên trong đường dẫn api/ok31/** với quyền apiList.get(1)
                .antMatchers(HttpMethod.GET, "/api/ok31/**").access("hasAnyRole('" + apiList.get(1).getApiUrl() + "')")

                //cho phép truy cập POST 2 api /ok31 và /ok32  với quyền apiList.get(3)
                .antMatchers(HttpMethod.POST, "/api/ok31", "api/ok32").access("hasAnyRole('" + apiList.get(3).getApiUrl() + "')")

                //cho phép truy cập DELETE với api/ok31 với các quyền get(3) hoặc get(2)
                .antMatchers(HttpMethod.DELETE, "/api/ok31").access("hasAnyRole('" + apiList.get(4).getApiUrl() + "','" + apiList.get(3).getApiUrl() + "')")

                .antMatchers("/api/ok4").access("hasAuthority('" + "ROLE_" + apiList.get(4).getApiUrl() + "')")
*/
//                -------------new code------------------------
//                .antMatchers(HttpMethod.PUT,"/web/fn/fees/order/view/one").access("hasAnyRole('" + apiList.get(1) + "')")
//                .antMatchers(HttpMethod.PUT,"/web/fn/fees/order/locked/one").access("hasAnyRole('" + apiList.get(2) + "')")


                /**
                 * 1. học phí và dịch vụ
                 */
                /*
                quản lý ví
                 */
                //thống kê ví
                .antMatchers(HttpMethod.GET, apiData.getFeesWallet(3)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(6) + "') and hasRole('" + apiList.get(8) + "')")
                //Nạp rút ví
                .antMatchers(HttpMethod.POST, apiData.getFeesWallet(2)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(6) + "') and hasRole('" + apiList.get(7) + "')")
                //menu quản lý ví
                .antMatchers(apiData.getFeesWallet(1)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(6) + "')")
                /*
                Danh sách khoản
                 */
                .antMatchers(apiData.getFeesPackage(1)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(5) + "')")
                /*
                Đăng ký khoản
                 */
                .antMatchers(apiData.getFeesKidsPackage(1)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(4) + "')")
                /*
                Duyệt học phí
                 */
                //thay đổi số sử dụng
                .antMatchers(apiData.getFeesApproved(2)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(3) + "')  and hasRole('" + apiList.get(10) + "')")
                //duyệt khoản
                .antMatchers(apiData.getFeesApproved(3)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(3) + "')  and hasRole('" + apiList.get(11) + "')")
                //khóa khoản
                .antMatchers(apiData.getFeesApproved(4)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(3) + "')  and hasRole('" + apiList.get(12) + "')")
                //menu duyệt học phí
                .antMatchers(apiData.getFeesApproved(1)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(3) + "')")
                /*
                Thu học phí
                 */
                //khóa hóa đơn
                .antMatchers(apiData.getFeesOrder(2)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(2) + "')  and hasRole('" + apiList.get(13) + "')")
                //thanh toán
                .antMatchers(apiData.getFeesOrder(3)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(2) + "')  and hasRole('" + apiList.get(14) + "')")
                //thống kê
                .antMatchers(apiData.getFeesOrder(4)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(2) + "')  and hasRole('" + apiList.get(15) + "')")
                //menu thu học phí
                .antMatchers(apiData.getFeesOrder(1)).access("hasRole('" + apiList.get(1) + "') and hasRole('" + apiList.get(2) + "')")
                /*
                menu to học phí và dịch vụ
                 */
                .antMatchers(apiData.getFeesMenu(1)).hasRole(apiList.get(1))


                /**
                 * 2. công lương
                 */
                /*
                  thanh toán lương
                */
                //Khóa hóa đơn
                .antMatchers(apiData.getSalaryOrder(2)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(22) + "') and hasRole('" + apiList.get(23) + "')")
                //thanh toán hóa đơn
                .antMatchers(apiData.getSalaryOrder(3)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(22) + "') and hasRole('" + apiList.get(24) + "')")
                //thống kê hóa đơn
                .antMatchers(apiData.getSalaryOrder(4)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(22) + "') and hasRole('" + apiList.get(25) + "')")
                //menu thanh toán lương
                .antMatchers(apiData.getSalaryOrder(1)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(22) + "')")
                /*
                duyệt bảng lương
                 */
                //Khóa khoản lương
                .antMatchers(apiData.getSalaryApproved(4)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(18) + "') and hasRole('" + apiList.get(21) + "')")
                //Duyệt khoản lương
                .antMatchers(apiData.getSalaryApproved(3)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(18) + "') and hasRole('" + apiList.get(20) + "')")
                //thay đổi số sử dụng
                .antMatchers(apiData.getSalaryApproved(2)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(18) + "') and hasRole('" + apiList.get(19) + "')")
                //menu duyệt bảng lương
                .antMatchers(apiData.getSalaryApproved(1)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(18) + "')")
                /*
                thiết lập tiền lương
                 */
                .antMatchers(apiData.getSalarySetting(1), apiData.getSalarySetting(2), apiData.getSalarySetting(3)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(17) + "')")
                /*
                mẫu công lương
                 */
                .antMatchers(apiData.getSalarySample(1)).access("hasRole('" + apiList.get(9) + "') and hasRole('" + apiList.get(16) + "')")
                /*
                menu to thiết lập công lương
                 */
                .antMatchers(apiData.getSalaryMenu(1)).hasRole(apiList.get(9))


                /*
                3. chấm công nhân sự
                 */
                //xem chấm công
                .antMatchers(HttpMethod.GET, apiData.getAttendanceEmployeeAttendance(1), apiData.getAttendanceEmployeeAttendance(4)).access("hasRole('" + apiList.get(26) + "') and hasRole('" + apiList.get(28) + "')")
                //cập nhật chấm công
                .antMatchers(apiData.getAttendanceEmployeeAttendance(2), apiData.getAttendanceEmployeeAttendance(3), apiData.getAttendanceEmployeeAttendance(4)).access("hasRole('" + apiList.get(26) + "') and hasRole('" + apiList.get(28) + "') and hasRole('" + apiList.get(29) + "')")
                /*
                    thiết lập chấm công
                 */
                .antMatchers(apiData.getAttendanceEmployeeConfig(1)).access("hasRole('" + apiList.get(26) + "') and hasRole('" + apiList.get(27) + "')")
                /*
                 menu to chấm công
                 */
                .antMatchers(apiData.getAttendanceEmployeeMenu(1)).hasRole(apiList.get(26))


                //4. thu chi nội bộ
                /*
                  quản lý phiếu chi
                 */
                //thanh toán phiếu chi
                .antMatchers(apiData.getCashInternalOut(4)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(41) + "') and hasRole('" + apiList.get(44) + "')")
                //duyệt phiếu chi
                .antMatchers(apiData.getCashInternalOut(3)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(41) + "') and hasRole('" + apiList.get(43) + "')")
                //tạo phiếu chi
                .antMatchers(apiData.getCashInternalOut(2)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(41) + "') and hasRole('" + apiList.get(42) + "')")
                //menu quản lý phiếu chi
                .antMatchers(apiData.getCashInternalOut(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(41) + "')")
                /*
                quản lý phiếu thu
                 */
                //thanh toán phiếu thu
                .antMatchers(apiData.getCashInternalIn(4)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(37) + "') and hasRole('" + apiList.get(40) + "')")
                //duyệt phiếu thu
                .antMatchers(apiData.getCashInternalIn(3)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(37) + "') and hasRole('" + apiList.get(39) + "')")
                //tạo phiếu thu
                .antMatchers(apiData.getCashInternalIn(2)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(37) + "') and hasRole('" + apiList.get(38) + "')")
                //menu quản lý phiếu thu
                .antMatchers(apiData.getCashInternalIn(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(37) + "')")
                /*
                    quản lý quỹ
                */
                //xem quỹ
                .antMatchers(HttpMethod.GET, apiData.getCashInternalCashBook(2)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(35) + "') and hasRole('" + apiList.get(36) + "')")
                //xem chi tiết quỹ
                .antMatchers(apiData.getCashInternalCashBook(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(35) + "')")
                /*
                 quản lý đối tượng
                 */
                //xem đối tượng
                .antMatchers(HttpMethod.GET, apiData.getCashInternalPeople(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(33) + "')")
                //cập nhật đối tượng
                .antMatchers(apiData.getCashInternalPeople(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(33) + "') and hasRole('" + apiList.get(34) + "')")
                /**
                 * thông tin thanh toán
                 */
                //xem thông tin thanh toán
                .antMatchers(HttpMethod.GET, apiData.getCashInternalBank(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(31) + "')")
                //cập nhật thông tin thanh toán
                .antMatchers(apiData.getCashInternalBank(1)).access("hasRole('" + apiList.get(30) + "') and hasRole('" + apiList.get(31) + "') and hasRole('" + apiList.get(32) + "')")
                /*
                menu to thu chi nội bộ
                */
                .antMatchers(apiData.getCashInternal(1)).hasRole(apiList.get(30))

                //5 cấu hình nhà trường
                /**
                 * thiết lập thông tin trường
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(2)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(46) + "')")
                .antMatchers(apiData.getSchoolConfig(2)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(46) + "') and hasRole('" + apiList.get(47) + "')")
                /**
                 * cấu hình ngày nghỉ
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(3)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(48) + "')")
                .antMatchers(apiData.getSchoolConfig(3)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(48) + "') and hasRole('" + apiList.get(49) + "')")
                /**
                 * cấu hình nhân sự
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(4)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(50) + "')")
                .antMatchers(apiData.getSchoolConfig(4)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(50) + "') and hasRole('" + apiList.get(51) + "')")
                /**
                 * cấu hình tài chính
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(5)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(52) + "')")
                .antMatchers(apiData.getSchoolConfig(5)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(52) + "') and hasRole('" + apiList.get(53) + "')")
                /**
                 * quản lý môn học
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(6)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(54) + "')")
                .antMatchers(apiData.getSchoolConfig(6)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(54) + "') and hasRole('" + apiList.get(55) + "')")
                /**
                 * mẫu nhận xét
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(7)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(56) + "')")
                .antMatchers(apiData.getSchoolConfig(7)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(56) + "') and hasRole('" + apiList.get(57) + "')")
                /**
                 * mẫu điểm danh
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(8)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(58) + "')")
                .antMatchers(apiData.getSchoolConfig(8)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(58) + "') and hasRole('" + apiList.get(59) + "')")
                /**
                 * mẫu lời chúc
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(9)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(60) + "')")
                .antMatchers(apiData.getSchoolConfig(9)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(60) + "') and hasRole('" + apiList.get(61) + "')")
                /**
                 * quản lý đối tượng
                 */
                .antMatchers(HttpMethod.GET, apiData.getSchoolConfig(10)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(62) + "')")
                .antMatchers(apiData.getSchoolConfig(10)).access("hasRole('" + apiList.get(45) + "') and hasRole('" + apiList.get(62) + "') and hasRole('" + apiList.get(63) + "')")
                /**
                 *vào menu cấu hình nhà trường
                 */
                .antMatchers(apiData.getSchoolConfig(1)).hasRole(apiList.get(45))

                //Bản tin
                /**
                 * thông báo hệ thống
                 */
                .antMatchers(apiData.getNews(1), apiData.getNews(2)).access("hasRole('" + apiList.get(65) + "')")
                /**
                 * phụ huynh góp ý
                 */
                .antMatchers(HttpMethod.GET, apiData.getNews(3)).access("hasRole('" + apiList.get(66) + "')")
                .antMatchers(apiData.getNews(3)).access("hasRole('" + apiList.get(66) + "') and hasRole('" + apiList.get(67) + "')")
                /**
                 * giáo viên góp ý
                 */
                .antMatchers(HttpMethod.GET, apiData.getNews(4)).access("hasRole('" + apiList.get(68) + "')")
                .antMatchers(apiData.getNews(4)).access("hasRole('" + apiList.get(68) + "') and hasRole('" + apiList.get(69) + "')")

                //nhật ký phụ huynh
                /**
                 * lời nhắn
                 */
                .antMatchers(HttpMethod.GET, apiData.getParentDiary(1)).access("hasRole('" + apiList.get(71) + "')")
                .antMatchers(apiData.getParentDiary(1)).access("hasRole('" + apiList.get(71) + "') and hasRole('" + apiList.get(72) + "')")
                /**
                 * lời nhắn
                 */
                .antMatchers(HttpMethod.GET, apiData.getParentDiary(2)).access("hasRole('" + apiList.get(73) + "')")
                .antMatchers(apiData.getParentDiary(2)).access("hasRole('" + apiList.get(73) + "') and hasRole('" + apiList.get(74) + "')")
                /**
                 * xin nghỉ
                 */
                .antMatchers(HttpMethod.GET, apiData.getParentDiary(3)).access("hasRole('" + apiList.get(75) + "')")
                .antMatchers(apiData.getParentDiary(3)).access("hasRole('" + apiList.get(75) + "') and hasRole('" + apiList.get(76) + "')")

                //quản lý nhân sự
                /**
                 * danh sách phòng ban
                 */
                .antMatchers(HttpMethod.GET, apiData.getManageEmployee(1)).access("hasRole('" + apiList.get(78) + "')")
                .antMatchers(apiData.getManageEmployee(1)).access("hasRole('" + apiList.get(78) + "') and hasRole('" + apiList.get(79) + "')")
                /**
                 * danh sách nhân sự
                 */
                .antMatchers(HttpMethod.POST, apiData.getManageEmployee(3)).access("hasRole('" + apiList.get(82) + "')")
                .antMatchers(HttpMethod.POST, apiData.getManageEmployee(4)).access("hasRole('" + apiList.get(83) + "')")
                .antMatchers(HttpMethod.GET, apiData.getManageEmployee(2)).access("hasRole('" + apiList.get(80) + "')")
                .antMatchers(apiData.getManageEmployee(2)).access("hasRole('" + apiList.get(80) + "') and hasRole('" + apiList.get(81) + "')")

                //quản lý học sinh
                /**
                 * danh sách khối
                 */
                .antMatchers(HttpMethod.GET, apiData.getManageKids(1)).access("hasRole('" + apiList.get(85) + "')")
                .antMatchers(apiData.getManageKids(1)).access("hasRole('" + apiList.get(85) + "') and hasRole('" + apiList.get(86) + "')")
                /**
                 * danh sách lớp
                 */
                .antMatchers(HttpMethod.GET, apiData.getManageKids(2)).access("hasRole('" + apiList.get(87) + "')")
                .antMatchers(apiData.getManageKids(2)).access("hasRole('" + apiList.get(87) + "') and hasRole('" + apiList.get(88) + "')")
                /**
                 * nhóm học sinh
                 */
                .antMatchers(HttpMethod.GET, apiData.getManageKids(3)).access("hasRole('" + apiList.get(89) + "')")
                .antMatchers(apiData.getManageKids(3)).access("hasRole('" + apiList.get(89) + "') and hasRole('" + apiList.get(90) + "')")
                /**
                 * danh sách học sinh
                 */
                .antMatchers(HttpMethod.POST, apiData.getManageKids(5)).access("hasRole('" + apiList.get(93) + "')")
                .antMatchers(HttpMethod.POST, apiData.getManageKids(6)).access("hasRole('" + apiList.get(94) + "')")
                .antMatchers(HttpMethod.GET, apiData.getManageKids(4)).access("hasRole('" + apiList.get(91) + "')")
                .antMatchers(apiData.getManageKids(4)).access("hasRole('" + apiList.get(91) + "') and hasRole('" + apiList.get(92) + "')")

                //chất lượng học sinh
                /**
                 * điểm danh
                 */
                .antMatchers(HttpMethod.GET, apiData.getStudentQuality(1)).access("hasRole('" + apiList.get(96) + "')")
                .antMatchers(apiData.getStudentQuality(1)).access("hasRole('" + apiList.get(96) + "') and hasRole('" + apiList.get(97) + "')")
                /**
                 * nhận xét
                 */
                .antMatchers(HttpMethod.GET, apiData.getStudentQuality(2)).access("hasRole('" + apiList.get(98) + "')")
                .antMatchers(apiData.getStudentQuality(2)).access("hasRole('" + apiList.get(98) + "') and hasRole('" + apiList.get(99) + "')")
                /**
                 * album ảnh
                 */
                .antMatchers(HttpMethod.GET, apiData.getStudentQuality(3)).access("hasRole('" + apiList.get(100) + "')")
                .antMatchers(apiData.getStudentQuality(3)).access("hasRole('" + apiList.get(100) + "') and hasRole('" + apiList.get(101) + "')")
                /**
                 * chiều cao, cân nặng
                 */
                .antMatchers(HttpMethod.GET, apiData.getStudentQuality(4)).access("hasRole('" + apiList.get(102) + "')")
                .antMatchers(apiData.getStudentQuality(4)).access("hasRole('" + apiList.get(102) + "') and hasRole('" + apiList.get(103) + "')")
                /**
                 * thời khóa biểu
                 */
                .antMatchers(HttpMethod.GET, apiData.getStudentQuality(5)).access("hasRole('" + apiList.get(104) + "')")
                .antMatchers(apiData.getStudentQuality(5)).access("hasRole('" + apiList.get(104) + "') and hasRole('" + apiList.get(105) + "')")
                /**
                 * thực đơn
                 */
                .antMatchers(HttpMethod.GET, apiData.getStudentQuality(6)).access("hasRole('" + apiList.get(106) + "')")
                .antMatchers(apiData.getStudentQuality(6)).access("hasRole('" + apiList.get(106) + "') and hasRole('" + apiList.get(107) + "')")
                //Quản lý sinh nhật
                /**
                 * sinh nhật học sinh
                 */
                .antMatchers(HttpMethod.GET, apiData.getBirthday(1)).access("hasRole('" + apiList.get(109) + "')")
                .antMatchers(apiData.getBirthday(1)).access("hasRole('" + apiList.get(109) + "') and hasRole('" + apiList.get(110) + "')")
                /**
                 * sinh nhật phụ huynh
                 */
                .antMatchers(HttpMethod.GET, apiData.getBirthday(2)).access("hasRole('" + apiList.get(111) + "')")
                .antMatchers(apiData.getBirthday(2)).access("hasRole('" + apiList.get(111) + "') and hasRole('" + apiList.get(112) + "')")
                /**
                 * sinh nhật giáo viên
                 */
                .antMatchers(HttpMethod.GET, apiData.getBirthday(3)).access("hasRole('" + apiList.get(113) + "')")
                .antMatchers(apiData.getBirthday(3)).access("hasRole('" + apiList.get(113) + "') and hasRole('" + apiList.get(114) + "')")

                //lịch sử thông báo
                /**
                 * tin nhắn đặt lịch
                 */
                .antMatchers(HttpMethod.GET, apiData.getNotifyHistory(1)).access("hasRole('" + apiList.get(116) + "')")
                .antMatchers(apiData.getNotifyHistory(1)).access("hasRole('" + apiList.get(116) + "') and hasRole('" + apiList.get(117) + "')")
                /**
                 * tin nhắn tùy chỉnh
                 */
                .antMatchers(apiData.getNotifyHistory(2)).access("hasRole('" + apiList.get(118) + "')")
                /**
                 * lịch sửa tin nhắn sms
                 */
                .antMatchers(apiData.getNotifyHistory(3)).access("hasRole('" + apiList.get(119) + "')")
                /**
                 * tin sử thông báo app
                 */
                .antMatchers(HttpMethod.GET, apiData.getNotifyHistory(4)).access("hasRole('" + apiList.get(120) + "')")
                .antMatchers(apiData.getNotifyHistory(4)).access("hasRole('" + apiList.get(120) + "') and hasRole('" + apiList.get(121) + "')")

                //thống kê báo cáo
                /**
                 * thống kê
                 */
                .antMatchers(apiData.getStatisticalFinance(2)).access("hasRole('" + apiList.get(123) + "')")
                /**
                 * số liệu báo cáo
                 */
                .antMatchers(apiData.getStatisticalFinance(3)).access("hasRole('" + apiList.get(124) + "')")
                /**
                 * nhóm học phí
                 */
                .antMatchers(apiData.getStatisticalFinance(4)).access("hasRole('" + apiList.get(125) + "')")
                /**
                 * nhóm công lương
                 */
                .antMatchers(apiData.getStatisticalFinance(5)).access("hasRole('" + apiList.get(126) + "')")
                /**
                 *vào menu thống kê báo cáo
                 */
                .antMatchers(apiData.getStatisticalFinance(1)).hasRole(apiList.get(122))

                //chấm công nhân sự
                /**
                 * xin nghỉ nhân sự
                 */
                .antMatchers(HttpMethod.GET, apiData.getAbsentTeacher(1)).access("hasRole('" + apiList.get(127) + "')")
                /**
                 * phản hồi xin nghỉ nhân sự
                 */
                .antMatchers(apiData.getAbsentTeacher(1)).access("hasRole('" + apiList.get(128) + "')")
//                -------------end code------------------------
                //cho phép truy cập tất cả các url còn lại chỉ yêu cầu đăng nhập mà không cần quyền
                .anyRequest()
                .authenticated();

        // chạy quay filter đầu tiên
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
