package ru.codepink_glitch.filecare.controllers;

import org.springframework.web.bind.annotation.*;
import ru.codepink_glitch.filecare.dto.UserDetailsDto;
import ru.codepink_glitch.filecare.services.UserDetailsServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterController {

    UserDetailsServiceImpl userDetailsService;

    @PostMapping
    public ResponseEntity register(@RequestBody UserDetailsDto userDetailsDto) {
        userDetailsService.save(userDetailsDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Boolean> isUsernameAvailable(@RequestParam String username) {
        return new ResponseEntity<>(userDetailsService.isUsernameAvailable(username), HttpStatus.OK);
    }
}
