import java.io.*;
import java.sql.*;

public class requete{
    Connexion co; //la connexion à la base
    Statement st;
    PrepareStatement insert;
    PrepareStatement delete;
    PrepareStatement update;
    
    public requete (String utilisateur,String mdp)throw SQLException, ClassNotFoundException{
        Class.ForName("orgs.postsql.Driver");
        co = Driver.Manager.getConnection("jdbc:postgresql:bd6",utilisateur,mdp); 
    }
    
    public boolean connexion(String login,String mdp){
        this.update = cp.prepareStatement("SELECT mot_de_passe FROM personne WHERE login = '"+login+"';");
        ResultSet re = this.update.executeQuery();
    }
}
