package com.test.forleven.api.mapper;

import com.test.forleven.api.request.EstudanteRequest;
import com.test.forleven.model.entity.Estudante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstudanteRequestMapper {

    Estudante toEstudanteRequest(EstudanteRequest estudanteRequest);
}
