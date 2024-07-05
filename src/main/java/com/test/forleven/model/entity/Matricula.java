package com.test.forleven.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.forleven.infra.config.utils.DateTimeUtil;
import com.test.forleven.infra.exceptions.NegocioException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "tb_matricula")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Matricula {

    @Id
    @Column(name = "matricula", unique = true)
    private String numeroMatricula;

    @OneToOne
    private Estudante estudante;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusMatriculaEstudante statusMatricula;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataRegistroMatricula;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dataTrancamentoMatricula;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dataReaberturaMatricula;

    public String gerarMatricula() {
        int anoAtual = LocalDateTime.now().getYear();
        int numeroAleatorio = ThreadLocalRandom.current().nextInt(100000, 100000000); // Gerar um número aleatório de 8 dígitos
        return anoAtual + String.format("%08d", numeroAleatorio); // Unificar o ano com o número aleatório
    }

    public void reabrirMatricula(){
        if(canBeLocked()){
            throw new NegocioException("Matricula não pode ser reaberta.");
        }
        setStatusMatricula(StatusMatriculaEstudante.ATIVA);
        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        setDataReaberturaMatricula(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));
    }

    public void trancarMatricula() {
        if (cannotBeTracked()){
            throw new NegocioException("Matricula não pode ser trancada.");
        }
        setStatusMatricula(StatusMatriculaEstudante.TRANCADA);
        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        setDataTrancamentoMatricula(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));
    }
    public boolean canBeLocked(){
        return StatusMatriculaEstudante.ATIVA.equals(getStatusMatricula());
    }
    public boolean cannotBeTracked(){
        return !canBeLocked();
    }

}
