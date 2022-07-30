package ru.codepink_glitch.filecare.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailsDto {

    String username;
    String password;

}
