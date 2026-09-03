# Labo Academy — Backend (Spring Boot)

Docyline est une application de vente de documents numériques et de préparation aux concours en ligne,
conforme au cahier des charges (Labo Academy, Douala).

## Stack technique
- Java 17, Spring Boot 3.3
- Spring Data JPA (Hibernate) + MySQL
- Spring Security + JWT (authentification stateless)
- Lombok, Bean Validation, Springdoc/Swagger

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

## Endpoints déjà disponibles

| Méthode | URL | Accès |
|---|---|---|
| POST | /api/auth/inscription | Public |
| POST | /api/auth/connexion | Public |
| GET | /api/concours | Public |
| GET | /api/concours/actifs | Public |
| POST/PUT | /api/concours | Admin, Gestionnaire |
| PATCH | /api/concours/{id}/statut | Admin, Gestionnaire |
| GET | /api/documents | Public |
| GET | /api/documents/rechercher?motCle= | Public |
| GET | /api/documents/filtrer/... | Public |
| POST | /api/documents (multipart) | Admin, Gestionnaire |
