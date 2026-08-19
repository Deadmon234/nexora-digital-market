# Configuration locale — Nexora Digital Market

Guide pour installer et lancer le projet en développement.

## Prérequis

| Outil | Version minimale |
|-------|------------------|
| Java | 21 |
| Maven | 3.9+ |
| Node.js | 20+ |
| npm | 10+ |
| Docker | 24+ (pour PostgreSQL) |
| Git | 2.40+ |

## 1. Cloner le projet

```bash
git clone <url-du-repo>
cd nexora-digital-market
```

## 2. Base de données PostgreSQL

Démarrer PostgreSQL via Docker Compose :

```bash
docker compose up -d
```

Cela crée :
- **Base** : `nexora_dev`
- **Utilisateur** : `postgres`
- **Mot de passe** : `root`
- **Port** : `5432`

Vérifier que le conteneur est prêt :

```bash
docker compose ps
```

## 3. Backend (Spring Boot)

```bash
cd backend

# Copier les variables d'environnement (optionnel en dev — valeurs par défaut dans application-dev.properties)
cp .env.example .env

# Lancer l'application (Maven Wrapper — pas besoin d'installer Maven)
.\mvnw.cmd spring-boot:run
```

Le backend démarre sur **http://localhost:8080**

### Endpoints utiles

| URL | Description |
|-----|-------------|
| http://localhost:8080/api/health | Health check |
| http://localhost:8080/swagger-ui.html | Documentation Swagger |
| http://localhost:8080/api-docs | OpenAPI JSON |

### Profils Spring

| Profil | Usage |
|--------|-------|
| `dev` (défaut) | PostgreSQL local, ddl-auto=update |
| `prod` | PostgreSQL production, ddl-auto=validate |
| `test` | H2 en mémoire (tests uniquement) |

Changer de profil :

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=prod
```

## 4. Frontend (Next.js)

```bash
cd frontend

# Installer les dépendances
npm install

# Copier les variables d'environnement
cp .env.example .env.local

# Lancer en développement
npm run dev
```

Le frontend démarre sur **http://localhost:3000**

## 5. Vérification

1. Backend : `curl http://localhost:8080/api/health` → `{"status":"OK","service":"nexora-backend"}`
2. Swagger : ouvrir http://localhost:8080/swagger-ui.html
3. Frontend : ouvrir http://localhost:3000
4. Tables BD : Hibernate crée automatiquement les tables au premier démarrage

## Structure des modules backend

```
com.nexora/
├── common/       # Config, enums, health
├── user/         # User, Role, Address, UserVerification
├── seller/       # Seller
├── shop/         # Shop
├── product/      # Category, Brand, Product, ProductOffer, ProductImage
└── cart/         # Cart, CartItem
```

## Dépannage

### PostgreSQL : connexion refusée

```bash
docker compose down
docker compose up -d
# Attendre que le healthcheck soit OK
```

### Port 8080 déjà utilisé

Modifier `server.port` dans `application-dev.properties`.

### Erreur Lombok / compilation

Vérifier que Java 21 est actif :

```bash
java -version
.\mvnw.cmd -version
```

## Prochaine étape

Phase 2 — Authentification JWT. Voir [ROADMAP.md](../ROADMAP.md).

Pour PostgreSQL via Docker, voir [DOCKER.md](./DOCKER.md).
