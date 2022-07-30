package ru.codepink_glitch.filecare.services;

import ru.codepink_glitch.filecare.db.entities.UserDetailsImpl;
import ru.codepink_glitch.filecare.db.repositories.UserDetailsRepository;
import ru.codepink_glitch.filecare.dto.UserDetailsDto;
import ru.codepink_glitch.filecare.exceptions.ExceptionsEnum;
import ru.codepink_glitch.filecare.exceptions.ServiceException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserDetailsServiceImpl implements UserDetailsService {

    UserDetailsRepository userDetailsRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.findUserDetailsImplByUsername(username)
                .orElseThrow(() -> new ServiceException(ExceptionsEnum.USER_NOT_FOUND));
    }

    public Boolean save(UserDetailsDto userDetailsDto) {
        try {
            UserDetailsImpl userDetails =
                    new UserDetailsImpl(userDetailsDto.getUsername(), bCryptPasswordEncoder.encode(userDetailsDto.getPassword()));

            userDetailsRepository.save(userDetails);
        } catch (Exception e) {
            throw new ServiceException(ExceptionsEnum.EXCEPTION_SAVING_USER);
        }
        return true;
    }

    public Boolean isUsernameAvailable(String username) {
        return !userDetailsRepository.existsByUsernameEqualsIgnoreCase(username);
    }
}
