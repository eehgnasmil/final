package ggs.ggs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringConfig {

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                                                .requestMatchers(new AntPathRequestMatcher("/admin/**"))
                                                .hasRole("ADMIN") // "/admin/**" 경로는 ROLE_ADMIN 권한을 가진 사용자만 접근 가능
                                                .anyRequest().permitAll()) // 그 외의 모든 요청은 모든 사용자에게 허용
                                .formLogin((formLogin) -> formLogin
                                                .loginPage("/member/login")
                                                .defaultSuccessUrl("/"))
                                .logout((logout) -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true))
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .maximumSessions(-1)
                                                .maxSessionsPreventsLogin(true)
                                                .expiredUrl("/member/login")) // 오타 수정: expiredURL -> expiredUrl
                                .csrf().ignoringRequestMatchers(new AntPathRequestMatcher("/mail/**"));

                return http.build();
        }
}