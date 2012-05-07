import javax.swing.*;
import java.awt.*;

public class fenetre_co extends JFrame{
    requete re;
    JTextField login;
    JTextField mdp;

    public Connexion(requete r){
        super();
        setSize(400,400);  //taille de la fenêtre
        setLocationRelativeTO(null); //on centre notre fenêtre
        setResizable(false); //interdit de redimentionner la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //fermeture si clic sur croix
        setVisible(true); //on rend la fenêtre visible 
        this.add(buildJP()); 

        this.re = r;
    }

    public JPanel buildJP(){
        JPanel panel = new Jpanel();
        panel.SetLayout(new FlowLayout());

        login = new JTextField();
        login.setColumns(10);
        mdp = new JTextField();
        mdp.setColumns(10);

        panel.add(login);
        panel.add(mdp);
        return panel;
    }

    public static void main(String [] args){
        requete re = new requete("david","david");
        JFrame fenetre = new  Connexion(re);    
    }
}
