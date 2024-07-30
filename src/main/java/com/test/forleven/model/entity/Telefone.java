package com.test.forleven.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    @ManyToOne
    @JoinColumn(name = "estudante_id")
    private Estudante estudante;

    @Column(name = "data_registro")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataRegistro;
}
