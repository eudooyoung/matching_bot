package com.multi.matchingbot.common.config;

import com.multi.matchingbot.common.security.JwtAuthenticationFilter;
import com.multi.matchingbot.common.security.MBotAccessDeniedHandler;
import com.multi.matchingbot.common.security.MBotAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableConfigurationProperties(RoleAccessProperties.class)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;
    private final MBotAuthenticationEntryPoint mBotAuthenticationEntryPoint;
    private final MBotAccessDeniedHandler mBotAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final RoleAccessProperties roleAccessProperties;

//    @Bean
//    public DispatcherServlet dispatcherServlet() {
//        DispatcherServlet servlet = new DispatcherServlet();
//        servlet.setThrowExceptionIfNoHandlerFound(false); // 명시적으로 지정
//        return servlet;
//    }


     /***************************************security filter chain 수정 금지*****************************************************/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //예외 처리
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(mBotAuthenticationEntryPoint)
                        .accessDeniedHandler(mBotAccessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(toArray(roleAccessProperties.getPermitAll())).permitAll()
                        .requestMatchers(toArray(roleAccessProperties.getAdminPaths())).hasRole("ADMIN")
                        .requestMatchers(toArray(roleAccessProperties.getCompanyPaths())).hasRole("COMPANY")
                        .requestMatchers(toArray(roleAccessProperties.getMemberPaths())).hasRole("MEMBER")
                        .requestMatchers(toArray(roleAccessProperties.getApiPaths())).authenticated()
                        .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
    /***************************************security filter chain 수정 금지*****************************************************/

//    파이썬 쓸 때 확인
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*"); // 개발 중엔 전체 허용
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true); // 쿠키/토큰 포함 가능

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();  // 테스트용 주의!!!
//        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    private String[] toArray(List<String> list) {
        return list == null ? new String[0] : list.toArray(new String[0]);
    }

}
