package com.test.forleven.controller;

import com.test.forleven.model.dto.PhoneDTO;
import com.test.forleven.model.entity.Phone;
import com.test.forleven.service.PhonesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/phones")
@RequiredArgsConstructor
public class PhonesController {

    private final PhonesService phonesService;

    @PostMapping
    @Operation(summary = "Create a new Phone", description = "Create a new student and return the created student's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Phone created successfully"),
            @ApiResponse(responseCode = "400", description = "One or more parameters are incorrect, check and try again.")
    })
    public ResponseEntity<Phone> createPhone(@RequestBody PhoneDTO phonesDTO){
        Phone savedPhone = phonesService.createPhone(phonesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPhone);
    }

    @GetMapping("{student_id}/student")
    @Operation(summary = "Get phones using student ID", description = "Recover all student's registered phones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<List<Phone>> getAllPhones(@PathVariable Long student_id){
        return ResponseEntity.status(HttpStatus.OK).body(phonesService.getAllPhonesForStudent(student_id));
    }
}
