package com.example.filecare.services;

import com.example.filecare.db.entities.UserDetailsImpl;
import com.example.filecare.db.repositories.UserDetailsRepository;
import com.example.filecare.dto.UserDetailsDto;
import com.example.filecare.exceptions.ExceptionsEnum;
import com.example.filecare.exceptions.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDetailsRepository.findUserDetailsImplByUsername(username)
                .orElseThrow(() -> new ServiceException(ExceptionsEnum.USER_NOT_FOUND));
    }

    public Boolean save(UserDetailsDto userDetailsDto) {
        try {
            UserDetailsImpl userDetails =
                    new UserDetailsImpl(userDetailsDto.getUsername(),
                            bCryptPasswordEncoder.encode(userDetailsDto.getPassword()));

            userDetailsRepository.save(userDetails);
        } catch (Exception e) {
            throw new ServiceException(ExceptionsEnum.EXCEPTION_SAVING_USER);
        }
        return true;
    }
}
