package com.test.forleven.api.mapper;

import com.test.forleven.api.request.TelefoneRequest;
import com.test.forleven.model.entity.Telefone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TelefoneRequestMapper {

    Telefone toTelefoneRequest(TelefoneRequest telefoneRequest);
}
