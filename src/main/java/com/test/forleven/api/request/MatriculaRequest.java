package com.test.forleven.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatriculaRequest {
    @NotBlank
    private String estudante;
}
