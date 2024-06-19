package com.test.forleven.api.mapper;

import com.test.forleven.api.response.EstudanteResponse;
import com.test.forleven.model.entity.Estudante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstudanteResponseMapper {

    EstudanteResponse toEstudanteResponse(Estudante estudante);

}
