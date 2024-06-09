package com.test.forleven.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstudanteRequest {

    private String cpf;

    private String name;

    private String lastName;

    private String email;

    private String cep;
}

