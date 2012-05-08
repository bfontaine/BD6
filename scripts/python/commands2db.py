#! /usr/bin/python3.2
# -*- coding: UTF-8 -*-

import random as rd
import datetime as dt
import time

CSV_FILE    = '../data/data.csv'
OUTPUT_FILE = '../sql/commands.sql'

# génère 250 commandes de façon aléatoire
#
# - 180 commandes complètes (expédiées)
# - 20 commandes à moitié expédiées
# - 50 commandes non expédiées
#

# liste des insertions à faire dans la BDD
# sous la forme: (<nom table>, [ <valeurs>, … ])
insertions = []

# id de la derniere commande
last_cmd_id = 0

# labels par tables
labels = {
    'commande': ['id_client', 'date_commande', 'date_prevue', 'frais'],
    'commande_produits': ['id_commande', 'ref_produit', 'quantite'],
}

f = open(CSV_FILE, 'r')
csv = f.read().split("\n")
f.close()

# références de tous les produits
refs_produits = [li.split("|")[1] for li in csv if li.startswith("30|")]
# ids de tous les clients
ids_clients = [li.split("|")[1] for li in csv if li.startswith("20|")]

# 50 commandes non expédiées
for i in range(50):
    id_client = rd.choice(ids_clients)
    nb_produits = int(rd.random()*50)+1 # 1-50 produits par commande
    # date_prevue = dans 1 à 30 jours
    date_prevue = time.time() + int(rd.random()*30+1)*24*3600
    date_prevue = dt.date.fromtimestamp(date_prevue).isoformat()
    frais = rd.random()*20 # [0,20[

    insertions.append(('commande',
        [id_client, dt.date.today().isoformat(), date_prevue, frais]
    ))
    last_cmd_id += 1

    for j in range(nb_produits):
        ref_produit = rd.choice(refs_produits)
        quantite = int(rd.random()*100)+1 # 1-100 produits du meme type
        insertions.append(('commande_produits',
            [last_cmd_id, ref_produit, quantite]
        ))

# 20 commandes à moitié expédiées : TODO

# insertions de tout
f = open(OUTPUT_FILE, 'w')

for ins in insertions:
    table = ins[0]
    labels_ = ','.join(labels[ins[0]])
    values = "'"+"','".join([str(e) for e in ins[1]])+"'"
    l = "INSERT INTO %s (%s) VALUES(%s)" % (table, labels_, values)
    f.write(l+";\n")

f.close()
