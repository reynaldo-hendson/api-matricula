package com.test.forleven.api.response;

import com.test.forleven.model.entity.Endereco;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EstudanteResponse {

    private String cpf;

    private String name;

    private String lastName;

    private String email;

    private Endereco endereco;

    private LocalDateTime dataRegistro;
}
