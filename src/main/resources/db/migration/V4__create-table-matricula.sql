CREATE TABLE tb_matricula (
  matricula VARCHAR(255) NOT NULL,
   estudante_estudante_id VARCHAR(11),
   status_matricula VARCHAR(255),
   data_registro_matricula TIMESTAMP WITHOUT TIME ZONE,
   data_trancamento_matricula TIMESTAMP WITHOUT TIME ZONE,
   data_reabertura_matricula TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_tb_matricula PRIMARY KEY (matricula)
);

ALTER TABLE tb_matricula ADD CONSTRAINT FK_TB_MATRICULA_ON_ESTUDANTE_ESTUDANTE FOREIGN KEY (estudante_estudante_id) REFERENCES tb_students (estudante_id);