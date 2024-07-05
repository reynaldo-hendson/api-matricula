package com.test.forleven.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TelefoneRequest {

    @NotBlank
    private String phone;
    @NotBlank
    private String estudante;

}
