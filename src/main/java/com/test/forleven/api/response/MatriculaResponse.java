package com.test.forleven.api.response;

import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.StatusMatriculaEstudante;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class MatriculaResponse {

    private String numeroMatricula;

    private Estudante estudante;

    private StatusMatriculaEstudante statusMatricula;

    private LocalDateTime dataRegistroMatricula;



}
