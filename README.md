# ELIM-project
## Contributeurs
* DZIRI Aghiles
* ROULAMELLAH ken
* BONNY Pierre
## Mise en place du projet
###### Requis :
 * [Java](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
 * [Maven](https://maven.apache.org/)
 * [nodejs](https://nodejs.org/en/)
 * [emulateur sur mobile](https://www.genymotion.com/)
 * [MySQL](https://www.mysql.com/fr/)

##### Démarrer la partie mobile
* Aller dans le dossier ReactProject
* Exécuter npm install
* Dans le fichier ipAdress du dossier utils mettre à jour l'adresse IP pour correspondre à celle du serveur
* Exécuter expo start puis se rendre à l'adresse indiquée dans le navigateur
* Lancer l'application dans un émulateur ou sur un device android ou ios

##### Démarrer la partie server
* Installer les package maven :
  * mvn clean package
  * mvn install
  * mvn exec:java
- cette manipulation peut se faire aussi depuis un IDE :
  * Importer les packages Maven
  * Run la classe Main
