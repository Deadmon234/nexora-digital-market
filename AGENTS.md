# Nexora Digital Market — regles projet

Marketplace e-commerce multi-vendeurs de produits electroniques (zone FCFA).
Ce n'est pas une boutique en ligne mono-vendeur : chaque decision technique doit rester valable
pour un grand nombre de vendeurs et de clients simultanes.

## Stack imposee

- Frontend : Next.js (app router) + React + TypeScript + Tailwind CSS.
- Backend : Java 17 + Spring Boot + Spring Security + Spring Data JPA / Hibernate + Maven.
- Base de donnees : PostgreSQL, schema gere par migrations Flyway (`ddl-auto: validate`).
- API : REST / JSON, documentee via Swagger (springdoc OpenAPI).
- Outils : Git, GitHub, Docker.

## Architecture backend

Packages par module metier sous `com.marketplace` : `auth`, `user`, `seller`, `shop`, `product`,
`category`, `brand`, `cart`, `order`, `payment`, `commission`, `withdrawal`, `inventory`, `review`,
`notification`, `admin`, `security`, `common`.

Chaque module porte ses propres `controller`, `service`, `repository`, `entity`, `dto`, `mapper`,
`exception`. La logique metier vit dans les services, l'acces aux donnees dans les repositories,
et les echanges API passent exclusivement par des DTO — jamais une entite JPA exposee directement.

## Regles metier structurantes

- Roles : `CLIENT`, `SELLER`, `ADMIN`. L'inscription publique cree toujours un `CLIENT` ; le role
  `SELLER` est attribue par l'administrateur en approuvant une demande vendeur.
- Isolation vendeur : un vendeur n'accede qu'a ses propres donnees (boutique, produits, stock,
  commandes, revenus, retraits). L'appartenance de la ressource doit etre verifiee cote backend a
  chaque requete — un controle de role seul ne suffit pas, et un identifiant modifie dans l'URL ne
  doit jamais donner acces aux donnees d'un autre vendeur.
- Catalogue : separer la fiche produit generale (`Product`) de l'offre d'un vendeur (`ShopOffer` :
  prix, stock, promotion, garantie, etat). Panier, commandes et stock referencent l'offre.
- Commandes multi-vendeurs : une commande client se decompose en sous-commandes, une par boutique.
  Chaque vendeur ne voit que sa sous-commande ; l'administrateur voit l'ensemble.
- Commissions : taux configurable globalement, par categorie ou par vendeur, et fige sur la
  sous-commande au moment de la commande pour ne jamais reecrire l'historique financier.
- Finances vendeur : le solde se deduit d'un grand-livre d'ecritures, jamais d'une colonne
  modifiable. Les retraits suivent les statuts PENDING, APPROVED, PROCESSING, COMPLETED, REJECTED.
- Montants : `BigDecimal` uniquement, devise XAF.
- Commandes passees : nom, prix et boutique sont figes dans la ligne de commande.

## Regles de developpement

- Aucun secret dans le code : tout passe par des variables d'environnement (voir `.env.example`).
- Composants frontend reutilisables ; autorisations toujours revalidees cote backend.
- Ne pas modifier inutilement du code fonctionnel existant.
- Expliquer une modification importante d'architecture avant de l'appliquer, et recommander la
  solution la plus adaptee a une marketplace multi-vendeurs quand plusieurs options existent.
- Chaque fonctionnalite est testee avant de passer a la suivante.

## Commandes

```bash
docker compose up -d postgres      # base locale

cd backend && mvn verify           # build + tests (Testcontainers, Docker requis)
cd backend && mvn spring-boot:run  # API sur http://localhost:8080, Swagger sur /swagger-ui.html

cd frontend && npm ci
cd frontend && npm run lint
cd frontend && npm run build
cd frontend && npm run dev         # http://localhost:3000
```
