package com.test.forleven.api.controller;

import com.test.forleven.model.dto.MatriculaDTO;
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

    @PostMapping
    @Operation(summary = "Cria uma nova matricula", description = "Cria uma nova matricula e retorna os dados criados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Matricula gerada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Um ou mais campos estão errados, tente novamente."),
            @ApiResponse(responseCode = "404", description = "Cpf não válido..")})
    public ResponseEntity<Matricula> criarMatricula(@RequestBody @Valid MatriculaDTO matriculaDto){
        Matricula newMatricula = matriculaService.criarMatricula(matriculaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMatricula);

    }

    @GetMapping
    @Operation(summary = "Lista todas as matricula", description = "Retorna todas as matriculas existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada")})
    public ResponseEntity <List<Matricula>> listTodasMatriculas(){
        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.matriculaList());
    }

    @GetMapping("/ativas")
    @Operation(summary = "Lista todas as matricula ativas.", description = "Retorna todas as matriculas ativas existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada")})
    public ResponseEntity<List<Matricula>> listarMatriculasAtivas(){
        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.matriculaAtiva());
    }

    @GetMapping("/trancadas")
    @Operation(summary = "Lista todas as matricula trancadas.", description = "Retorna todas as matriculas trancadsa existentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada")})
    public ResponseEntity<List<Matricula>> listarMatriculasTrancadas(){
        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.matriculaTrancada());
    }

    @GetMapping("/{matriculaId}/buscar")
    @Operation(summary = "Mostra matricula pelo número.", description = "Retorna dados da matricula.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada"),
            @ApiResponse(responseCode = "404", description = "Matricula não existente")})
    public ResponseEntity<Optional<Matricula>> buscarPorMatricula(@PathVariable @Valid String matriculaId){
        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.findByMatricula(matriculaId));
    }

//    @GetMapping("estudante/{estudanteId}/buscar")
//    @Operation(summary = "Mostra matricula pelo número.", description = "Retorna dados da matricula.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Operação realizada"),
//            @ApiResponse(responseCode = "404", description = "Matricula não existente")})
//    public ResponseEntity< List<Matricula>> buscarPorEstudante(@PathVariable String estudanteId){
//        return ResponseEntity.status(HttpStatus.OK).body(matriculaService.resumeMatriculas(estudanteId));
//    }


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
