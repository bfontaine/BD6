import java.io.*;
import java.sql.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Calendar;
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
        String estConnecte = co.connecteUtilisateur("nexiste", "pas");
        assertNull(estConnecte);
    }

    @Test
    public void testBonneConnexion() {
        // premiere personne de la BDD
        String estConnecte = co.connecteUtilisateur("SEGZE03368", "IVW24HJB2RU");
        assertEquals("client", estConnecte);
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

    @Test
    public void testCommandeMauvaisClient() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 1);

        boolean commandeValidee
          = co.nouvelleCommande("nexistepas", maintenant, apres, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandePasDeProduits() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, apres, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandeNbDeProduitsNegatif() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", -1); 
        produits.put("TL-338853-AIN-30", 2); 

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, apres, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandeNbDeProduitsNul() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 0); 

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, apres, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandeNbDeProduitsTropGrand() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 99999999); 

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, apres, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandeDatePrevuePassee() {

        Calendar maintenant = Calendar.getInstance();
        Calendar avant      = Calendar.getInstance();
        avant.set(1999, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 0); 
        produits.put("TL-338853-AIN-30", 0); 

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, avant, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandeMauvaiseRefProduit() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("nexistepas", 1); 

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, apres, produits);

        assertFalse(commandeValidee);
    }

    @Test
    public void testCommandeOk() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 1);

        boolean commandeValidee
          = co.nouvelleCommande("SEGZE03368", maintenant, apres, produits);

        assertTrue(commandeValidee);

        // version courte
        commandeValidee = co.nouvelleCommande("SEGZE03368", apres, produits);
        assertTrue(commandeValidee);
    }

    @Test
    public void testInfosMauvaisePersonne() {

        HashMap<String,String> hm = co.infosPersonne("nexistepas");

        assertNull(hm);
    }

    @Test
    public void testInfosPersonne() {

        String login = "SEGZE03368";

        HashMap<String,String> hm = co.infosPersonne(login);

        assertNotNull(hm);
        assertFalse(hm.isEmpty());
        assertEquals(4, hm.size());

        assertEquals(login, hm.get("login"));
    }
}
