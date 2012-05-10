#! /bin/bash

rm -f ./createtables.log

echo -n "Création de la base de données 'bd6'... "
createdb bd6 > /dev/null 2>&1 && echo "ok" || echo "fail! "
echo -n "Ajout du langage PL/PgSQL ... "
createlang plpgsql bd6 > /dev/null 2>&1 && echo "ok" || echo "fail! "
echo -n "Création des tables... "
psql bd6 -f ../sql/create_db.sql > /dev/null 2>createtables.log && echo "ok" || echo "fail! "

if [ -s ./createtables.log ];then
    if [ $(which sponge) ];then
        grep -v 'NOTICE' ./createtables.log | sponge ./createtables.log
    else
        grep -v 'NOTICE' ./createtables.log > .tmp-log;
        mv .tmp-log ./createtables.log;
    fi
fi

if [ -s ./createtables.log ]; then
    echo 'Des erreurs sont survenues, consultez le fichier "createtable.log" pour'
    echo "plus d'informations."
else
    rm -f ./createtables.log
fi
# read -a choix -p "Se connecter ? (o/N) "
# [ $choix != "n" ] && psql bd6
