package com.test.forleven.api.mapper;

import com.test.forleven.api.response.MatriculaResponse;
import com.test.forleven.api.response.MatriculaResumeResponse;
import com.test.forleven.model.entity.Matricula;
import com.test.forleven.model.entity.StatusMatriculaEstudante;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatriculaMapper {

    MatriculaResponse matriculaToMatriculaResponse(Matricula matricula);

    default List<MatriculaResumeResponse> toMatriculaResponseList(List<Matricula> matriculas){
        return matriculas.stream()
                .map(this::toMatriculaResumeResponse)
                .toList();
    }

   default MatriculaResumeResponse toMatriculaResumeResponse(Matricula matricula){
        MatriculaResumeResponse response = new MatriculaResumeResponse();
        response.setNumeroMatricula(matricula.getNumeroMatricula());
        response.setStatusMatricula(matricula.getStatusMatricula());
        response.setDataRegistroMatricula(matricula.getDataRegistroMatricula());

       if (matricula.getStatusMatricula() == StatusMatriculaEstudante.TRANCADA) {
           response.setDataTrancamentoMatricula(matricula.getDataTrancamentoMatricula());
       }

       if (matricula.getStatusMatricula() == StatusMatriculaEstudante.ATIVA &&
               matricula.getDataReaberturaMatricula() != null) {
           response.setDataReaberturaMatricula(matricula.getDataReaberturaMatricula());
       }

        return response;
    }
}
