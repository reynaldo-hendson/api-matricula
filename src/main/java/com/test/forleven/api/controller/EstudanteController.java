package com.test.forleven.api.controller;

import com.test.forleven.api.mapper.EstudanteResponseMapper;
import com.test.forleven.api.mapper.EstudanteResumeResponseMapper;
import com.test.forleven.api.request.EstudanteRequest;
import com.test.forleven.api.response.EstudanteResponse;
import com.test.forleven.api.response.EstudanteResumeResponse;
import com.test.forleven.model.entity.Estudante;
import com.test.forleven.service.EstudanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/estudantes")
@RequiredArgsConstructor
public class EstudanteController {

    private final EstudanteService estudanteService;
    private final EstudanteResponseMapper estudanteResponseMapper;
    private final EstudanteResumeResponseMapper estudanteResumeResponseMapper;

    @PostMapping
    @Operation(summary = "Cria novo Estudante.", description = "Crie um novo aluno e retorna os dados do aluno criado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro do estudante criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Um ou mais parâmetros estão incorretos, verifique e tente novamente.")})
    public ResponseEntity<EstudanteResponse> createStudent(@RequestBody EstudanteRequest estudanteRequest) {
        var estudanteNovo = estudanteService.save(estudanteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(estudanteResponseMapper.toEstudanteResponse(estudanteNovo));
    }

    @Operation(summary = "Atualiza dados do Estudante.", description = "Atualiza os dados do aluno e retorna os dados do aluno atualizados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro do estudante criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Um ou mais parâmetros estão incorretos, verifique e tente novamente."),
            @ApiResponse(responseCode = "404", description = "Registro de estudante não encontrado.")
    })
    @PutMapping("/{id}/atualizar")
    public ResponseEntity<EstudanteResponse> updateStudent(@PathVariable String id, @RequestBody EstudanteRequest estudanteRequest) {
        var estudanteAtualizado = estudanteService.update(id, estudanteRequest);
        return ResponseEntity.ok(estudanteResponseMapper.toEstudanteResponse(estudanteAtualizado));
    }

    @GetMapping
    @Operation(summary = "Busca a lista com todos os estudantes.", description = "Recuperar uma lista de todos os alunos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada.")})
    public ResponseEntity<List<EstudanteResumeResponse>> getAllStudent() {
        List<Estudante> estudanteList = estudanteService.getAllStudent();
        List<EstudanteResumeResponse> estudanteResponses = estudanteResumeResponseMapper.toEstudanteResumeResponseList(estudanteList);
        return ResponseEntity.status(HttpStatus.OK).body(estudanteResponses);
    }


    @GetMapping("/{estudanteId}/buscar/")
    @Operation(summary = "Busca estudante pelo cpf.", description = "Mostra os dados do estudante com o cpf requisitado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada."),
            @ApiResponse(responseCode = "404", description = "Registro de estudante não encontrado.")})
    public ResponseEntity<Optional<Estudante>> buscarPorCpf(@PathVariable String estudanteId) {
        return ResponseEntity.status(HttpStatus.OK).body(estudanteService.findById(estudanteId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Apaga o registro do estudante.", description = "Exclui um aluno existente com base em seu cpf.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro do estudante apagado."),
            @ApiResponse(responseCode = "404", description = "Registro de estudante não encontrado.")})
    public ResponseEntity<Void> deleteStudent(@PathVariable ("id") String id){
        estudanteService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
