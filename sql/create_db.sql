-- CREATE DATABASE BD6;
CREATE TYPE type_p AS ENUM('client', 'gerant', 'douane', 'transporteur', 'emballeur');
CREATE TYPE qualif AS ENUM('normal', 'fragile', 'dangereux');
CREATE TYPE etat_c AS ENUM('normal', 'renvoye', 'livre' /*, … */);

CREATE TABLE personne(

                  prenom VARCHAR(255),
                  nom VARCHAR(255),
                  
                  /*
                    login = SCAC               pour le transporteur
                          = numéro d'emballeur pour l'emballeur

                    le login est l'identifiant unique
                  */
                  login VARCHAR UNIQUE NOT NULL,
                  mot_de_passe VARCHAR(255) NOT NULL DEFAULT 'bd6',
                  
                  type_personne type_p NOT NULL,

                  PRIMARY KEY(login)
);

CREATE TABLE douane(
  
                  id VARCHAR(255) UNIQUE NOT NULL,

                  pays VARCHAR(255) NOT NULL,

                  FOREIGN KEY(id) REFERENCES personne(login) ON DELETE CASCADE
);

CREATE TABLE client(

                  id VARCHAR(255) UNIQUE NOT NULL,
                  
                  /* nom_societe VARCHAR,
                     suffixe_societe VARCHAR, -> fusionnés en 1 champ: personne.nom */
                  
                  adresse VARCHAR(255) NOT NULL,
                  ville VARCHAR(255) NOT NULL,
                  /* d'après Wikipedia, un code postal est <= 7 caractères, mais
                     on laisse de la marge à cause des CP fantaisistes de
                     data.csv */
                  code_postal VARCHAR(10) NOT NULL,
                  pays VARCHAR(255) NOT NULL,
                  
                  telephone VARCHAR(255) NOT NULL,

                  FOREIGN KEY(id) REFERENCES personne(login) ON UPDATE CASCADE
);


CREATE TABLE catalogue(

                ref VARCHAR UNIQUE NOT NULL,
                description VARCHAR(255) DEFAULT '',
                qualifiant qualif NOT NULL,

                prix FLOAT CONSTRAINT prix_positif CHECK (prix >= 0),
                poids FLOAT CONSTRAINT poids_positif CHECK (poids >= 0),

                quantite_restante INTEGER CONSTRAINT quantite_positive CHECK (quantite_restante >= 0) DEFAULT 0,

                quantite_par_carton INTEGER CONSTRAINT quantite_par_carton_positive CHECK (quantite_par_carton > 0),
                cartons_par_palette INTEGER CONSTRAINT cartons_par_palette_positif CHECK (cartons_par_palette > 0),

                PRIMARY KEY(ref)
);

CREATE TABLE commande(
              
                id SERIAL UNIQUE NOT NULL,
                id_client VARCHAR NOT NULL,

                date_commande DATE NOT NULL DEFAULT current_date, -- défaut: aujourd'hui
                date_prevue DATE NOT NULL DEFAULT current_date + integer '30', -- défaut: dans 30 jours

                date_livree DATE DEFAULT NULL, -- null tant que non livrée

                frais FLOAT CONSTRAINT frais_positifs CHECK (frais >= 0),
                prix FLOAT CONSTRAINT prix_positif CHECK (prix >= 0) DEFAULT 0,

                FOREIGN KEY(id_client) REFERENCES client(id) ON UPDATE CASCADE,
                PRIMARY KEY(id)
);

CREATE TABLE commande_produits(

                id_commande INTEGER NOT NULL,
                ref_produit VARCHAR NOT NULL,
                quantite INTEGER CONSTRAINT quantite_positive CHECK (quantite > 0),

                FOREIGN KEY(id_commande) REFERENCES commande(id) ON DELETE CASCADE,
                FOREIGN KEY(ref_produit) REFERENCES catalogue(ref) ON DELETE NO ACTION
);

CREATE TABLE colis(

                id SERIAL UNIQUE NOT NULL,

                date_emballage DATE NOT NULL DEFAULT current_date,
                date_expedie DATE DEFAULT NULL,
                date_livraison DATE DEFAULT NULL,

                etat etat_c NOT NULL DEFAULT 'normal',

                qualifiant qualif DEFAULT 'normal', -- mis à jour avec un trigger

                id_commande INTEGER NOT NULL,

                PRIMARY KEY(id),
                FOREIGN KEY(id_commande) REFERENCES commande(id) ON DELETE CASCADE
);

CREATE TABLE colis_produits(

                id_colis INTEGER UNIQUE NOT NULL,
                ref_produit VARCHAR(255) NOT NULL,
                quantite INTEGER CONSTRAINT quantite_positive CHECK (quantite > 0),

                FOREIGN KEY(id_colis) REFERENCES colis(id) ON DELETE CASCADE,
                FOREIGN KEY(ref_produit) REFERENCES catalogue(ref) ON DELETE CASCADE
);

CREATE TABLE palette(
                id SERIAL UNIQUE NOT NULL,
                PRIMARY KEY(id)
);

CREATE TABLE palette_colis(
                
                id_palette INTEGER NOT NULL,
                id_colis INTEGER NOT NULL,

                FOREIGN KEY(id_palette) REFERENCES palette(id) ON DELETE CASCADE,
                FOREIGN KEY(id_colis) REFERENCES colis(id) ON DELETE CASCADE
);

CREATE TABLE container(

                id INTEGER UNIQUE NOT NULL,
                id_transporteur VARCHAR(255),
                id_emballeur VARCHAR(255) NOT NULL,

                FOREIGN KEY(id_transporteur) REFERENCES personne(login) ON DELETE SET NULL,
                FOREIGN KEY(id_emballeur) REFERENCES personne(login),
                PRIMARY KEY(id)
);

CREATE TABLE container_palettes(

                id_container INTEGER NOT NULL,
                id_palette INTEGER NOT NULL,

                FOREIGN KEY(id_container) REFERENCES container(id) ON DELETE CASCADE,
                FOREIGN KEY(id_palette) REFERENCES palette(id) ON DELETE CASCADE
);

-- fonctions

/*
  Met à jour le qualifiant du colis en fonction des produits qu'on y insère:
    - si le colis est 'normal' et qu'on insère un produit fragile,
        le colis devient 'fragile'
    - idem pour 'dangereux'
    - si on insère un produit 'dangereux' dans un colis 'fragile' -> exception
    - idem pour 'fragile' dans 'dangereux'
    - tout se passe normalement si le produit inséré a le même qualifiant que
        le colis
*/

CREATE FUNCTION update_qualif_colis() RETURNS trigger AS $update_qualif_colis$
DECLARE
  qualif_prod qualif := (SELECT qualifiant FROM catalogue WHERE ref=NEW.ref_produit);
  qualif_colis qualif := (SELECT qualifiant FROM colis WHERE id=NEW.id_colis);
BEGIN
  -- on ne peux pas mélanger fragile et dangereux
  IF qualif_prod='fragile' AND qualif_colis='dangereux' THEN
    RAISE EXCEPTION 'Le colis est déjà fragile, impossible d''y mettre un produit dangereux';
  END IF;
  IF qualif_prod='dangereux' AND qualif_colis='fragile' THEN
    RAISE EXCEPTION 'Le colis est déjà dangereux, impossible dE''y mettre un produit fragile';
  END IF;
  -- si on ajoute un produit fragile/dangereux, on met à jour le colis
  IF qualif_prod IN ('fragile','dangereux') AND qualif_colis='normal' THEN
    UPDATE colis SET qualifiant=qualif_prod WHERE id=NEW.id_colis;
  END IF;
  RETURN NEW;
END
$update_qualif_colis$ LANGUAGE plpgsql;

/*
Augmente le prix d'une commande en fonction des ajouts dans commande_produits
*/

CREATE FUNCTION update_prix_commande_up() RETURNS trigger AS $update_prix_commande_up$
DECLARE
  prix_produits FLOAT := NEW.quantite*(SELECT prix FROM catalogue WHERE ref=NEW.ref_produit);
BEGIN
  UPDATE commande SET prix=prix+prix_produits WHERE id=NEW.id_commande;
  RETURN NEW;
END
$update_prix_commande_up$ LANGUAGE plpgsql;

/*
Diminue le prix d'une commande en fonction des suppressions dans commande_produits
*/

CREATE FUNCTION update_prix_commande_down() RETURNS trigger AS $update_prix_commande_down$
DECLARE
  prix_catalogue FLOAT := (SELECT prix FROM catalogue WHERE ref=OLD.ref_produit);
  prix_produits FLOAT := OLD.quantite*prix_catalogue;
  prix_cmd FLOAT := (SELECT prix FROM commande WHERE id=OLD.id_commande);
BEGIN
  -- TODO utiliser 'CASE'
  IF prix_produits > prix_cmd THEN
    UPDATE commande SET prix=0 WHERE id=OLD.id_commande;
    RETURN OLD;
  END IF;
  UPDATE commande SET prix=prix-prix_produits WHERE id=OLD.id_commande;
  RETURN OLD;
END
$update_prix_commande_down$ LANGUAGE plpgsql;

/*
Incrémente la quantité restante dans le catalogue quand on supprime une ligne
dans commande_produits.
*/
CREATE FUNCTION update_qte_catalogue_up() RETURNS trigger AS $update_qte_catalogue_up$
BEGIN
  UPDATE catalogue SET quantite_restante=quantite_restante+OLD.quantite WHERE ref=OLD.ref_produit;
  RETURN OLD;
END
$update_qte_catalogue_up$ LANGUAGE plpgsql;

-- TODO si possible: idem quand on ajoute une ligne (supprimer l'équivalent dans Java)

/*
Supprime une palette quand il n'y a plus aucun colis dedans
*/
CREATE FUNCTION del_palette_vide() RETURNS trigger AS $del_palette_vide$
DECLARE
  nb_colis INTEGER := (SELECT SUM(1) FROM palette_colis WHERE id_palette=OLD.id_palette);
BEGIN
  IF nb_colis=0 THEN
    DELETE FROM palette WHERE id=OLD.id_palette;
  END IF;
  RETURN OLD;
END
$del_palette_vide$ LANGUAGE plpgsql;

/*
Supprime un colis quand il n'y a plus de produits dedans
*/
CREATE FUNCTION del_colis_vide() RETURNS trigger AS $del_colis_vide$
DECLARE
  nb_produits INTEGER := (SELECT SUM(1) FROM colis_produits WHERE id_colis=OLD.id_colis);
BEGIN
  IF nb_produits=0 THEN
    DELETE FROM colis WHERE id=OLD.id_colis;
  END IF;
  RETURN OLD;
END
$del_colis_vide$ LANGUAGE plpgsql;

/*
Quand un colis est marqué 'livre' et que tous les autres correspondant à la même commande
le sont aussi, la commande est marquée comme livrée et les colis sont supprimés.
*/
CREATE FUNCTION livre_colis() RETURNS trigger AS $livre_colis$
BEGIN
  IF NEW.etat<>'livre' THEN
    RETURN NEW; -- si le colis n'est pas livré, ne rien faire
  END IF;
  IF ('livre'=ALL(SELECT etat FROM colis WHERE id_commande=NEW.id_commande)) THEN
    DELETE FROM colis WHERE id_commande=NEW.id_commande;
    UPDATE commande SET date_livree=current_date WHERE id=NEW.id_commande;
  END IF;
  RETURN NEW;
END
$livre_colis$ LANGUAGE plpgsql;

/*
Lors de l'ajout d'un colis sur une palette, lève une exception si le colis
est déjà enregistré sur une autre palette.
*/
CREATE FUNCTION check_colis_palette() RETURNS trigger AS $check_colis_palette$
DECLARE
  deja INTEGER := (SELECT COUNT(*) FROM palette_colis WHERE id_colis=NEW.id_colis AND id_palette<>NEW.id_palette);
BEGIN
  IF deja > 0 THEN
    RAISE EXCEPTION 'Le colis est déjà enregistré sur une autre palette';
  END IF;
  RETURN NEW;
END
$check_colis_palette$ LANGUAGE plpgsql;

/*
Lors de l'ajout d'une palette sur un container, lève une exception si la
palette est déjà enregistrée sur un autre container.
*/
CREATE FUNCTION check_palette_container() RETURNS trigger AS $check_palette_container$
DECLARE
  deja INTEGER := (SELECT COUNT(*) FROM container_palettes WHERE id_palette=NEW.id_palette AND id_container<>NEW.id_container);
BEGIN
  IF deja > 0 THEN
    RAISE EXCEPTION 'La palette est déjà enregistrée sur un autre container';
  END IF;
  RETURN NEW;
END
$check_palette_container$ LANGUAGE plpgsql;

-- triggers

CREATE TRIGGER update_qualif_colis AFTER INSERT OR UPDATE ON colis_produits
  FOR EACH ROW EXECUTE PROCEDURE update_qualif_colis();

CREATE TRIGGER update_prix_commande_up AFTER INSERT ON commande_produits
  FOR EACH ROW EXECUTE PROCEDURE update_prix_commande_up();

CREATE TRIGGER update_prix_commande_down AFTER DELETE ON commande_produits
  FOR EACH ROW EXECUTE PROCEDURE update_prix_commande_down();

CREATE TRIGGER update_qte_catalogue_up AFTER DELETE ON commande_produits
  FOR EACH ROW EXECUTE PROCEDURE update_qte_catalogue_up();

CREATE TRIGGER del_palette_vide AFTER DELETE ON palette_colis
  FOR EACH ROW EXECUTE PROCEDURE del_palette_vide();

CREATE TRIGGER del_colis_vide AFTER DELETE ON colis_produits
  FOR EACH ROW EXECUTE PROCEDURE del_colis_vide();

CREATE TRIGGER livre_colis AFTER UPDATE ON colis
  FOR EACH ROW EXECUTE PROCEDURE livre_colis();

CREATE TRIGGER check_colis_palette BEFORE UPDATE ON palette_colis
  FOR EACH ROW EXECUTE PROCEDURE check_colis_palette();

CREATE TRIGGER check_palette_container BEFORE UPDATE ON container_palettes
  FOR EACH ROW EXECUTE PROCEDURE check_palette_container();
