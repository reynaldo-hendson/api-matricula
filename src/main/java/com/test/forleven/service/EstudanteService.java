package com.test.forleven.service;

import com.test.forleven.api.request.EstudanteRequest;
import com.test.forleven.infra.config.utils.DateTimeUtil;
import com.test.forleven.infra.config.utils.ValidaCpf;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.entity.Endereco;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.repository.EnderecoRepository;
import com.test.forleven.repository.EstudanteRepository;
import com.test.forleven.repository.TelefoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EstudanteService {

    private final EstudanteRepository estudanteRepository;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;
    private final ViaCepService viaCepService;

    public Estudante save(EstudanteRequest estudanteRequest) {

        Estudante estudante = new Estudante();
        estudante.setName(estudanteRequest.getName());
        estudante.setLastName(estudanteRequest.getLastName());
        estudante.setEmail(estudanteRequest.getEmail());

        boolean existsEmail = estudanteRepository.findByEmail(estudante.getEmail())
                .stream().anyMatch(emailExists -> !emailExists.equals(estudante));
        log.info("verificando se email existe");
        if(existsEmail){
            throw new NegocioException("Email já cadastrado.");
        }

        log.info("Validated CPF");
        if (!ValidaCpf.isCPF(estudanteRequest.getCpf())) {
            throw new NegocioException("CPF inválido.");
        }else {
            estudante.setCpf(estudanteRequest.getCpf());
        }

        //Validação de cep válido
        String cep = estudanteRequest.getCep();
        if(!isValidCep(cep)) {
            throw new NegocioException("CEP inválido.");
        }else{
            Endereco endereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(endereco);
            estudante.setEndereco(endereco);
        }

        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        estudante.setDataRegistro(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

        log.info("Salvando dados do estudante.");
        return estudanteRepository.save(estudante);

    }

    public List<Estudante> getAllStudent(){
        log.info("listando todos os estudantes.");
        return estudanteRepository.findAll();
    }

    public Optional<Estudante> findById(String id) {
        Optional<Estudante> optStudent = estudanteRepository.findById(id);
        log.info(logInfoExists());
        if (optStudent.isPresent()) {
            log.info("carregando dados do estudante.");
            return estudanteRepository.findById(id);
        }else{
            throw new EntidadeNaoEncontradaException("Estudante não encontrado.");
        }
}

    public Estudante update(String id, EstudanteRequest estudanteRequest) {
        // Verificar se o estudante existe
        Estudante estudante = estudanteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Estudante não encontrado."));

        // Atualizar as informações do estudante
        estudante.setName(estudanteRequest.getName());
        estudante.setLastName(estudanteRequest.getLastName());

        // Verificar se o email já está em uso por outro estudante
        boolean existsEmail = estudanteRepository.findByEmail(estudanteRequest.getEmail())
                .stream()
                .anyMatch(emailExists -> !emailExists.equals(estudante));
        log.info("Verificando se email existe");
        if (existsEmail) {
            throw new NegocioException("Email já cadastrado.");
        } else {
            estudante.setEmail(estudanteRequest.getEmail());
        }

        // Validar CEP e atualizar endereço
        String cep = estudanteRequest.getCep();
        if (!isValidCep(cep)) {
            throw new NegocioException("CEP inválido.");
        } else {
            Endereco endereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(endereco);
            estudante.setEndereco(endereco);
        }

        // Atualizar data de registro
        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        estudante.setDataRegistro(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

        log.info("Atualizando dados do estudante.");
        return estudanteRepository.save(estudante);
    }

    public void delete(String id){
        Optional<Estudante> student = estudanteRepository.findById(id);
        log.info(logInfoExists());
        if(student.isPresent()){
            log.info("Deletando dados.");
            estudanteRepository.delete(student.get());
        }else {
            throw new EntidadeNaoEncontradaException("Estudante não encontrado.");
        }
    }

    private String logInfoExists(){
        return ("Vericando se aluno existe.");
    }

    private boolean isValidCep(String cep) {
        // Neste exemplo, apenas verifica se o CEP tem o formato correto
        return cep != null && cep.matches("\\d{8}");
    }

}
