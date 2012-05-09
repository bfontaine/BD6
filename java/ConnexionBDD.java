import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Une connexion à la base de données
 **/
public class ConnexionBDD {
    private Connection co; //la connexion à la base

    private static HashSet<String> typePersonnes = new HashSet<String>();

    static {
        typePersonnes.add("client");
        typePersonnes.add("douane");
        typePersonnes.add("emballeur");
        typePersonnes.add("gerant");
        typePersonnes.add("transporteur");
    }
    
    /**
     * Crée une nouvelle connexion à une base de données locale donnée
     * @param bd nom de la base de données
     * @param utilisateur nom de l'utilisateur
     * @param mdp mot de passe
     **/
    public ConnexionBDD(String bd, String utilisateur, String mdp) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        co = DriverManager.getConnection("jdbc:postgresql://localhost/"+bd,utilisateur,mdp); 
    }
    
    /**
     * Crée une nouvelle connexion à la base de données 'bd6'
     * @param utilisateur nom de l'utilisateur
     * @param mdp mot de passe
     **/
    public ConnexionBDD(String utilisateur, String mdp) throws SQLException, ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        co = DriverManager.getConnection("jdbc:postgresql://localhost/bd6",utilisateur,mdp); 
    }
    
    /**
     * Teste si la connexion de l'utilisateur donné est acceptée
     * @param login login de l'utilisateur
     * @param mdp mot de passe de l'utilisateur
     * @return `true` si le login existe et le mot de passe correspond, `false` sinon
     **/
    public boolean connectUtilisateur(String login, String mdp) {
        try {
            PreparedStatement ps = co.prepareStatement("SELECT mot_de_passe FROM personne WHERE login=?;");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            return (rs.next() && (rs.getString("mot_de_passe").equals(mdp)));

        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Change le prix d'un produit du catalogue
     * @param ref référence du produit
     * @param nouveauPrix nouveau prix du produit
     * @return `true` si tout s'est bien passé, `false` si la
     * référence est mauvaise, et/ou le prix négatif.
     **/
    public boolean changePrix(String ref, float nouveauPrix) {
        if (nouveauPrix < 0)
            return false;

        try {
            PreparedStatement ps = co.prepareStatement("UPDATE catalogue SET prix=? WHERE ref=?;");
            ps.setFloat(1, nouveauPrix);
            ps.setString(2, ref);
            return (ps.executeUpdate() > 0);

        } catch (SQLException e) {
            return false;
        }
    }

    // === Listes === //

    /**
     * Liste tous les employés
     * @return une liste de `HashMap` avec une correspondance entre nom de
     * colonnes et valeurs
     **/
    public LinkedList<HashMap<String,Object>> listeEmployes() {
        return listeEmployes("tous");
    }

    /**
     * Liste les employés selon leur type
     * @param type le type des employés ("tous", "transporteur", "emballeur")
     * @return une liste de `HashMap` avec une correspondance entre nom de
     * colonnes et valeurs
     **/
    public LinkedList<HashMap<String,Object>> listeEmployes(String type) {

        if (type == null)
            return null;

        PreparedStatement ps = null;
        String q = "SELECT prenom,nom,login,type_personne FROM personne";

        if (type.equals("tous")) {
            q += " WHERE type_personne='transporteur'";
            q += " OR type_personne='emballeur'";
        } else {
            q += " WHERE type_personne=?::type_p";
        }
        q += " ORDER BY nom";

        ResultSet rs = null;
        
        try {
            ps = co.prepareStatement(q+";");
            if (!type.equals("tous")) {
                ps.setString(1, type);
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            return null;
        }

        LinkedList<HashMap<String,Object>> liste
            = new LinkedList<HashMap<String,Object>>();

        try {
            while(rs.next()) {
                HashMap<String,Object> hm = new HashMap<String,Object>();

                hm.put("prenom", rs.getString(1));
                hm.put("nom", rs.getString(2));
                hm.put("login", rs.getString(3));
                hm.put("type", rs.getString(4));

                liste.add(hm);
            }
        } catch (SQLException e) {
            return null;
        }

        return liste;
    }

    /**
     * Liste les clients
     * @return une liste de `HashMap` avec une correspondance entre nom de
     * colonnes et valeurs
     **/
    public LinkedList<HashMap<String,Object>> listeClients() {
        return listeClients(false);
    }


    /**
     * Liste les clients
     * @param etendue précise si la liste doit être étendue, par exemple en ajoutant
     * le nombre de commandes faites par ce client, le nombre de produits achetés,
     * le total dépensé, etc.
     * @return une liste de `HashMap` avec une correspondance entre nom de
     * colonnes et valeurs
     **/
    public LinkedList<HashMap<String,Object>> listeClients(boolean etendue) {

        PreparedStatement ps = null;
        String q = "SELECT prenom,nom,login,adresse,ville,code_postal,pays";
        q += ",telephone FROM personne NATURAL JOIN client";
        q += " WHERE personne.login=client.id ORDER BY nom;";

        ResultSet rs = null;
        ResultSet rs2 = null;
        
        try {
            ps = co.prepareStatement(q);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            return null;
        }

        LinkedList<HashMap<String,Object>> liste
            = new LinkedList<HashMap<String,Object>>();

        try {
            while(rs.next()) {
                HashMap<String,Object> hm = new HashMap<String,Object>();

                hm.put("prenom", rs.getString(1));
                hm.put("nom", rs.getString(2));
                String login = rs.getString(3);
                hm.put("login", login);
                hm.put("adresse", rs.getString(4));
                hm.put("ville", rs.getString(5));
                hm.put("code postal", rs.getString(6));
                hm.put("pays", rs.getString(7));
                hm.put("téléphone", rs.getString(8));

                if (etendue) {
                    q = "SELECT COUNT(*) FROM commande WHERE id_client=?;";
                    ps = co.prepareStatement(q);
                    ps.setString(1, login);
                    rs2 = ps.executeQuery();
                    if (!rs2.next()) {
                        return null;
                    }
                    hm.put("commandes", rs2.getInt(1));

                    q = "SELECT COUNT(*) FROM commande_produits WHERE";
                    q += " id_commande = ANY (SELECT id FROM commande WHERE";
                    q += " id_client=?);";
                    ps = co.prepareStatement(q);
                    ps.setString(1, login);
                    rs2 = ps.executeQuery();
                    if (!rs2.next()) {
                        return null;
                    }
                    hm.put("produits", rs2.getInt(1));

                    //TODO ajouter le total (prix) commandé
                }

                liste.add(hm);
            }
        } catch (SQLException e) {
            return null;
        }

        return liste;
    }

    /**
     * Liste les produits
     * @return une liste de `HashMap` avec une correspondance entre nom de
     * colonnes et valeurs
     **/
    public LinkedList<HashMap<String,Object>> listeProduits() {

        PreparedStatement ps = null;
        String q = "SELECT ref,description,qualifiant,prix,poids,";
        q += "quantite_restante FROM catalogue;";

        ResultSet rs = null;
        
        try {
            ps = co.prepareStatement(q);
            rs = ps.executeQuery();
        } catch (SQLException e) {
            return null;
        }

        LinkedList<HashMap<String,Object>> liste
            = new LinkedList<HashMap<String,Object>>();

        try {
            while(rs.next()) {
                HashMap<String,Object> hm = new HashMap<String,Object>();

                hm.put("ref", rs.getString(1));
                hm.put("description", rs.getString(2));
                hm.put("qualifiant", rs.getString(3));
                hm.put("prix", rs.getFloat(4));
                hm.put("poids", rs.getFloat(5));
                hm.put("quantité restante", rs.getFloat(6));

                liste.add(hm);
            }
        } catch (SQLException e) {
            return null;
        }

        return liste;
    }

    // === Créations === //

    /**
     * Crée une nouvelle personne
     * @param prenom Prénom de la personne
     * @param nom Nom de la personne
     * @param login Login de la personne (doit être unique)
     * @param mdp Mot de passe de la personne
     * @param type Type de la personne
     * @return true si l'insertion s'est déroulée avec succès, false sinon ou
     * si le type n'est pas reconnu.
     *
     * Note: si la personne est un client ou une douane, il faut aussi faire un
     * ajout dans les tables "client" ou "douane".
     **/
    public boolean nouvellePersonne(String prenom, String nom,
                                      String login, String mdp, String type) {

        if (!typePersonnes.contains(type)) {
            return false;
        }

        String q = "INSERT INTO personne VALUES(?,?,?,?,?::type_p);";

        try {
            PreparedStatement ps = co.prepareStatement(q);
            ps.setString(1, prenom);
            ps.setString(2, nom);
            ps.setString(3, login);
            ps.setString(4, mdp);
            ps.setString(5, type);
            int result = ps.executeUpdate();
            return (result > 0);
        }
        catch (SQLException e) {
            return false;
        }
    }

    //TODO enlever les new Date et utiliser Calendar
    /**
     * Crée une nouvelle commande
     * @param client login du client
     * @param date_commande date de la commande
     * @param date_prevue date de livraison prévue
     * @param produits mapping entre les références des produits et les
     * quantités commandées
     * @return true si l'insertion s'est bien déroulée
     **/
    public boolean nouvelleCommande(String client, Date date_commande,
                        Date date_prevue, HashMap<String, Integer> produits) {

        String q = "INSERT INTO commande (id_client,date_commande,date_prevue";
        q += ",frais) VALUES(?,?,?,?);";

        try {
            PreparedStatement ps = co.prepareStatement(q);
            ps.setString(1, client);
            ps.setDate(2, date_commande);
            ps.setDate(3, date_prevue);
            int result = ps.executeUpdate();
            //TODO
            return (result > 0);
        }
        catch (SQLException e) {
            return false;
        }

    }

    // === Suppressions === //

    /**
     * Supprime une personne
     * @param login Login de la personne à supprimer
     * @return true si la suppression s'est déroulée avec succès
     *
     * Note: si la personne est un client ou une douane, il faut aussi faire
     * une suppression dans les tables "client" ou "douane".
     **/
    public boolean supprimePersonne(String login) {

        String q = "DELETE FROM personne WHERE login=?;";

        try {
            PreparedStatement ps = co.prepareStatement(q);
            ps.setString(1, login);
            int result = ps.executeUpdate();
            return (result > 0);
        }
        catch (SQLException e) {
            return false;
        }
    }
}
