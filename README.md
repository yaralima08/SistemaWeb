# Sistema Web de Compra e Venda de Roupas

## Sobre o Projeto

O Sistema Web de Compra e Venda de Roupas é uma aplicação desenvolvida para simular um ambiente de comércio eletrônico de vestuário.

O sistema permite que vendedores cadastrem roupas para venda e que clientes realizem pedidos desses produtos. Além disso, disponibiliza funcionalidades administrativas para gerenciamento de clientes, vendedores, roupas e pedidos.

O objetivo do projeto é aplicar conceitos de desenvolvimento Web utilizando a arquitetura MVC e persistência de dados com JPA.

## Funcionalidades

### Clientes

* Cadastro de clientes
* Edição de clientes
* Exclusão de clientes
* Listagem de clientes

### Vendedores

* Cadastro de vendedores
* Edição de vendedores
* Exclusão de vendedores
* Listagem de vendedores

### Roupas

* Cadastro de roupas
* Edição de roupas
* Exclusão de roupas
* Listagem de roupas

### Pedidos

* Cadastro de pedidos
* Edição de pedidos
* Exclusão de pedidos
* Listagem de pedidos

## Tecnologias Utilizadas

* Java 17
* Spring Boot
* Spring MVC
* Spring Data JPA
* Thymeleaf
* Hibernate
* Maven
* H2 Database

## Modelo de Dados

O sistema possui quatro entidades principais:

### Cliente

Representa os compradores cadastrados no sistema.

### Vendedor

Representa as lojas ou vendedores responsáveis pela venda das roupas.

### Roupa

Representa os produtos disponíveis para compra.

### Pedido

Representa a relação entre clientes e roupas adquiridas.

## Banco de Dados

Banco utilizado: H2 Database

Console do banco:

http://localhost:8080/h2-console

Configurações:

* JDBC URL: jdbc:h2:mem:compravendadb
* Usuário: sa
* Senha: (vazia)

## Como Executar

1. Clonar o repositório:

git clone URL_DO_REPOSITORIO

2. Entrar na pasta do projeto:

cd SistemaDeRoupas

3. Executar:

mvn spring-boot:run

4. Abrir no navegador:

http://localhost:8080

## Arquitetura

O projeto foi desenvolvido seguindo o padrão MVC (Model-View-Controller):

* Model: entidades JPA
* View: páginas HTML utilizando Thymeleaf
* Controller: controle das requisições HTTP
* Service: regras de negócio
* Repository: acesso aos dados

## Autor

Yara  Vieira de Lima

Projeto desenvolvido para a disciplina de Desenvolvimento Web.
