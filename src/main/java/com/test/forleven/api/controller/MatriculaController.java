package com.test.forleven.api.controller;

import com.test.forleven.api.mapper.MatriculaMapper;
import com.test.forleven.api.request.MatriculaRequest;
import com.test.forleven.api.response.MatriculaResponse;
import com.test.forleven.api.response.MatriculaResumeResponse;
import com.test.forleven.model.entity.Matricula;
import com.test.forleven.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;
    private final MatriculaMapper mapper;

    @PostMapping
    @Operation(summary = "Criar uma nova matricula", description = "Gera um número de matricula para um aluno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Matricula gerada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Um ou mais campos estão errados, tente novamente."),
            @ApiResponse(responseCode = "404", description = "Cpf não válido.")})
    public ResponseEntity<MatriculaResponse> criarMatricula(@RequestBody @Valid MatriculaRequest matriculaRequest){
        Matricula novaMatricula = matriculaService.criarMatricula(matriculaRequest);
        MatriculaResponse response = mapper.matriculaToMatriculaResponse(novaMatricula);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping
    @Operation(summary = "Listar todas as matricula.", description = "Retorna todas as matriculas existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada")})
    public ResponseEntity <List<MatriculaResumeResponse>> listTodasMatriculas(){
        List<Matricula> matriculaList = matriculaService.matriculaList();
        List<MatriculaResumeResponse> matriculaResumeResponses = mapper.toMatriculaResponseList(matriculaList);
        return ResponseEntity.status(HttpStatus.OK).body(matriculaResumeResponses);
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar matriculas ativas.", description = "Retorna todas as matriculas com status 'Ativa'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada")})
    public ResponseEntity<List<MatriculaResumeResponse>> listarMatriculasAtivas(){
        List<Matricula> matriculaList = matriculaService.matriculaAtiva();
        List<MatriculaResumeResponse> matriculaResumeResponses = mapper.toMatriculaResponseList(matriculaList);
        return ResponseEntity.status(HttpStatus.OK).body(matriculaResumeResponses);
    }

    @GetMapping("/trancadas")
    @Operation(summary = "Listar matriculas trancadas.", description = "Retorna todas as matriculas com status 'Trancada'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada")})
    public ResponseEntity<List<MatriculaResumeResponse>> listarMatriculasTrancadas(){
        List<Matricula> matriculaList = matriculaService.matriculaTrancada();
        List<MatriculaResumeResponse> matriculaResumeResponses = mapper.toMatriculaResponseList(matriculaList);
        return ResponseEntity.status(HttpStatus.OK).body(matriculaResumeResponses);
    }

    @GetMapping("/{matriculaId}/buscar")
    @Operation(summary = "Buscar Matricula.", description = "Busca matricula pelo número da matricula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada"),
            @ApiResponse(responseCode = "404", description = "Matricula não existente")})
    public ResponseEntity<Optional<Matricula>> buscarPorMatricula(@PathVariable @Valid String matriculaId){
        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.findByMatricula(matriculaId));
    }

    @GetMapping("estudante/{estudanteCpf}/buscar")
    @Operation(summary = "Buscar Matricula.", description = "Busca matricula pelo CPF do aluno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada"),
            @ApiResponse(responseCode = "404", description = "Matricula não existente")})
    public ResponseEntity<Optional<Matricula>> buscarPorEstudante(@PathVariable String estudanteCpf){
        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.findByMatriculaPorCpf(estudanteCpf));
    }


    @PutMapping("/{numeroMatricula}/trancar_matricula")
    @Operation(summary = "Trancar matricula pelo número.", description = "Muda a situação da matricula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitação foi bem sucedida"),
            @ApiResponse(responseCode = "404", description = "Matricula não existente")})
    public ResponseEntity<Void> trancarMatricula(@PathVariable @Valid String numeroMatricula){
        matriculaService.trancarMatricula(numeroMatricula);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{numerMatricula}/reabrir_matricula")
    @Operation(summary = "Reabrir matricula pelo número.", description = "Muda a situação da matricula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitação foi bem sucedida"),
            @ApiResponse(responseCode = "404", description = "Matricula não existente")})
    public ResponseEntity<Void> reabrirMatricula(@PathVariable @Valid String numerMatricula){
        matriculaService.reabrirMatricula(numerMatricula);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
