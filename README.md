# 🛒 E-commerce & Marketplace API

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7.2-red.svg)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)

Uma API RESTful completa e robusta para gerenciamento de um ecossistema de E-commerce e Marketplace. Este projeto foi desenvolvido com foco em boas práticas de engenharia de software, segurança avançada e performance.

## 🚀 Principais Funcionalidades

- **Gestão de Usuários & Autenticação:** Registro seguro, autenticação JWT (JSON Web Tokens) stateless, sistema de Refresh Tokens e revogação imediata (Blacklist em Redis).
- **Catálogo de Produtos:** Gerenciamento de categorias, produtos e imagens. Filtragem dinâmica e busca avançada usando Criteria API (Specifications).
- **Carrinho de Compras:** Gestão do carrinho de usuário com controle de itens, quantidades e cálculos de subtotais.
- **Processamento de Pedidos:** Geração de pedidos a partir de carrinhos, gestão de status do pedido e vinculação de endereços de entrega.
- **Segurança:** Senhas criptografadas (BCrypt 12), proteção de rotas por perfis de acesso (Roles ADMIN/USER), CORS configurado.

## 🛠️ Stack Tecnológica

- **Linguagem:** Java 21 (Records para DTOs imutáveis)
- **Framework:** Spring Boot (Web, Data JPA, Security, Validation)
- **Banco de Dados Relacional:** PostgreSQL
- **Cache & Key-Value Store:** Redis
- **Segurança:** Spring Security, Auth0 Java JWT
- **Documentação:** SpringDoc OpenAPI 3 (Swagger UI)
- **Infraestrutura:** Docker & Docker Compose
- **Ferramentas:** Maven, Lombok

## 🏗️ Arquitetura do Sistema

A API segue o modelo de arquitetura em camadas (Layered Architecture):

1.  **Controllers:** Exposição de endpoints RESTful e documentação via Swagger.
2.  **Services:** Isolamento da lógica de negócios e regras do domínio.
3.  **Repositories:** Interface com o banco de dados utilizando Spring Data JPA e Specifications.
4.  **Security/Config:** Configurações globais de segurança, filtros de requisição JWT, CORS e integração com Redis.

**Fluxo de Autenticação (JWT Blacklist):**
Para garantir segurança máxima em sessões stateless, os tokens possuem tempo de vida configurado e, em caso de logout, o token atual é movido para uma Blacklist no Redis. O `JwtFilter` intercepta todas as requisições autenticadas e verifica no Redis (busca O(1)) se o token foi revogado.

## ⚙️ Pré-requisitos

Para rodar o projeto localmente, você precisará de:

- [Java JDK 21](https://jdk.java.net/21/)
- [Docker](https://docs.docker.com/get-docker/) e Docker Compose (para os serviços de banco de dados)
- [Maven](https://maven.apache.org/install.html) (ou utilizar o wrapper `./mvnw` incluso no projeto)

## 📦 Como Executar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/marketplace-test.git
   cd marketplace-test
   ```

2. **Configure as Variáveis de Ambiente:**
   Copie o arquivo de exemplo e crie o seu `.env`:
   ```bash
   cp .env.example .env
   ```
   *Preencha o arquivo `.env` com suas credenciais para o banco de dados.*

3. **Inicie a Infraestrutura (PostgreSQL & Redis):**
   *(Nota: Atualmente o docker-compose provisiona o banco de dados. O Redis precisará ser instanciado ou o compose atualizado)*
   ```bash
   docker-compose up -d
   ```

4. **Execute a Aplicação Spring Boot:**
   Utilize o Maven Wrapper para iniciar a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

5. **Acesse a Documentação (Swagger UI):**
   Com a aplicação rodando, acesse a documentação interativa da API em:
   👉 `http://localhost:8080/docs` (ou `/swagger-ui.html`)

## 📡 Endpoints Principais

A API é versionada sob a rota `/api/v1/`.

| Recurso | Rota Base | Autenticação | Descrição |
| :--- | :--- | :--- | :--- |
| **Auth** | `/auth` | Pública | Login, registro, refresh token e logout. |
| **Products** | `/products` | Pública (GET) / Admin (POST, PUT, DEL) | Gestão e busca do catálogo. |
| **Categories** | `/categories` | Pública (GET) / Admin (POST, PUT, DEL) | Organização dos produtos. |
| **Cart** | `/cart` | Requerida | Gestão do carrinho de compras do usuário. |
| **Orders** | `/orders` | Requerida | Criação e acompanhamento de pedidos. |
| **Users** | `/users` | Requerida | Gestão de perfis e endereços de entrega. |

## 📊 Métricas do Projeto

- **~2.800+ Linhas de Código (LOC)** distribuídas em uma arquitetura limpa.
- **23 DTOs** (utilizando Java Records para imutabilidade).
- **10 Entidades de Domínio** gerenciadas via JPA/Hibernate.
- **10 Serviços** concentrando as regras de negócio de e-commerce.

---

*Desenvolvido como projeto de portfólio (PBL - Project-Based Learning) focado em soluções backend reais e escaláveis.*
