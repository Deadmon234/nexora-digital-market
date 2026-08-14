# Nexora Digital Market

Plateforme e-commerce multi-vendeurs avec Next.js et Spring Boot.

> Suivi d'avancement : [ROADMAP.md](./ROADMAP.md)

## Structure du projet

```
nexora-digital-market/
├── backend/          # Spring Boot (Java 21, PostgreSQL)
├── frontend/         # Next.js (TypeScript, Tailwind)
├── docs/             # Documentation
├── docker-compose.yml
└── ROADMAP.md
```

## Démarrage rapide

```bash
# 1. Base de données
docker compose up -d

# 2. Backend
cd backend && .\mvnw.cmd spring-boot:run

# 3. Frontend
cd frontend && npm install && npm run dev
```

Documentation complète : [docs/SETUP.md](./docs/SETUP.md)

## URLs locales

| Service | URL |
|---------|-----|
| Frontend | http://localhost:3000 |
| Backend API | http://localhost:8080 |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Health check | http://localhost:8080/api/health |

## Stack

- **Frontend** : Next.js 14, React, TypeScript, Tailwind CSS
- **Backend** : Java 21, Spring Boot 3.2, Spring Security, JPA
- **Base de données** : PostgreSQL 16

## Documentation

- [Configuration locale](./docs/SETUP.md)
- [API](./docs/API_DOCUMENTATION.md)
- [Base de données](./docs/DATABASE.md)
- [Roadmap](./ROADMAP.md)
