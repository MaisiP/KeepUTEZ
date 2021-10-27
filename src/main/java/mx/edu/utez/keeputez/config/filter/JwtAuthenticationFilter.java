package mx.edu.utez.keeputez.config.filter;

import mx.edu.utez.keeputez.model.User;
import mx.edu.utez.keeputez.util.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    private final JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationFilter(String authenticationEndPoint, JwtTokenUtil jwtTokenUtil,
                                   AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        setFilterProcessesUrl(authenticationEndPoint);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)  {
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