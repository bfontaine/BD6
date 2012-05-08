-- CREATE DATABASE BD6;
CREATE TYPE type_p AS ENUM('client', 'gerant', 'douane', 'transporteur', 'emballeur');
CREATE TYPE qualif AS ENUM('normal', 'fragile', 'dangereux');
CREATE TYPE etat_c AS ENUM('normal', 'renvoye' /*, … */);

CREATE TABLE personne(

                  prenom VARCHAR,
                  nom VARCHAR,
                  
                  /*
                    login = SCAC               pour le transporteur
                          = numéro d'emballeur pour l'emballeur

                    le login est l'identifiant unique
                  */
                  login VARCHAR UNIQUE,
                  mot_de_passe VARCHAR,
                  
                  type_personne type_p,

                  PRIMARY KEY(login)
);

CREATE TABLE douane(
  
                  id VARCHAR UNIQUE,

                  pays VARCHAR,

                  FOREIGN KEY(id) REFERENCES personne(login)
);

CREATE TABLE client(

                  id VARCHAR UNIQUE,
                  
                  /* nom_societe VARCHAR,
                     suffixe_societe VARCHAR, -> fusionnés en 1 champ: personne.nom */
                  
                  adresse VARCHAR,
                  ville VARCHAR,
                  code_postal VARCHAR,
                  pays VARCHAR,
                  
                  telephone VARCHAR,

                  FOREIGN KEY(id) REFERENCES personne(login)
);


CREATE TABLE catalogue(

                ref VARCHAR UNIQUE,
                description VARCHAR,
                qualifiant qualif,

                prix FLOAT,
                poids FLOAT,

                quantite_restante INTEGER,

                quantite_par_carton INTEGER,
                cartons_par_palette INTEGER,

                PRIMARY KEY(ref)
);

CREATE TABLE commande(
              
                id SERIAL UNIQUE,
                id_client VARCHAR,

                date_commande DATE,
                date_prevue DATE,

                frais FLOAT,

                FOREIGN KEY(id_client) REFERENCES client(id),
                PRIMARY KEY(id)
);

CREATE TABLE commande_produits(

                id_commande INTEGER,
                ref_produit VARCHAR,
                quantite INTEGER,

                FOREIGN KEY(id_commande) REFERENCES commande(id),
                FOREIGN KEY(ref_produit) REFERENCES catalogue(ref)
);

CREATE TABLE colis(

                id SERIAL UNIQUE,

                date_emballage DATE,
                date_expedie DATE,
                date_livraison DATE,

                etat etat_c,

                id_commande INTEGER,

                PRIMARY KEY(id),
                FOREIGN KEY(id_commande) REFERENCES commande(id)
);

CREATE TABLE colis_produits(

                id_colis INTEGER UNIQUE,
                ref_produit VARCHAR,
                quantite INTEGER,

                FOREIGN KEY(id_colis) REFERENCES colis(id),
                FOREIGN KEY(ref_produit) REFERENCES catalogue(ref)
);

CREATE TABLE palette(
                id INTEGER UNIQUE,
                /* … */

                PRIMARY KEY(id)
);

CREATE TABLE palette_colis(
                
                id_palette INTEGER,
                id_colis INTEGER,

                FOREIGN KEY(id_palette) REFERENCES palette(id),
                FOREIGN KEY(id_colis) REFERENCES colis(id)
);

CREATE TABLE container(

                id INTEGER UNIQUE,
                id_transporteur VARCHAR,
                id_emballeur VARCHAR,

                FOREIGN KEY(id_transporteur) REFERENCES personne(login),
                FOREIGN KEY(id_emballeur) REFERENCES personne(login),
                PRIMARY KEY(id)
);

CREATE TABLE container_palettes(

                id_container INTEGER,
                id_palette INTEGER,

                FOREIGN KEY(id_container) REFERENCES container(id),
                FOREIGN KEY(id_palette) REFERENCES palette(id)
);
