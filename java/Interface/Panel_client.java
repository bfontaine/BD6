import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Panel_client  extends Panel_princ implements ActionListener{
    boolean connection_F=true;

    //objet swing de notre Panel connexion

    public Panel_client(inter_princ jfr){
        this.connexion = this.buildJP();
        this.frame = jfr;
    }

    public JPanel buildJP(){
        Jpanel panel = new Jpanel();
        return panel;
    }

    public void actionPerformed(ActionEvent e){
    }
}
