import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Panel_co  extends Panel_princ implements ActionListener{
    //objet swing de notre Panel connexion
    JTextField login_JT;
    JTextField mdp_JT;
    JButton bouton ;

    public Panel_co(){
        this.connexion = this.buildJP();
    }

    public JPanel buildJP(){
        JPanel panel_co = new JPanel();
        panel_co.setLayout(new BorderLayout());

        JPanel panel_NO = new JPanel();
        panel_NO.setLayout(new FlowLayout());
        JLabel user = new JLabel("Utilisateur :");
        login_JT = new JTextField();
        login_JT.setColumns(10);
        panel_NO.add(user);
        panel_NO.add(login_JT);


        JPanel panel_CEN = new JPanel();
        panel_CEN.setLayout(new FlowLayout());
        JLabel mdp_text = new JLabel("Mot de Passe :");
        mdp_JT = new JTextField();
        mdp_JT.setColumns(10);
        panel_CEN.add(mdp_text);
        panel_CEN.add(mdp_JT);

        JPanel panel_SU = new JPanel();
        panel_SU.setLayout(new FlowLayout());
        bouton = new JButton("connexion");
        bouton.addActionListener(this);
        panel_SU.add(bouton);


        panel_co.add(panel_NO, BorderLayout.NORTH);
        panel_co.add(panel_CEN, BorderLayout.CENTER);
        panel_co.add(panel_SU, BorderLayout.SOUTH);


        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panel_co,BorderLayout.CENTER);
        return panel;
    }

    public void actionPerformed(ActionEvent e){
        System.out.println("Vous avez cliqué ici.");
        //  String login = login_JT.getText(); 
        //  String mdp = mdp_JT.getText();
        //  try{
        //      Container cp = this.getContentPane();
        //      cp.removeAll();
        //      if(re.connexion(login,mdp)){
        //          JLabel label = new JLabel("Connexion reussi : "+login);
        //          cp.add(label);
        //      }
        //      else{
        //          JLabel label = new JLabel("Login ou mot de passe incorrect");
        //          JPanel panel = buildJP();
        //          panel.add(label);
        //          cp.add(panel);
        //      }
        //      this.setContentPane(cp);
        //  }catch(SQLException sqle){}
    }
}
