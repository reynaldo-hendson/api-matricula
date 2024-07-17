CREATE TABLE tb_phones (
  id BIGINT NOT NULL,
   phone VARCHAR(255) NOT NULL,
   estudante_id VARCHAR(11),
   data_registro TIMESTAMP WITHOUT TIME ZONE,
   CONSTRAINT pk_tb_phones PRIMARY KEY (id)
);

ALTER TABLE tb_phones ADD CONSTRAINT FK_TB_PHONES_ON_ESTUDANTE FOREIGN KEY (estudante_id) REFERENCES tb_students (estudante_id);