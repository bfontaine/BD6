import java.io.*;
import java.sql.*;

public class requete{
    Connection co; //la connexion à la base
    Statement st;
    PreparedStatement insert;
    PreparedStatement delete;
    PreparedStatement update;
    
    public requete (String utilisateur,String mdp)throws SQLException, ClassNotFoundException{
        Class.forName("org.postgresql.Driver");
        co = DriverManager.getConnection("jdbc:postgresql://localhost/bd6",utilisateur,mdp); 
    }
    
    //fonction qui vérifie si une connexion est accepter
    public boolean connexion(String login,String mdp)throws SQLException{
        this.update = co.prepareStatement("SELECT mot_de_passe FROM personne WHERE login = ?;");
        this.update.setString(1,login);
        ResultSet rs = this.update.executeQuery();
        if(rs.next()){
           if(rs.getString("mot_de_passe").equals(mdp) )
                System.out.println("Connexion acceptée :"+login);
                return true;
        }

        return false;
    }

    public static void main(String [] args)throws SQLException,ClassNotFoundException{
        requete test = new requete("david","david");
        test.connexion("david","mdp");
    }
}
