
# TP 5 : Intégration de Spring et Hibernate dans une Architecture Microservices

**Réalisé par : Amine Chekkouri**

## Objectif

Ce TP consiste à intégrer Spring et Hibernate pour gérer la persistance des données, l'injection de dépendances et les transactions dans une architecture organisée en couches.

## Technologies utilisées

- Java 17
- Spring Framework 5.3.20
- Hibernate 5.6.9.Final
- MySQL 8
- Maven
- IntelliJ IDEA

## 1. Structure du projet

Le projet est organisé en plusieurs couches : configuration, DAO générique, entité et service métier.

<img width="313" height="521" alt="cap1_tp5" src="https://github.com/user-attachments/assets/1fc80603-f4a2-4b33-b446-274911bd1e20" />


## 2. Initialisation de Spring et Hibernate

L'exécution de TestHibernate confirme l'initialisation du contexte Spring et la connexion à la base de données MySQL.

!<img width="419" height="298" alt="cap2_tp5" src="https://github.com/user-attachments/assets/6fc6994d-4013-408c-a1df-df6d59b21171" />


## 3. Exécution de MainApp

L'application permet d'enregistrer et de consulter les produits via Hibernate. Le rollback Spring est également vérifié, garantissant l'annulation des modifications en cas d'erreur.

<img width="512" height="561" alt="cap3 1_tp5" src="https://github.com/user-attachments/assets/bfb10145-2426-4b1a-bf5d-e46c345e41ba" />


<img width="459" height="334" alt="cap3 2_tp5" src="https://github.com/user-attachments/assets/7220d90f-0650-4fa7-b5fd-820861683f04" />


## 4. Vérification de la base de données MySQL

La base product_db contient la table products avec les produits enregistrés et leurs prix.

<img width="982" height="655" alt="cap4_tp5" src="https://github.com/user-attachments/assets/02a6a2d1-d401-4142-b9c4-87083218e802" />


## 5. Tests Maven

Les tests automatisés ont été exécutés avec succès : 16 tests réussis, aucune erreur et BUILD SUCCESS.

<img width="878" height="457" alt="cap5_tp5" src="https://github.com/user-attachments/assets/591c0e5b-7554-40ce-a205-158c492dda2f" />


## Conclusion

Ce TP a été l'occasion de mettre en pratique l'intégration de Spring et Hibernate, le pattern DAO générique et la gestion des transactions, tout en assurant la persistance des données dans MySQL.
