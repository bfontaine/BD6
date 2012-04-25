#! /usr/bin/python3.2
# -*- coding: UTF-8 -*-

import json

INPUT_FILE = '../data/data.csv'
OUTPUT_FILE = 'data.json'


f = open(INPUT_FILE, 'r')

data_dict = {
        'emballeur':[],
        'client':[],
        'produit':[],
        'transporteur':[],
        'douane':[],
        'gerant':[]
}

data_types = {
        '10': 'emballeur',
        '20': 'client',
        '30': 'produit',
        '40': 'transporteur',
        '50': 'douane',
        '60': 'gerant'
}

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

json_data = json.dumps(data_dict, sort_keys=True, indent=2)

f = open(OUTPUT_FILE, 'w')
f.write(json_data)
f.close()
