package com.pifrans.auth.securities;

import com.pifrans.auth.constants.HeadersKeys;
import com.pifrans.auth.constants.HeadersValues;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;


public class AuthorizationSecurity extends BasicAuthenticationFilter {
    private static final Logger LOG = Logger.getLogger(AuthorizationSecurity.class.getName());
    private final TokenJWTSecurity jwtSecurity;
    private final UserDetailsService userDetailsService;

    public AuthorizationSecurity(AuthenticationManager authenticationManager, TokenJWTSecurity jwtSecurity, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtSecurity = jwtSecurity;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        String header = request.getHeader(HeadersKeys.X_AUTHORIZATION.getDescription());

        try {
            if (header != null && header.startsWith(HeadersValues.BEARER.getDescription())) {
                UsernamePasswordAuthenticationToken token = getAuthentication(header.substring(7));
                if (token != null) {
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        if (jwtSecurity.tokenValid(token)) {
            String username = jwtSecurity.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }
        LOG.severe("Token inválido! " + token);
        return null;
    }
}
