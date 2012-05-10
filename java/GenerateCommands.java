import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Calendar;

public class GenerateCommands {

    private static int NB_COMMANDES = 250;

    public static void main (String[] args) throws SQLException, ClassNotFoundException {
        String user = System.getProperty("user.name");
        ConnexionBDD co = new ConnexionBDD(user, user);

        // 250 commandes:
        // 50 non expédiées
        // 20 à moitié
        // 180 complètes

        LinkedList<HashMap<String,Object>> lhm = co.listeClients();
        LinkedList<HashMap<String,Object>> produits = co.listeProduitsRestants();

        // liste des logins des clients
        LinkedList<String> logins = new LinkedList<String>();

        for (HashMap hm : lhm) {
            logins.push((String)hm.get("login"));
        }
        lhm = null;

        int nb_clients = logins.size();
        int nb_produits = produits.size();

        int[] commandes = new int[NB_COMMANDES];

        for (int i=0; i<commandes.length; i++) {
            String client = logins.get((int)(Math.random()*nb_clients));
            Calendar date_prevue = Calendar.getInstance();

            int jour = date_prevue.get(date_prevue.DAY_OF_MONTH);
            int mois = date_prevue.get(date_prevue.MONTH);
            int annee = date_prevue.get(date_prevue.YEAR);

            // >= année courante (max: année courante +4)
            int annee_prevue = (int)(Math.random()*5)+annee;
            // >= mois courant +2
            int mois_prevu = (annee_prevue == annee) ? (int)(Math.random())*(9-mois)+mois+2 : (int)(Math.random()*9)+2;
            int jour_prevu = (int)(Math.random()*25);

            date_prevue.set(annee_prevue, mois_prevu, jour_prevu);

            HashMap<String,Integer> produits_commande = new HashMap<String,Integer>();

            // nombre de produits différents
            int nb_produits_diff = (int)(Math.random()*10)+1;

            for (int j=0; j<nb_produits_diff; j++) {

                int id_produit, qte_max, qte = 0;
                String ref = "";

                // tant que le produit courant n'est pas disponible en bonne quantité,
                // le changer avec un autre aléatoirement
                while (qte == 0) {

                    id_produit = (int)(Math.random()*nb_produits);

                    ref = (String)produits.get(id_produit).get("référence");
                    qte_max = ((Float)produits.get(id_produit).get("quantité restante")).intValue();

                    qte = Math.min((int)(Math.random()*20)+1, qte_max/100);

                }

                produits_commande.put(ref, qte);
            }

            int cmd = co.nouvelleCommande(client, date_prevue, produits_commande);

            if (cmd == -1) {
                System.err.println("Erreur: client="+client+", date_prevue="
                                    +jour_prevu+"/"+mois_prevu+"/"+annee_prevue
                                    + "(Aujourd'hui: "+jour+"/"+mois+"/"+annee+")");

                System.err.print("Produits (quantité): ");
                for (String ref : produits_commande.keySet()) {
                    System.err.print(ref+" ("+produits_commande.get(ref)+"), ");
                }
                System.err.println(".");

            } else {
                //System.out.println("Commande OK pour client "+client+".");
                commandes[i] = cmd;
            }
        }

        /* 
         * À ce stade, `commandes` contient les identifiant de 250 commandes
         * pas encore expédiées.
         */
    }
}
