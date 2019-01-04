## Wild adventures (application)

Ceci est le dépôt associé au projet [_Développez une application web de réservation de voyages_](https://openclassrooms.com/fr/projects/developpez-une-application-web-de-reservation-de-voyages)
sur [_OpenClassrooms_](https://www.openclassrooms.com).


### Technologies

- JAVA J2E
- Spring : boot, data
- Maven
- Postgre SQL database
- React JS
- Node JS
- AWS

### Micro-services

#### Micro-services métiers
- `wa-adventure` : gestion des aventures
- `wa-category` : gestion des catégories
- `wa-comment` : gestion des commentaires
- `wa-image` : gestion des contenus visuels
- `wa-order` : gestion des commandes et des paiements
- `wa-user` : gestion des utilisateurs

#### Micro-services edge
- `wa-admin` : administration de l'infrastructure
- `wa-auth` : gestion de l'authentification et génération du token
- `wa-config` : mutualisation de la configuration des microservices
- `wa-eureka` : serveur de registre des instances
- `wa-gateway` : point d'entrée de l'application, router et implémentation du load balancing
- `zipkin` : service de traçage des requêtes

### Diagramme de classes

![](docs/images/Fonctionnel__ClassDiagram.png?raw=true)


### MPD

#### MPD waadventure

![](docs/images/waadventure.png?raw=true)

#### MPD wacategory

![](docs/images/wacategory.png?raw=true)

#### MPD wacomment

![](docs/images/wacomment.png?raw=true)

#### MPD waimage

![](docs/images/waimage.png?raw=true)

#### MPD waorder

![](docs/images/waorder.png?raw=true)

#### MPD wauser

![](docs/images/wauser.png?raw=true)

### Déploiement

Le déploiement est réalisé sur la plateforme aws. L'application est buildé dans un environnement d'intégration continue grâce jenkins. 
L'intégrité du code et le résultat des tests sont analysés par un serveur sonarqube. Enfin le code buildé est déployé immédiatement après le build sur cette plateforme.
Les urls d'accessibilité à ces outils sont :

- `Jenkins` : http://www.wild-adventures.fr:8080
- `SonarQube` : http://www.wild-adventures.fr:9099
- `Application déployée` : http://www.wild-adventures.fr:3000

