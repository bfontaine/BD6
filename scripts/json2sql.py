#! /usr/bin/python3.2
#! -*- coding: UTF-8 -*-

INPUT_FILE = 'data.json'
OUTPUT_FILE = 'data.sql'

# converti un qualifiant entre la lettre donnee
# et la valeur dans la BDD
qualif = {
    'F': 'fragile',
    'D': 'dangereux',
    'N': 'normal'
}


# champs dans la base de données, pour chaque table
db_fields = {
 'personne' : ['prenom', 'nom', 'login', 'mot_de_passe', 'type_personne'],
 'douane'   : ['id', 'pays'],
 'client'   : ['id', 'adresse', 'ville', 'code_postal', 'pays', 'telephone'],
 'catalogue': ['ref', 'description', 'qualifiant', 'prix', 'poids',
                'quantite_restante', 'quantite_par_carton', 'cartons_par_palette']
}

def format_values(vals):
    vals2 = list(vals)
    for i,e in enumerate(vals2):
        if (e[0] != '('): # expression
            vals2[i] = "'"+e+"'"

    return ','.join(vals2)

import json

f = open(INPUT_FILE, 'r')
j_data = f.read()
f.close()

insertions = []

data = json.loads(j_data)

for emb in data['emballeur']:
    insertions.append(('personne',
        (
            emb['prenom'], # prenom
            emb['nom'],    # nom
            emb['numero'], # login
            emb['mdp'],    # mot_de_passe
            'emballeur'    # type
        )
    ))


for cli in data['client']:
    insertions.append(('personne',
        (
            'NULL',                                                  # prenom
            ' '.join([cli['nom societe'], cli['suffix societe']]), # nom
            cli['numero'],                                         # login
            cli['mdp'],                                            # mot_de_passe
            'client'                                               # type
        )
    ))
    insertions.append(('client',
        (
            '(SELECT last_value FROM personne_id_seq)', # id->personne.id
            cli['adresse'],                             # adresse
            cli['ville'],                               # ville
            cli['cp'],                                  # code_postal
            cli['pays'],                                # pays
            cli['tel']                                  # telephone
        )
    ))

for pro in data['produit']:
    insertions.append(('catalogue',
        (
            pro['numero'],             # ref
            pro['desc'],               # description
            qualif[pro['qualifiant']], # qualifiant
            pro['prix'],               # prix
            pro['poids'],              # poids
            pro['reserve'],            # quantite_restante
            pro['qte par carton'],     # quantite_par_carton
            pro['cartons par palette'] # cartons_par_palette
        )
    ))

for trans in data['transporteur']:
    insertions.append(('personne',
        (
           'NULL',          # prenom
           'NULL',          # nom
           trans['numero'], # login
           trans['mdp'],    # mot_de_passe
           'transporteur'   # type
        )
    ))

for douane in data['douane']:
    insertions.append(('personne',
        (
            'NULL',           # prenom
            'NULL',           # nom
            douane['numero'], # login
            douane['mdp'],    # mot_de_passe
            'douane'          # type
        )
    ))
    insertions.append(('douane',
        (
            '(SELECT last_value FROM personne_id_seq)', # id->personne.id
            douane['pays']                              # pays
        )
    ))

for ger in data['gerant']:
    insertions.append(('personne',
        (
            ger['prenom'], # prenom
            ger['nom'],    # nom
            ger['numero'], # login
            ger['mdp'],    # mot_de_passe
            'gerant'       # type
        )
    ))

f = open(OUTPUT_FILE, 'w')

for insertion in insertions:
    table = insertion[0]
    # noms des champs
    labels = ','.join(db_fields[table])
    # valeurs des champs
    values = format_values(insertion[1])

    line = "INSERT INTO %s (%s) VALUES(%s);\n" % (table, labels, values)
    f.write(line)

f.close()
