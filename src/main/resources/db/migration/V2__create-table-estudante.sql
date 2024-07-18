CREATE TABLE tb_students (
  estudante_id VARCHAR(11) NOT NULL,
   name VARCHAR(255),
   last_name VARCHAR(255),
   email VARCHAR(255),
   endereco_cep VARCHAR(255),
   data_registro TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_tb_students PRIMARY KEY (estudante_id)
);

ALTER TABLE tb_students ADD CONSTRAINT FK_TB_STUDENT
