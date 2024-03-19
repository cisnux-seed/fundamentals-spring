package dev.cisnux.webmvc.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePersonRequest {
    @NotBlank
    private String firstName;
    private String middleName;
    private String lastName;
    @Email
    private String email;
    @NotBlank
    private String phone;

    private CreateAddressRequest address;
    @NotEmpty
    private List<@NotBlank String> hobbies;
    private List<CreateSocialMediaRequest> socialMedias;
}
