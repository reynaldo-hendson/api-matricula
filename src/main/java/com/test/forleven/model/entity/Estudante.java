package com.test.forleven.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_students")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Estudante {

    @Id
    @Column(length = 11,name = "estudante_id")
    private String cpf;

    @Column
    @Size(min = 3)
    private String name;

    @Column(name = "last_name")
    @Size(min = 3)
    private String lastName;

    @Column
    @Size(min = 3)
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @OneToMany(mappedBy = "estudante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Telefone> telefones = new ArrayList<>();

    @ManyToOne
    private Endereco endereco;

    @Column(name = "data_registro")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataRegistro;

}
