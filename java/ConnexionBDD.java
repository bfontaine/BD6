import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Une connexion à la base de données
 **/
public class ConnexionBDD {
    private Connection co; //la connexion à la base
    
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
}
