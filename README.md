
# API de Gerenciamento de Eventos

Este projeto é uma API REST para gerenciamento de eventos (como conferências, seminários, workshops) que permite operações CRUD sobre os eventos, além de gerenciar usuários e suas permissões usando Spring Security e JWT para autenticação e autorização.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.0**
  - Spring Web
  - Spring Data JPA
  - Spring Security
- **JWT (JSON Web Token)**
- **H2 Database**
- **Springdoc OpenAPI 2.0.2 (Swagger UI)**
- **Maven**

## Funcionalidades

1. **Gerenciamento de Eventos**:
   - Cadastro, leitura, atualização e remoção de eventos.
   - Cada evento tem um nome, data, local e capacidade máxima de participantes.

2. **Gerenciamento de Usuários**:
   - Cadastro de usuários com os papéis: ADMIN e USER.
   - Usuários com papel ADMIN podem criar, atualizar e deletar eventos.
   - Usuários com papel USER podem apenas ler informações sobre os eventos.

3. **Autenticação e Autorização**:
   - Autenticação baseada em tokens JWT.
   - Utilização do Spring Security para restringir o acesso às operações baseadas nos papéis dos usuários.

4. **Inscrição em Eventos**:
   - Usuários (USER) podem se inscrever em eventos, desde que haja vagas disponíveis.
   - Não permite a inscrição de um usuário em um evento já lotado.

## Configuração e Execução

### Pré-requisitos

- Java 17
- Maven

### Passos para Executar

1. Clone o repositório:

   \`\`\`sh
   git clone https://github.com/RafValle/desafio-evento.git
   cd evento
   \`\`\`

2. Configure o arquivo `application.properties` se necessário. Por padrão, ele está configurado para usar o banco de dados H2 em memória.

3. Compile e execute o projeto:

   \`\`\`sh
   mvn clean install
   mvn spring-boot:run
   \`\`\`

4. A API estará disponível em \`http://localhost:8080\`.

### Endpoints Importantes

- **Swagger UI**: \`http://localhost:8080/swagger-ui/index.html\`
- **Documentação da API**: \`http://localhost:8080/v3/api-docs\`

### Autenticação

Use o endpoint \`/auth/login\` para obter um token JWT.

#### Usuário Padrão para Autenticação Inicial

- **Username**: \`username\`
- **Password**: \`password\`

### Exemplos de Requisições

#### Obter Token JWT

\`\`\`sh
curl --location --request POST 'http://localhost:8080/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "username",
    "password": "password"
}'
\`\`\`

#### Criar Evento (ADMIN)

\`\`\`sh
curl --location --request POST 'http://localhost:8080/events' \
--header 'Authorization: Bearer <TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Conference",
    "date": "2024-06-15",
    "location": "Conference Hall",
    "maxParticipants": 100
}'
\`\`\`

#### Listar Eventos (ADMIN e USER)

\`\`\`sh
curl --location --request GET 'http://localhost:8080/events' \
--header 'Authorization: Bearer <TOKEN>'
\`\`\`

#### Atualizar Evento (ADMIN)

\`\`\`sh
curl --location --request PUT 'http://localhost:8080/events/{eventId}' \
--header 'Authorization: Bearer <TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Updated Conference",
    "date": "2024-06-20",
    "location": "Main Hall",
    "maxParticipants": 150
}'
\`\`\`

#### Deletar Evento (ADMIN)

\`\`\`sh
curl --location --request DELETE 'http://localhost:8080/events/{eventId}' \
--header 'Authorization: Bearer <TOKEN>'
\`\`\`

#### Inscrever-se em Evento (USER)

\`\`\`sh
curl --location --request POST 'http://localhost:8080/events/{eventId}/register' \
--header 'Authorization: Bearer <TOKEN>' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "username"
}'
\`\`\`

## Estrutura do Projeto

\`\`\`plaintext
src/
└── main/
    ├── java/
    │   └── com/
    │       └── desafio/
    │           └── evento/
    │               ├── config/
    │               │   ├── OpenApiConfig.java
    │               │   ├── SecurityConfig.java
    │               ├── controller/
    │               │   ├── AuthController.java
    │               │   ├── EventController.java
    │               │   ├── UserController.java
    │               ├── dto/
    │               │   ├── EventRequest.java
    │               │   ├── EventResponse.java
    │               │   ├── RegisterRequest.java
    │               │   ├── UserDTO.java
    │               ├── exception/
    │               │   ├── CustomException.java
    │               │   ├── GlobalExceptionHandler.java
    │               ├── model/
    │               │   ├── Event.java
    │               │   ├── User.java
    │               │   ├── UserRole.java
    │               ├── repository/
    │               │   ├── EventRepository.java
    │               │   ├── UserRepository.java
    │               ├── security/
    │               │   ├── JwtRequestFilter.java
    │               │   ├── JwtService.java
    │               │   ├── UserDetailsServiceImpl.java
    │               ├── service/
    │               │   ├── EventService.java
    │               │   ├── UserService.java
    │               └── EventoApplication.java
    └── resources/
        ├── application.properties
        └── application.yml
\`\`\`

## Contribuição

Sinta-se à vontade para contribuir com este projeto. Faça um fork do repositório, crie uma branch para sua funcionalidade ou correção de bug, e envie um pull request.

## Licença

Este projeto é licenciado sob os termos da licença MIT.

---

Este README fornece uma visão geral abrangente do projeto, incluindo a descrição, tecnologias utilizadas, configuração e execução, endpoints importantes e estrutura do projeto. Além disso, foram fornecidos exemplos de requisições para facilitar os testes.
