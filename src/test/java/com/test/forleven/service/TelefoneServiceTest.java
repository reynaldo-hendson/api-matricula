package com.test.forleven.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.test.forleven.api.request.TelefoneRequest;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.model.entity.Telefone;
import com.test.forleven.repository.EstudanteRepository;
import com.test.forleven.repository.TelefoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TelefoneServiceTest {

    @Mock
    private TelefoneRepository telefoneRepository;

    @Mock
    private EstudanteRepository estudanteRepository;

    @InjectMocks
    private TelefoneService telefoneService;

    private Estudante estudante;
    private Telefone telefone;
    private TelefoneRequest telefoneRequest;

    @BeforeEach
    void setUp() {
        estudante = new Estudante();
        estudante.setCpf("12345678900");

        telefone = new Telefone();
        telefone.setId(1L);
        telefone.setEstudante(estudante);
        telefone.setPhone("987654321");

        telefoneRequest = new TelefoneRequest();
        telefoneRequest.setEstudante(estudante.getCpf());
        telefoneRequest.setPhone("987654321");
    }

    @Test
    void testCreatePhone_Success() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.of(estudante));
        when(telefoneRepository.findByPhone(telefone.getPhone())).thenReturn(Optional.empty());
        when(telefoneRepository.save(any(Telefone.class))).thenReturn(telefone);

        Telefone createdPhone = telefoneService.createPhone(telefoneRequest);

        assertNotNull(createdPhone);
        assertEquals(telefone.getPhone(), createdPhone.getPhone());

        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
        verify(telefoneRepository, times(1)).findByPhone(telefone.getPhone());
        verify(telefoneRepository, times(1)).save(any(Telefone.class));
    }

    @Test
    void testCreatePhone_EstudanteNotFound() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            telefoneService.createPhone(telefoneRequest);
        });

        assertEquals("Estudante não encontrado.", exception.getMessage());
        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
    }

    @Test
    void testCreatePhone_TelefoneExists() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.of(estudante));
        when(telefoneRepository.findByPhone(telefone.getPhone())).thenReturn(Optional.of(telefone));

        Exception exception = assertThrows(NegocioException.class, () -> {
            telefoneService.createPhone(telefoneRequest);
        });

        assertEquals("Telefone exists.", exception.getMessage());
        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
        verify(telefoneRepository, times(1)).findByPhone(telefone.getPhone());
    }

    @Test
    void testBuscarTelefonesEstudante_Success() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.of(estudante));
        when(telefoneRepository.findByPhones(estudante.getCpf())).thenReturn(List.of(telefone));

        List<Telefone> telefones = telefoneService.buscarTelefonesEstudante(estudante.getCpf());

        assertNotNull(telefones);
        assertFalse(telefones.isEmpty());
        assertEquals(1, telefones.size());
        assertEquals(telefone.getPhone(), telefones.get(0).getPhone());

        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
        verify(telefoneRepository, times(1)).findByPhones(estudante.getCpf());
    }

    @Test
    void testBuscarTelefonesEstudante_EstudanteNotFound() {
        when(estudanteRepository.findById(estudante.getCpf())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            telefoneService.buscarTelefonesEstudante(estudante.getCpf());
        });

        assertEquals("Estudante não encontrado.", exception.getMessage());
        verify(estudanteRepository, times(1)).findById(estudante.getCpf());
    }

    @Test
    void testBuscarTodosTelefones() {
        when(telefoneRepository.findAll()).thenReturn(List.of(telefone));

        List<Telefone> telefones = telefoneService.buscarTodosTelefones();

        assertNotNull(telefones);
        assertFalse(telefones.isEmpty());
        assertEquals(1, telefones.size());
        assertEquals(telefone.getPhone(), telefones.get(0).getPhone());

        verify(telefoneRepository, times(1)).findAll();
    }

    @Test
    void testBuscarTelefone_Success() {
        when(telefoneRepository.findByPhone(telefone.getPhone())).thenReturn(Optional.of(telefone));

        Optional<Telefone> result = telefoneService.buscarTelefone(telefone.getPhone());

        assertTrue(result.isPresent());
        assertEquals(telefone.getPhone(), result.get().getPhone());

        verify(telefoneRepository, times(1)).findByPhone(telefone.getPhone());
    }

    @Test
    void testBuscarTelefone_NotFound() {
        when(telefoneRepository.findByPhone(telefone.getPhone())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            telefoneService.buscarTelefone(telefone.getPhone());
        });

        assertEquals("Telefone não consta na base de dados.", exception.getMessage());
        verify(telefoneRepository, times(1)).findByPhone(telefone.getPhone());
    }

    @Test
    void testDeleteTelefonesByEstudanteId() {
        doNothing().when(telefoneRepository).deleteByEstudanteId(estudante.getCpf());

        telefoneService.deleteTelefonesByEstudanteId(estudante.getCpf());

        verify(telefoneRepository, times(1)).deleteByEstudanteId(estudante.getCpf());
    }

    @Test
    void testDeleteTelefone_Success() {
        when(telefoneRepository.findById(telefone.getId())).thenReturn(Optional.of(telefone));
        doNothing().when(telefoneRepository).deleteById(telefone.getId());

        telefoneService.deleteTelefone(telefone.getId());

        verify(telefoneRepository, times(1)).findById(telefone.getId());
        verify(telefoneRepository, times(1)).deleteById(telefone.getId());
    }

    @Test
    void testDeleteTelefone_NotFound() {
        when(telefoneRepository.findById(telefone.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            telefoneService.deleteTelefone(telefone.getId());
        });

        assertEquals("Telefone não consta na base de dados.", exception.getMessage());
        verify(telefoneRepository, times(1)).findById(telefone.getId());
    }
}