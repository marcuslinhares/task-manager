# 📋 Task Manager — FullStack Portfolio

> Aplicação full stack de gerenciamento de tarefas com autenticação JWT, CRUD completo, filtros inteligentes e deploy containerizado. Stack: Spring Boot + React + PostgreSQL.

![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-6DB33F?logo=springboot)
![React](https://img.shields.io/badge/React-18-61DAFB?logo=react)
![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178C6?logo=typescript)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker)

---

## 📋 Índice

- [Sobre](#-sobre)
- [Funcionalidades](#-funcionalidades)
- [Tech Stack](#-tech-stack)
- [Arquitetura](#-arquitetura)
- [Como Rodar](#-como-rodar)
- [API](#-api)
- [Testes](#-testes)
- [CI/CD](#-cicd)

---

## 🎯 Sobre

O **Task Manager** é uma aplicação full stack de gerenciamento de tarefas que demonstra boas práticas de desenvolvimento com **Spring Boot 3** no backend e **React 18** no frontend.

Diferenciais técnicos:
- 🔐 **Autenticação JWT** com Spring Security
- 🎯 **Filtros por status e prioridade** com React Query
- 🔄 **Drag and drop** para reordenação de tarefas
- 📡 **Notificações em tempo real** via WebSocket
- 🐳 **Containerizado** com Docker Compose
- 📚 **API documentada** com Swagger/OpenAPI

---

## ✨ Funcionalidades

| Funcionalidade | Descrição |
|---|---|
| ✅ **CRUD Completo** | Criar, listar, editar e deletar tarefas |
| 🔐 **Autenticação JWT** | Registro e login com tokens seguros |
| 🔍 **Busca e Filtros** | Por status (pendente/em andamento/concluída) e prioridade |
| 🔄 **Drag & Drop** | Reordenação visual com arrastar e soltar |
| 📡 **Tempo Real** | Notificações via WebSocket |
| 📚 **Swagger/OpenAPI** | Documentação interativa da API |

---

## 🛠️ Tech Stack

### Backend

| Tecnologia | Função |
|---|---|
| **Java 17** | Runtime |
| **Spring Boot 3** | Framework web |
| **Spring Data JPA** | Persistência e ORM |
| **Spring Security** | Autenticação e autorização JWT |
| **PostgreSQL** | Banco de dados relacional |
| **Maven** | Gerenciamento de dependências |
| **JUnit 5 + Mockito** | Testes unitários e de integração |

### Frontend

| Tecnologia | Função |
|---|---|
| **React 18** | UI components |
| **TypeScript** | Tipagem estática |
| **Vite** | Build tool e dev server |
| **Tailwind CSS** | Estilização utilitária |
| **Axios** | HTTP client |
| **React Query** | Cache e estado assíncrono |

### DevOps

| Tecnologia | Função |
|---|---|
| **Docker + Compose** | Containerização |
| **GitHub Actions** | CI/CD pipeline |
| **Swagger/OpenAPI** | Documentação da API |

---

## 🏗️ Arquitetura

```
task-manager/
├── backend/                          # Spring Boot 3 API
│   ├── src/main/java/com/marcus/taskmanager/
│   │   ├── controller/               # REST Controllers
│   │   ├── service/                  # Business Logic
│   │   ├── repository/               # JPA Repositories
│   │   ├── model/                    # Entities + DTOs
│   │   ├── config/                   # Security, Swagger, CORS
│   │   └── exception/                # Global Exception Handler
│   ├── src/test/                     # Testes (JUnit + Mockito)
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                         # React 18 + Vite
│   ├── src/
│   │   ├── components/               # React Components
│   │   ├── hooks/                    # Custom Hooks
│   │   ├── services/                 # API Calls (Axios)
│   │   ├── types/                    # TypeScript Types
│   │   └── pages/                    # Páginas
│   ├── Dockerfile
│   └── package.json
├── docker-compose.yml                # Orquestração (api + client + db)
└── .github/workflows/ci.yml          # CI/CD pipeline
```

### Fluxo de Autenticação

```
Client → POST /api/auth/login → JWT Token → Bearer Auth → Protected Routes
```

A autenticação é stateless via **JWT**. O token é gerado no login/registro e enviado no header `Authorization: Bearer <token>` para rotas protegidas.

---

## 🚀 Como Rodar

### Docker (recomendado)

```bash
git clone https://github.com/marcuslinhares/task-manager.git
cd task-manager
docker compose up -d

# Frontend: http://localhost:5173
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Backend (standalone)

```bash
cd backend
./mvnw spring-boot:run
# API em http://localhost:8080
```

### Frontend (standalone)

```bash
cd frontend
npm install
npm run dev
# UI em http://localhost:5173
```

### Variáveis de Ambiente

| Variável | Default | Descrição |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/tasks` | URL do banco |
| `JWT_SECRET` | — | Chave de assinatura JWT |
| `SPRING_PROFILES_ACTIVE` | `dev` | Perfil ativo |

---

## 🔌 API

A API é documentada com **Swagger/OpenAPI**. Com o servidor rodando, acesse:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

### Principais Endpoints

| Método | Rota | Auth | Descrição |
|---|---|---|---|
| POST | `/api/auth/register` | ❌ | Registrar novo usuário |
| POST | `/api/auth/login` | ❌ | Login → JWT token |
| GET | `/api/tasks` | ✅ JWT | Listar tarefas do usuário |
| POST | `/api/tasks` | ✅ JWT | Criar tarefa |
| GET | `/api/tasks/{id}` | ✅ JWT | Detalhes da tarefa |
| PUT | `/api/tasks/{id}` | ✅ JWT | Atualizar tarefa |
| DELETE | `/api/tasks/{id}` | ✅ JWT | Deletar tarefa |

### Filtros

`GET /api/tasks?status=PENDING&priority=HIGH&search=keyword`

| Parâmetro | Valores |
|---|---|
| `status` | `PENDING` / `IN_PROGRESS` / `COMPLETED` |
| `priority` | `LOW` / `MEDIUM` / `HIGH` |
| `search` | Texto para busca no título/descrição |

---

## 🧪 Testes

```bash
# Backend — testes unitários + integração
cd backend && ./mvnw test

# Frontend
cd frontend && npm run test
```

---

## 🔄 CI/CD

Pipeline GitHub Actions automatizado:

1. ✅ **Lint** — ESLint + Checkstyle
2. ✅ **Test** — JUnit + Mockito (backend) + Vitest (frontend)
3. ✅ **Build** — Maven + Vite build
4. ✅ **Docker** — Multi-stage build das imagens
5. ✅ **Deploy** — Automático para a VPS

---

## 📄 Licença

MIT © Marcus Linhares
