package com.test.forleven.api.controller;

import com.test.forleven.api.mapper.TelefoneResponseMapper;
import com.test.forleven.api.mapper.TelefoneResumeResponseMapper;
import com.test.forleven.api.request.TelefoneRequest;
import com.test.forleven.api.response.TelefoneResponse;
import com.test.forleven.api.response.TelefoneResumeResponse;
import com.test.forleven.model.entity.Telefone;
import com.test.forleven.service.TelefoneService;
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
@RequestMapping("/api/telefones")
@RequiredArgsConstructor
public class TelefoneController {

    private final TelefoneService telefoneService;
    private final TelefoneResponseMapper telefoneResponseMapper;
    private final TelefoneResumeResponseMapper telefoneResumeResponseMapper;

    @PostMapping
    @Operation(summary = "Create a new Telefone", description = "Create a new student and return the created student's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Telefone criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Um ou mais parâmetros estão incorretos, tente novamente.")
    })
    public ResponseEntity<TelefoneResponse> createPhone(@RequestBody TelefoneRequest request){
        var novoTelefone = telefoneService.createPhone(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(telefoneResponseMapper.toTelefoneResponse(novoTelefone));
    }

    @GetMapping("/{estudanteId}/estudante")
    @Operation(summary = "Lista todos os telefones do aluno.", description = "Retorna todos os telefones do aluno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada."),
            @ApiResponse(responseCode = "404", description = "Estudante não encontrado.")
    })
    public ResponseEntity<List<TelefoneResumeResponse>> buscarTelefones(@PathVariable String estudanteId){
        List<Telefone> telefones = telefoneService.buscarTelefonesEstudante(estudanteId);
        List<TelefoneResumeResponse> telefonesResponsesList = telefoneResumeResponseMapper.toTelefoneResumeResponseList(telefones);
        return ResponseEntity.status(HttpStatus.OK).body(telefonesResponsesList);
    }

    @GetMapping("/{numeroTelefone}/contato")
    @Operation(summary = "Retorna telefone pelo número.", description = "Retorna o telefone com os dados do aluno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada."),
            @ApiResponse(responseCode = "404", description = "Telefone não encontrado.")
    })
    public ResponseEntity<TelefoneResumeResponse>buscarTelefone(@PathVariable String numeroTelefone){
        Optional<Telefone> telefoneOptional = telefoneService.buscarTelefone(numeroTelefone);
        return telefoneOptional
                .map(telefone -> ResponseEntity.ok(telefoneResumeResponseMapper.toTelefoneResumeResponse(telefone)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    @Operation(summary = "Lista todos os telefones.", description = "Retorna todos os telefones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operação realizada.")
    })
    public ResponseEntity<List<TelefoneResumeResponse>> buscarTodosTelefones(){
        List <Telefone> telefoneList = telefoneService.buscarTodosTelefones();
        List<TelefoneResumeResponse> telefoneResumeResponseList = telefoneResumeResponseMapper.toTelefoneResumeResponseList(telefoneList);
        return ResponseEntity.status(HttpStatus.OK).body(telefoneResumeResponseList);
    }

    @DeleteMapping("/{estudantId}/aluno")
    @Operation(summary = "Remove todos os telefones do aluno", description = "Apaga todos os contatos do aluno.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitação bem sucedida."),
            @ApiResponse(responseCode = "404", description = "Estudante não encontrado.")
    })
    public ResponseEntity<Void> deleteTelefonesByEstudanteId(@PathVariable Long estudantId) {
        telefoneService.deleteTelefonesByEstudanteId(String.valueOf(estudantId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{telefoneId}/remover")
    @Operation(summary = "Remove o telefone pelo Id", description = "Remove o contato pelo Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solicitação bem sucedida."),
            @ApiResponse(responseCode = "404", description = "Estudante não encontrado.")
    })
    public ResponseEntity<Void> deleteTelefones(@PathVariable Long telefoneId) {
        telefoneService.deleteTelefone(telefoneId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
