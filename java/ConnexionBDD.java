import java.io.*;
import java.sql.*;

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
     **/
    public boolean connectUtilisateur(String login, String mdp) {
        try {
            PreparedStatement ps = co.prepareStatement("SELECT mot_de_passe FROM personne WHERE login = ?;");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            return (rs.next() && (rs.getString("mot_de_passe").equals(mdp)));

        } catch (SQLException e) {
            return false;
        }
    }
}
