package com.inside.messagesystem.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inside.messagesystem.dto.response.ApiBusinessMessageDto;
import com.inside.messagesystem.exception.SecurityException;
import com.inside.messagesystem.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.inside.messagesystem.util.Endpoint.Url.LOGIN_ENDPOINT;
import static com.inside.messagesystem.util.Endpoint.Url.REGISTRATION_ENDPOINT;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil, UserService userService, ObjectMapper objectMapper) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer_") ||
                request.getRequestURI().contains(LOGIN_ENDPOINT) ||
                request.getRequestURI().contains(REGISTRATION_ENDPOINT)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split("Bearer_")[1].trim();
        if (!jwtTokenUtil.accessTokenIsValid(token)) {
            handleException(response);
            return;
        }
        authenticateUserByToken(token, response, request);
        filterChain.doFilter(request, response);
    }

    private void authenticateUserByToken(String token, HttpServletResponse response, HttpServletRequest request) throws IOException {
        try {
            UserDetails userDetails = JwtUserFactory.create(userService.getUserFromToken(token));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SecurityException ex) {
            handleException(response);
        }
    }

    private void handleException(HttpServletResponse response) throws IOException {
        ApiBusinessMessageDto errorResponseDto = new ApiBusinessMessageDto("Jwt token is invalid");

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponseDto));
    }
}
