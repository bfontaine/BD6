#! /bin/bash

rm -f ./filldb.log
echo 'Import du CSV...'
./python/csv2db.py 2>./filldb.log && echo 'ok' || echo 'fail! '

if [ -s ./filldb.log ]; then
    echo 'Des erreurs sont survenues, consultez le fichier "filldb.log" pour'
    echo "plus d'informations."
else
    rm -f ./filldb.log
fi
