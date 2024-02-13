package com.test.forleven.controller;

import com.test.forleven.model.dto.StudentDTO;
import com.test.forleven.model.entity.Student;
import com.test.forleven.service.StudentService;
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
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    @PostMapping
    @Operation(summary = "Create a new Student", description = "Create a new student and return the created student's data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse(responseCode = "400", description = "One or more parameters are incorrect, check and try again.")})
    public ResponseEntity<Student> createStudent(@RequestBody @Valid StudentDTO student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.save(student));
    }
    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all registered students")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")})
    public ResponseEntity<List<Student>> getAllStudent() {
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudent());
    }

    @GetMapping("/active")
    @Operation(summary = "Get all students with registration Active", description = "Retrieve a list of all registered students active")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")})
    public ResponseEntity<List<Student>> getActiveStudents(){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getActiveStudents());
    }

    @GetMapping("/locked")
    @Operation(summary = "Get all students with locked enrollment", description = "Retrieve a list of all registered students lock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful")})
    public ResponseEntity<List<Student>> getLockStudents(){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getLockStudents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a student by ID", description = "Retrieve a specific student based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    public ResponseEntity<Student> findById(@PathVariable Long id){
        Optional<Student> optStudent = studentService.findById(id);

        return optStudent.map(student -> ResponseEntity.status(HttpStatus.OK).body(student))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{registration}/search")
    @Operation(summary = "Get a student by registration", description = "Retrieve a specific student based on its registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    public ResponseEntity<Optional<Student>> findStudentRegistration(@PathVariable String registration){
        Optional<Student> optionalStudent = studentService.findByRegistration(registration);
        return ResponseEntity.status(HttpStatus.OK).body(optionalStudent);
    }

    @PutMapping("/lock_registration/{studentRegistration}/")
    @Operation(summary = "Lock student registration", description = "Retrieve a specific student based on its registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operation successful"),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    public ResponseEntity<Void> lockRegistration(@PathVariable String studentRegistration){
        studentService.lockRegistration(studentRegistration);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student", description = "Delete an existing student based on its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Student not found")})
    public ResponseEntity<Void> deleteStudent(@PathVariable ("id") Long id){
        studentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
