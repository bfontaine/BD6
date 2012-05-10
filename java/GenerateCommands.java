import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Calendar;

public class GenerateCommands {

    private static int NB_COMMANDES = 250;

    public static void main (String[] args) throws SQLException, ClassNotFoundException {
        ConnexionBDD co = new ConnexionBDD("baptiste", "baptiste");

        // 250 commandes:
        // 50 non expédiées
        // 20 à moitié
        // 180 complètes

        LinkedList<HashMap<String,Object>> lhm = co.listeClients();
        LinkedList<HashMap<String,Object>> produits = co.listeProduits();

        // liste des logins des clients
        LinkedList<String> logins = new LinkedList<String>();

        for (HashMap hm : lhm) {
            logins.push((String)hm.get("login"));
        }
        lhm = null;

        int nb_clients = logins.size();
        int nb_produits = produits.size();

        // debug
        System.out.println("Nombre de clients: "+nb_clients);
        System.out.println("Nombre de produits: "+nb_produits);

        int[] commandes = new int[NB_COMMANDES];

        for (int i=0; i<commandes.length; i++) {
            String client = logins.get((int)(Math.random()*nb_clients));
            Calendar date_prevue = Calendar.getInstance();

            int jour = date_prevue.get(date_prevue.DAY_OF_MONTH);
            int mois = date_prevue.get(date_prevue.MONTH);
            int annee = date_prevue.get(date_prevue.YEAR);

            // >= année courante (max: année courante +4)
            int annee_prevue = (int)(Math.random())*5+annee;
            // >= mois courant +2
            int mois_prevu = (annee_prevue == annee) ? (int)(Math.random())*(9-mois)+mois+2 : (int)(Math.random()*9)+2;
            int jour_prevu = (int)(Math.random()*25);

            date_prevue.set(annee_prevue, mois_prevu, jour_prevu);

            HashMap<String,Integer> produits_commande = new HashMap<String,Integer>();

            // nombre de produits différents
            int nb_produits_diff = (int)(Math.random()*100)+1;

            for (int j=0; j<nb_produits_diff; j++) {

                int id_produit = (int)(Math.random()*nb_produits);

                String ref = (String)produits.get(id_produit).get("référence");
                int qte_max = ((Float)produits.get(id_produit).get("quantité restante")).intValue();

                int qte = Math.min((int)(Math.random()*20)+1, qte_max/100);

                if (qte == 0) {
                    if (qte_max == 0) {
                        continue;
                    }
                    qte = 1;
                }
                produits_commande.put(ref, qte);
            }

            int cmd = co.nouvelleCommande(client, date_prevue, produits_commande);

            if (cmd == -1) {
                System.out.println("Erreur: client="+client+", date_prevue="
                                    +jour_prevu+"/"+mois_prevu+"/"+annee_prevue
                                    + "(Aujourd'hui: "+jour+"/"+mois+"/"+annee+")");

                System.out.print("Produits (quantité): ");
                for (String ref : produits_commande.keySet()) {
                    System.out.print(ref+" ("+produits_commande.get(ref)+"), ");
                }
                System.out.println(".");

            } else {
                System.out.println("Commande OK pour client "+client+".");
                commandes[i] = cmd;
            }
        }
    }
}
