package com.frontend.domainDto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @Pattern(regexp = "^\\+?[0-9\\- ]{9,}$", message = "Invalid phone number")
    private String phoneNumber;

    @Pattern(regexp = "^(?!anonymousUser$)[a-zA-Z0-9_]+$", message = "Username can only contain letters, digits and underscores")
    @Size(min = 3, max = 50, message = "Username should be at least 3 characters, with maximum of 50.")
    private String username;

    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W])(?=\\S+$).{8,}", message = "Password should contain at least 8 characters, one uppercase letter, one lowercase letter, and one special character.")
    private String password;
}
