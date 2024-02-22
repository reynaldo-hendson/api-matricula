# Backend Developer - Test

Projeto API Restful elaborado para teste de desenvolvedor Backend.

## Índice
- <a href="#sobreDescricao">Sobre o Desafio</a>
- <a href="#diagrama">Diagrama de classe</a>
- <a href="#funcionalidades">Funcionalidades do projeto</a>
- <a href="#demonstracao">Demonstração</a>
- <a href="#rodar">Como rodar este projeto?</a>
- <a href="#tecnologias">Tecnologias Utilizadas</a>
- <a href="#autoras">Pessoas Autoras</a>
- <a href="#proximospassos">Próximos passos</a>

<h2 id="sobreDescricao">Sobre o Desafio</h2>
Desenvolva uma API Rest para um cadastro de estudantes com intuito de consultar, criar novos registros, realizar atualizações e exclusões nestes (CRUD) utilizando um banco de dados. Em um cenário de trabalho esta API seria consumida por um desenvolvedor front-end trabalhando em par contigo.

<h3>Informações técnicas</h3>

Utilizar linguagens suportadas pela empresa que são Java ou Kotlin, preferencialmente Java que é a linguagem predominante no back-end da empresa. Banco de dados preferencialmente MySQL/MariaDB ou algum banco similar ao mesmo (Postgre, SQLite ou H2).

Escrever uma aplicação que exponha uma API Rest
Esta API deve conter ao menos um CRUD básico
Regras de negócio
Cadastrar campos nome, sobrenome e matrícula;
Todos os campos devem ser preenchidos;
Todos os campos devem conter mais de 3 caracteres;
O campo de matrícula não pode se repetir dentro da base;

<h2 id="diagrama">Diagrama de classes</h2>

```mermaid
classDiagram
 Student "1" <--> "1..*" Phone
 Student "1" <--> "1..*"Endereco 
 Student *-- StatusRegistrationStudent 

  class Student {
    -String name
    -String lastName
    -String cpf
    -String email
    -Endereco endereco
    -List<Phone> phone
    -String registration
    -StatusRegistrationStudent status
    -LocalDateTime dateRegistration
    -LocalDateTime lockingDateRegistration
  }

  class Phone {
    -String phone
    -Student student
  }

  class Endereco {
    -String cep
    -String logradouro
    -String complemento
    -String bairro
    -String localidade
    -String uf
  }

  class StatusRegistrationStudent {
    «enumeration»
    ATIVA
    TRANCADA
  }

 
```

<h2 id = "funcionalidades"> 🖥️Funcionalidades do Projeto </h2>

- [x] Cadastro de Estudante integrado a API ViaCep
- [x] Consulta de Estudante 
- [x] Consulta de Estudante por número de matrícula
- [x] Consulta de Estudante por status da matrícula
- [x] Exclusão do Estudante
- [x] Atualização de Estudante
- [x] Cadastro de Telefone
- [x] Buscar Telefones do Estudante por id.

## 📺Layout
![endpoints](https://github.com/reynaldo-hendson/Teste_Desenvolvedor_Backend/assets/80369346/816e046f-4641-4b59-9df6-a4ece0742131)

<h2 id="demonstracao"> Demonstração </h2>

[Projeto](endereço da aplicação)


<h2 id="rodar">💿Como Rodar este projeto</h2>

```bash
# Clone o repositório 
$ git clone https://github.com/reynaldo-hendson/Teste_Desenvolvedor_Backend.git

# Acesse a pasta do projeto
$ cd Teste_Desenvolvedor_Backend

# Instale as dependências
$ mvn clean install

# Execute o projeto
Abra o projeto na IDE de sua preferência.

# Configurações Adicionais:
Se o projeto usa um banco de dados, certifique-se de configurar as informações de conexão no arquivo de configuração.

Consulte o arquivo application.properties (ou application.yml) para configurar outras propriedades da aplicação, como porta, URL da base de dados, é os ambientes.

```
<h2 id="tecnologias">🛠️Tecnologias Utilizadas</h2>

1. [Java 17](https://www.java.com/pt-BR/)
2. [Spring Boot 3](https://spring.io/projects/spring-boot)
3. [OpenAPI (Swagger)](https://springdoc.org/)
4. [Spring Cloud OpenFeign](https://docs.spring.io/spring-cloud-openfeign/docs/current/reference/html/)

<h2 id="autoras">👤Pessoas Autoras</h2>

<img src="https://avatars.githubusercontent.com/u/80369346?v=4" alt="imagem do desenvolvedor"></img>

[Linkedin](https://www.linkedin.com/in/reynaldo-hendson/)

<h2 id="proximospassos"> 🖊️Próximos passos </h2>

- [] Implementar camada de segurança.
- [] Implementar notificação via email
- [] Implementar Frontend

