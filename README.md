# 🔐 API Gestion des Utilisateurs avec JWT

Une API REST sécurisée pour gérer les utilisateurs et leur authentification, développée avec **Spring Boot**, **Spring Security**, **JWT** et **PostgreSQL**.

---

## 🎯 Objectif du projet

Développer une API permettant de gérer les utilisateurs ainsi que leur authentification, avec un système de sécurité basé sur des tokens JWT et une documentation interactive via Swagger.

---

## 🚀 Fonctionnalités

- ✅ Créer un utilisateur (nom, email, mot de passe crypté avec BCrypt)
- ✅ Consulter la liste des utilisateurs (réservé aux ADMIN)
- ✅ Récupérer un utilisateur par son ID
- ✅ Mettre à jour un utilisateur
- ✅ Supprimer un utilisateur
- ✅ Authentification via email/mot de passe avec génération de token JWT
- ✅ Documentation interactive via Swagger

---

## 🛠️ Stack Technique

- Java 21
- Spring Boot 3.5.1
- Spring Security 6 (authentification JWT)
- Spring Data JPA (Hibernate)
- PostgreSQL 18
- Lombok
- Swagger / OpenAPI 3
- Maven

---

## 📦 Prérequis

Avant de lancer l'application, assure-toi d'avoir installé :

- [Java 21](https://adoptium.net/) (ou supérieur)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/) (version 18 recommandée)
- [Git](https://git-scm.com/) (optionnel, pour cloner le projet)
- Un client HTTP pour tester l'API : [Postman](https://www.postman.com/), [Insomnia](https://insomnia.rest/) ou `curl`

---

## 🗄️ Configuration de la base de données

### 1. Créer la base de données PostgreSQL

```bash
sudo -u postgres createdb user_db
```

### 2. (Optionnel) Vérifier que la base est créée

```bash
sudo -u postgres psql -c "\l" | grep user_db
```

### 3. Définir le mot de passe pour l'utilisateur postgres

```bash
sudo -u postgres psql -c "ALTER USER postgres WITH PASSWORD 'postgres';"
```

---

## ⚙️ Configuration de l'application

Le fichier `application.yaml` est déjà préconfiguré avec les paramètres suivants :

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/user_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: maCleSecreteSuperLonguePourJWT1234567890
  expiration: 86400000  # 24 heures
```

> ⚠️ En production, ne jamais laisser le secret JWT et les identifiants de base de données en clair dans le code. Utilise des variables d'environnement.

---

## 🚀 Lancement de l'API

### 1. Cloner le projet (si ce n'est pas déjà fait)

```bash
git clone https://github.com/landrytagne/API_Gestion_Utilisateurs.git
cd API_Gestion_Utilisateurs
```

### 2. Compiler et lancer l'application avec Maven

```bash
mvn clean install
mvn spring-boot:run
```

L'application démarre sur le port **8080**.

---

## 📄 Documentation de l'API (Swagger)

Une fois l'application lancée, accède à la documentation interactive :

| Ressource       | URL                                             |
|-----------------|--------------------------------------------------|
| Swagger UI      | http://localhost:8080/swagger-ui.html            |
| OpenAPI JSON    | http://localhost:8080/api-docs                   |

---

## 🔑 Tester l'authentification

Cette section explique comment tester le flux complet d'authentification : inscription, connexion, et utilisation du token JWT pour accéder aux routes protégées.

### Étape 1 — Créer un utilisateur (inscription)

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Jean Dupont",
    "email": "jean.dupont@example.com",
    "motDePasse": "MotDePasse123!"
  }'
```

**Réponse attendue (201 Created)** : les informations de l'utilisateur créé (le mot de passe est stocké crypté avec BCrypt, jamais retourné en clair).

### Étape 2 — Se connecter (login)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "jean.dupont@example.com",
    "motDePasse": "MotDePasse123!"
  }'
```

**Réponse attendue (200 OK)** :

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 86400000
}
```

### Étape 3 — Utiliser le token JWT pour accéder à une route protégée

Copie le token reçu et ajoute-le dans l'en-tête `Authorization` de tes requêtes suivantes :

```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

- Sans token (ou token invalide/expiré) → réponse **401 Unauthorized**
- Avec token valide mais rôle insuffisant (route ADMIN, utilisateur non-admin) → réponse **403 Forbidden**
- Avec token valide et rôle autorisé → réponse **200 OK** avec les données demandées

### Étape 4 — Tester directement depuis Swagger UI

1. Ouvre http://localhost:8080/swagger-ui.html
2. Exécute `POST /api/auth/login` avec un email/mot de passe valide
3. Copie le `token` retourné
4. Clique sur le bouton **Authorize** en haut de la page Swagger
5. Colle le token au format `Bearer <ton_token>`
6. Toutes les requêtes suivantes envoyées depuis Swagger incluront automatiquement le token

---

## 📋 Endpoints principaux

| Méthode | Endpoint                | Description                          | Accès          |
|---------|--------------------------|---------------------------------------|----------------|
| POST    | `/api/auth/register`     | Créer un utilisateur                  | Public         |
| POST    | `/api/auth/login`        | Authentification et génération du JWT | Public         |
| GET     | `/api/users`              | Lister tous les utilisateurs          | ADMIN          |
| GET     | `/api/users/{id}`         | Récupérer un utilisateur par ID       | Authentifié    |
| PUT     | `/api/users/{id}`         | Mettre à jour un utilisateur          | Authentifié    |
| DELETE  | `/api/users/{id}`         | Supprimer un utilisateur              | ADMIN          |

> Adapte ce tableau si les chemins exacts de tes contrôleurs diffèrent.

---

## 📦 Livrables

- ✅ Code source disponible sur GitHub
- ✅ API documentée avec Swagger / OpenAPI 3
- ✅ README expliquant l'installation, la configuration et le test de l'authentification

---

## 👤 Auteur

**Landry Tagne**
Projet réalisé dans le cadre d'un projet Java Spring Boot.
