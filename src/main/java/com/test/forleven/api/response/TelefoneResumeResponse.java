package com.test.forleven.api.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TelefoneResumeResponse {

    private String phone;

    private EstudanteResumeResponse estudante;

    private LocalDateTime dataRegistro;
}
