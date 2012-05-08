import java.io.*;
import java.sql.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnexionBDD_tests {
    
    public static ConnexionBDD co = null;
    
    @Test
    public void testCreation() {
        String username = System.getProperty("user.name");
        try {
            co = new ConnexionBDD(username, username);
        }
        catch(SQLException e) {
            fail("SQLException: "+e.getMessage());
        }
        catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: "+e.getMessage());
        }

        assertNotNull(co);
    }

    @Test
    public void testMauvaiseConnexion() {
        boolean estConnecte = co.connectUtilisateur("nexiste", "pas");
        assertFalse(estConnecte);
    }

    @Test
    public void testBonneConnexion() {
        // premiere personne de la BDD
        boolean estConnecte = co.connectUtilisateur("SEGZE03368", "IVW24HJB2RU");
        assertTrue(estConnecte);
    }

    // --- Gérant ---
    
    @Test
    public void testChangerPrixBonneRef() {
        boolean estChange = co.changePrix("PW-403570-TGG-27", 1337);
        assertTrue(estChange);
    }
    
    @Test
    public void testChangerPrixMauvaiseRef() {
        boolean estChange = co.changePrix("nexistepas", 1337);
        assertFalse(estChange);
    }
    
    @Test
    public void testChangerPrixNegatif() {
        boolean estChange = co.changePrix("PW-403570-TGG-27", -1337);
        assertFalse(estChange);
    }
}
