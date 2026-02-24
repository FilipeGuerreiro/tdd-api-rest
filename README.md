# E-Commerce Product API

API RESTful para gestão de catálogo de produtos e processamento de pedidos de e-commerce. Este projeto foi desenvolvido com foco prático em **Test-Driven Development (TDD)** e modelagem avançada de dados utilizando **MongoDB**.

## Objetivos do Projeto
- Implementar desenvolvimento guiado por testes (Red-Green-Refactor).
- Aplicar padrões de testes (Arrange, Act, Assert).
- Modelagem de documentos e relacionamentos no MongoDB.
- Utilização de transações ACID multiobjeto.
- Construção de relatórios com Aggregation Framework.

## 🛠️ Stack Tecnológica
- **Java 21** (ou a versão que estiver utilizando)
- **Spring Boot 3**
- **Spring Data MongoDB**
- **JUnit 5 & Mockito** (Testes Unitários)
- **Testcontainers** (Testes de Integração)
- **Docker**

## 🚀 Como Executar

### Pré-requisitos
- Java JDK instalado
- Maven instalado
- Docker rodando (necessário para o Testcontainers)

### Rodando a Aplicação
```bash
mvn spring-boot:run
