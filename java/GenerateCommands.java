import java.io.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Calendar;

public class GenerateCommands {

    private static int NB_COMMANDES = 250;
    private static int NB_COMMANDES_NON_EXPEDIEES = 50;
    private static int NB_COMMANDES_EN_COLIS = 10;
    private static int NB_COMMANDES_EN_PALETTES = 10;

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

        // pour chaque ref de produit, [0] = quantité par carton,
        //                             [1] = cartons par palettes
        HashMap<String,Integer[]> qte_cartons_produits = new HashMap<String,Integer[]>();

        // association id commande -> ids des colis concernés
        HashMap<Integer,LinkedList<Integer>> commandes_colis
            = new HashMap<Integer,LinkedList<Integer>>();

        // association id des colis -> ref des produits dedans
        HashMap<Integer,String> colis_produits
            = new HashMap<Integer,String>();

        for (HashMap hm : lhm) {
            logins.push((String)hm.get("login"));
        }
        lhm = null;

        int nb_clients = logins.size();
        int nb_produits = produits.size();
        int i = 0; // curseur sur le tableau des commandes

        // liste des commandes: associe l'identifiant de la commande
        // à un mapping entre les références de produits et leur quantités
        // dans cette commande
        // commandes[i].get("_id") == l'id de la commande
        LinkedList<HashMap<String,Integer>> commandes
            = new LinkedList<HashMap<String,Integer>>();

        for (; i<NB_COMMANDES; i++) {
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

                // on stocke les informations sur les quantités par cartons/palette
                if (!qte_cartons_produits.containsKey(ref)) {
                    HashMap<String,Object> produit = co.infosProduit(ref);
                    Integer[] tmp = new Integer[2];
                    tmp[0] = (Integer)produit.get("quantité par carton");
                    tmp[1] = (Integer)produit.get("cartons par palette");
                    qte_cartons_produits.put(ref, tmp);
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
                //commandes[i] = cmd;
            
                produits_commande.put("_id", cmd);

                commandes.push(produits_commande);
            }
        }

        /* 
         * À ce stade, `commandes` contient les identifiant de 250 commandes
         * pas encore expédiées.
         *
         * On en garde 50, et on en expédie 200.
         */
        i = NB_COMMANDES_NON_EXPEDIEES;
        
        for (;i<NB_COMMANDES;i++) {

            // colis de cette commande
            LinkedList<Integer> cmd_colis = new LinkedList<Integer>();

            // références des produits de cette commande
            HashMap<String,Integer> cmd_produits = commandes.get(i);

            for (String ref : cmd_produits.keySet()) {
                if (ref.equals("_id")) { continue; }
                
                // pour chaque produit

                int qte_par_carton = qte_cartons_produits.get(ref)[0];
                int qte = cmd_produits.get(ref);

                int tmp,nb_cartons = qte/qte_par_carton + 1; 

                while (nb_cartons > 0) {
                    tmp = Math.min(qte_par_carton, qte); // nb de produits dans ce carton
                    qte -= tmp;
                    nb_cartons--;

                    HashMap<String,Integer> current_colis_produits
                        = new HashMap<String,Integer>();
                    current_colis_produits.put(ref, tmp);
                    
                    int id_colis = co.nouveauColis(cmd_produits.get("_id"), current_colis_produits);
                    
                    cmd_colis.push(id_colis);

                    colis_produits.put(new Integer(id_colis), ref);
                }
            }

            commandes_colis.put(cmd_produits.get("_id"), cmd_colis);
        }

        /*
         * À ce stade, on a toujours 50 commandes non emballées, et
         * 200 commandes dans des colis. On en laisse 10 dans des colis,
         * et on continue;
         */
        i = NB_COMMANDES_NON_EXPEDIEES + NB_COMMANDES_EN_COLIS;


    }
}
