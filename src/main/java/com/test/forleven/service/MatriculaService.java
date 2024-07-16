package com.test.forleven.service;

import com.test.forleven.api.request.MatriculaRequest;
import com.test.forleven.infra.config.utils.DateTimeUtil;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.dto.MatriculaDTO;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.Matricula;
import com.test.forleven.model.entity.StatusMatriculaEstudante;
import com.test.forleven.repository.EstudanteRepository;
import com.test.forleven.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudanteRepository estudanteRepository;

    public Matricula criarMatricula(MatriculaRequest matricula){
        Matricula newMatricula = new Matricula();
        String numeroMatricula = newMatricula.gerarMatricula();
        String estudanteCpf = matricula.getEstudante();

        log.info("Verificando se aluno existe na base de dados.");
        Optional<Estudante> estudante = estudanteRepository.findById(estudanteCpf);
        if(estudante.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Cpf do estudante não encontrado." + matricula.getEstudante());
        }else {
            // Verificar se já existe uma matrícula associada ao estudante
            Optional<Matricula> matriculaExistente = matriculaRepository.findByEstudante(estudante.get());
            if (matriculaExistente.isPresent()) {
                throw new NegocioException("Já existe uma matrícula associada a este estudante.");
            } else{
                newMatricula.setEstudante(estudante.get());
                newMatricula.setNumeroMatricula(numeroMatricula);
                newMatricula.setStatusMatricula(StatusMatriculaEstudante.valueOf("ATIVA"));
                String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
                newMatricula.setDataRegistroMatricula(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));
            }
        }

        return matriculaRepository.save(newMatricula);
    }

    public List<Matricula> matriculaList(){
        return matriculaRepository.findAll();
    }

    public List<Matricula> matriculaAtiva(){
        List<Matricula> listMatricula = matriculaRepository.findAll();
        return listMatricula.stream()
                .filter(matricula -> matricula.getStatusMatricula() == StatusMatriculaEstudante.ATIVA)
                .toList();
    }

    public List<Matricula> matriculaTrancada(){
        List<Matricula> listMatricula = matriculaRepository.findAll();
        return listMatricula.stream()
                .filter(matricula -> matricula.getStatusMatricula() == StatusMatriculaEstudante.TRANCADA)
                .toList();
    }

    public Optional<Matricula> findByMatricula(String matricula){
        Optional<Matricula> result = matriculaRepository.findByNumeroMatricula(matricula);
        if(result.isPresent()){
            return result;
        }else{
            throw new EntidadeNaoEncontradaException("Número de matricula não encontrada " + matricula);
        }
    }

    public Optional<Matricula> findByMatriculaPorCpf(String estudanteCpf){
        Optional<Matricula> result = matriculaRepository.findByMatriculaPorCpf(estudanteCpf);
        if(result.isPresent()){
            return result;
        }else{
            throw new EntidadeNaoEncontradaException("Número de Cpf não encontrado " + estudanteCpf);
        }
    }

    public void trancarMatricula(String matricula){
        Optional<Matricula> registro = matriculaRepository.findByNumeroMatricula(matricula);
        if(registro.isEmpty()){
            throw new EntidadeNaoEncontradaException("Matricula não encontrada.");
        } else{
            Matricula updateMatricula = registro.get();
            updateMatricula.trancarMatricula();
            matriculaRepository.save(updateMatricula);
        }
    }

    public void reabrirMatricula(String matricula){
        Optional<Matricula> registro = matriculaRepository.findByNumeroMatricula(matricula);
        if(registro.isEmpty()){
            throw new EntidadeNaoEncontradaException("Matricula não encontrada.");
        } else{
            Matricula updateMatricula = registro.get();
            updateMatricula.reabrirMatricula();
            matriculaRepository.save(updateMatricula);
        }

    }

}
