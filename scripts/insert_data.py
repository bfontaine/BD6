#!  /usr/bin/python3.2
# -*- coding: UTF-8 -*-

###############################################
# Programme : insertion data.csv ds bd6       #
# Auteur : David                              #
#                                             #
###############################################

#type 'client', 'gerant', 'douane', 'transporteur', 'emballeur'
def insert(data):
    insert_text = ""

    type_data = {
            '10': insert_emballeur,
            '20': insert_client,
            '30': insert_produit,
            '40': insert_transporteur,
            '50': insert_douane,
            '60': insert_gerant
            }

    for i,d in enumerate(data):
        if (d == ""):
            continue

        liste_ligne = d[:-1].split("|")

        # on appelle la bonne methode
        insert_text += type_data[liste_ligne[0]](liste_ligne, i)

    return insert_text

def check_len(liste, length, l):
    if (len(liste) != length):
        raise IndexError(("line %d: liste expected to be %d"
                          +"length, got %d") % (l, length, len(liste)))

def format_values(liste, indices, replacements=None):
    values = [liste[i] for i in indices]
    # si on doit remplacer des valeurs
    if (replacements != None):
        # on les parcourt toutes
        for i,e in enumerate(values):
            # si la valeur courante doit etre remplacee
            if (e in replacements):
                # on la remplace
                values[i] = replacements[e]

    return "'" + "','".join([str(v) for v in values]) + "'"

def insert_emballeur(liste, l):
    check_len(liste, 6, l)
    values = format_values(liste, (3,2,1,5))
    return "INSERT INTO personne VALUES(NULL,%s,'emballeur');\n" % values

def insert_client(liste, l):
    check_len(liste, 10, l)
    person_values = format_values(liste, (2,3,1,9))
    client_values = format_values(liste, (4,5,6,7,8))
    return ("INSERT INTO personne VALUES(NULL,NULL,%s,'client');\n"
    +"INSERT INTO client VALUES (NULL,%s);\n") % (person_values, client_values)

def insert_produit(liste, l):
    check_len(liste, 10, l)
    replacements = {'F':'fragile', 'D':'dangereux', 'N':'normal'}
    values = format_values(liste, (1,2,3,5,6,8,9,3,4), replacements)
    return "INSERT INTO catalogue VALUES (%s);\n" % values

def insert_transporteur(liste, l):
    check_len(liste, 4, l)
    values = format_values(liste, (2,1,3))
    return "INSERT INTO personne VALUES (NULL,NULL,%s);\n" % values

def insert_douane(liste, l):
    check_len(liste, 5, l)
    person_values = format_values(liste, (3,4))
    douane_values = format_values(liste, (1,))
    return ("INSERT INTO personne VALUES (NULL,NULL,NULL,%s,'douane');"
            +"\nINSERT INTO douane VALUES (NULL,%s);"
            +"\n") % (person_values, douane_values)

def insert_gerant(liste, l):
    check_len(liste, 5, l)
    values = format_values(liste, (1,2,3,4))
    return "INSERT INTO personne VALUES (NULL,%s,'gerant');\n" % values

#corps du programme
if __name__ == '__main__':
    data = open('data.csv','r').readlines()
    insert_text = insert(data)
    data_fic = open('data.sql','w').write(insert_text)
