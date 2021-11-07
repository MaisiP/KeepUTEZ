package mx.edu.utez.keeputez.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mx.edu.utez.keeputez.model.User;
import mx.edu.utez.keeputez.model.dto.LoginDTO;
import mx.edu.utez.keeputez.util.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final SpringValidatorAdapter adapter;

    public JwtAuthenticationFilter(String authenticationEndPoint, JwtTokenUtil jwtTokenUtil,
                                   AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                   Validator validator) {
        super(authenticationManager);
        setFilterProcessesUrl(authenticationEndPoint);
        this.jwtTokenUtil = jwtTokenUtil;
        this.objectMapper = objectMapper;
        this.validator = validator;
        this.adapter = new SpringValidatorAdapter(this.validator);
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        LoginDTO credentials = objectMapper.readValue(request.getInputStream(), LoginDTO.class);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(credentials, "credentials");
        adapter.validate(credentials, result);
        if (result.hasErrors()) {
            throw new AuthenticationServiceException("Bad credentials format");
        }
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        String token = jwtTokenUtil.generateToken((User) authResult.getPrincipal());
        String bearer = "Bearer ";
        response.addHeader("Authorization", bearer + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.addHeader("Authorization", "Unsuccessful Authentication");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}