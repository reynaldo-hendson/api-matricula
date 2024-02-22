package com.test.forleven.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.test.forleven.infra.config.utils.DateTimeUtil;
import com.test.forleven.infra.exceptions.NegocioException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "tb_students")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column
    @Size(min = 3)
    private String name;

    @Column
    @Size(min = 3)
    private String lastName;

    @Column(length = 14)
    private String cpf;

    @Column
    @Size(min = 3)
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Phone> phone = new ArrayList<>();

    @ManyToOne
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private StatusRegistrationStudent status;

    @Column(unique = true)
    private String registration;

    private LocalDateTime dateRegistration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lockingDateRegistration;

    public String generateRegistration(){
        int anoAtual = LocalDateTime.now().getYear();

        // Gerar um número aleatório de 6 dígitos
        int numeroAleatorio = generateRandomNumber();

        // Unificar o ano com o número aleatório
        return Integer.toString(anoAtual) + Integer.toString(numeroAleatorio);
    }

    private int generateRandomNumber() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }

    public void lockRegistration() {
        if (cannotBeTracked()){
            throw new NegocioException("Registration cannot be locked.");
        }
        setStatus(StatusRegistrationStudent.TRANCADA);
        String dataHoraAtualFormatada = DateTimeUtil.obterDataHoraAtualFormatada();
        setLockingDateRegistration(DateTimeUtil.converterStringParaLocalDateTime(dataHoraAtualFormatada));
    }
    public boolean canBeLocked(){
        return StatusRegistrationStudent.ATIVA.equals(getStatus());
    }
    public boolean cannotBeTracked(){
        return !canBeLocked();
    }

}
