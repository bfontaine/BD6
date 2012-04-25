-- CREATE DATABASE BD6;
CREATE TYPE type_p AS ENUM('client', 'gerant', 'douane', 'transporteur', 'emballeur');
CREATE TYPE qualif AS ENUM('normal', 'fragile', 'dangereux');
CREATE TYPE etat_c AS ENUM('normal', 'renvoye' /*, … */);

CREATE TABLE personne(
                  
                  id SERIAL UNIQUE,
                  -- id INTEGER UNIQUE,

                  prenom VARCHAR,
                  nom VARCHAR,
                  
                  /*
                    login = SCAC               pour le transporteur
                          = numéro d'emballeur pour l'emballeur
                  */
                  login VARCHAR UNIQUE,
                  mot_de_passe VARCHAR,
                  
                  type_personne type_p,

                  PRIMARY KEY(id)
);

/*
CREATE TABLE transporteur(
                  
                  id INTEGER UNIQUE,
                  scac CHAR(4),

                  FOREIGN KEY(id) REFERENCES personne
);*/

CREATE TABLE douane(
  
                  id INTEGER UNIQUE,

                  pays VARCHAR,

                  FOREIGN KEY(id) REFERENCES personne(id)
);

/*
CREATE TABLE emballeur(
                  
                  id INTEGER UNIQUE,
                  num CHAR(7),

                  FOREIGN KEY(id) REFERENCES personne
);*/

CREATE TABLE client(

                  id INTEGER UNIQUE,
                  
                  /* nom_societe VARCHAR,
                     suffixe_societe VARCHAR, -> fusionnés en 1 champ: personne.nom */
                  
                  adresse VARCHAR,
                  ville VARCHAR,
                  code_postal VARCHAR,
                  pays VARCHAR,
                  
                  telephone VARCHAR,

                  FOREIGN KEY(id) REFERENCES personne(id)
);


CREATE TABLE catalogue(

                ref VARCHAR UNIQUE,
                -- nom VARCHAR,
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
              
                id INTEGER UNIQUE,
                id_client INTEGER,

                date_commande DATE,
                date_prevue DATE,

                frais FLOAT,

                FOREIGN KEY(id_client) REFERENCES client(id),
                PRIMARY KEY(id)
);

CREATE TABLE commande_produits(

                id_commande INTEGER,
                ref_produit INTEGER,
                quantite INTEGER,

                FOREIGN KEY(id_commande) REFERENCES commande(id),
                FOREIGN KEY(ref_produit) REFERENCES catalogue(ref)
);

CREATE TABLE colis(

                id INTEGER UNIQUE,

                ref_produit VARCHAR,
                quantite INTEGER,

                date_prevue DATE,
                date_emballage DATE,
                date_expedie DATE,
                date_livraison DATE,

                etat etat_c,

                id_commande INTEGER,

                PRIMARY KEY(id),
                FOREIGN KEY(id_commande) REFERENCES commande(id)
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
                id_transporteur INTEGER,
                id_emballeur INTEGER,

                FOREIGN KEY(id_transporteur) REFERENCES personne(id),
                PRIMARY KEY(id)
);

CREATE TABLE container_palettes(

                id_container INTEGER,
                id_palette INTEGER,

                FOREIGN KEY(id_container) REFERENCES container(id),
                FOREIGN KEY(id_palette) REFERENCES palette(id)
);
