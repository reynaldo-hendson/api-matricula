package com.test.forleven.service;

import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.dto.PhoneDTO;
import com.test.forleven.model.entity.Phone;
import com.test.forleven.model.entity.Student;
import com.test.forleven.repository.PhonesRepository;
import com.test.forleven.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhonesService {

    private final PhonesRepository phonesRepository;
    private final StudentRepository studentRepository;

    public Phone createPhone(PhoneDTO phoneDTO) {

        Phone phone = new Phone();

        Optional<Student> student = studentRepository.findById(phoneDTO.studentId());
        if (student.isPresent()) {
            phone.setStudent(student.get());
            phone.setPhone(phoneDTO.phone());
        }else{
            throw new EntidadeNaoEncontradaException("Student not found");
        }

        if(phonesRepository.findByPhone(phone.getPhone()).isPresent()) {
            throw new NegocioException("Phone exists.");
        }

       return phonesRepository.save(phone);
    }

    public List<Phone> getAllPhonesForStudent(Long student_id){
        Optional<Student> optionalStudent = studentRepository.findById(student_id);
        if(optionalStudent.isEmpty()){
            throw new EntidadeNaoEncontradaException("Student not found");
        }
        return phonesRepository.findByPhone(optionalStudent.get().getId());
    }


}
