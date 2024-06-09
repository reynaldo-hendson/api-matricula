package com.test.forleven.api.mapper;

import com.test.forleven.api.response.EstudanteResumeResponse;
import com.test.forleven.model.entity.Estudante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstudanteResumeResponseMapper {
    EstudanteResumeResponse toEstudanteResumeResponse(Estudante estudante);

}
