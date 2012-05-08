import java.io.*;
import java.sql.*;
import java.util.*;
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

    @Test
    public void testListerEmballeurs() {
        LinkedList<HashMap<String,Object>> liste = co.listeEmployes("emballeur");

        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        // à faire uniquement si on a pas ajouté/enlevé un emballeur
        assertEquals(5, liste.size());
    }

    @Test
    public void testListerTransporteurs() {
        LinkedList<HashMap<String,Object>> liste = co.listeEmployes("transporteur");

        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        // à faire uniquement si on a pas ajouté/enlevé un transporteur
        assertEquals(10, liste.size());
    }

    @Test
    public void testListerTousLesEmployes() {
        LinkedList<HashMap<String,Object>> liste = co.listeEmployes();

        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        // à faire uniquement si on a pas ajouté/enlevé un employé
        assertEquals(15, liste.size());
    }

    @Test
    public void testListerClients() {
        LinkedList<HashMap<String,Object>> liste = co.listeClients();

        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        // à faire uniquement si on a pas ajouté/enlevé un client
        assertEquals(100, liste.size());
    }

    @Test
    public void testListeEtendueClients() {
        LinkedList<HashMap<String,Object>> liste = co.listeClients(true);

        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        // à faire uniquement si on a pas ajouté/enlevé un client
        assertEquals(100, liste.size());
        
        HashMap<String,Object> hm = liste.getFirst();
        assertNotNull(hm);

        assertTrue(hm.containsKey("commandes"));
        assertTrue(hm.containsKey("produits"));
    }

    @Test
    public void testListerProduits() {
        LinkedList<HashMap<String,Object>> liste = co.listeProduits();

        assertNotNull(liste);
        assertFalse(liste.isEmpty());
        // à faire uniquement si on a pas ajouté/enlevé un produit
        assertEquals(2000, liste.size());
    }

    @Test
    public void testNouveauGerant() {
        boolean estNouvellePersonne
            = co.nouvellePersonne("Chuck", "Norris", "chucknorris", "42", "gerant");
        assertTrue(estNouvellePersonne);

        // impossible: la personne existe déjà
        estNouvellePersonne
            = co.nouvellePersonne("Chuck", "Norris", "chucknorris", "42", "gerant");
        assertFalse(estNouvellePersonne);

        // impossible: le login existe déjà
        estNouvellePersonne
            = co.nouvellePersonne("Chuque", "Nauris", "chucknorris", "24", "gerant");
        assertFalse(estNouvellePersonne);
    }

    @Test
    public void testNouvellePersonneMauvaisType() {
        boolean estNouvellePersonne
            = co.nouvellePersonne("Chuck", "Norris", "chucknorris", "42", "acteur");
        assertFalse(estNouvellePersonne);
    }

    @Test
    public void testSupprimeGerant() {
        boolean personneSupprimee = co.supprimePersonne("chucknorris");
        assertTrue(personneSupprimee);

        // impossible: la personne n'existe plus
        personneSupprimee = co.supprimePersonne("chucknorris");
        assertFalse(personneSupprimee);
    }
}
