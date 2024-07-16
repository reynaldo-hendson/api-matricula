package com.test.forleven.service;

import com.test.forleven.api.request.MatriculaRequest;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.Matricula;
import com.test.forleven.model.entity.StatusMatriculaEstudante;
import com.test.forleven.repository.EstudanteRepository;
import com.test.forleven.repository.MatriculaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {
    @Mock
    private MatriculaRepository matriculaRepository;

    @Mock
    private EstudanteRepository estudanteRepository;

    @InjectMocks
    private MatriculaService matriculaService;

    private MatriculaRequest matriculaRequest;
    private Estudante estudante;
    private Matricula matricula;

    @BeforeEach
    void setUp() {
        estudante = new Estudante();
        estudante.setCpf("12345678900");

        matricula = new Matricula();
        matricula.setNumeroMatricula("202100000001");
        matricula.setEstudante(estudante);
        matricula.setStatusMatricula(StatusMatriculaEstudante.ATIVA);

        matriculaRequest = new MatriculaRequest();
        matriculaRequest.setEstudante(estudante.getCpf());
    }

    @Test
    void testCriarMatricula_Success() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.of(estudante));
        when(matriculaRepository.save(any(Matricula.class))).thenReturn(matricula);

        Matricula createdMatricula = matriculaService.criarMatricula(matriculaRequest);

        assertNotNull(createdMatricula);
        assertEquals("202100000001", createdMatricula.getNumeroMatricula());
        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
        verify(matriculaRepository, times(1)).save(any(Matricula.class));
    }

    @Test
    void testCriarMatricula_StudentNotFound() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            matriculaService.criarMatricula(matriculaRequest);
        });

        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
        verify(matriculaRepository, times(0)).save(any(Matricula.class));
    }

    @Test
    void testTrancarMatricula_Success() {
        when(matriculaRepository.findByNumeroMatricula(matricula.getNumeroMatricula())).thenReturn(Optional.of(matricula));
        matriculaService.trancarMatricula(matricula.getNumeroMatricula());

        verify(matriculaRepository, times(1)).findByNumeroMatricula(matricula.getNumeroMatricula());
        verify(matriculaRepository, times(1)).save(matricula);
    }

    @Test
    void testTrancarMatricula_NotFound() {
        when(matriculaRepository.findByNumeroMatricula(matricula.getNumeroMatricula())).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            matriculaService.trancarMatricula(matricula.getNumeroMatricula());
        });

        verify(matriculaRepository, times(1)).findByNumeroMatricula(matricula.getNumeroMatricula());
        verify(matriculaRepository, times(0)).save(any(Matricula.class));
    }

    @Test
    void testReabrirMatricula_Success() {
        matricula.setStatusMatricula(StatusMatriculaEstudante.TRANCADA);
        when(matriculaRepository.findByNumeroMatricula(matricula.getNumeroMatricula())).thenReturn(Optional.of(matricula));
        matriculaService.reabrirMatricula(matricula.getNumeroMatricula());

        verify(matriculaRepository, times(1)).findByNumeroMatricula(matricula.getNumeroMatricula());
        verify(matriculaRepository, times(1)).save(matricula);
    }

    @Test
    void testReabrirMatricula_NotFound() {
        when(matriculaRepository.findByNumeroMatricula(matricula.getNumeroMatricula())).thenReturn(Optional.empty());

        assertThrows(EntidadeNaoEncontradaException.class, () -> {
            matriculaService.reabrirMatricula(matricula.getNumeroMatricula());
        });

        verify(matriculaRepository, times(1)).findByNumeroMatricula(matricula.getNumeroMatricula());
        verify(matriculaRepository, times(0)).save(any(Matricula.class));
    }
}

