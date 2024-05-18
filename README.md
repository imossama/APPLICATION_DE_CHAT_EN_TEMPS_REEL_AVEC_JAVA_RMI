# Cahier des Charges de l'Application de Chat en Temps Réel avec RMI

<div align="center">
   <h3>Image de l'application finale</h3>
   <img src="https://github.com/imossama/APPLICATION_DE_CHAT_EN_TEMPS_REEL_AVEC_JAVA_RMI/assets/119759894/42ce0764-04ea-4b3b-9ff6-f11b5eec67b0" width="500"/>
</div>

Ce projet vise à créer une plateforme de chat permettant à plusieurs utilisateurs de communiquer en temps réel dans une salle de discussion virtuelle. L'application sera basée sur le middleware RMI (Remote Method Invocation) en Java, offrant une expérience de chat fluide et sécurisée.

## Table des Matières
- [Objectifs du Projet](#objectifs)
- [Fonctionnalités](#fonctionnalités)
- [Technologies Utilisées](#technologies-utilisées)
- [Installation et Utilisation](#installation-et-utilisation)

## Objectifs du Projet <a name="objectifs"></a>

Le but principal de ce projet est de fournir une plateforme de chat conviviale et robuste, permettant aux utilisateurs de communiquer en temps réel dans une seule salle de chat. Les objectifs spécifiques incluent :

- Créer une interface utilisateur intuitive pour la saisie des noms d'utilisateur et la navigation dans la salle de chat.
- Utiliser RMI pour permettre la communication entre les différents clients et le serveur de chat.
- Assurer une transmission rapide et fiable des messages entre les utilisateurs.
- Intégrer des fonctionnalités de sécurité pour protéger la confidentialité des utilisateurs et prévenir les attaques malveillantes.

## Fonctionnalités <a name="fonctionnalités"></a>

### Interface Utilisateur

- Permet aux utilisateurs de saisir leur nom d'utilisateur avant de rejoindre la salle de chat.
- Affiche la liste des utilisateurs présents dans la salle de chat.
- Affiche les messages envoyés par les utilisateurs dans la salle de chat.

### Communication en Temps Réel

- Assure une transmission rapide et en temps réel des messages entre les utilisateurs dans la salle de chat.
- Permet aux utilisateurs de voir les nouveaux messages dès qu'ils sont envoyés par d'autres utilisateurs.

### Gestion de la Salle de Chat

- Limite la salle de chat à une seule instance, où tous les utilisateurs se connectent automatiquement.
- Permet aux utilisateurs de quitter la salle de chat à tout moment.

### Gestion des Utilisateurs

- Permet aux utilisateurs de choisir un nom d'utilisateur unique lors de la connexion.
- Gère les cas où plusieurs utilisateurs choisissent le même nom d'utilisateur.

## Technologies Utilisées <a name="technologies-utilisées"></a>

1. **Java**:
   - Langage de programmation principal pour le développement de l'application de chat.
   - Utilisation de Java pour la logique métier, la gestion des interactions utilisateur et la communication réseau.

2. **Java Swing**:
   - Utilisé pour créer l'interface utilisateur graphique de l'application de chat, y compris les zones de texte et les boutons.

3. **RMI (Remote Method Invocation)**:
   - Middleware Java utilisé pour faciliter la communication entre les différents clients et le serveur de chat.
   - Permet l'appel de méthodes distantes entre les composants du système.

## Installation et Utilisation <a name="installation-et-utilisation"></a>

1. Cloner le dépôt GitHub.
2. Compiler les fichiers Java.
3. Exécuter le serveur RMI.
4. Exécuter l'application cliente.
