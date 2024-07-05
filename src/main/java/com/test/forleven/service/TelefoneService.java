package com.test.forleven.service;

import com.test.forleven.api.request.TelefoneRequest;
import com.test.forleven.infra.config.utils.DateTimeUtil;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.Telefone;
import com.test.forleven.repository.EstudanteRepository;
import com.test.forleven.repository.TelefoneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelefoneService {

    private final TelefoneRepository telefoneRepository;
    private final EstudanteRepository estudanteRepository;

    public Telefone createPhone(TelefoneRequest request) {

        Telefone telefone = new Telefone();

        String estudante = request.getEstudante();

        Optional<Estudante> student = estudanteRepository.findById(estudante);
        if (student.isPresent()) {
            telefone.setEstudante(student.get());
            telefone.setPhone(request.getPhone());
        }else{
            throw new EntidadeNaoEncontradaException("Estudante não encontrado.");
        }

        if(telefoneRepository.findByPhone(telefone.getPhone()).isPresent()) {
            throw new NegocioException("Telefone exists.");
        }

        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        telefone.setDataRegistro(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

       return telefoneRepository.save(telefone);
    }

    public List<Telefone> buscarTelefonesEstudante(String estudanteId){
        Optional<Estudante> optionalStudent = estudanteRepository.findById(estudanteId);
        if (optionalStudent.isEmpty()) {
            throw new EntidadeNaoEncontradaException("Estudante não encontrado.");
        }
        List<Telefone> telefones = telefoneRepository.findByPhones(optionalStudent.get().getCpf());
         log.info("Listando todos os telefones.");
        return telefones;
    }

    public List<Telefone> buscarTodosTelefones(){
        return telefoneRepository.findAll();
    }

    public Optional<Telefone> buscarTelefone(String telefone){
        Optional<Telefone> optionalTelefone = telefoneRepository.findByPhone(telefone);
        if (optionalTelefone.isPresent()){
            return optionalTelefone;
        } else {
            throw new EntidadeNaoEncontradaException("Telefone não consta na base de dados.");
        }
    }

    @Transactional
    public void deleteTelefonesByEstudanteId(String estudanteId) {
        telefoneRepository.deleteByEstudanteId(estudanteId);
    }

    @Transactional
    public void deleteTelefone(Long id){
        Optional<Telefone> optionalTelefone = telefoneRepository.findById(id);
        if(optionalTelefone.isPresent()){
            telefoneRepository.deleteById(id);
        } else {
            throw new EntidadeNaoEncontradaException("Telefone não consta na base de dados.");
        }

    }
}
