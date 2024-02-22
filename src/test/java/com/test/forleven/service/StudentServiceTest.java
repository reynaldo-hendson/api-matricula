package com.test.forleven.service;

import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.model.dto.StudentDTO;
import com.test.forleven.model.entity.StatusRegistrationStudent;
import com.test.forleven.model.entity.Student;
import com.test.forleven.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;
    @Mock
    private StudentRepository repository;


    @Test
    void testGetAllStudents() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(new Student(), new Student()));

        // Act
        List<Student> students = studentService.getAllStudent();

        // Assert
        assertEquals(2, students.size());
    }

    @Test
    void testFindByIdExistingStudent() {
        // Arrange
        Long studentId = 1L;
        when(repository.findById(studentId)).thenReturn(Optional.of(new Student()));

        // Act
        Optional<Student> student = studentService.findById(studentId);

        // Assert
        assertTrue(student.isPresent());
    }

    @Test
    void testFindByIdNonExistingStudent() {
        // Arrange
        Long nonExistingStudentId = 99L;
        when(repository.findById(nonExistingStudentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntidadeNaoEncontradaException.class, () -> studentService.findById(nonExistingStudentId));
    }

    @Test
    void testDeleteExistingStudent() {
        // Arrange
        Long studentId = 1L;
        when(repository.findById(studentId)).thenReturn(Optional.of(new Student()));

        // Act
        studentService.delete(studentId);

        // Assert: No exceptions should be thrown
        // Assert
        verify(repository, times(1)).delete(any());
    }

    @Test
    void testDeleteNonExistingStudent() {
        // Arrange
        Long nonExistingStudentId = 99L;
        when(repository.findById(nonExistingStudentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(EntidadeNaoEncontradaException.class, () -> studentService.delete(nonExistingStudentId));
    }

    @Test
    void testGetActiveStudents(){

        // Lista de DTOs simulados
        List<StudentDTO> mockStudentDTOs = Arrays.asList(
                new StudentDTO("John", "Doe", "john@example.com", "72195872012",StatusRegistrationStudent.ATIVA),
                new StudentDTO("Jane", "Smith", "jane@example.com", "10924413018",StatusRegistrationStudent.TRANCADA),
                new StudentDTO("Bob", "Johnson", "bob@example.com", "51914253086", StatusRegistrationStudent.ATIVA)
        );

        // Converter DTOs para entidades Student simuladas

        List<Student> mockStudents = mockStudentDTOs.stream()
                .map(studentDTO -> {
                    Student student = new Student();
                    student.setName(studentDTO.name());
                    student.setLastName(studentDTO.lastName());
                    student.setEmail(studentDTO.email());
                    student.setCpf(studentDTO.cpf());
                    student.setStatus(studentDTO.status());
                    return student;
                })
                .toList();

        // Configurar comportamento do mock
        when(repository.findAll()).thenReturn(mockStudents);

        // Chamar o método que queremos testar
        List<Student> activeStudents = studentService.getActiveStudents();

        // Verificar se a lista retornada contém apenas estudantes ativos
        assertEquals(2, activeStudents.size());
        assertTrue(activeStudents.stream().allMatch(student -> student.getStatus() == StatusRegistrationStudent.ATIVA));
    }

    @Test
    void testGetLockStudents(){

        // Lista de DTOs simulados
        List<StudentDTO> mockStudentDTOs = Arrays.asList(
                new StudentDTO("John", "Doe", "john@example.com", "72195872012",StatusRegistrationStudent.TRANCADA),
                new StudentDTO("Jane", "Smith", "jane@example.com", "10924413018",StatusRegistrationStudent.TRANCADA),
                new StudentDTO("Bob", "Johnson", "bob@example.com", "51914253086", StatusRegistrationStudent.TRANCADA)
        );

        // Converter DTOs para entidades Student simuladas

        List<Student> mockStudents = mockStudentDTOs.stream()
                .map(studentDTO -> {
                    Student student = new Student();
                    student.setName(studentDTO.name());
                    student.setLastName(studentDTO.lastName());
                    student.setEmail(studentDTO.email());
                    student.setCpf(studentDTO.cpf());
                    student.setStatus(studentDTO.status());
                    return student;
                })
                .toList();

        // Configurar comportamento do mock
        when(repository.findAll()).thenReturn(mockStudents);

        // Chamar o método que queremos testar
        List<Student> activeStudents = studentService.getLockStudents();

        // Verificar se a lista retornada contém apenas estudantes ativos
        assertEquals(3, activeStudents.size());
        assertTrue(activeStudents.stream().allMatch(student -> student.getStatus() == StatusRegistrationStudent.TRANCADA));
    }

}

