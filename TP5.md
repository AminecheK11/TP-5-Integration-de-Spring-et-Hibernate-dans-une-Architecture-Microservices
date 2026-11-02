# TP 5 — Intégration Spring et Hibernate

Projet Maven indépendant com.microservice:microservice-product:1.0-SNAPSHOT.
Java 17, Spring Framework 5.3.20, Hibernate 5.6.9.Final, javax.persistence, MySQL Connector 8.0.33. Pas de Spring Boot.

## Ouvrir et exécuter

Ouvrir pom.xml dans IntelliJ IDEA, sélectionner JDK 17 (ms-17) et importer Maven.
Exécuter com.microservice.product.TestHibernate pour vérifier le contexte et la connexion.
Exécuter com.microservice.product.MainApp pour créer le produit « Microservice Gateway » à 149.99 et démontrer le rollback.

Le répertoire de travail doit être la racine du projet. Les configurations .run sont fournies.
MySQL local doit être démarré sur 3306. application.properties contient la configuration, application-local.properties le mot de passe local et est ignoré par Git. Le mot de passe « secret » de l'exemple est remplacé par celui de la machine dans ce fichier local.
Les propriétés système JVM peuvent surcharger jdbc.url, jdbc.username et jdbc.password.

~~~powershell
.\mvn17.ps1 clean test
.\mvn17.ps1 compile exec:java '-Dexec.mainClass=com.microservice.product.TestHibernate'
.\mvn17.ps1 compile exec:java
.\mvn17.ps1 package
~~~

Le script sélectionne le JDK 17 et Maven installés sur cette machine. Avec JAVA_HOME et PATH configurés, utiliser directement mvn à la place du script.

## Architecture et couverture de l'énoncé

- entity.Product : id Long auto-généré, name non nullable, price double non nullable.
- dao.IDao<T> : signatures save/update/delete/findById/findAll exactes.
- dao.ProductDaoImpl : @Repository et SessionFactory.getCurrentSession().
- config.HibernateConfig : DataSource, LocalSessionFactoryBean, propriétés externes et scan des entités.
- config.TransactionConfig : HibernateTransactionManager et @EnableTransactionManagement.
- config.AppConfig : @ComponentScan("com.microservice.product").
- service.ProductService : interface complétée.
- service.ProductServiceImpl : @Service, injection IDao<Product>, CRUD transactionnel, lectures readOnly.
- TestHibernate : vérification du contexte Spring et requête réelle SELECT DATABASE(), VERSION().
- MainApp : création, lecture, liste et démonstration d'annulation d'un lot.
- ProductServiceTest : CRUD, injection, proxy Spring, validations et rollback sur MySQL réel.

## Corrections et compléments

L'import erroné org.SessionFactory est corrigé en org.hibernate.SessionFactory.
Le pilote JDBC absent du pom initial est ajouté.
L'interface ProductService annoncée dans l'énoncé est créée et implémentée.
proxyTargetClass=true permet de conserver context.getBean(ProductServiceImpl.class) du professeur tout en implémentant l'interface.
Les lectures et écritures passent par le proxy Spring ; le DAO n'ouvre pas lui-même de transaction.
addProducts ajoute une opération atomique de démonstration : le premier INSERT a lieu puis une exception métier annule tout le lot. Les tests vérifient également une erreur SQL après la première insertion.
Les prix négatifs, NaN et infinis sont refusés, ainsi que les noms absents ou blancs.
createDatabaseIfNotExist permet de créer les bases dédiées. allowPublicKeyRetrieval avec useSSL=false est limité à l'usage MySQL localhost de ce TP.
Les contextes sont fermés par try-with-resources.

## Bases et tests

product_db : application, table products. Chaque lancement de MainApp ajoute un produit, conformément au scénario du professeur.
product_db_test : tests uniquement, avec create-drop ; aucune donnée de product_db n'est effacée.
Les tests construisent un vrai contexte Spring avec une configuration de test prioritaire, également lors d'une exécution JUnit dans IntelliJ.
Les erreurs SQL provoquées par les tests sont attendues et vérifiées.

Il s'agit du socle de persistance du microservice demandé : aucune API HTTP, découverte de services ou orchestration n'est ajoutée, ces éléments étant présentés comme perspectives dans l'énoncé.

## Captures utiles

1. Packages, pom.xml et versions Spring/Hibernate.
2. TestHibernate : contexte initialisé et connexion product_db.
3. MainApp : produit ajouté et rollback confirmé avec total inchangé.
4. Table products dans MySQL.
5. Tests JUnit et BUILD SUCCESS.
6. Annotations @Transactional et HibernateTransactionManager.
