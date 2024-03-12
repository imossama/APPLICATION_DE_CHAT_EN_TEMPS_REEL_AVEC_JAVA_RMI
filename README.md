### Cahier des Charges - Application de Chat en Temps Réel avec RMI

**Introduction**

L'Application de Chat en Temps Réel avec RMI est un projet visant à créer une plateforme de chat permettant à plusieurs utilisateurs de communiquer en temps réel dans une salle de discussion virtuelle. Cette application sera basée sur le middleware RMI (Remote Method Invocation) en Java, offrant une expérience de chat fluide et sécurisée.

**Objectifs du Projet**

Le but principal de ce projet est de fournir une plateforme de chat conviviale et robuste, permettant aux utilisateurs de communiquer en temps réel dans une seule salle de chat. Les objectifs spécifiques incluent :
- Créer une interface utilisateur intuitive pour la saisie des noms d'utilisateur et la navigation dans la salle de chat.
- Utiliser RMI pour permettre la communication entre les différents clients et le serveur de chat.
- Assurer une transmission rapide et fiable des messages entre les utilisateurs.
- Intégrer des fonctionnalités de sécurité pour protéger la confidentialité des utilisateurs et prévenir les attaques malveillantes.

**Exigences Fonctionnelles**

1. **Interface Utilisateur**:
   - Permettre aux utilisateurs de saisir leur nom d'utilisateur avant de rejoindre la salle de chat.
   - Afficher la liste des utilisateurs présents dans la salle de chat.
   - Afficher les messages envoyés par les utilisateurs dans la salle de chat.

2. **Communication en Temps Réel**:
   - Assurer une transmission rapide et en temps réel des messages entre les utilisateurs dans la salle de chat.
   - Permettre aux utilisateurs de voir les nouveaux messages dès qu'ils sont envoyés par d'autres utilisateurs.

3. **Gestion de la Salle de Chat**:
   - Limiter la salle de chat à une seule instance, où tous les utilisateurs se connectent automatiquement.
   - Permettre aux utilisateurs de quitter la salle de chat à tout moment.

4. **Gestion des Utilisateurs**:
   - Authentifier les utilisateurs lors de leur connexion pour garantir la sécurité du système.
   - Permettre aux utilisateurs de choisir un nom d'utilisateur unique lors de la connexion.
   - Gérer les cas où plusieurs utilisateurs choisissent le même nom d'utilisateur.

**Exigences Non-Fonctionnelles**

1. **Performance**:
   - Assurer une faible latence dans la transmission des messages entre les utilisateurs.
   - Optimiser les performances du système pour gérer un grand nombre d'utilisateurs simultanés.

2. **Sécurité**:
   - Utiliser des mécanismes d'authentification robustes pour vérifier l'identité des utilisateurs lors de leur connexion.
   - Chiffrer les messages envoyés entre les utilisateurs pour protéger la confidentialité des communications.

3. **Interface Utilisateur**:
   - Concevoir une interface utilisateur attrayante et conviviale pour améliorer l'expérience utilisateur.
   - Utiliser des éléments graphiques intuitifs pour faciliter la navigation dans l'application.

**Technologies Utilisées**

1. **Java**:
   - Langage de programmation principal pour le développement de l'application de chat.
   - Utilisation de Java pour la logique métier, la gestion des interactions utilisateur et la communication réseau.

2. **Java Swing**:
   - Utilisé pour créer l'interface utilisateur graphique de l'application de chat, y compris les zones de texte et les boutons.

3. **RMI (Remote Method Invocation)**:
   - Middleware Java utilisé pour faciliter la communication entre les différents clients et le serveur de chat.
   - Permet l'appel de méthodes distantes entre les composants du système.

**Conclusion**

L'Application de Chat en Temps Réel avec RMI vise à offrir une plateforme de communication efficace et sécurisée pour les utilisateurs. En utilisant Java, Java Swing et RMI, nous créerons une application de chat robuste et conviviale, répondant aux exigences fonctionnelles et non fonctionnelles énoncées dans ce cahier des charges.

*Crédit*

L'Application de Chat en Temps Réel avec RMI est développé par OSSAMA ETTAQAFI avec la contribution des membres suivants :
- Ali GHOUFRANE
- Soufiane LAAROUSSI

Ce projet est réalisé dans le cadre du cours universitaire sur les Systèmes Distribués.
