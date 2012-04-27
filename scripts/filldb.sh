#! /bin/bash

echo -n 'CSV->SQL...'
./csv2sql.py && echo 'ok' || echo 'fail! '
echo -n 'exécution des requêtes...'
psql bd6 -f ./data.sql >/dev/null 2>./filldb.log && echo 'ok' || echo 'fail! '

if [ -s ./filldb.log ]; then
    echo 'Des erreurs sont survenues, consultez le fichier "filldb.log" pour'
    echo "plus d'informations."
else
    rm -f ./filldb.log
fi
