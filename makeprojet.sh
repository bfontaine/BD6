#! /bin/bash

TGZ=DavidGalichetBaptisteFontaine.tar.gz
DIR=DavidGalichetBaptisteFontaine

mkdir -p $DIR
cp rapport.pdf $DIR/
cp -r data $DIR/data
cp -r java $DIR/java
cp -r scripts $DIR/scripts
cp -r sql $DIR/sql
rm -f $DIR/*~ $DIR/java/*.class $DIR/*/*~ $DIR/*/*/*~
rm -f $DIR/sql/data.sql
rm -f $DIR/data/catalogue.csv
tar czf $TGZ $DIR
rm -Rf $DIR
