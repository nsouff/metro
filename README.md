# Projet itinéraires de métro

## Présentation

Le projet itinéraires de métro est un projet orienté algorithmique.

### But
Le programme doit permettre à un utilisateur de trouver son chemin dans un graphe sous certaines conditions.
Par exemple, trouver un itinéraire dans un réseau de transport tel que le métro.



### Fonctionnalités
#### Recherche de chemin
La principale fonctionnalité est donc de chercher un itinéraire d'une station à une autre, pour cela différent algorithmes sont utilisés.
* Recherche du plus court chemin avec Dijkstra
* Recherche d'un chemin avec le moins de correspondances possible avec l'algorithme de Floyd (attention les pertubations pourront ne pas être prise en compte avec l'utilisation de cet algorithme)
* Recherche d'un chemin avec le moins de correspondance l'algorithme de Bouarah

#### Ajout de perturbation
Il est possible d'ajouter différent type de perturbation qui modifieront le graphe et ainsi modifieront bien sûr les temps et chemins des itinéraires. Un nom par défaut est donnée aux perturbations et il est possible de les annuler.

#### Statistiques
Pour chaque ville il est possible d'afficher différentes statistiques sur le réseaux de cette ville


### Villes disponibles
* Paris
* Marseille
* Lyon
* Lille
* Toulouse
* Rennes
* Orly

## Compilation

### Prérequis
*  Java JDK (>= 8.0)
*  Maven (>= 3.6)

### Instructions
Pour compiler le projet utiliser la commande : `make`


## Utilisation

### Webserver
Pour lancer le webserver entrer : `make run` puis ouvrer votre navigateur et aller sur http://localhost:8080/.
### Version terminal
Il existe une version terminal du projet il vous suffira alors d'enter `make run_terminal`. Attention cependant beaucoup de fonctionnalité de sont pas implémenter dans cette version.
### Exportation d'un graphe
Pour voir la manière dont nous avons modéliser les différents graphe il est possible de les exporter au format dot qui pourront ensuite être converti en format svg ou pdf par exemple. \
Pour cela entrer : `make export_to_dot ARGS="city"`. Cela exportera au format dot le graph de la ville `city`, remplacer donc `city` par une des villes possibles. \
Attention après la conversion du fichier dot le fichier obtenue est énorme et lors de la visualisation il est possible que vous ne voyez rien. Dans ce cas pensez à dé-zoomer plusieurs fois pour voir des parties du schéma. Tout les graphes au format `svg` sont dans `doc/networksModelisation/`.
