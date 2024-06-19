package com.test.forleven.api.mapper;

import com.test.forleven.api.response.EstudanteResumeResponse;
import com.test.forleven.api.response.TelefoneResumeResponse;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.Telefone;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TelefoneResumeResponseMapper {

    default List<TelefoneResumeResponse> toTelefoneResumeResponseList(List<Telefone> telefones){
        return telefones.stream()
                .map(this::toTelefoneResumeResponse)
                .toList();
    }

    default TelefoneResumeResponse toTelefoneResumeResponse(Telefone telefone) {
        TelefoneResumeResponse response = new TelefoneResumeResponse();
        response.setPhone(telefone.getPhone());
        response.setDataRegistro(telefone.getDataRegistro());
        response.setEstudante(toEstudanteResumeResponses(telefone.getEstudante()));
        return response;
    }

    default EstudanteResumeResponse toEstudanteResumeResponses(Estudante estudante) {
        EstudanteResumeResponse response = new EstudanteResumeResponse();
        response.setCpf(estudante.getCpf());
        response.setName(estudante.getName());
        // Mapeie outros campos, se necessário
        return response;
    }

}
