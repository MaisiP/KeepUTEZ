package mx.edu.utez.keeputez.config.filter;

import mx.edu.utez.keeputez.model.User;
import mx.edu.utez.keeputez.util.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtTokenUtil jwtTokenUtil;
    final String AUTHORIZATION_HEADER = "Authorization";

    final Logger loggerMessage = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

    public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer")) {
            loggerMessage.info("Bearer confirmado");
            UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
            loggerMessage.warn("JWT Token does not begin with Bearer String");
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(6);
        User user = jwtTokenUtil.parseToken(token);
        if (user != null) {
            return new UsernamePasswordAuthenticationToken(user, null,
                    Collections.emptyList());
        } else {
            loggerMessage.warn("Token inválido");
            return null;
        }
    }
}