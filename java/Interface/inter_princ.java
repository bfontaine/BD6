import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class inter_princ extends JFrame{

    ConnexionBDD conn;

    Panel_princ contenue;

    public inter_princ (ConnexionBDD co){
        super();
        
        this.contenue = new Panel_co();
        //crée notre objet requete
        this.conn = co;

        setSize(700,500); // taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre
        setResizable(false); // interdit de redimentionner la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fermeture si clic sur croix
        setContentPane(contenue.getConnexion()); // insere le contenue de la fenêtre
        setVisible(true); //rend la fenêtre visible
    }

    public static void main (String [] args) throws SQLException,ClassNotFoundException {
        //prend en argument le nom bdd et utilisateur et mdp_bdd
        if(args.length == 3){
            String nom_bdd = args[0];
            String user = args[1];
            String mdp_bdd = args[2];
            //crée notre interface et la lance
            ConnexionBDD co = new ConnexionBDD(nom_bdd, user, mdp_bdd);
            inter_princ fenetre = new inter_princ(co);
        }
    }
}
