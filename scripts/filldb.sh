#! /bin/bash

echo -n 'CSV->SQL...'
./python/csv2sql.py && echo 'ok' || echo 'fail! '
echo -n 'exécution des requêtes...'
psql bd6 -f ../sql/data.sql >/dev/null 2>./filldb.log && echo 'ok' || echo 'fail! '
echo -n 'importation du catalogue CSV...'
psql bd6 -c "\copy catalogue from '../data/catalogue.csv' delimiter as '|'" \
    >/dev/null 2>./filldb.log && echo 'ok' || echo 'fail! '

if [ -s ./filldb.log ]; then
    echo 'Des erreurs sont survenues, consultez le fichier "filldb.log" pour'
    echo "plus d'informations."
else
    rm -f ./filldb.log
fi
