import java.io.*;
import java.sql.*;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Calendar;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConnexionBDD_tests {
    
    private static ConnexionBDD co = null;

    private static String loginOk = "SEGZE03368";
    private static String refOk = "PW-403570-TGG-27";
    
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
        String estConnecte = co.connecteUtilisateur(loginOk, "IVW24HJB2RU");
        assertEquals("client", estConnecte);
    }
    
    @Test
    public void testChangerPrixBonneRef() {
        boolean estChange = co.changePrix(refOk, 1337);
        assertTrue(estChange);
    }
    
    @Test
    public void testChangerPrixMauvaiseRef() {
        boolean estChange = co.changePrix("nexistepas", 1337);
        assertFalse(estChange);
    }
    
    @Test
    public void testChangerPrixNegatif() {
        boolean estChange = co.changePrix(refOk, -1337);
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

        int commande
          = co.nouvelleCommande("nexistepas", maintenant, apres, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandePasDeProduits() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();

        int commande
          = co.nouvelleCommande(loginOk, maintenant, apres, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandeNbDeProduitsNegatif() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", -1); 
        produits.put("TL-338853-AIN-30", 2); 

        int commande
          = co.nouvelleCommande(loginOk, maintenant, apres, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandeNbDeProduitsNul() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 0); 

        int commande
          = co.nouvelleCommande(loginOk, maintenant, apres, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandeNbDeProduitsTropGrand() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 99999999); 

        int commande
          = co.nouvelleCommande(loginOk, maintenant, apres, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandeDatePrevuePassee() {

        Calendar maintenant = Calendar.getInstance();
        Calendar avant      = Calendar.getInstance();
        avant.set(1999, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 0); 
        produits.put("TL-338853-AIN-30", 0); 

        int commande
          = co.nouvelleCommande(loginOk, maintenant, avant, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandeMauvaiseRefProduit() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("nexistepas", 1); 

        int commande
          = co.nouvelleCommande(loginOk, maintenant, apres, produits);

        assertEquals(-1, commande);
    }

    @Test
    public void testCommandeOk() {

        Calendar maintenant = Calendar.getInstance();
        Calendar apres      = Calendar.getInstance();
        apres.set(2012, 11, 21);

        HashMap<String, Integer> produits = new HashMap<String, Integer>();
        produits.put("GN-746901-SIY-63", 1);

        int commande
          = co.nouvelleCommande(loginOk, maintenant, apres, produits);

        assertTrue(commande >= 0);

        // version courte
        int commande2 = co.nouvelleCommande(loginOk, apres, produits);
        assertTrue(commande2 > commande);
    }

    @Test
    public void testInfosMauvaisePersonne() {

        HashMap<String,String> hm = co.infosPersonne("nexistepas");

        assertNull(hm);
    }

    @Test
    public void testInfosPersonne() {

        HashMap<String,String> hm = co.infosPersonne(loginOk);

        assertNotNull(hm);
        assertFalse(hm.isEmpty());
        assertEquals(3, hm.size());
    }

    @Test
    public void testInfosCommandeIdNegatif() {

        HashMap<String,Object> hm = co.infosCommande(-1);

        assertNull(hm);
    }

    @Test
    public void testInfosCommandeInexistante() {

        HashMap<String,Object> hm = co.infosCommande(99999);

        assertNull(hm);
    }

    @Test
    @SuppressWarnings("unchecked") // cast Object->HashMap = warning
    public void testInfosCommandeOk() {

        HashMap<String,Integer> produits = new HashMap<String,Integer>();
        Calendar c = Calendar.getInstance();
        c.set(2014, 5, 15);

        int qte = 2;

        produits.put(refOk, qte);

        int id = co.nouvelleCommande(loginOk, c, produits);

        assertFalse(-1 == id);

        HashMap<String,Object> hm = co.infosCommande(id);

        assertNotNull(hm);
        assertFalse(hm.isEmpty());

        int qte_2 = ((HashMap<String,Integer>)hm.get("produits")).get(refOk);

        assertEquals(qte, qte_2);
        assertEquals(loginOk, hm.get("login client"));
    }
}
