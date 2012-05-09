import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Panel_co  extends Panel_princ implements ActionListener{
    boolean connection_F=true;

    //objet swing de notre Panel connexion
    JTextField login_JT;
    JTextField mdp_JT;
    JButton bouton ;

    public Panel_co(inter_princ jfr){
        this.connexion = this.buildJP();
        this.frame = jfr;
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
        panel_SU.setLayout(new BorderLayout());
        if(!connection_F){
            JLabel label = new JLabel("Login ou mot de passe incorrect");
            label.setForeground(Color.red);
            panel_SU.add(label, BorderLayout.NORTH);
        }
        bouton = new JButton("connexion");
        bouton.addActionListener(this);
        panel_SU.add(bouton, BorderLayout.CENTER);


        panel_co.add(panel_NO, BorderLayout.NORTH);
        panel_co.add(panel_CEN, BorderLayout.CENTER);
        panel_co.add(panel_SU, BorderLayout.SOUTH);


        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,200,200));
        Color color = new Color(0,153,102);
        panel.setBackground(color);
        panel.add(panel_co);
        return panel;
    }

    public void actionPerformed(ActionEvent e){
        String login = login_JT.getText(); 
        String mdp = mdp_JT.getText();
        Container cp = this.frame.getContentPane();
        cp.removeAll();
        if(this.frame.conn.connectUtilisateur(login,mdp)){
            JLabel label = new JLabel("Connexion reussi : "+login);
            cp.add(label);
        }
        else{
            JPanel panel = this.buildJP();
            cp.add(panel);
        }
        this.frame.setContentPane(cp);

    }
}
