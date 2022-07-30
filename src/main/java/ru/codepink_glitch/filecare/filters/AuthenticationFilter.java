package ru.codepink_glitch.filecare.filters;

import ru.codepink_glitch.filecare.db.entities.UserDetailsImpl;
import ru.codepink_glitch.filecare.services.TokenService;
import ru.codepink_glitch.filecare.services.UserDetailsServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter class verifying authentication token
 * Used in spring web-configuration
 */

@RequiredArgsConstructor
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationFilter extends OncePerRequestFilter {

    UserDetailsServiceImpl userDetailsService;
    TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String token = null;

        // Checking not null and correct header format

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = tokenService.extractUsername(token);
        }

        // Checking username not null and SecurityContext is empty for this user

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(username);

            // Checking token for validity

            if (tokenService.validateToken(token, userDetails)) {

                // And finally authenticate user in SecurityContext

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                userDetails.getPassword(), userDetails.getAuthorities());

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }

        // Passing control to filterChain

        filterChain.doFilter(request, response);

    }
}
