package com.test.forleven.service;

import com.test.forleven.infra.config.utils.DateTimeUtil;
import com.test.forleven.infra.config.utils.ValidaCpf;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.dto.StudentDTO;
import com.test.forleven.model.entity.Endereco;
import com.test.forleven.model.entity.StatusRegistrationStudent;
import com.test.forleven.model.entity.Student;
import com.test.forleven.repository.EnderecoRepository;
import com.test.forleven.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final EnderecoRepository enderecoRepository;
    private final ViaCepService viaCepService;

    public Student save(StudentDTO studentDTO) {

        Student student = new Student();
        student.setName(studentDTO.name());
        student.setLastName(studentDTO.lastName());

        student.setEmail(studentDTO.email());

        boolean existsEmail = studentRepository.findByEmail(student.getEmail())
                        .stream().anyMatch(emailExists -> !emailExists.equals(student));
        log.info("Verified email");
        if(existsEmail){
            throw new NegocioException("Email já cadastrado.");
        }

        log.info("Validated CPF");
        if (!ValidaCpf.isCPF(studentDTO.cpf())) {
            throw new NegocioException("CPF inválido.");
        }else {
            String cpfFormatado = ValidaCpf.imprimeCPF(studentDTO.cpf());
            student.setCpf(cpfFormatado);
        }

        //Validação de cep válido
        String cep = studentDTO.cep();
        if(!isValidCep(cep)) {
            throw new NegocioException("CEP inválido.");
        }else{
            Endereco endereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(endereco);
            student.setEndereco(endereco);
        }
        student.setStatus(studentDTO.status());

        log.info("Generating registration number.");
        student.setRegistration(student.generateRegistration());

        // Obtendo a data e hora atuais formatadas no estilo brasileiro
        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        student.setDateRegistration(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));

        log.info("Saving student data.");
        return studentRepository.save(student);

    }

    public List<Student> getAllStudent(){
        log.info("listing all student records.");
        return studentRepository.findAll();
    }

    public Optional<Student> findById(Long id) {
        Optional<Student> optStudent = studentRepository.findById(id);
        log.info(logInfoExists());
        if (optStudent.isPresent()) {
            log.info("displaying data.");
            return studentRepository.findById(id);
        }else{
            throw new EntidadeNaoEncontradaException("Student not found.");
        }
    }

    public Optional<Student> findByRegistration(String registration){
        Optional<Student> optionalStudent = studentRepository.findByRegistration(registration);

        log.info(logInfoExists());
        if (optionalStudent.isPresent()){
            return studentRepository.findByRegistration(registration);
        }else{
            throw new EntidadeNaoEncontradaException("student registration"+ registration + "not found.");
        }
    }

    public List<Student> getActiveStudents() {
        List<Student> allStudents = studentRepository.findAll();

        return allStudents.stream()
                .filter(student -> student.getStatus() == StatusRegistrationStudent.ATIVA)
                .toList();
    }

    public List<Student> getLockStudents(){
        List<Student> studentsLock = studentRepository.findAll();

        return studentsLock.stream()
                .filter(student -> student.getStatus() == StatusRegistrationStudent.TRANCADA)
                .toList();
    }

    public void delete(Long id){
        Optional<Student> student = studentRepository.findById(id);
        log.info(logInfoExists());
        if(student.isPresent()){
            log.info("Delete data.");
            studentRepository.delete(student.get());
        }else {
            throw new EntidadeNaoEncontradaException("Student not found");
        }
    }

    public void lockRegistration (String registration){
        Optional<Student> student = studentRepository.findByRegistration(registration);
        if(student.isEmpty()){
            throw new EntidadeNaoEncontradaException("Student not found");
        } else{
            Student updateStudent = student.get();
            updateStudent.lockRegistration();
            studentRepository.save(updateStudent);
        }
    }

    private String logInfoExists(){
        return ("Checking if student exists.");
    }

    private boolean isValidCep(String cep) {
        // Neste exemplo, apenas verifica se o CEP tem o formato correto
        return cep != null && cep.matches("\\d{8}");
    }

}
