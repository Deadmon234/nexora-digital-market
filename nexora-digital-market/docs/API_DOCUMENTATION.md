# Documentation API — Nexora Digital Market

> Version : 0.1.0 — Phase 1 (architecture)

## Base URL

| Environnement | URL |
|---------------|-----|
| Développement | `http://localhost:8080` |
| Production | `https://api.nexora.example.com` |

## Documentation interactive

Swagger UI : **http://localhost:8080/swagger-ui.html**

OpenAPI JSON : **http://localhost:8080/api-docs**

## Authentification

JWT Bearer token :

```
Authorization: Bearer <access_token>
```

### Endpoints auth (Phase 2 — implémentés)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Inscription (rôle CLIENT par défaut) |
| POST | `/api/auth/login` | Connexion |
| POST | `/api/auth/refresh` | Renouvellement token |
| POST | `/api/auth/logout` | Déconnexion (révoque refresh token) |

**Exemple — Inscription :**
```json
POST /api/auth/register
{
  "email": "client@example.com",
  "password": "password123",
  "firstName": "Jean",
  "lastName": "Dupont"
}
```

**Réponse 201 :**
```json
{
  "accessToken": "eyJ...",
  "refreshToken": "uuid...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "email": "client@example.com",
    "firstName": "Jean",
    "lastName": "Dupont",
    "roles": ["ROLE_CLIENT"]
  }
}
```

## Endpoints disponibles (Phase 1)

### Health

```
GET /api/health
```

**Réponse 200 :**
```json
{
  "status": "OK",
  "service": "nexora-backend"
}
```

## Endpoints prévus par phase

### Phase 2 — Authentification

| Méthode | Endpoint | Description | Statut |
|---------|----------|-------------|--------|
| POST | `/api/auth/register` | Inscription | ✅ |
| POST | `/api/auth/login` | Connexion | ✅ |
| POST | `/api/auth/refresh` | Renouvellement token | ✅ |
| POST | `/api/auth/logout` | Déconnexion | ✅ |

### Phase 3 — Marketplace (public)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/products` | Liste produits |
| GET | `/api/products/{slug}` | Détail produit |
| GET | `/api/categories` | Liste catégories |
| GET | `/api/categories/{slug}` | Détail catégorie |
| GET | `/api/brands` | Liste marques |
| GET | `/api/search` | Recherche |

### Phase 4 — Vendeur

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/sellers/me` | Mon profil vendeur |
| PUT | `/api/sellers/me` | Modifier profil |
| GET | `/api/sellers/me/shop` | Ma boutique |
| GET | `/api/sellers/me/products` | Mes produits |
| POST | `/api/sellers/me/products` | Ajouter produit |

### Phase 5 — Client

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/cart` | Mon panier |
| POST | `/api/cart/items` | Ajouter au panier |
| GET | `/api/favorites` | Mes favoris |
| GET | `/api/addresses` | Mes adresses |

### Phase 6 — Commandes

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/orders` | Créer commande |
| GET | `/api/orders` | Mes commandes |
| GET | `/api/orders/{id}` | Détail commande |

### Phase 8 — Admin

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/admin/sellers` | Tous les vendeurs |
| POST | `/api/admin/sellers/{id}/approve` | Approuver vendeur |
| GET | `/api/admin/orders` | Toutes les commandes |

## Codes de réponse HTTP

| Code | Signification |
|------|---------------|
| 200 | Succès |
| 201 | Créé |
| 204 | Succès sans contenu |
| 400 | Requête invalide |
| 401 | Non authentifié |
| 403 | Accès refusé |
| 404 | Ressource introuvable |
| 409 | Conflit |
| 500 | Erreur serveur |

## Format d'erreur (Phase 2+)

```json
{
  "timestamp": "2026-08-13T14:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Email déjà utilisé",
  "path": "/api/auth/register"
}
```

## CORS

Origines autorisées en développement : `http://localhost:3000`

Configurable via `nexora.cors.allowed-origins` dans les properties backend.
