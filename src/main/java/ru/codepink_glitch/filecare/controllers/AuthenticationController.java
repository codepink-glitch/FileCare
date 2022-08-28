package ru.codepink_glitch.filecare.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import ru.codepink_glitch.filecare.dto.AuthenticationRequest;
import ru.codepink_glitch.filecare.dto.AuthenticationResponse;
import ru.codepink_glitch.filecare.dto.TokenVerificationRequest;
import ru.codepink_glitch.filecare.services.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.OK);
    }

    @PostMapping("/isTokenValid")
    public ResponseEntity<Boolean> isTokenExpired(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestBody TokenVerificationRequest request) {
        return new ResponseEntity<>(authenticationService.isTokenValid(userDetails, request), HttpStatus.OK);
    }
}
