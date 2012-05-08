import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class fenetre_co extends JFrame implements ActionListener{
    requete re;
    JTextField login_JT;
    JTextField mdp_JT;
    JButton bouton ;

    public fenetre_co(requete r){
        super();
        setSize(400,400);  //taille de la fenêtre
        setLocationRelativeTo(null); //on centre notre fenêtre
        setResizable(false); //interdit de redimentionner la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //fermeture si clic sur croix
        setContentPane(buildJP()); 
        setVisible(true); //on rend la fenêtre visible 

        this.re = r;
    }

    public JPanel buildJP(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        login_JT = new JTextField();
        login_JT.setColumns(10);
        mdp_JT = new JTextField();
        mdp_JT.setColumns(10);
        bouton = new JButton("connexion");
        bouton.addActionListener(this);

        panel.add(login_JT);
        panel.add(mdp_JT);
        panel.add(bouton);
        return panel;
    }

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == bouton){
            System.out.println("Vous avez cliqué ici.");
            String login = login_JT.getText(); 
            String mdp = mdp_JT.getText();
            try{
                Container cp = this.getContentPane();
                cp.removeAll();
                if(re.connexion(login,mdp)){
                    JLabel label = new JLabel("Connexion reussi : "+login);
                    cp.add(label);
                }
                else{
                    JLabel label = new JLabel("Login ou mot de passe incorrect");
                    JPanel panel = buildJP();
                    panel.add(label);
                    cp.add(panel);
                }
                this.setContentPane(cp);
            }catch(SQLException sqle){}
        }
    }

    public static void main(String [] args) throws SQLException,ClassNotFoundException{
        requete re = new requete("david","david");
        fenetre_co fenetre = new  fenetre_co(re);    
    }
}
