import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Panel_tran  extends Panel_princ implements ActionListener{
    boolean connection_F=true;

    //objet swing de notre Panel connexion

    public Panel_tran(inter_princ jfr){
        this.contenu = this.buildJP();
        this.frame = jfr;
    }

    public JLayeredPane buildJP(){
        JLayeredPane panel = new JLayeredPane();
        return panel;
    }

    public void actionPerformed(ActionEvent e){
    }
}
