# Labo Academy — Backend (Spring Boot)

Docyline est une application de vente de documents numériques et de préparation aux concours en ligne,
conforme au cahier des charges (Labo Academy, Douala).

## Stack technique
- Java 17, Spring Boot 3.3
- Spring Data JPA (Hibernate) + MySQL
- Spring Security + JWT (authentification stateless)
- Lombok, Bean Validation, Springdoc/Swagger

```bash
# Créer la base de données
mysql -u root -p
CREATE DATABASE labo_academy;
USE labo_academy;
```


## Architecture des packages

```
com.laboacademy.app
├── model/          → 13 entités JPA (Utilisateur, Concours, Matiere, Categorie, Document,
│                      Panier, LignePanier, Commande, Paiement, Quiz, Question, Reponse, Resultat)
├── enums/          → Role, StatutCompte, StatutDocument, StatutConcours, StatutPanier,
│                      StatutCommande, StatutPaiement, ModePaiement, StatutQuiz
├── repository/     → Interfaces Spring Data JPA (une par entité)
├── security/       → JwtService, JwtAuthFilter, CustomUserDetails(Service)
├── config/         → SecurityConfig (routes publiques/protégées selon matrice des droits)
├── dto/            → Objets d'échange API (Request/Response), ne jamais exposer les entités brutes
├── service / service.impl → Logique métier (interface + implémentation)
├── controller/     → Contrôleurs REST (endpoints /api/...)
└── exception/      → Exceptions métier + GlobalExceptionHandler (réponses d'erreur uniformes)
```

### Frontend (Angular)

```
src/app/
├── core/
│   ├── guards/         # Auth, Role Guards
│   ├── interceptors/   # JWT Interceptor
│   ├── models/         # Interfaces
│   └── services/       # Services HTTP
├── shared/             # Composants partagés
└── features/           # Modules métier
```


## 🔒 Authentification JWT

### Flow

1. **Inscription** → `POST /auth/inscription`
    - Email, Password, Nom, Prénom
    - Retour: `{ token, utilisateur }`

2. **Connexion** → `POST /auth/connexion`
    - Email, Password
    - Retour: `{ token, utilisateur }`

3. **Requêtes protégées**
    - Header: `Authorization: Bearer <token>`
    - L'interceptor Angular ajoute automatiquement

### Rôles

- 🟢 **VISITEUR** - Accès public uniquement
- 🔵 **CANDIDAT** - Utilisateur standard
- 🟣 **GESTIONNAIRE** - Gestion du contenu
- 🔴 **ADMIN** - Accès administrateur complet

---

## 🚨 Erreurs Communes

### CORS Error

**Symptôme**: `No 'Access-Control-Allow-Origin' header`

**Solution**: Le proxy Angular dans `src/proxy.conf.json` redirige automatiquement. Si ça ne marche pas, vérifiez que le backend tourne sur `http://localhost:8080`.

### 401 Unauthorized

**Symptôme**: Erreur après login

**Solution**:
- Vérifiez que le JWT secret dans `application.properties` est correct
- Vérifiez que le token est stocké dans localStorage
- Vérifiez que l'interceptor ajoute le header `Authorization`

### Base de données vide

**Symptôme**: Pas de données après lancement

**Solution**:
- Vérifiez que `spring.jpa.hibernate.ddl-auto=update` est configuré
- Vérifiez que la base existe et est accessible
- Insérez des données de test via SQL ou via l'API

---


## 📋 Checklist Avant Production

- [ ] Backend configuré (`application.properties`)
- [ ] Base de données créée et accessible
- [ ] Frontend configuré (`environment.ts`)
- [ ] Authentification testée
- [ ] JWT secret généré et sécurisé
- [ ] CORS configuré correctement
- [ ] Tous les endpoints testés
- [ ] Guards de route fonctionnels
- [ ] Paiements intégrés (si applicable)
- [ ] Upload/Download de fichiers testés
- [ ] Logs en production désactivés
- [ ] Security headers configurés

---

## 🆘 Support & Dépannage

### Lancer le backend en mode debug
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"
```

### Lancer le frontend en mode debug
```bash
ng serve --poll=2000
```

### Réinitialiser la base de données
```bash
DROP DATABASE labo_academy;
CREATE DATABASE labo_academy;
```

### Nettoyer les caches Maven
```bash
mvn clean
rm -rf ~/.m2/repository
```

---

## 📞 Contacts & Ressources

- **Spring Boot Docs**: https://spring.io/projects/spring-boot
- **Angular Docs**: https://angular.io/docs
- **MySQL Docs**: https://dev.mysql.com/doc/
- **JWT Docs**: https://jwt.io/

---



## Ce qui est livré dans cette première partie

1. **Toutes les 13 entités JPA** du modèle de données du cahier des charges, avec leurs relations
   (OneToMany, ManyToOne, ManyToMany) et leurs commentaires.
2. **Tous les repositories** correspondants.
3. **Sécurité complète** : JWT, filtre d'authentification, configuration des rôles
   (ADMINISTRATEUR / GESTIONNAIRE / CANDIDAT) selon la matrice des droits d'accès du cahier
   des charges.
4. **Module Authentification complet** : inscription candidat, connexion, génération de token JWT.
5. **Module Concours complet** : CRUD + activation/désactivation.
6. **Module Document complet** : ajout avec upload de fichier PDF + image, recherche par mot-clé,
   filtrage par concours/matière/catégorie, documents les plus vendus.
7. **Gestion globale des exceptions** (404, 400, 403, erreurs de validation).

## Modules restants (livrés dans les prochains messages, même pattern à dupliquer)

- Matière / Catégorie (CRUD simple — même structure que Concours)
- Panier / LignePanier (ajout/suppression au panier, calcul du total)
- Commande / Paiement (création de commande, confirmation de paiement,
  déblocage automatique de l'accès aux documents)
- Bibliothèque du candidat / téléchargements sécurisés (vérifie que le document a bien été acheté)
- Quiz / Question / Réponse (création par le gestionnaire)
- Passage de quiz / Résultat (soumission, correction et calcul automatiques)
- Statistiques et tableaux de bord (ventes, quiz réalisés, taux de réussite)
- Gestion des comptes (activation/désactivation, rôles) côté administrateur

## Lancer le projet

1. Créer une base MySQL `labo_academy_db` (ou laisser `createDatabaseIfNotExist=true` le faire).
2. Adapter `src/main/resources/application.yml` (identifiants MySQL).
3. `mvn spring-boot:run`
4. Documentation API : `http://localhost:8080/swagger-ui.html`

## 🔗 Endpoints Intégrés

### ✅ Déjà Implémentés (Frontend + Backend)

| Endpoint | Méthode | Status |
|----------|---------|--------|
| `/auth/inscription` | POST | ✅ Complet |
| `/auth/connexion` | POST | ✅ Complet |
| `/concours` | GET | ✅ Complet |
| `/concours/actifs` | GET | ✅ Complet |
| `/concours/{id}` | GET | ✅ Complet |
| `/documents` | GET | ✅ Complet |
| `/documents/{id}` | GET | ✅ Complet |
| `/documents/rechercher` | GET | ✅ Complet |
| `/documents/filtrer/concours/{id}` | GET | ✅ Complet |
| `/matieres` | GET | ✅ Complet |
| `/categories` | GET | ✅ Complet |
| `/quiz` | GET | ✅ Complet |
| `/quiz/publies` | GET | ✅ Complet |
| `/quiz/{id}` | GET | ✅ Complet |


### 🔨 À Adapter (Structure créée, logique à implémenter)

| Endpoint | Méthode | Statut |
|----------|---------|--------|
| `/panier` | GET | 🔨 À adapter |
| `/panier/ajouter` | POST | 🔨 À adapter |
| `/panier/{ligneId}` | DELETE | 🔨 À adapter |
| `/commandes` | GET | 🔨 À adapter |
| `/commandes` | POST | 🔨 À adapter |
| `/commandes/{id}` | GET | 🔨 À adapter |
| `/paiements` | POST | 🔨 À adapter |
| `/paiements/{id}/confirmer` | PUT | 🔨 À adapter |
| `/bibliotheque` | GET | 🔨 À adapter |
| `/bibliotheque/{id}/download` | GET | 🔨 À adapter |
| `/resultats` | GET | 🔨 À adapter |
| `/resultats/{id}` | GET | 🔨 À adapter |
| `/quiz/{id}/submit` | POST | 🔨 À adapter |
| `/statistiques` | GET | 🔨 À adapter |

---