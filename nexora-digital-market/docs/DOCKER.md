# Configuration Docker — Nexora Digital Market

Guide pour lancer la base de données PostgreSQL avec Docker et connecter le backend et le frontend en développement local.

---

## Prérequis

| Outil | Version | Rôle |
|-------|---------|------|
| [Docker Desktop](https://www.docker.com/products/docker-desktop/) | 24+ | Conteneur PostgreSQL *(optionnel si PostgreSQL est déjà installé localement)* |
| PostgreSQL | 16+ | Base de données (local ou Docker) |
| Java | 21 | Backend Spring Boot |
| Node.js | 20+ | Frontend Next.js |

> **Configuration locale par défaut :** utilisateur `postgres`, mot de passe `root`, base `nexora_dev` sur le port `5432`.

Sur Windows, installez **Docker Desktop** et vérifiez qu'il est démarré (icône Docker dans la barre des tâches).

```powershell
docker --version
docker compose version
```

---

## 1. Démarrer PostgreSQL

Depuis la racine du projet (`nexora-digital-market/`) :

```powershell
docker compose up -d
```

Cela démarre un conteneur **PostgreSQL 16** avec :

| Paramètre | Valeur |
|-----------|--------|
| Conteneur | `nexora-postgres` |
| Base de données | `nexora_dev` |
| Utilisateur | `postgres` |
| Mot de passe | `root` |
| Port hôte | `5432` |
| Volume persistant | `nexora_pg_data` |

### Vérifier que la base est prête

```powershell
docker compose ps
```

La colonne **STATUS** doit afficher `healthy` (healthcheck automatique).

```powershell
docker compose logs postgres
```

---

## 2. Configurer le backend

Le profil `dev` (actif par défaut) pointe déjà vers PostgreSQL local :

```properties
# backend/src/main/resources/application-dev.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/nexora_dev
spring.datasource.username=postgres
spring.datasource.password=root
```

### Variables d'environnement optionnelles

Copiez le fichier d'exemple si vous souhaitez surcharger les valeurs :

```powershell
cd backend
copy .env.example .env
```

| Variable | Description | Défaut (dev) |
|----------|-------------|--------------|
| `DATABASE_URL` | URL JDBC PostgreSQL | `jdbc:postgresql://localhost:5432/nexora_dev` |
| `DATABASE_USERNAME` | Utilisateur BD | `postgres` |
| `DATABASE_PASSWORD` | Mot de passe BD | `root` |
| `JWT_SECRET` | Clé secrète JWT | Valeur dev (à changer en prod) |
| `CORS_ALLOWED_ORIGINS` | Origines frontend autorisées | `http://localhost:3000` |

### Lancer le backend

```powershell
cd backend
java -classpath .\.mvn\wrapper\maven-wrapper.jar "-Dmaven.multiModuleProjectDirectory=$PWD" org.apache.maven.wrapper.MavenWrapperMain spring-boot:run
```

Le backend écoute sur **http://localhost:8080**.

Test rapide :

```powershell
curl http://localhost:8080/api/health
```

Au premier démarrage, Hibernate crée automatiquement les tables dans `nexora_dev`.

---

## 3. Configurer le frontend

Créez un fichier `.env.local` dans `frontend/` :

```env
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Lancer le frontend

```powershell
cd frontend
npm install
npm run dev
```

Le site est accessible sur **http://localhost:3000**.

---

## 4. Ordre de démarrage recommandé

```
1. docker compose up -d          → PostgreSQL
2. Attendre STATUS = healthy
3. Backend (spring-boot:run)     → API sur :8080
4. Frontend (npm run dev)        → Site sur :3000
```

---

## 5. Commandes Docker utiles

| Commande | Action |
|----------|--------|
| `docker compose up -d` | Démarrer PostgreSQL en arrière-plan |
| `docker compose ps` | État des conteneurs |
| `docker compose logs -f postgres` | Logs en temps réel |
| `docker compose stop` | Arrêter sans supprimer les données |
| `docker compose down` | Arrêter et retirer le conteneur |
| `docker compose down -v` | **Attention** : supprime aussi le volume (données perdues) |

### Accéder à PostgreSQL en ligne de commande

```powershell
docker exec -it nexora-postgres psql -U postgres -d nexora_dev
```

Exemples SQL :

```sql
\dt                          -- lister les tables
SELECT count(*) FROM users;  -- vérifier les données
\q                           -- quitter
```

---

## 6. Dépannage

### Anciennes variables d'environnement

Si le backend tente encore de se connecter avec l'utilisateur `nexora`, des variables `DATABASE_*` obsolètes sont peut-être définies dans votre terminal ou votre IDE. Fermez et rouvrez le terminal, ou supprimez-les :

```powershell
Remove-Item Env:DATABASE_URL -ErrorAction SilentlyContinue
Remove-Item Env:DATABASE_USERNAME -ErrorAction SilentlyContinue
Remove-Item Env:DATABASE_PASSWORD -ErrorAction SilentlyContinue
```

### Port 5432 déjà utilisé

Une autre instance PostgreSQL (locale ou Docker) occupe le port.

**Option A** — Arrêter l'autre service PostgreSQL.

**Option B** — Changer le port dans `docker-compose.yml` :

```yaml
ports:
  - "5433:5432"
```

Puis mettez à jour `application-dev.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/nexora_dev
```

### Connexion refusée au démarrage du backend

Le conteneur n'est pas encore prêt. Attendez le healthcheck :

```powershell
docker compose ps
# Attendre "healthy", puis relancer le backend
```

Ou redémarrez PostgreSQL :

```powershell
docker compose down
docker compose up -d
```

### Docker Desktop non démarré (Windows)

Erreur du type `Cannot connect to the Docker daemon`. Ouvrez **Docker Desktop** et réessayez.

### Réinitialiser la base de données

Pour repartir de zéro (supprime toutes les données) :

```powershell
docker compose down -v
docker compose up -d
```

Relancez ensuite le backend pour recréer les tables.

---

## 7. Production (aperçu)

En production, Docker Compose ne lance actuellement **que PostgreSQL**. Le backend et le frontend se déploient séparément (JAR Spring Boot + build Next.js).

Recommandations :

| Élément | Recommandation |
|---------|----------------|
| PostgreSQL | Conteneur ou service managé (RDS, Cloud SQL…) |
| Backend | JAR + variables d'environnement sécurisées |
| Frontend | `npm run build` + serveur Node ou export statique |
| HTTPS | Reverse proxy (Nginx, Traefik, Caddy) devant l'API et le site |
| Secrets | `JWT_SECRET`, `DATABASE_PASSWORD` via secrets manager, jamais en clair dans le repo |
| CORS | `CORS_ALLOWED_ORIGINS=https://votre-domaine.com` |

Exemple de variables production pour le backend :

```env
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://db-host:5432/nexora_prod
DATABASE_USERNAME=nexora
DATABASE_PASSWORD=<secret-fort>
JWT_SECRET=<secret-256-bits-minimum>
CORS_ALLOWED_ORIGINS=https://nexora.example.com
```

---

## 8. Fichiers concernés

| Fichier | Rôle |
|---------|------|
| `docker-compose.yml` | Définition du service PostgreSQL |
| `backend/src/main/resources/application-dev.properties` | Connexion BD en dev |
| `backend/.env.example` | Modèle de variables backend |
| `frontend/.env.local` | URL de l'API (à créer) |
| `docs/SETUP.md` | Guide d'installation complet |

---

## Comptes de démonstration

Après le premier démarrage (données de seed si activées) :

| Rôle | Email | Mot de passe |
|------|-------|--------------|
| Admin | `admin@nexora.dev` | `password123` |
| Vendeur | `vendeur@nexora.dev` | `password123` |

Pour plus de détails sur l'installation sans Docker, voir [SETUP.md](./SETUP.md).
