-- CREATE DATABASE BD6;
CREATE TYPE type_p AS ENUM('client', 'gerant', 'douane', 'transporteur', 'emballeur');
CREATE TYPE qualif AS ENUM('normal', 'fragile', 'dangereux');
CREATE TYPE etat_c AS ENUM('normal', 'renvoye' /*, … */);

CREATE TABLE personne(
                  
                  id INT UNIQUE,

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
                  
                  id INT UNIQUE,
                  scac CHAR(4),

                  FOREIGN KEY(id) REFERENCES personne
);*/

CREATE TABLE douane(
  
                  id INT UNIQUE,

                  pays VARCHAR,

                  FOREIGN KEY(id) REFERENCES personne(id)
);

/*
CREATE TABLE emballeur(
                  
                  id INT UNIQUE,
                  num CHAR(7),

                  FOREIGN KEY(id) REFERENCES personne
);*/

CREATE TABLE client(

                  id INT UNIQUE,
                  
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

                id INT UNIQUE,
                nom VARCHAR,
                description VARCHAR,
                qualifiant qualif,

                prix FLOAT,
                poids FLOAT,

                quantite_restante INT,

                quantite_par_carton INT,
                cartons_par_palette INT,

                PRIMARY KEY(id)
);

CREATE TABLE commande(
              
                id INT UNIQUE,
                id_client INT,

                date_commande DATE,
                date_prevue DATE,

                frais FLOAT,

                FOREIGN KEY(id_client) REFERENCES client(id),
                PRIMARY KEY(id)
);

CREATE TABLE commande_produits(

                id_commande INT,
                id_produit INT,
                quantite INT,

                FOREIGN KEY(id_commande) REFERENCES commande(id),
                FOREIGN KEY(id_produit) REFERENCES catalogue(id)
);

CREATE TABLE colis(

                id INT UNIQUE,
                qualifiant qualif,

                date_prevue DATE,
                date_emballage DATE,
                date_expedie DATE,
                date_livraison DATE,

                etat etat_c,

                id_commande INT,

                PRIMARY KEY(id),
                FOREIGN KEY(id_commande) REFERENCES commande(id)
);

CREATE TABLE palette(
                id INT UNIQUE,
                /* … */

                PRIMARY KEY(id)
);

CREATE TABLE palette_colis(
                
                id_palette INT,
                id_colis INT,

                FOREIGN KEY(id_palette) REFERENCES palette(id),
                FOREIGN KEY(id_colis) REFERENCES colis(id)
);

CREATE TABLE container(

                id INT UNIQUE,
                id_transporteur INT,
                id_emballeur INT,

                FOREIGN KEY(id_transporteur) REFERENCES personne(id),
                PRIMARY KEY(id)
);

CREATE TABLE container_palettes(

                id_container INT,
                id_palette INT,

                FOREIGN KEY(id_container) REFERENCES container(id),
                FOREIGN KEY(id_palette) REFERENCES palette(id)
);
