#! /bin/bash

echo -n "Création de la base de données 'bd6'... "
createdb bd6 > /dev/null 2>&1 && echo "ok" || echo "fail! "
echo -n "Création des tables... "
psql bd6 -f ../sql/create_db.sql > /dev/null 2>&1 && echo "ok" || echo "fail! "

read -a choix -p "Se connecter ? (o/N) "
[ $choix != "n" ] && psql bd6
