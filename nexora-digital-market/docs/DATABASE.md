# Base de données — Nexora Digital Market

> PostgreSQL 16 — Schéma Phase 1

## Connexion (développement)

| Paramètre | Valeur |
|-----------|--------|
| Hôte | `localhost` |
| Port | `5432` |
| Base | `nexora_dev` |
| Utilisateur | `postgres` |
| Mot de passe | `root` |

## Diagramme des relations (Phase 1)

```
roles ──────< user_roles >────── users
                                  │
                    ┌─────────────┼─────────────┐
                    │             │             │
               addresses   user_verifications  sellers
                                                  │
                                                shops

categories ──┐                    brands
             │                       │
             └──── products ─────────┘
                      │
              ┌───────┴───────┐
              │               │
        product_images   product_offers ──> sellers

users ──> carts ──> cart_items ──> product_offers
```

## Tables

### `roles`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| name | VARCHAR(50) UNIQUE | ROLE_CLIENT, ROLE_SELLER, ROLE_ADMIN |

### `users`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| email | VARCHAR UNIQUE NOT NULL | |
| password | VARCHAR NOT NULL | BCrypt hash |
| first_name | VARCHAR | |
| last_name | VARCHAR | |
| phone | VARCHAR | |
| enabled | BOOLEAN DEFAULT true | |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

### `user_roles`
| Colonne | Type | Description |
|---------|------|-------------|
| user_id | FK → users | |
| role_id | FK → roles | |

### `addresses`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| user_id | FK → users | |
| label | VARCHAR | Ex: Domicile, Bureau |
| street | VARCHAR | |
| city | VARCHAR | |
| postal_code | VARCHAR | |
| country | VARCHAR | |
| is_default | BOOLEAN | |
| created_at | TIMESTAMP | |

### `user_verifications`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| user_id | FK → users | |
| token | VARCHAR UNIQUE | |
| type | VARCHAR | EMAIL, PHONE, PASSWORD_RESET |
| expires_at | TIMESTAMP | |
| verified | BOOLEAN | |
| created_at | TIMESTAMP | |

### `sellers`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| user_id | FK → users UNIQUE | |
| company_name | VARCHAR | |
| tax_id | VARCHAR UNIQUE | |
| status | VARCHAR | PENDING, APPROVED, REJECTED, SUSPENDED |
| commission_rate | DECIMAL(5,2) | Défaut 10.00 % |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

### `shops`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| seller_id | FK → sellers | |
| name | VARCHAR NOT NULL | |
| slug | VARCHAR UNIQUE | |
| description | TEXT | |
| logo_url | VARCHAR | |
| banner_url | VARCHAR | |
| status | VARCHAR | PENDING, APPROVED, etc. |
| active | BOOLEAN | |
| created_at | TIMESTAMP | |
| updated_at | TIMESTAMP | |

### `categories`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| name | VARCHAR | |
| slug | VARCHAR UNIQUE | |
| description | TEXT | |
| parent_id | FK → categories | Sous-catégories |
| active | BOOLEAN | |

### `brands`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| name | VARCHAR | |
| slug | VARCHAR UNIQUE | |
| description | TEXT | |
| logo_url | VARCHAR | |
| active | BOOLEAN | |

### `products`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| name | VARCHAR | |
| slug | VARCHAR UNIQUE | |
| description | TEXT | |
| category_id | FK → categories | |
| brand_id | FK → brands | |
| active | BOOLEAN | |

### `product_images`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| product_id | FK → products | |
| url | VARCHAR | |
| alt_text | VARCHAR | |
| display_order | INT | |
| is_primary | BOOLEAN | |

### `product_offers`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| product_id | FK → products | |
| seller_id | FK → sellers | |
| price | DECIMAL(12,2) | |
| stock | INT | |
| condition_label | VARCHAR | Neuf, Reconditionné… |
| active | BOOLEAN | |
| UNIQUE | (product_id, seller_id) | Une offre par vendeur/produit |

### `carts`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| user_id | FK → users UNIQUE | Un panier par utilisateur |

### `cart_items`
| Colonne | Type | Description |
|---------|------|-------------|
| id | BIGSERIAL PK | |
| cart_id | FK → carts | |
| product_offer_id | FK → product_offers | |
| quantity | INT | |
| unit_price | DECIMAL(12,2) | Prix au moment de l'ajout |
| UNIQUE | (cart_id, product_offer_id) | |

## Gestion du schéma

| Environnement | Mode Hibernate | Comportement |
|---------------|----------------|--------------|
| dev | `update` | Crée/met à jour les tables automatiquement |
| prod | `validate` | Vérifie le schéma sans modification |
| test | `create-drop` | Recrée à chaque test |

## Données initiales (dev)

Au démarrage en profil `dev`, les rôles système sont créés automatiquement :
- `ROLE_CLIENT`
- `ROLE_SELLER`
- `ROLE_ADMIN`

## Commandes utiles

```bash
# Accéder à PostgreSQL
docker exec -it nexora-postgres psql -U postgres -d nexora_dev

# Lister les tables
\dt

# Décrire une table
\d users
```
