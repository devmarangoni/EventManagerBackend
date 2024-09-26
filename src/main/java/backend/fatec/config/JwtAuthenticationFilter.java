package backend.fatec.config;

import backend.fatec.helpers.JwtHelper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;

    public JwtAuthenticationFilter(JwtHelper jwtHelper){
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String email;

        if(
            request.getRequestURI().equals("/register") || 
            request.getRequestURI().equals("/login") || 
            request.getRequestURI().equals("/validate_token") ||
            (request.getRequestURI().equals("/customer") && request.getMethod().equals("POST")) ||
            request.getRequestURI().startsWith("/customer/user/") ||
            request.getRequestURI().equals("/schedule/events/next")
        ){
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        email = jwtHelper.extractEmail(jwtToken);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtHelper.validateToken(jwtToken)) {
                var authenticationToken = new UsernamePasswordAuthenticationToken(
                    email, null, Collections.emptyList()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token de autenticação invalido ou expirado.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}