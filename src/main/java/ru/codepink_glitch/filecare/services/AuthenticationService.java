package ru.codepink_glitch.filecare.services;

import org.springframework.security.core.userdetails.UserDetails;
import ru.codepink_glitch.filecare.db.entities.UserDetailsImpl;
import ru.codepink_glitch.filecare.dto.AuthenticationRequest;
import ru.codepink_glitch.filecare.dto.AuthenticationResponse;
import ru.codepink_glitch.filecare.dto.TokenVerificationRequest;
import ru.codepink_glitch.filecare.exceptions.ExceptionsEnum;
import ru.codepink_glitch.filecare.exceptions.ServiceException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    AuthenticationManager authenticationManager;
    UserDetailsServiceImpl userDetailsService;
    TokenService tokenService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ServiceException(ExceptionsEnum.BAD_AUTHENTICATION_DATA);
        } catch (Exception e) {
            System.getLogger("default").log(System.Logger.Level.INFO, e);
        }

        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        return new AuthenticationResponse(tokenService.generateToken(userDetails));
    }

    public boolean isTokenValid(UserDetails userDetails, TokenVerificationRequest request) {
        return tokenService.validateToken(request.token(), userDetails);
    }

}
