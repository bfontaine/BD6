#! /usr/bin/python3.2
# -*- coding: UTF-8 -*-

import json

INPUT_FILE = '../data/data.csv'
OUTPUT_FILE = 'data.sql'

f = open(INPUT_FILE, 'r')

# donnees lues dans le CSV, à transformer en insertions dans la BDD
data_dict = {
        'emballeur':[],
        'client':[],
        'produit':[],
        'transporteur':[],
        'douane':[],
        'gerant':[]
}

# types des lignes dans le CSV
data_types = {
        '10': 'emballeur',
        '20': 'client',
        '30': 'produit',
        '40': 'transporteur',
        '50': 'douane',
        '60': 'gerant'
}

# champs utilisés dans le CSV
data_fields = {
        'emballeur': {
            1: 'numero',
            2: 'nom',
            3: 'prenom',
            5: 'mdp'
        },
        'client': {
            1: 'numero',
            2: 'nom societe',
            3: 'suffix societe',
            4: 'adresse',
            5: 'ville',
            6: 'cp',
            7: 'pays',
            8: 'tel',
            9: 'mdp'
        },
        'produit': {
            1: 'numero',
            2: 'desc',
            3: 'qte par carton',
            4: 'cartons par palette',
            5: 'qualifiant',
            6: 'prix',
            8: 'poids',
            9: 'reserve'
        },
        'transporteur': {
            1: 'numero',
            2: 'nom',
            3: 'mdp'
        },
        'douane': {
            1: 'pays',
            3: 'numero',
            4: 'mdp'
        },
        'gerant': {
            1: 'prenom',
            2: 'nom',
            3: 'numero',
            4: 'mdp'
        }
}

# converti un qualifiant entre la lettre donnee
# et la valeur dans la BDD
qualif = {
    'F': 'fragile',
    'D': 'dangereux',
    'N': 'normal'
}

# champs à remplir dans la base de données, pour chaque table
db_fields = {
 'personne' : ['prenom', 'nom', 'login', 'mot_de_passe', 'type_personne'],
 'douane'   : ['id', 'pays'],
 'client'   : ['id', 'adresse', 'ville', 'code_postal', 'pays', 'telephone'],
 'catalogue': ['ref', 'description', 'qualifiant', 'prix', 'poids',
                'quantite_restante', 'quantite_par_carton', 'cartons_par_palette']
}

# lecture du CSV, et remplissage du dictionnaire `data_dict`
line = f.readline()
while (line != ''):
    fields = line.strip("\n").split('|')

    l_type = data_types[fields[0]]

    values = {}

    for i, name in data_fields[l_type].items():
        values[name] = fields[i]

    data_dict[l_type].append(values)

    line = f.readline()

f.close()

# formate les valeurs des colonnes pour l'insertion dans la BDD
def format_values(vals):
    for i,e in enumerate(vals):
        # si la valeur est une expression (ex: (SELECT … FROM …),
        # on ne l'entoure pas de guillemets, pour toutes les
        # autres on ajoute des guillemets (PgSQL se charge de convertir
        # les nombres, ex: "5"->5)
        if (e[0] != '('):
            vals[i] = "'"+e+"'"

    return ','.join(vals)

# liste des insertions dans la BDD
# sous la forme: (<type>, [ <champs>, … ])
insertions = []

# pour chaque type de données dans `data_dict`, on ajoute les bonnes
# informations pour l'insertion dans la BDD
for emb in data_dict['emballeur']:
    insertions.append(('personne',
        [
            emb['prenom'], # prenom
            emb['nom'],    # nom
            emb['numero'], # login
            emb['mdp'],    # mot_de_passe
            'emballeur'    # type
        ]
    ))


for cli in data_dict['client']:
    insertions.append(('personne',
        [
            'NULL',                                                # prenom
            ' '.join([cli['nom societe'], cli['suffix societe']]), # nom
            cli['numero'],                                         # login
            cli['mdp'],                                            # mot_de_passe
            'client'                                               # type
        ]
    ))
    insertions.append(('client',
        [
            '(SELECT last_value FROM personne_id_seq)', # id->personne.id
            cli['adresse'],                             # adresse
            cli['ville'],                               # ville
            cli['cp'],                                  # code_postal
            cli['pays'],                                # pays
            cli['tel']                                  # telephone
        ]
    ))

for pro in data_dict['produit']:
    insertions.append(('catalogue',
        [
            pro['numero'],             # ref
            pro['desc'],               # description
            qualif[pro['qualifiant']], # qualifiant
            pro['prix'],               # prix
            pro['poids'],              # poids
            pro['reserve'],            # quantite_restante
            pro['qte par carton'],     # quantite_par_carton
            pro['cartons par palette'] # cartons_par_palette
        ]
    ))

for trans in data_dict['transporteur']:
    insertions.append(('personne',
        [
           'NULL',          # prenom
           'NULL',          # nom
           trans['numero'], # login
           trans['mdp'],    # mot_de_passe
           'transporteur'   # type
        ]
    ))

for douane in data_dict['douane']:
    insertions.append(('personne',
        [
            'NULL',           # prenom
            'NULL',           # nom
            douane['numero'], # login
            douane['mdp'],    # mot_de_passe
            'douane'          # type
        ]
    ))
    insertions.append(('douane',
        [
            '(SELECT last_value FROM personne_id_seq)', # id->personne.id
            douane['pays']                              # pays
        ]
    ))

for ger in data_dict['gerant']:
    insertions.append(('personne',
        [
            ger['prenom'], # prenom
            ger['nom'],    # nom
            ger['numero'], # login
            ger['mdp'],    # mot_de_passe
            'gerant'       # type
        ]
    ))

f = open(OUTPUT_FILE, 'w')

# on ecrit les insertions dans le fichier de sortie
for insertion in insertions:
    table = insertion[0]
    # noms des champs
    labels = ','.join(db_fields[table])
    # valeurs des champs
    values = format_values(insertion[1])

    line = "INSERT INTO %s (%s) VALUES(%s);\n" % (table, labels, values)
    f.write(line)

f.close()
