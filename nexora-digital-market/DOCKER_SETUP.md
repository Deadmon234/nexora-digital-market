# Guide de Configuration Docker - Nexora Digital Market

Cette guide vous montre comment configurer Docker pour votre plateforme e-commerce multi-vendeurs avec frontend Next.js et backend Spring Boot.

## 📋 Table des matières

1. [Architecture Docker](#architecture-docker)
2. [Prérequis](#prérequis)
3. [Configuration actuelle](#configuration-actuelle)
4. [Mise en place complète](#mise-en-place-complète)
5. [Dockerfiles](#dockerfiles)
6. [Docker Compose amélioré](#docker-compose-amélioré)
7. [Commandes utiles](#commandes-utiles)
8. [Dépannage](#dépannage)

---

## Architecture Docker

```
┌─────────────────────────────────────────────────────┐
│           Docker Compose Network                     │
├─────────────────────────────────────────────────────┤
│                                                       │
│  ┌──────────────┐    ┌──────────────┐              │
│  │  Frontend    │    │  Backend     │              │
│  │  (Next.js)   │───→│  (Spring)    │              │
│  │  :3000       │    │  :8080       │              │
│  └──────────────┘    └──────────────┘              │
│         │                    │                      │
│         └────────┬───────────┘                      │
│                  │                                  │
│          ┌───────▼────────┐                        │
│          │  PostgreSQL    │                        │
│          │  (Database)    │                        │
│          │  :5432         │                        │
│          └────────────────┘                        │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## Prérequis

### Installation requise

- **Docker** : Version 20.10+ 
  - [Windows/Mac](https://www.docker.com/products/docker-desktop)
  - [Linux](https://docs.docker.com/engine/install/)

- **Docker Compose** : Version 2.0+
  - Généralement inclus avec Docker Desktop

### Vérification

```bash
# Vérifier Docker
docker --version
# Output: Docker version 24.0.0+

# Vérifier Docker Compose
docker compose version
# Output: Docker Compose version 2.20.0+
```

---

## Configuration actuelle

### 📄 Fichier : `docker-compose.yml` (existant)

Votre configuration actuelle contient **uniquement la base de données** :

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: nexora-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: nexora_dev
      POSTGRES_USER: nexora
      POSTGRES_PASSWORD: nexora
    ports:
      - "5432:5432"
    volumes:
      - nexora_pg_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U nexora -d nexora_dev"]
      interval: 5s
      timeout: 5s
      retries: 5

volumes:
  nexora_pg_data:
```

**Démarrage** :
```bash
docker compose up -d
```

---

## Mise en place complète

### Étape 1 : Créer les Dockerfiles

#### A. Backend Dockerfile

**Créer le fichier** : `backend/Dockerfile`

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copier les fichiers de dépendances
COPY pom.xml .
COPY mvnw mvnw
COPY .mvn .mvn

# Télécharger les dépendances
RUN ./mvnw dependency:go-offline

# Copier le code source
COPY src src

# Compiler l'application
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copier le JAR du stage de build
COPY --from=builder /app/target/*.jar app.jar

# Créer un utilisateur non-root
RUN addgroup -g 1000 nexora && adduser -D -u 1000 -G nexora nexora
USER nexora

# Exposer le port
EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/api/health || exit 1

# Démarrer l'application
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
```

**Note** : Créer aussi `backend/src/main/resources/application-docker.properties` :

```properties
# Database
spring.datasource.url=jdbc:postgresql://postgres:5432/nexora_dev
spring.datasource.username=nexora
spring.datasource.password=nexora
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8080
server.servlet.context-path=/api

# Logging
logging.level.root=INFO
logging.level.com.nexora=DEBUG
```

#### B. Frontend Dockerfile

**Créer le fichier** : `frontend/Dockerfile`

```dockerfile
# Stage 1: Build
FROM node:20-alpine AS builder

WORKDIR /app

# Copier les fichiers de dépendances
COPY package.json package-lock.json ./

# Installer les dépendances
RUN npm ci

# Copier le code source
COPY . .

# Compiler l'application Next.js
RUN npm run build

# Stage 2: Runtime
FROM node:20-alpine

WORKDIR /app

# Copier les dépendances de production
COPY --from=builder /app/node_modules node_modules
COPY --from=builder /app/.next .next
COPY --from=builder /app/public public
COPY --from=builder /app/package.json .

# Créer un utilisateur non-root
RUN addgroup -g 1000 nextjs && adduser -D -u 1000 -G nextjs nextjs
USER nextjs

# Exposer le port
EXPOSE 3000

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
  CMD node -e "require('http').get('http://localhost:3000', (r) => {if (r.statusCode !== 200) throw new Error(r.statusCode)})" || exit 1

# Démarrer l'application
CMD ["npm", "start"]
```

### Étape 2 : Fichiers .dockerignore

#### `backend/.dockerignore`

```
.git
.gitignore
target
.mvn/wrapper/maven-wrapper.jar
mvnw.cmd
README.md
.env
.env.local
.idea
*.iml
*.swp
*.swo
```

#### `frontend/.dockerignore`

```
.git
.gitignore
node_modules
.next
.env.local
.env.*.local
npm-debug.log
yarn-error.log
.swp
.swo
.DS_Store
README.md
.idea
```

---

## Docker Compose amélioré

### 📄 Fichier : `docker-compose.yml` (complet)

```yaml
version: '3.9'

services:
  # Base de données PostgreSQL
  postgres:
    image: postgres:16-alpine
    container_name: nexora-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: nexora_dev
      POSTGRES_USER: nexora
      POSTGRES_PASSWORD: nexora
    ports:
      - "5432:5432"
    volumes:
      - nexora_pg_data:/var/lib/postgresql/data
      - ./docs/schema-reference.sql:/docker-entrypoint-initdb.d/01-schema.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U nexora -d nexora_dev"]
      interval: 5s
      timeout: 5s
      retries: 5
    networks:
      - nexora-network
    labels:
      description: "PostgreSQL Database for Nexora"

  # Backend Spring Boot
  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    container_name: nexora-backend
    restart: unless-stopped
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/nexora_dev
      SPRING_DATASOURCE_USERNAME: nexora
      SPRING_DATASOURCE_PASSWORD: nexora
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SERVER_PORT: 8080
    depends_on:
      postgres:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "wget", "--quiet", "--tries=1", "--spider", "http://localhost:8080/api/health"]
      interval: 30s
      timeout: 10s
      start_period: 40s
      retries: 3
    networks:
      - nexora-network
    labels:
      description: "Spring Boot Backend API"

  # Frontend Next.js
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    container_name: nexora-frontend
    restart: unless-stopped
    ports:
      - "3000:3000"
    environment:
      NODE_ENV: production
      NEXT_PUBLIC_API_URL: http://localhost:8080/api
    depends_on:
      - backend
    healthcheck:
      test: ["CMD", "node", "-e", "require('http').get('http://localhost:3000', (r) => {if (r.statusCode !== 200) throw new Error(r.statusCode)})"]
      interval: 30s
      timeout: 10s
      start_period: 40s
      retries: 3
    networks:
      - nexora-network
    labels:
      description: "Next.js Frontend Application"

volumes:
  nexora_pg_data:
    driver: local

networks:
  nexora-network:
    driver: bridge
```

### 📄 Fichier : `docker-compose.dev.yml` (développement)

Pour le développement, vous pouvez créer une version alternative :

```yaml
version: '3.9'

services:
  postgres:
    image: postgres:16-alpine
    container_name: nexora-postgres-dev
    restart: unless-stopped
    environment:
      POSTGRES_DB: nexora_dev
      POSTGRES_USER: nexora
      POSTGRES_PASSWORD: nexora
    ports:
      - "5432:5432"
    volumes:
      - nexora_pg_data_dev:/var/lib/postgresql/data
    networks:
      - nexora-network-dev

volumes:
  nexora_pg_data_dev:

networks:
  nexora-network-dev:
    driver: bridge
```

**Usage** :
```bash
# Démarrer uniquement la base de données pour le développement
docker compose -f docker-compose.dev.yml up -d
```

### 📄 Fichier : `.dockerignore` (racine - optionnel)

```
.git
.gitignore
.github
node_modules
.env.local
.env.*.local
npm-debug.log
.idea
*.swp
.DS_Store
docs
ROADMAP.md
```

---

## Dockerfiles

### 1️⃣ Dockerfile Backend - Explications

**Multi-stage build** (optimisation de taille) :

| Stage | Rôle | Taille finale |
|-------|------|---------------|
| **builder** | Compile l'application Maven | ~1.5 GB |
| **runtime** | Exécute uniquement le JAR compilé | ~500 MB |

**Points clés** :

- **JRE Alpine** : Base légère (25 MB vs 500 MB avec full JDK)
- **Utilisateur non-root** : Sécurité (pas d'exécution en tant que root)
- **Healthcheck** : Vérifie que l'application démarre correctement
- **Profile Spring** : `application-docker.properties` pour les variables d'environnement

### 2️⃣ Dockerfile Frontend - Explications

**Multi-stage build** :

| Stage | Rôle | Taille finale |
|-------|------|---------------|
| **builder** | Construit l'app Next.js | ~1.2 GB |
| **runtime** | Exécute uniquement l'app compilée | ~200 MB |

**Points clés** :

- **Node Alpine** : Base ultra-légère
- **npm ci** : Installation sécurisée des dépendances
- **Utilisateur non-root** : Sécurité
- **Healthcheck** : Vérifie la disponibilité du serveur

---

## Commandes utiles

### 🚀 Démarrage

```bash
# Démarrer tous les services
docker compose up -d

# Démarrer en affichant les logs
docker compose up

# Démarrer un service spécifique
docker compose up -d postgres
docker compose up -d backend
docker compose up -d frontend
```

### 🔨 Construction

```bash
# Construire tous les images
docker compose build

# Construire un service spécifique
docker compose build backend
docker compose build frontend

# Construire sans cache
docker compose build --no-cache
```

### 📊 Monitoring

```bash
# Voir les logs
docker compose logs -f

# Logs d'un service spécifique
docker compose logs -f backend
docker compose logs -f frontend

# 50 dernières lignes
docker compose logs --tail=50 backend

# Lister les conteneurs
docker compose ps

# Inspecter un conteneur
docker inspect nexora-backend
```

### 🧪 Tests et Vérification

```bash
# Vérifier la santé des services
docker compose ps

# Entrer dans un conteneur
docker exec -it nexora-backend /bin/sh
docker exec -it nexora-frontend /bin/sh

# Exécuter une commande dans un conteneur
docker exec nexora-backend wget --spider http://localhost:8080/api/health
```

### 🧹 Nettoyage

```bash
# Arrêter tous les services
docker compose down

# Arrêter et supprimer les volumes
docker compose down -v

# Supprimer les images inutilisées
docker image prune

# Tout nettoyer (⚠️ attention)
docker compose down -v
docker system prune -a
```

### 🔄 Redémarrage

```bash
# Redémarrer tous les services
docker compose restart

# Redémarrer un service
docker compose restart backend

# Arrêter et redémarrer
docker compose down && docker compose up -d
```

---

## Accès aux services

### Une fois les conteneurs démarrés

| Service | URL | Credentials |
|---------|-----|-------------|
| **Frontend** | http://localhost:3000 | - |
| **Backend API** | http://localhost:8080/api | - |
| **Swagger UI** | http://localhost:8080/api/swagger-ui.html | - |
| **Health Check** | http://localhost:8080/api/health | - |
| **PostgreSQL** | localhost:5432 | User: `nexora` / Pass: `nexora` |

### Connexion à la base de données

```bash
# Via psql
psql -h localhost -U nexora -d nexora_dev

# Ou via un client GUI
- Host: localhost
- Port: 5432
- Database: nexora_dev
- User: nexora
- Password: nexora
```

---

## Dépannage

### ❌ Problème : Port déjà utilisé

```bash
# Trouver le processus qui utilise le port
# Windows
netstat -ano | findstr :3000

# Linux/Mac
lsof -i :3000

# Tuer le processus
# Windows
taskkill /PID <PID> /F

# Linux/Mac
kill -9 <PID>

# Ou changer les ports dans docker-compose.yml
```

### ❌ Problème : Conteneur qui crash au démarrage

```bash
# Voir les logs
docker compose logs backend

# Vérifier le statut
docker compose ps

# Redémarrer avec logs
docker compose down && docker compose up
```

### ❌ Problème : Backend ne se connecte pas à la DB

```bash
# Vérifier la connexion
docker exec nexora-backend pg_isready -h postgres -U nexora

# Inspecter la réservation du réseau
docker network inspect nexora-network

# Vérifier les variables d'environnement
docker exec nexora-backend env | grep SPRING
```

### ❌ Problème : Frontend ne trouve pas l'API

```bash
# Vérifier les variables d'environnement
docker exec nexora-frontend env | grep NEXT_PUBLIC_API_URL

# Modifier docker-compose.yml
environment:
  NEXT_PUBLIC_API_URL: http://backend:8080/api  # URL interne
```

### ❌ Problème : Volume non créé

```bash
# Recréer les volumes
docker compose down -v
docker compose up -d

# Vérifier les volumes
docker volume ls
docker volume inspect nexora_pg_data
```

---

## Fichiers à créer/modifier

### Résumé des fichiers nécessaires

```
nexora-digital-market/
├── docker-compose.yml              ✏️  MODIFIER
├── docker-compose.dev.yml          ✅ CRÉER
├── DOCKER_SETUP.md                 ✅ CRÉER (ce fichier)
├── backend/
│   ├── Dockerfile                  ✅ CRÉER
│   ├── .dockerignore               ✅ CRÉER
│   └── src/main/resources/
│       └── application-docker.properties  ✅ CRÉER
└── frontend/
    ├── Dockerfile                  ✅ CRÉER
    └── .dockerignore               ✅ CRÉER
```

---

## Configuration pour Production

### Points de sécurité importants

```yaml
# 1. Secrets management (utiliser .env)
# backend/
SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}

# 2. CORS configuration
# application-docker.properties
spring.web.cors.allowed-origins=https://yourdomain.com

# 3. Utiliser les healthchecks
# Pour orchestration Kubernetes/Swarm

# 4. Logging
logging.level.root=WARN
logging.level.com.nexora=INFO
```

### Variables d'environnement (.env)

```bash
# .env (ne jamais commiter !)
POSTGRES_USER=nexora
POSTGRES_PASSWORD=your_secure_password
SPRING_PROFILES_ACTIVE=docker
NODE_ENV=production
NEXT_PUBLIC_API_URL=https://api.yourdomain.com
```

---

## Prochaines étapes

1. ✅ Créer les Dockerfiles
2. ✅ Mettre à jour docker-compose.yml
3. ✅ Tester les conteneurs
4. ⏭️  Configurer le CI/CD (GitHub Actions)
5. ⏭️  Déployer sur Azure/Cloud

---

## Ressources utiles

- [Documentation Docker](https://docs.docker.com/)
- [Docker Compose Guide](https://docs.docker.com/compose/)
- [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker)
- [Next.js Docker](https://nextjs.org/docs/deployment/docker)
- [Best practices Docker](https://docs.docker.com/develop/dev-best-practices/)

---

**Créé le** : 2024-08-18  
**Version** : 1.0  
**Auteur** : GitHub Copilot
