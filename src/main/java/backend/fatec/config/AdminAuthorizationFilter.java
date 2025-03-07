package backend.fatec.config;

import backend.fatec.helpers.JwtHelper;
import backend.fatec.controllers.UserController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AdminAuthorizationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserController userController;

    public AdminAuthorizationFilter(JwtHelper jwtHelper, UserController userController) {
        this.jwtHelper = jwtHelper;
        this.userController = userController;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();
        return !(path.startsWith("/admin"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String email;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        email = jwtHelper.extractEmail(jwtToken);

        if(email != null){
            Boolean isAdmin = userController.checkIfUserIsAdmin(email);
            
            if(!isAdmin){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Acesso negado. Usuario nao e administrador.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}