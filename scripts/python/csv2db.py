#! /usr/bin/python3.2
# -*- coding: UTF-8 -*-

import getpass
import postgresql as pg # paquet 'python3-postgresql'

INPUT_FILE = '../data/data.csv'

BASE_NAME = 'bd6'
HOST_NAME = 'localhost'
USER_NAME = getpass.getuser() # nom d'utilisateur courant

db = None

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
            3: 'suffixe societe',
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

# formate les valeurs des colonnes pour l'insertion dans la BDD
def format_values(vals):
    for i,e in enumerate(vals):
        # si la valeur est une expression (ex: (SELECT … FROM …),
        # on ne l'entoure pas de guillemets, pour toutes les
        # autres on ajoute des guillemets (PgSQL se charge de convertir
        # les nombres, ex: "5"->5)
        if (e[0] != '(' and e != 'NULL'):
            vals[i] = "'"+e+"'"

    return ','.join(vals)

def insert(table, values):
    # noms des champs
    labels = ','.join(db_fields[table])
    # valeurs des champs
    values = format_values(values)
    # prepare la requete
    s = "INSERT INTO %s (%s) VALUES(%s);"
    ps = db.prepare(s % (table, labels, values))
    # execute la requete
    ps()

def insert_emballeur(emb):
    chps = ['prenom', 'nom', 'numero', 'mdp']

    # prenom, nom, login, mot_de_passe, type
    insert('personne', [emb[c] for c in chps]+['emballeur'])

def insert_produit(pro):
    pro['qualifiant'] = qualif[pro['qualifiant']]
    chps = ['numero', 'desc', 'qualifiant', 'prix', 'poids', 'reserve', \
                'qte par carton', 'cartons par palette']

    # ref, description, qualifiant, prix, poids,
    #  quantite_restante, quantite_par_carton, cartons_par_palette
    insert('catalogue', [pro[c] for c in chps])

def insert_transporteur(trans):
    # prenom, nom, login, mot_de_passe, type
    insert('personne',['NULL','NULL',trans['numero'],trans['mdp'],'transporteur'])

def insert_douane(douane):
    chps = ['numero', 'pays']

    # prenom, nom, login, mot_de_passe, type
    insert('personne', ['NULL', 'NULL',douane['numero'],douane['mdp'],'douane'])
    # id->personne.login, pays
    insert('douane', [douane[c] for c in chps])

def insert_gerant(ger):
    chps = ['prenom', 'nom', 'numero', 'mdp']

    # prenom, nom, login, mot_de_passe, type
    insert('personne', [ger[c] for c in chps]+['gerant'])

def insert_client(cli):
    nom = ' '.join([cli['nom societe'], cli['suffixe societe']])
    chps = ['numero', 'adresse', 'ville', 'cp', 'pays', 'tel']

    # prenom, nom, login, mot_de_passe, type
    insert('personne', ['NULL', nom, cli['numero'], cli['mdp'], 'client'])
    # id->personne.login, adresse,ville, code_postal, pays, telephone
    insert('client', [cli[c] for c in chps])

if __name__ == '__main__':

    db = pg.open('pq://%s@%s/%s' % (USER_NAME, HOST_NAME, BASE_NAME))
    print('Connexion à la base de données: ok')

    # lecture du CSV, et exécution des requêtes d'insertion
    f = open(INPUT_FILE, 'r')
    line = f.readline()
    nb_req = 0
    while (line != ''):
        fields = line.strip("\n").split('|')

        l_type = data_types[fields[0]]

        values = {}

        for i, name in data_fields[l_type].items():
            values[name] = fields[i]

        # appelle la bonne methode
        {'client':insert_client,
         'douane':insert_douane,
         'emballeur':insert_emballeur,
         'gerant':insert_gerant,
         'produit':insert_produit,
         'transporteur':insert_transporteur}[l_type](values)
        nb_req += 1

        line = f.readline()

    f.close()
    print('%d requêtes effectuées avec succès.' % nb_req)
    db.close()
    print('Fermeture de la connexion: ok')
