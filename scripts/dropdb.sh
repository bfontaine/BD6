#! /bin/bash

echo -n "Suppression de la base de données 'bd6'... "
dropdb bd6 2> /dev/null && echo "ok" || echo "fail! "
