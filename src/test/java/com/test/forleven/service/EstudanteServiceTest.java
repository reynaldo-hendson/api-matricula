package com.test.forleven.service;

import com.test.forleven.api.request.EstudanteRequest;
import com.test.forleven.infra.exceptions.EntidadeNaoEncontradaException;
import com.test.forleven.infra.exceptions.NegocioException;
import com.test.forleven.model.entity.Endereco;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.repository.EnderecoRepository;
import com.test.forleven.repository.EstudanteRepository;
import com.test.forleven.repository.TelefoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class EstudanteServiceTest {
    @Mock
    private EstudanteRepository estudanteRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private TelefoneRepository telefoneRepository;

    @Mock
    private ViaCepService viaCepService;

    @InjectMocks
    private EstudanteService estudanteService;

    private EstudanteRequest estudanteRequest;
    private Estudante estudante;
    private Endereco endereco;

    @BeforeEach
    public void setUp() {
        estudanteRequest = new EstudanteRequest();
        estudanteRequest.setName("João");
        estudanteRequest.setLastName("Silva");
        estudanteRequest.setEmail("joao.silva@example.com");
        estudanteRequest.setCpf("16946191099");
        estudanteRequest.setCep("60340195");

        estudante = new Estudante();
        estudante.setName("João");
        estudante.setLastName("Silva");
        estudante.setEmail("joao.silva@example.com");
        estudante.setCpf("16946191099");

        endereco = new Endereco();
        endereco.setCep("60340195");
    }

    @Test
    void testSaveEstudante() {
        when(estudanteRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(viaCepService.consultarCep(anyString())).thenReturn(endereco);
        when(estudanteRepository.save(any(Estudante.class))).thenReturn(estudante);

        Estudante savedEstudante = estudanteService.save(estudanteRequest);

        assertNotNull(savedEstudante);
        assertEquals("João", savedEstudante.getName());
        assertEquals("Silva", savedEstudante.getLastName());
        assertEquals("joao.silva@example.com", savedEstudante.getEmail());
    }

    @Test
     void testSaveEstudanteWithExistingEmail() {
        when(estudanteRepository.findByEmail(anyString())).thenReturn(Optional.of(estudante));

        NegocioException thrown = assertThrows(NegocioException.class, () -> {
            estudanteService.save(estudanteRequest);
        });

        assertEquals("Email já cadastrado.", thrown.getMessage());
    }

    @Test
    void testSaveEstudanteWithInvalidCpf() {
        estudanteRequest.setCpf("invalidCpf");

        NegocioException thrown = assertThrows(NegocioException.class, () -> {
            estudanteService.save(estudanteRequest);
        });

        assertEquals("CPF inválido.", thrown.getMessage());
    }

    @Test
    void testSaveEstudanteWithInvalidCep() {
        estudanteRequest.setCep("invalidCep");

        NegocioException thrown = assertThrows(NegocioException.class, () -> {
            estudanteService.save(estudanteRequest);
        });

        assertEquals("CEP inválido.", thrown.getMessage());
    }

    @Test
    void testGetAllStudents() {
        when(estudanteRepository.findAll()).thenReturn(List.of(estudante));

        List<Estudante> estudantes = estudanteService.getAllStudent();

        assertNotNull(estudantes);
        assertFalse(estudantes.isEmpty());
        assertEquals(1, estudantes.size());
    }

    @Test
    void testFindById() {
        when(estudanteRepository.findById(anyString())).thenReturn(Optional.of(estudante));

        Optional<Estudante> optEstudante = estudanteService.findById("1");

        assertTrue(optEstudante.isPresent());
        assertEquals("João", optEstudante.get().getName());
    }

    @Test
    void testFindByIdNotFound() {
        when(estudanteRepository.findById(anyString())).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException thrown = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            estudanteService.findById("1");
        });

        assertEquals("Estudante não encontrado.", thrown.getMessage());
    }

    @Test
    void testUpdateEstudante() {
        when(estudanteRepository.findById(anyString())).thenReturn(Optional.of(estudante));
        when(viaCepService.consultarCep(anyString())).thenReturn(endereco);
        when(estudanteRepository.save(any(Estudante.class))).thenReturn(estudante);

        Estudante updatedEstudante = estudanteService.update("1", estudanteRequest);

        assertNotNull(updatedEstudante);
        assertEquals("João", updatedEstudante.getName());
        assertEquals("Silva", updatedEstudante.getLastName());
        assertEquals("joao.silva@example.com", updatedEstudante.getEmail());
    }

    @Test
    void testDeleteEstudante() {
        when(estudanteRepository.findById(anyString())).thenReturn(Optional.of(estudante));

        assertDoesNotThrow(() -> estudanteService.delete("1"));

        verify(estudanteRepository, times(1)).delete(any(Estudante.class));
    }

    @Test
    void testDeleteEstudanteNotFound() {
        when(estudanteRepository.findById(anyString())).thenReturn(Optional.empty());

        EntidadeNaoEncontradaException thrown = assertThrows(EntidadeNaoEncontradaException.class, () -> {
            estudanteService.delete("1");
        });

        assertEquals("Estudante não encontrado.", thrown.getMessage());
    }
}
