import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Panel_client  extends Panel_princ implements ActionListener{
    String login;
    //objet swing de notre Panel connexion
    JButton deconnexion;

    public Panel_client(inter_princ jfr,String log){
        this.frame = jfr;
        this.login = log;
        this.contenu = this.buildJP();
    }

    public JLayeredPane buildJP(){
        JLayeredPane panel = new JLayeredPane();
        
        //construit le panel des info
        JPanel info = buildJP_info();
        info.setBounds(10,10,100,200);
        panel.add(info);

        //construit le panel des choix d'action

        //construit le panel action
        

        return panel;
    }

    public JPanel buildJP_info(){
        HashMap<String,String> info_client = this.frame.conn.infosPersonne(this.login);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4,1));
        
        JLabel type = new JLabel(info_client.get("type"));
        panel.add(type);
        JLabel login = new JLabel("Login : "+this.login);
        panel.add(login);
        JLabel nom = new JLabel("Nom :"+info_client.get("nom"));
        panel.add(nom);

        this.deconnexion = new JButton("deconnexion");
        deconnexion.addActionListener(this);
        panel.add(deconnexion);

        return panel;
    }

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == deconnexion){
            this.frame.connexion_co(true);
        }
    }
}
