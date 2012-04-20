# -*- coding: Utf8 -*-

###############################################
# Programme : insertion data.csv ds bd6       #
# autheur : david                             #
#                                             #
###############################################

#Importation de fonction
import os

#Définition des fonctions et variables local
insert_text = ""
l = 0
#type 'client', 'gerant', 'douane', 'transporteur', 'emballeur'
def insert(data):
    global l
    d = data.readline()
    while d != "":
        liste_ligne = d[:len(d)-1].split("|")
        if liste_ligne[0] == "10":
            insert_emballeur(liste_ligne)
        elif liste_ligne[0] == "20":
            insert_client(liste_ligne)
        elif liste_ligne[0] == "30":
            insert_produit(liste_ligne)
        elif liste_ligne[0] == "40":
            insert_transporteur(liste_ligne)
        elif liste_ligne[0] == "50":
            insert_douane(liste_ligne)
        elif liste_ligne[0] == "60":
            insert_gerant(liste_ligne)
        d = data.readline()
        l = l + 1

def insert_emballeur(liste):
    global l
    global insert_text
    if(len(liste) != 6):
        print(l, liste, sep = " :")
    else:
        insert_text += "INSERT INTO personne VALUES(,'"+liste[3]+"','"+liste[2]+"','"+liste[1]+"','"+liste[5]+"','emballeur');\n"

def insert_client(liste):
    global insert_text
    global l
    if(len(liste) != 10):
        print(l, liste, sep = " :")
    else:
        insert_text += "INSERT INTO personne VALUES(,'','"+liste[2]+"','"+liste[3]+"','"+liste[1]+"','"+liste[9]+"','client');\n"
        insert_text += "INSERT INTO client VALUES (,'"+liste[4]+"','"+liste[5]+"' ,'"+liste[6]+"','"+liste[7]+"','"+liste[8]+"' );\n"

def insert_produit(liste):
    global insert_text
    global l
    if(len(liste) != 10):
        print(l, liste, sep = " :")
    else:
        insert_text += "INSERT INTO catalogue VALUES ('"+liste[1]+"','"+liste[2]+"','"+liste[5]+"','"+liste[6]+"','"+liste[8]+"','"+liste[9]+"','"+liste[3]+"','"+liste[4]+"');\n"

def insert_transporteur(liste):
    global insert_text
    global l
    if(len(liste) != 4):
        print(l, liste, sep = " :")
    else:
        insert_text += "INSERT INTO personne VALUES (,'','"+liste[2]+"','"+liste[1]+"','"+liste[3]+"','transporteur');\n"

def insert_douane(liste):
    global insert_text
    global l
    if(len(liste) != 5):
        print(l, liste, sep = " :")
    else:
        insert_text += "INSERT INTO personne VALUES (,'','','"+liste[3]+"','"+liste[4]+"','douane');\n"
        insert_text += "INSERT INTO douane VALUES (,'"+liste[1]+"');\n"

def insert_gerant(liste):
    global insert_text
    global l
    if(len(liste) != 5):
        print(l, liste, sep = " :")
    else:
        insert_text += "INSERT INTO personne VALUES (,'"+liste[1]+"','"+liste[2]+"','"+liste[3]+"','"+liste[4]+"','gerant');\n"

#corps du programme
if __name__ == '__main__':
    data = open('data.csv','r')
    insert(data)
    data.close()
    data_fic = open('data.sql','w')
    data_fic.write(insert_text)
    data_fic.close()
