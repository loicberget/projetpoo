# Création du personnage et de la plateforme 
J'utilise les librairies Box2D, AI, Freetype, Tools, Ashley, Box2Dlights
## Compilation sous Windows
A la création d'un projet libGDX si les warnings suivants apparaissent :  
warning: [options] source value 7 is obsolete and will be removed in a future release
warning: [options] target value 7 is obsolete and will be removed in a future release
warning: [options] To suppress warnings about obsolete options, use -Xlint:-options.
3 warnings
Aller dans core -> src [main] -> build.gradle et changer sourceCompatibility : 1.7 pour 1.8
