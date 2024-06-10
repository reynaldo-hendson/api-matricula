package com.test.forleven.api.mapper;

import com.test.forleven.api.response.TelefoneResponse;
import com.test.forleven.model.entity.Telefone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TelefoneResponseMapper {

    TelefoneResponse toTelefoneResponse(Telefone response);
}
