# 📋 Task Manager — FullStack Portfolio

Projeto fullstack de gerenciamento de tarefas, demonstrando habilidades em **Spring Boot**, **React**, **Docker** e **CI/CD**.

## 🛠️ Tech Stack

### Backend
- **Java 17** + **Spring Boot 3**
- **Spring Data JPA** + **PostgreSQL**
- **Spring Security** + **JWT**
- **Maven**
- **JUnit 5** + **Mockito**

### Frontend
- **React 18** + **TypeScript**
- **Vite**
- **Tailwind CSS**
- **Axios**
- **React Query**

### DevOps
- **Docker** + **Docker Compose**
- **GitHub Actions** (CI/CD)

## 📋 Funcionalidades
- [x] CRUD de tarefas (criar, listar, editar, deletar)
- [x] Autenticação JWT (registro/login)
- [x] Filtros e busca
- [x] Drag and drop para reordenação
- [x] Notificações em tempo real
- [x] Testes unitários e de integração
- [x] CI/CD pipeline

## 🚀 Como Rodar

### Com Docker (recomendado)
```bash
docker-compose up -d
# Frontend: http://localhost:5173
# Backend: http://localhost:8080
# API Docs: http://localhost:8080/swagger-ui.html
```

### Backend
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

## 📁 Estrutura
```
task-manager/
├── backend/
│   ├── src/main/java/com/marcus/taskmanager/
│   │   ├── controller/     # REST Controllers
│   │   ├── service/        # Business Logic
│   │   ├── repository/     # JPA Repositories
│   │   ├── model/          # Entities + DTOs
│   │   ├── config/         # Security, Swagger, etc
│   │   └── exception/      # Global Exception Handler
│   ├── src/test/           # Unit + Integration Tests
│   ├── pom.xml
│   └── Dockerfile
├── frontend/
│   ├── src/
│   │   ├── components/     # React Components
│   │   ├── hooks/          # Custom Hooks
│   │   ├── services/       # API Calls
│   │   ├── types/          # TypeScript Types
│   │   └── pages/          # Pages
│   ├── package.json
│   └── Dockerfile
├── docker-compose.yml
├── .github/workflows/ci.yml
└── README.md
```

## 🧪 Testes
```bash
# Backend
cd backend && ./mvnw test

# Frontend
cd frontend && npm run test
```

## 📝 Licença
MIT
