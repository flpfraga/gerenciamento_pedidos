# Sistema de Gerenciamento de Pedidos

## Sobre o Projeto

Este é um sistema completo para gerenciamento de pedidos desenvolvido em Java com Spring Boot. O sistema permite o cadastro de clientes, produtos e gerenciamento de pedidos, com funcionalidades estatísticas para análise de vendas.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Security com JWT
- Spring Data MySQL
- Lombok
- Swagger/OpenAPI para documentação da API
- Maven

## Estrutura do Projeto

O projeto está organizado em camadas seguindo os princípios de arquitetura limpa:

- **Controller**: Responsável por receber as requisições HTTP
- **Service**: Implementa a lógica de negócio
- **Repository**: Gerencia o acesso aos dados
- **Domain**: Contém as entidades do sistema
- **Security**: Contém configurações e implementações de segurança

## Funcionalidades Principais

### Módulo de Cliente
- Cadastro de clientes
- Atualização de dados do cliente

### Módulo de Produto
- Cadastro de produtos
- Listagem de produtos
- Busca por produto específico
- Atualização de produtos
- Remoção de produtos

### Módulo de Pedido
- Criação de pedidos
- Processamento de pagamentos
- Busca de pedidos por cliente
- Busca de pedido por ID

### Módulo de Estatísticas
- Top 5 clientes que mais compraram
- Ticket médio por cliente
- Faturamento mensal

### Módulo de Segurança
- Autenticação via JWT
- Registro de usuários
- Controle de acesso baseado em roles (ROLE_USER, ROLE_ADMIN)

## Requisitos

- Java 17 ou superior
- MongoDB
- Maven

## Como Executar

1. Clone o repositório
   ```
   git clone [https://github.com/flpfraga/gerenciamento_pedidos]
   ```

2. Configure o MongoDB no arquivo `application.properties`

3. Execute a aplicação
   ```
   mvn spring-boot:run
   ```

4. A aplicação estará disponível em `http://localhost:8080`

5. A documentação Swagger estará disponível em `http://localhost:8080/swagger-ui.html`

## Endpoints da API

### Autenticação
- **POST /api/v1/auth/login**: Autenticar usuário
- **POST /api/v1/auth/register**: Registrar novo usuário
- **GET /api/v1/auth/roles**: Listar roles disponíveis

### Cliente
- **POST /api/v1/cliente/cadastrar**: Cadastrar novo cliente
- **PUT /api/v1/cliente**: Atualizar dados do cliente

### Produto
- **POST /api/v1/produto/cadastrar**: Cadastrar novo produto
- **PUT /api/v1/produto/atualizar/{id}**: Atualizar produto
- **GET /api/v1/produto/{id}**: Buscar produto por ID
- **GET /api/v1/produto/produtos**: Listar todos os produtos
- **DELETE /api/v1/produto/{id}**: Remover produto

### Pedido
- **POST /api/v1/pedido/cadastrar**: Cadastrar novo pedido
- **PATCH /api/v1/pedido/realizar_pagamento/{id}**: Realizar pagamento
- **GET /api/v1/pedido/todos_por_cliente/**: Buscar pedidos do cliente
- **GET /api/v1/pedido/id/{id}**: Buscar pedido por ID

### Estatísticas
- **GET /api/v1/estatisticas/top_5-clientes**: Top 5 clientes em compras
- **GET /api/v1/estatisticas/ticket_medio_cliente/{id}**: Ticket médio por cliente
- **GET /api/v1/estatisticas/faturamento_mes?ano=XXXX&mes=XX**: Faturamento mensal

## Autenticação

O sistema utiliza autenticação baseada em token JWT. Para acessar endpoints protegidos:

1. Faça login ou registro para obter um token
2. Inclua o token no cabeçalho das requisições:
   ```
   Authorization: Bearer [SEU_TOKEN]
   ```

## Permissões

- **ROLE_USER**: Permite acesso aos endpoints de cliente e pedido
- **ROLE_ADMIN**: Permite acesso a todos os endpoints, incluindo produtos e estatísticas

## Exemplos de Uso

### Registrar um novo usuário
```
POST /api/v1/auth/register
{
  "nome": "Exemplo",
  "email": "exemplo@email.com",
  "senha": "senha123",
  "role": "ROLE_USER"
}
```

### Login
```
POST /api/v1/auth/login
{
  "email": "exemplo@email.com",
  "senha": "senha123"
}
```

### Cadastrar um cliente
```
POST /api/v1/cliente/cadastrar
{
  "nome": "Cliente Exemplo",
  "cpf": "123.456.789-00",
  "email": "cliente@email.com",
  "telefone": "(11) 99999-9999",
  "endereco": {
    "rua": "Rua Exemplo",
    "numero": "123",
    "bairro": "Bairro",
    "cidade": "Cidade",
    "estado": "Estado",
    "cep": "12345-678"
  }
}
```

### Criar um pedido
```
POST /api/v1/pedido/cadastrar
{
  "produtoId1": 2,
  "produtoId2": 1
}
```

## Contribuição

Contribuições são bem-vindas! Por favor, sinta-se à vontade para enviar um Pull Request.

## Licença

Este projeto está licenciado sob a licença MIT. 