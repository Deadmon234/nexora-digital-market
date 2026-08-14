# Nexora Digital Market — Roadmap

> **Dernière mise à jour :** 2026-08-13  
> **Statut global :** 🟢 Phase 9 terminée — Phase 10 à démarrer

Plateforme e-commerce multi-vendeurs pour l'électronique.

| Couche | Stack |
|--------|-------|
| Frontend | Next.js 14 · React · TypeScript · Tailwind CSS |
| Backend | Java 21 · Spring Boot 3.2 · Spring Security · JPA |
| Base de données | PostgreSQL 16 (dev via Docker Compose) |

**Acteurs :** Client · Vendeur · Administrateur

---

## Légende

| Symbole | Signification |
|---------|---------------|
| ✅ | Terminé et vérifié dans le code |
| 🔄 | En cours |
| ⬜ | À faire |
| ⚠️ | Partiellement fait / écart avec la spec |

---

## Vue d'ensemble des phases

| Phase | Nom | Durée estimée | Priorité | Statut |
|-------|-----|---------------|----------|--------|
| 1 | Architecture | 3–4 jours | 🔴 Critique | ✅ Terminée |
| 2 | Authentification & Sécurité | 3–4 jours | 🔴 Critique | ✅ Terminée |
| 3 | Marketplace | 4–5 jours | 🔴 Critique | ✅ Terminée |
| 4 | Espace vendeur | 5–6 jours | 🟠 Haute | ✅ Terminée |
| 5 | Espace client | 5–6 jours | 🟠 Haute | ✅ Terminée |
| 6 | Commandes | 4–5 jours | 🟠 Haute | ✅ Terminée |
| 7 | Paiements & Commissions | 4–5 jours | 🟡 Moyenne | ✅ Terminée |
| 8 | Administration | 5–6 jours | 🟡 Moyenne | ✅ Terminée |
| 9 | Avis & Évaluations | 2–3 jours | 🟢 Basse | ✅ Terminée |
| 10 | Notifications | 2–3 jours | 🟢 Basse | ⬜ À faire |
| 11 | Sécurité avancée | 3–4 jours | 🔴 Critique | ⬜ À faire |
| 12 | Tests complets | 5–6 jours | 🟠 Haute | ⬜ À faire |
| 13 | Déploiement | 3–4 jours | 🟠 Haute | ⬜ À faire |

**Estimation totale : 12–14 semaines**

---

## Écarts architecture — résolus en Phase 1

| Spec cible | État | Statut |
|------------|------|--------|
| Java 21 | `pom.xml` cible Java 21 (compatible JDK 25 local) | ✅ |
| PostgreSQL | Configuré en dev + `docker-compose.yml` | ✅ |
| Package `com.nexora.*` | Structure modulaire en place | ✅ |
| Structure modulaire | `user`, `seller`, `shop`, `product`, `cart`, `common` | ✅ |
| Swagger/OpenAPI | springdoc-openapi configuré | ✅ |
| Lombok (@Builder, @Data) | Entités avec Lombok 1.18.38 | ✅ |

---

## Phase 1 — Architecture

### 1.1 Initialisation du projet

- [x] Créer le dépôt Git
- [x] Initialiser `.gitignore`
- [ ] Configurer GitHub (branches, collaborateurs)
- [x] Documenter la structure du projet (`README.md` basique)

### 1.2 Backend — Configuration de base

- [x] Créer le projet Spring Boot
- [x] Configurer `pom.xml` (Java 21, PostgreSQL, Lombok, Swagger)
- [x] Configurer PostgreSQL (`application-dev.properties` + Docker Compose)
- [x] Configurer Hibernate/JPA
- [x] Créer la structure modulaire (`com.nexora.*`)
- [x] Configurer Swagger/OpenAPI
- [ ] Tester la connexion PostgreSQL *(nécessite Docker en local)*
- [x] Générer les tables initiales (Hibernate `ddl-auto=update`)

### 1.3 Frontend — Configuration de base

- [x] Créer le projet Next.js
- [x] Configurer TypeScript
- [x] Configurer Tailwind CSS
- [x] Créer la structure des dossiers (`components/`, `services/`, `hooks/`, `types/`, `utils/`)
- [x] Configurer variables d'environnement (`.env.example`)
- [x] Service API de base (`services/api.service.ts`)

### 1.4 Documentation

- [x] Créer `README.md` global
- [x] Créer `docs/SETUP.md`
- [x] Créer `docs/API_DOCUMENTATION.md`
- [x] Créer `docs/DATABASE.md`
- [x] Créer `.env.example` (backend + frontend)

---

## Phase 2 — Authentification & Sécurité

### 2.1 Backend — Entités

- [x] Entité `User` (@Builder, @Data, rôles ManyToMany)
- [x] Entité `Role`
- [x] Entité `Address`
- [x] Entité `UserVerification`

### 2.2 Backend — Authentification JWT

- [x] Créer `JwtProvider`
- [x] Créer `JwtValidator`
- [x] Créer `SecurityConfig` (JWT + endpoints publics)
- [x] Créer `JwtAuthenticationFilter`
- [x] Créer `AuthController`
  - [x] `POST /api/auth/register`
  - [x] `POST /api/auth/login`
  - [x] `POST /api/auth/refresh`
  - [x] `POST /api/auth/logout`

### 2.3 Backend — Services & Repositories

- [x] `UserRepository`
- [x] `RoleRepository`
- [x] `UserService`
- [x] `AuthService`
- [x] `PasswordEncoder` (BCrypt)

### 2.4 Backend — Exceptions personnalisées

- [x] `GlobalExceptionHandler`
- [x] `NexoraAuthenticationException`
- [x] `ResourceNotFoundException`
- [x] `ValidationException`

### 2.5 Frontend — Authentification

- [x] Hook `useAuth()`
- [x] Service API `authService`
- [x] Page `/auth/login`
- [x] Page `/auth/register`
- [x] JWT storage (localStorage + cookie)
- [x] Middleware d'authentification

### 2.6 Tests

- [x] Tester l'inscription (backend)
- [x] Tester la connexion (backend)
- [x] Tester la validation du token (refresh)
- [ ] Tester la gestion des rôles

---

## Phase 3 — Marketplace

### 3.1 Backend — Entités

- [x] Entité `Category`
- [x] Entité `Brand`
- [x] Entité `Product`
- [x] Entité `ProductOffer`
- [x] Entité `ProductImage`

### 3.2 Backend — Repositories

- [x] `CategoryRepository`
- [x] `BrandRepository`
- [x] `ProductRepository`
- [x] `ProductOfferRepository`
- [x] `ProductImageRepository`

### 3.3 Backend — Services

- [x] `CategoryService`
- [x] `BrandService`
- [x] `ProductService`
- [ ] `ProductOfferService` (CRUD vendeur — Phase 4)

### 3.4 Backend — Controllers

- [x] `CategoryController` (via ProductController)
- [x] `BrandController` (via ProductController)
- [x] `ProductController` (endpoints publics)

### 3.5 Frontend — Pages publiques

- [x] Page `/` (accueil avec produits et catégories)
- [x] Page `/products`
- [x] Page `/products/[slug]`
- [x] Page `/categories`
- [x] Page `/categories/[slug]`
- [x] Page `/search`

### 3.6 Frontend — Composants réutilisables

- [x] `ProductCard`
- [x] `ProductGrid`
- [x] `CategoryCard`
- [x] `SearchBar`
- [x] `FilterPanel`
- [x] `Pagination`

### 3.7 Tests

- [x] Tester endpoints produits
- [x] Tester recherche
- [x] Tester filtrage

---

## Phase 4 — Espace vendeur

### 4.1 Backend — Entités

- [x] Entité `Seller`
- [x] Entité `Shop`
- [x] Entité `InventoryMovement`
- [ ] Entité `SellerAnalytics` (reporting — phase ultérieure)

### 4.2 Backend — Repositories

- [x] `SellerRepository`
- [x] `ShopRepository`
- [x] `InventoryMovementRepository`

### 4.3 Backend — Services

- [x] `SellerService`
- [x] `ShopService`
- [x] `InventoryService`

### 4.4 Backend — Controllers (Vendeur)

- [x] `SellerController`
- [x] `InventoryController`

### 4.5 Backend — Sécurité vendeur

- [x] Annotation `@SellerAccess`
- [x] Validateurs de propriété vendeur (`SellerContextService`)
- [x] Isolation multi-vendeurs

### 4.6 Frontend — Pages vendeur

- [x] `/seller` (dashboard)
- [x] `/seller/shop`
- [x] `/seller/products`
- [x] `/seller/products/new`
- [x] `/seller/products/[id]/edit`
- [x] `/seller/inventory`
- [x] `/seller/orders` (stub Phase 6)

### 4.7 Frontend — Composants vendeur

- [x] `SellerLayout`
- [x] `SellerSidebar`
- [x] `ProductForm`
- [x] `StockManager`
- [x] `DashboardMetrics`

### 4.8 Tests

- [x] Postulation vendeur + dashboard
- [x] Création boutique + produit (vendeur approuvé)
- [ ] Gestion stock (intégration)
- [ ] Sécurité multi-vendeurs (intégration)

---

## Phase 5 — Espace client

### 5.1 Backend — Entités

- [x] Entité `Cart`
- [x] Entité `CartItem`
- [x] Entité `Favorite`
- [x] Entité `Address` (équivalent `UserAddress`)

### 5.2 Backend — Repositories

- [x] `CartRepository`
- [x] `CartItemRepository`
- [x] `FavoriteRepository`
- [x] `AddressRepository`

### 5.3 Backend — Services

- [x] `CartService`
- [x] `FavoriteService`
- [x] `AddressService`

### 5.4 Backend — Controllers (Client)

- [x] `CartController`
- [x] `FavoriteController`
- [x] `AddressController` + `UserController` (profil)

### 5.5 Frontend — Pages client

- [x] `/account`
- [x] `/account/profile`
- [x] `/account/addresses`
- [x] `/account/orders` (stub Phase 6)
- [x] `/account/favorites`
- [x] `/cart`
- [x] `/checkout` (stub Phase 6)

### 5.6 Frontend — Composants client

- [x] `AccountLayout`
- [x] `CartSummary`
- [x] `CartItem`
- [x] `CheckoutForm`
- [x] `AddressForm`
- [x] `FavoriteButton`

### 5.7 Tests

- [x] Panier multi-vendeurs
- [x] Favoris
- [x] Adresses

---

## Phase 6 — Commandes

### 6.1 Backend — Entités

- [x] `CustomerOrder` (table `orders`)
- [x] `OrderItem`
- [x] `SellerOrder`
- [x] `OrderStatus`

### 6.2 Backend — Repositories

- [x] `CustomerOrderRepository`
- [x] `OrderItemRepository`
- [x] `SellerOrderRepository`

### 6.3 Backend — Services

- [x] `OrderService`
- [x] `OrderNotificationService` (stub log)

### 6.4 Backend — Controllers

- [x] `OrderController` (client)
- [x] `SellerOrderController` (vendeur)

### 6.5 Frontend — Pages commandes

- [x] `/account/orders`
- [x] `/account/orders/[id]`
- [x] `/checkout/confirmation`
- [x] `/seller/orders`
- [x] `/seller/orders/[id]`

### 6.6 Frontend — Composants

- [x] `OrderSummary`
- [x] `OrderTimeline`
- [x] `OrderStatusBadge`

### 6.7 Tests

- [x] Création commande
- [x] Panier → commande
- [x] Sous-commandes multi-vendeurs
- [x] Gestion statuts vendeur

---

## Phase 7 — Paiements & Commissions

### 7.1 Backend — Entités

- [x] `Payment`
- [x] `Commission`
- [x] `SellerBalance`
- [x] `WithdrawalRequest`

### 7.2 Backend — Services

- [x] `PaymentService`
- [x] `CommissionService`
- [x] `SellerBalanceService`
- [x] `WithdrawalService`

### 7.3 Backend — Controllers

- [x] `PaymentController`
- [x] `SellerBalanceController` (solde + commissions + retraits)

### 7.4 Frontend

- [x] `/checkout/payment`
- [x] `/seller/revenues`
- [x] `/seller/withdrawals`
- [x] Composants : `PaymentForm`, `BalanceSummary`, `WithdrawalForm`

### 7.5 Tests

- [x] Calcul commissions
- [x] Paiement
- [x] Mise à jour solde
- [x] Demande retrait

---

## Phase 8 — Administration

### 8.1 Backend — Controllers Admin

- [x] `AdminSellerController`
- [x] `AdminShopController`
- [x] `AdminProductController`
- [x] `AdminOrderController`
- [x] `AdminCategoryController`
- [x] `AdminBrandController`
- [x] `AdminCommissionController`
- [x] `AdminWithdrawalController`

### 8.2 Backend — Services Admin

- [x] `AdminAnalyticsService`

### 8.3 Frontend — Pages Admin

- [x] `/admin` (dashboard)
- [x] `/admin/sellers`, `/admin/shops`, `/admin/products`
- [x] `/admin/orders`, `/admin/categories`, `/admin/brands`
- [x] `/admin/commissions`, `/admin/withdrawals`, `/admin/statistics`

### 8.4 Frontend — Composants Admin

- [x] `AdminLayout`, `AdminSidebar`, `DataTable`, `StatCard`, `ChartComponent`

### 8.5 Tests

- [x] Approbation vendeur
- [x] Gestion commissions
- [x] Gestion produits
- [x] Statistiques

---

## Phase 9 — Avis & Évaluations

- [x] Entités : `Review`, `ProductReview`, `ShopReview`
- [x] `ReviewService`, `RatingService`
- [x] `ReviewController`
- [x] Composants : `ReviewForm`, `ReviewList`, `RatingStars`, `ReviewCard`
- [x] Tests

---

## Phase 10 — Notifications

- [ ] Entités : `Notification`, `NotificationTemplate`
- [ ] `NotificationService` (DB, email, SMS optionnel)
- [ ] `NotificationController`
- [ ] Composants : `NotificationBell`, `NotificationDropdown`, `NotificationCenter`

---

## Phase 11 — Sécurité avancée

### Backend

- [ ] Validations robustes
- [ ] Rate limiting
- [ ] CORS
- [ ] HTTPS
- [ ] Chiffrement données sensibles
- [ ] Audit logging

### Frontend

- [ ] Validation formulaires
- [ ] Sanitisation input
- [ ] CSRF protection
- [ ] JWT sécurisé + refresh token

### Tests de sécurité

- [ ] Injections SQL
- [ ] XSS
- [ ] Accès non autorisé
- [ ] Escalade de privilèges

---

## Phase 12 — Tests complets

### Backend

- [ ] Tests unitaires : UserService, SellerService, ProductService, OrderService, PaymentService, CommissionService
- [ ] Tests d'intégration : auth, produits, commandes, paiements, multi-vendeurs

### Frontend

- [ ] Tests unitaires : composants, hooks, services API
- [ ] Tests e2e : flux client, vendeur, admin

### Performance

- [ ] Optimiser queries BD
- [ ] Cache
- [ ] Optimiser bundle frontend
- [ ] Load testing

---

## Phase 13 — Déploiement

### Préparation

- [ ] `.env.production`
- [ ] Secrets
- [ ] Base de données production
- [ ] `docs/DEPLOYMENT.md`

### Backend

- [ ] PostgreSQL production
- [ ] JAR Spring Boot
- [ ] Serveur + domaine + SSL/TLS
- [ ] Sauvegardes BD + monitoring

### Frontend

- [ ] Build Next.js production
- [ ] CDN + compression + caching headers

### CI/CD

- [ ] GitHub Actions
- [ ] Tests automatiques
- [ ] Build + déploiement automatique

### Monitoring & Maintenance

- [ ] Logs centralisés
- [ ] Alertes
- [ ] Plan de sauvegarde/restauration

---

## Structure cible du projet

```
nexora-digital-market/
├── backend/
│   ├── src/main/java/com/nexora/
│   │   ├── auth/
│   │   ├── user/
│   │   ├── seller/
│   │   ├── shop/
│   │   ├── product/
│   │   ├── category/
│   │   ├── brand/
│   │   ├── cart/
│   │   ├── order/
│   │   ├── payment/
│   │   ├── commission/
│   │   ├── withdrawal/
│   │   ├── inventory/
│   │   ├── review/
│   │   ├── notification/
│   │   ├── admin/
│   │   ├── security/
│   │   └── common/
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   ├── application-dev.properties
│   │   └── schema.sql
│   └── pom.xml
├── frontend/
│   ├── app/
│   ├── components/
│   ├── services/
│   ├── hooks/
│   ├── types/
│   └── utils/
├── docs/
│   ├── API_DOCUMENTATION.md
│   ├── SETUP.md
│   ├── DATABASE.md
│   └── DEPLOYMENT.md
├── ROADMAP.md          ← ce fichier
└── README.md
```

---

## Conventions de code

### Java

| Type | Convention |
|------|------------|
| Packages | `com.nexora.moduleName` |
| Entités | `EntityName.java` |
| Services | `IEntityService.java` / `EntityServiceImpl.java` |
| Controllers | `EntityController.java` |
| Repositories | `EntityRepository.java` |
| DTOs | `EntityDTO.java` |
| Mappers | `EntityMapper.java` |

### TypeScript / React

| Type | Convention |
|------|------------|
| Composants | `ComponentName.tsx` |
| Pages dynamiques | `[slug].tsx` |
| Hooks | `useHookName.ts` |
| Services | `serviceName.service.ts` |
| Types | `type.ts` (préfixe module) |

---

## Checkpoints clés

| Checkpoint | Phase | Statut |
|------------|-------|--------|
| Architecture fonctionnelle | 1 | ✅ |
| Authentification testée | 2 | ✅ |
| Marketplace accessible | 3 | ✅ |
| Vendeurs gèrent produits | 4 | ⬜ |
| Clients peuvent acheter | 5 | ⬜ |
| Commandes fonctionnelles | 6 | ⬜ |
| Paiements processés | 7 | ⬜ |
| Admin dashboard complet | 8 | ⬜ |
| Avis fonctionnels | 9 | ⬜ |
| Notifications envoyées | 10 | ⬜ |
| Sécurité validée | 11 | ⬜ |
| Tests passent | 12 | ⬜ |
| Déploiement réussi | 13 | ⬜ |

---

## Checklist de fin de projet

- [ ] Toutes les fonctionnalités implémentées
- [ ] 100 % des endpoints testés
- [ ] Sécurité multi-vendeurs validée
- [ ] Performance optimisée (< 2 s chargement)
- [ ] Documentation complète
- [ ] Couverture tests > 80 %
- [ ] Déployé en production
- [ ] Monitoring en place
- [ ] Sauvegardes configurées
- [ ] Plan de maintenance établi

---

## Prochaines étapes immédiates

1. **Démarrer Phase 3** — Marketplace (ProductService, pages catalogue)
2. Démarrer PostgreSQL : `docker compose up -d`
3. Lancer backend : `cd backend && .\mvnw.cmd spring-boot:run`
4. Tester auth : http://localhost:3000/auth/register

---

## Journal des mises à jour

| Date | Changement |
|------|------------|
| 2026-08-13 | Phase 3 terminée — API marketplace + pages catalogue frontend |
| 2026-08-13 | Phase 2 terminée — JWT auth backend + frontend login/register |
| 2026-08-13 | Phase 1 terminée — refactor `com.nexora`, PostgreSQL, Swagger, entités, docs |
| 2026-08-13 | Création du ROADMAP.md — audit initial du code vs spec |
