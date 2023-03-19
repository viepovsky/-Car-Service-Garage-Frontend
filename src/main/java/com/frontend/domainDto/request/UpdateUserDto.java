package com.frontend.domainDto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\- ]{9,}$", message = "Invalid phone number")
    private String phoneNumber;

    private String username;

    @Pattern(regexp = "(?=.*[A-Z])(?=.*[\\W])(?=\\S+$).{8,}", message = "Password should contain at least 8 characters, one uppercase letter, and one special character.")
    private String password;
}
