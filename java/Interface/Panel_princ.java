import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Panel_princ {
    //Contenu de notre panel
    JLayeredPane contenu;
    
    //le JFrame qui appelle les Panel
    inter_princ frame;

    public Panel_princ(){
    }
    
    public JLayeredPane getContenu(){
        return contenu;
    }

}
