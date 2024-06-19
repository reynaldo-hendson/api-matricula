package com.test.forleven.api.response;

import com.test.forleven.model.entity.Estudante;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TelefoneResponse {

    private String phone;

    private Estudante estudante;

    private LocalDateTime dataRegistro;
}
