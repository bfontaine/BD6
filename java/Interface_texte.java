import java.io.*;
import java.util.Scanner;
import java.sql.*;

public class Interface_texte{
    static ConnexionBDD co;
    static Scanner in = new Scanner(System.in);
    
    /**
    * Imprime le menu de connection
    **/
    public static void menuConnexion(){
       
        System.out.print("\033c"); //nettoyage de l'ecran
        String type;
        boolean mdp_incorrect = false; 

        do{
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Connexion Projet BDD :");
            System.out.println("-------------------------------------------------------------");
            if(mdp_incorrect)
                System.out.println("Login ou Password incorrecte");

            System.out.print("Utilisateur : ");
            String login = in.next();
            String password = PasswordField.readPassword(" Password : ");  

            type = co.connecteUtilisateur(login,password);

        }while(type == null);

        if (type.equals("client")) {
            System.out.println("client");
        }
        else if (type.equals("transporteur")) {
            System.out.println("transporteur");
        }
        else if (type.equals("emballeur")) {
            System.out.println("emballeur");
        }
        else if (type.equals("gerant")) {
            System.out.println("gerant");
        }
        else if (type.equals("douane")) {
            System.out.println("douane");
        }
    }

    public static void main(String[]args)throws SQLException , ClassNotFoundException {
        //prend en argument le nom bdd et utilisateur et mdp_bdd
        if(args.length == 3){
            String nom_bdd = args[0];
            String user = args[1];
            String mdp_bdd = args[2];
            //crée notre interface et la lance
            co = new ConnexionBDD(nom_bdd, user, mdp_bdd);
            menuConnexion();
        }
    }
}
