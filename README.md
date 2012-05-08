Projet BD6
==========

Remplissage de la base de données:

    $ cd scripts
    $ ./createdb.sh # crée la base BD6 avec toutes les tables
    $ ./filldb.sh   # remplie la base avec les lignes de data.csv

Si la base existe déjà, pour la réinitialiser utilisez:

    $ cd scripts
    $ ./dropdb.sh

Ne pas oublier d'installer le paquet `python3-postgresql` pour le remplissage de
la base de données.
