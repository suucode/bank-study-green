package shop.mtcoding.bank.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import shop.mtcoding.bank.config.enums.UserEnum;
import shop.mtcoding.bank.config.jwt.JwtAuthenticationFilter;
import shop.mtcoding.bank.config.jwt.JwtAuthorizationFilter;
import shop.mtcoding.bank.util.CustomResponseUtil;

@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : passwordEncoder Bean 등록됨");
        return new BCryptPasswordEncoder();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            log.debug("디버그 : SecurityConfig의 configure");
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http.addFilter(new JwtAuthenticationFilter(authenticationManager));
            http.addFilter(new JwtAuthorizationFilter(authenticationManager));
        }
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : SecurityConfig의 filterChain");
        http.headers().frameOptions().disable();
        http.csrf().disable();

        // ExcpetionTranslationFilter (인증 권한 확인 필터)
        http.exceptionHandling().authenticationEntryPoint(
                (request, response, authException) -> {
                    CustomResponseUtil.fail(response, "권한없음");
                });

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.formLogin().disable();
        http.httpBasic().disable();
        http.apply(new MyCustomDsl());
        http.authorizeHttpRequests()
                .antMatchers("/api/transaction/**").authenticated()
                .antMatchers("/api/user/**").authenticated()
                .antMatchers("/api/account/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("ROLE_" + UserEnum.ADMIN)
                .anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource() { //controller 위에 @crossorigin 붙여도되는데 매번 붙이긴 귀찮으니까 전역적인 설정을 하는 것
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); //헤더는 다 열어줘도 됨
        configuration.addAllowedMethod("*"); //method를 제한 할 수는 없음.. 하나씩 해도되긴 함..!
        configuration.addAllowedOriginPattern("*"); //원래는 프론트 서버의 주소만 허용하면 됨 (웹만 해당, 폰은 주소가 계속 바뀜)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키, 인증(Authorization)과 같은 헤더를 서버에서 허용해줄지 말지 설정하는 것
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); //모든 요청을 허용하겠다
        return source;
    }
}
