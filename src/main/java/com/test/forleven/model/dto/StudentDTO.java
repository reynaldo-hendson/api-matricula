package com.test.forleven.model.dto;


import com.test.forleven.model.entity.StatusRegistrationStudent;
import jakarta.validation.constraints.NotBlank;

public record StudentDTO(@NotBlank String name,
                         @NotBlank String lastName,
                         @NotBlank String email,
                         @NotBlank String cpf,
                         @NotBlank String cep,
                         StatusRegistrationStudent status) {
}
