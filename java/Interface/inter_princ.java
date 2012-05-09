import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class inter_princ extends JFrame{

    ConnexionBDD conn;

    public inter_princ (ConnexionBDD co){
        super();
        setSize(700,500); // taille de la fenêtre
        setLocationRelativeTo(null); // centre la fenêtre
        setResizable(false); // interdit de redimentionner la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // fermeture si clic sur croix
        setContentPane(); // insere le contenue de la fenêtre
        setVisible(true); //rend la fenêtre visible

        //crée notre objet requete
        this.conn = co;
    }

    public static void main (String [] args){
        //prend en argument le nom utilisateur et mdp_bdd
        if(args.length == 3){
            String nom_bdd = args[0];
            String user = args[1];
            String mdp_bdd = args[2];
            //crée notre interface et la lance
            ConnexionBDD co = new Connexion(nom_bdd, user, mdp_bdd);
            inter_princ fenetre = new inter_princ(co);
    }
}
