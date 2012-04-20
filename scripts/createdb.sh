#! /bin/bash

createdb bd6
psql bd6 -f ../sql/create_db.sql
