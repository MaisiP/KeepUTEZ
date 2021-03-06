package mx.edu.utez.keeputez.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import mx.edu.utez.keeputez.config.filter.JwtAuthenticationEntryPoint;
import mx.edu.utez.keeputez.config.filter.JwtAuthenticationFilter;
import mx.edu.utez.keeputez.config.filter.JwtAuthorizationFilter;
import mx.edu.utez.keeputez.service.UserService;
import mx.edu.utez.keeputez.util.JwtTokenUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.annotation.PostConstruct;
import javax.validation.Validator;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserService userService;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    private final ObjectMapper objectMapper;

    private final Validator validator;

    public WebSecurityConfig(JwtTokenUtil jwtTokenUtil, UserService userService,
                             JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                             BCryptPasswordEncoder bCryptPasswordEncoder, ObjectMapper objectMapper, Validator validator) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @PostConstruct
    public void init() throws Exception {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter("/login", jwtTokenUtil,
                authenticationManager(), objectMapper, validator);
        this.jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtTokenUtil, authenticationManager());
    }


    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()//a??ade los permisos para cors
                .and()
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers("/public/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(jwtAuthorizationFilter, BasicAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}