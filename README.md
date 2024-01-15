# Librairies utilisées
On utilise les librairies libGDX, Box2D, Scene2D

libGDX : Permet de créer des jeux en Java

Box2D : Implémente les lois de la physique dans le jeu (gravité, deux objets ne peuvent pas se mélanger, etc...)

Scene2D : Permet de créer des objets graphiques (boutons, images, etc...)

# Compilation sous Windows ou MacOS
Pour compiler le projet sous Windows ou MacOS,
il faut utilise Gradle, et une version de JDK 17 comme
correto-17.

Il suffit ensuite d'utiliser la commande
```bash
gradlew desktop:run
```
dans le répertoire du projet pour lancer le jeu à partir du code source.

Pour compiler le projet en un exécutable, il faut utiliser la commande
```bash
gradlew desktop:dist
```
dans le répertoire du projet. L'exécutable se trouvera dans le répertoire
desktop/build/libs.
Pour l'éxécuter, il faut utiliser la commande
```bash
java -jar desktop-1.0.jar
```

### Attention !
Pour compiler le projet, il faut s'assurer que la variable environnement
JAVA_HOME est bien définie et pointe vers le répertoire d'installation de
JDK 17.
Il faut aussi s'assurer que la variable environnement PATH contient le
répertoire bin de JDK 17 et le répertoire bin de Gradle, à moins
d'appeler Gradle et Java directement avec leur chemin absolu.