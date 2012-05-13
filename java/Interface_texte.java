import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;
import java.util.LinkedList;
import java.util.Calendar;
import java.util.InputMismatchException;


public class Interface_texte{
    static ConnexionBDD co;
    static Scanner in = new Scanner(System.in);
    static String Login;

    /**
     * Imprime le menu de connection
     **/
    public static void menuConnexion(){
        String type;
        boolean mdp_incorrect = false; 

        do{
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Connexion Projet BDD :");
            System.out.println("-------------------------------------------------------------");
            if(mdp_incorrect)
                System.out.println("Login ou Password incorrecte");

            System.out.print("Utilisateur : ");
            Login = in.next();
            String password = PasswordField.readPassword(" Password : ");  

            type = co.connecteUtilisateur(Login,password);
            if(type == null)
                mdp_incorrect = true;

        }while(type == null);
        int choix = -1;
        if (type.equals("client")) {
            while(choix != 4 ){
                choix = menuClient();
                if((choix < 4) || (choix > -1)){
                    choix_client(choix);
                }
            }

        }
        else if (type.equals("transporteur")) {
            while(choix != 3 ){
                choix = menuTransporteur();
                if((choix < 2) || (choix > -1)){
                    choix_transporteur(choix);
                }
            }
        }
        else if (type.equals("emballeur")) {
            while(choix != 3 ){
                choix = menuEmballeur();
                if((choix < 2) || (choix > -1)){
                    choix_emballeur(choix);
                }
            }
        }
        else if (type.equals("gerant")) {
            while(choix != 5 ){
                choix = menuGerant();
                if((choix < 4) || (choix > -1)){
                    choix_gerant(choix);
                }
            }
        }
        else if (type.equals("douane")) {
            while(choix != 5 ){
                choix = menuDouane();
                if((choix < 4) || (choix > -1)){
                    choix_douane(choix);
                }
            }
        }
    }

    public static int menuClient(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("CLIENT");
        System.out.println();
        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Passer une commande");
        System.out.println("1 - Situation colis"); 
        System.out.println("2 - Lister les produits disponibles");
        System.out.println("3 - Changer son login/mdp");
        System.out.println("4 - quitter");
        System.out.println("-------------------------------------------------------------");

        return priseEntier("choix : ");
    }

    public static int menuGerant(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------
        System.out.println("GERANT");
        System.out.println();

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Accès à la liste des employés/clients");
        System.out.println("1 - Changer le prix des produits"); 
        System.out.println("2 - Voir les produits les plus vendus");
        System.out.println("3 - Voir les clients les plus depensiés");
        System.out.println("4 - Voir les employés les moins actifs");
        System.out.println("5 - quitter");
        System.out.println("-------------------------------------------------------------");

        return priseEntier("choix : ");
    }

    public static int  menuEmballeur(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------
        System.out.println("EMBALLEUR");
        System.out.println();

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Information sur les commandes d'un client");
        System.out.println("1 - Entrer les colis emballés"); 
        System.out.println("2 - Entrer les palettes préparées");
        System.out.println("3 - quitter");
        System.out.println("-------------------------------------------------------------");

        return priseEntier("choix : ");
    }

    public static int menuTransporteur(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------
        System.out.println("TRANSPORTEUR");
        System.out.println();

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Connaître la date limite de livraison souhaitée par le client");
        System.out.println("1 - Savoir si un colis est fragile ou dangereux"); 
        System.out.println("2 - Changer situation du colis");
        System.out.println("3 - quitter");
        System.out.println("-------------------------------------------------------------");

        return priseEntier("choix : ");
    }

    public static int menuDouane(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------
        System.out.println("DOUANE");
        System.out.println();

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - ");
        System.out.println("1 - "); 
        System.out.println("2 - ");
        System.out.println("3 - ");
        System.out.println("4 - ");
        System.out.println("5 - quitter");
        System.out.println("-------------------------------------------------------------");

        return priseEntier("choix : ");
    }

    public static void Pause(int n){
        try{
           Thread.sleep(n); 
        }catch(Exception e){}

    }

    public static int priseEntier(String demande){
        boolean valide = false ;
        int valeur = -1;
        do{
            try{
                System.out.print(demande);
                valeur = in.nextInt();
                in.nextLine();
                valide = false;
            }catch(InputMismatchException e){
                System.out.println("Un entier est demandé");     
                in.nextLine();
                valide = true;
            }
        }while(valide);
        return valeur;
    }

    public static void choix_client(int choix){
        if(choix == 0){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Insertion d'un commande :");
            System.out.println("-------------------------------------------------------------");
            int total_colis = priseEntier("Nombre de produits : ");
            HashMap<String,Integer> hb = new HashMap<String,Integer>();
            for (int i = 0; i < total_colis; i++){
                int quantite;
                String produit;
                do{
                    System.out.print("Réference du produit "+(i+1)+" :");
                    produit = in.next();
                    quantite = priseEntier("Quantite : ");
                }while(!produitsExiste(produit,quantite));
                    hb.put(produit,quantite);
            }
            System.out.println("-------------------------------------------------------------");
            System.out.println("Date de Livraison:");
            System.out.println("-------------------------------------------------------------");
            int year = priseEntier("Année : ");
            int mois = priseEntier("Mois : ");
            int jour = priseEntier("Jours : ");
            Calendar date = Calendar.getInstance();
            date.set(year,mois,jour);
            int id_commande = co.nouvelleCommande(Login,date,hb); 
            if(id_commande > 0)
                System.out.println("Commande valide");
            else
                System.out.println("Commande echoué");
            Pause(1000);
        }
        else if(choix == 1){
            int [] taille = {5,15,15,15,10};
            String[] champ = {"id","date de commande","date de livraison prévue","date de livraison","frais"};
            affichage_less(co.listeCommandesClient(Login,true),champ,taille,"Information des commandes "+Login+" :");
            int id_commande;
            HashMap<String,Object> h;
            do{
                id_commande = priseEntier("Identification de la commande : ");
                h = co.infosCommande(id_commande);
                if(h == null)
                    System.out.println("Commande non reconnu");
            }while(h == null);
            int [] taille_commande = {5,20,21,10,22,22,22};
            String [] champ_commande = {"id","id commande","référence produit","qualifiant","date d'emballage","date d'expédition","date de livraison"};
            affichage_less(co.listerColisParCommande(id_commande),champ_commande,taille_commande,"Information des colis de la commande :");
        }
        else if(choix == 2){
            int [] taille = {16,78,10,7,5,17};
            String[] champ = {"référence","description","qualifiant","prix","poids","quantité restante"};

            affichage_less(co.listeProduitsRestants(),champ,taille,"Liste des Produit disponible :");
        }
        else if(choix == 3){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Changement de password :");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Authentification:");
            String mdp = PasswordField.readPassword("Password : ");  
            String t = co.connecteUtilisateur(Login,mdp);
            System.out.println(Login+" "+mdp+" "+t);
            if(t != null)
                System.out.println("Authentification: validé");
            else{
                System.out.println("Authentification: rejeté");
                Pause(1000);
                return ;
            }
            System.out.println("-------------------------------------------------------------");
            System.out.print("Nouveau login :");
            String new_login = in.next();
            String new_password = PasswordField.readPassword("Nouveaux Password : ");  
            if(co.changerLogin(Login,new_login)){
                System.out.print("Changement Login effectuer");
                Login = new_login;
            }
            else{
                System.out.print("Erreur lors du changement de login");

            }
            if(co.changerMdp(Login,mdp,new_password))
                System.out.print("Changement Password effectuer:");
            else{
                System.out.print("Erreur lors du changement de password");
            }
            Pause(1000);
        }
    }

    public static void choix_transporteur(int choix){
        if(choix == 2){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Changer situation d'une palette");
            System.out.println("-------------------------------------------------------------");
            int palette = priseEntier("Identification de la palette");
            if(co.livrerPalette(palette))
                System.out.println("Livraison palette "+palette+" effectuer");
            else{
                System.out.println("Palette non reconnu");
            }
            Pause(1000);

        }
        else if(choix == 1){
            int [] taille = {5,20,21,10,22,22,22};
            String[] champ = {"id","id commande","référence produit","qualifiant","date d'emballage","date d'expédition","date de livraison"};
            int colis;
            HashMap<String,Object> hb;
            do{
                colis = priseEntier("Identifiant du colis : ");
                hb = co.infosColis(colis);
                if(hb == null)
                    System.out.println("Colis non reconnu");
            }while(hb == null);
            affichage_less(hb,champ,taille,"Information sur le colis :");

        }
        else if(choix == 0){
            int [] taille = {5,15,20,25,10};
            String[] champ = {"id","login client","produits","date de livraison prévue","frais"};
            int commande = priseEntier("Identifiant de la commande : ");
            affichage_less(co.infosCommande(commande),champ,taille,"Information de la commande :");
        }
    }

    public static void choix_emballeur(int choix){
        if(choix == 0){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Connaître la liste des commandes d'un client : ");
            System.out.println("-------------------------------------------------------------");
            String login;
            HashMap<String,String> b ;
            do{
                System.out.print("Identifiant du client : ");
                login = in.next();
                b = co.infosPersonne(login);
                if(b == null)
                    System.out.println("Client pas reconnu");

            }while(b == null);
            int [] taille = {5,15,15,15,10};
            String[] champ = {"id","date de commande","date de livraison prévue","date de livraison","frais"};
            affichage_less(co.listeCommandesClient(login,true),champ,taille,"Information des commandes de"+login+" :");
            int id_commande;
            HashMap<String,Object> h;
            do{
                id_commande = priseEntier("Identification de la commande : ");
                h = co.infosCommande(id_commande);
                if(h == null)
                    System.out.println("Commande non reconnue");
            }while(h == null);
            int [] taille_com  = {10,10,10,20,20,20};
            String[] champ_com = {"référence","qualifiant","poids","quantité par carton","cartons par palette","quantité demandée"};
            affichage_less(co.listerProduitsParCommande(id_commande),champ_com,taille_com,"Information des produit de la commande "+id_commande+" :");
        }
        else if(choix == 1){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Ajouter une colis : ");
            System.out.println("-------------------------------------------------------------");
            int id_commande;
            HashMap<String,Object> h;
            do{
                id_commande = priseEntier("Identification de la commande : ");
                h = co.infosCommande(id_commande);
                if(h == null)
                    System.out.println("Commande non reconnu");
            }while(h == null);

            int nombre_produit = priseEntier("Nombre de produit : ");
            HashMap<String,Integer> produit_colis = new HashMap<String,Integer>();
            for(int i = 0 ; i < nombre_produit; i++){
                int quantite;
                String produit;
                do{
                    System.out.print("Réference du produit "+(i+1)+" :");
                    produit = in.next();
                    quantite = priseEntier("Quantite : ");
                }while(!produitsExiste(produit,quantite));
                    produit_colis.put(produit,quantite);
            }
            if(co.nouveauColis(id_commande,produit_colis) > 0)
                System.out.println("Ajout accepter");
            else
                System.out.println("Ajouter refuséé ");
            Pause(1000);

        }
        else if(choix == 2){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Ajouter une palette : ");
            System.out.println("-------------------------------------------------------------");
            int nombre_colis = priseEntier("Nombre de colis : ");
            LinkedList list = new LinkedList();
            for(int i = 0 ; i < nombre_colis; i++){
                int colis;
                HashMap<String,Object> hb;
                do{
                    colis = priseEntier("Identifiant du colis "+(i+1)+": ");
                    hb = co.infosColis(colis);
                    if(hb == null)
                        System.out.println("Colis non reconnu");
                }while(hb == null);
                list.push(colis);
            }
            if(co.nouvellePalette(list) > 0)
                System.out.println("Ajout accepter");
            else
                System.out.println("Ajouter refuséé ");
            Pause(1000);
        }

    }

    public static void choix_gerant(int choix){
        if(choix == 0){
            int pers = 1;
            do{
                System.out.println("Choix du type"); 
                System.out.println(" 0 - client"); 
                System.out.println(" 1 - employer"); 
                pers= priseEntier("Choix : ");
            }while((pers < 0)&&(pers > 1));
            if(pers == 1){
                int [] taille ={15,15,15,15};
                String [] champ = {"login","prénom","nom","type"};
                affichage_less(co.listeEmployes(),champ,taille,"Voir les clients les plus dépensiés :");

            }
            if(pers == 0){
                int [] taille = {10,16,34,16,11,10,18};
                String[] champ = {"login","nom","adresse","ville","code postal","pays","téléphone"}; 
                affichage_less(co.listeClients(),champ,taille,"Voir les clients les plus dépensiés :");

            }
        }
        else if(choix == 1){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Changer Un prix de produit");
            System.out.println("-------------------------------------------------------------");
            String prod;
            do{
                System.out.print("Identification du produit: ");
                prod = in.next();
            }while(!produitsExiste(prod,0));
            System.out.print("Nouveau prix du produit : ");
            float new_prix = in.nextFloat();
            if(co.changePrix(prod,new_prix))
                System.out.println("Modification effectuer");
            else{
                System.out.println("Modification echouer");
            }
            Pause(1000);

        }
        else if(choix == 2){
            int [] taille = {16,70,10,5,5,17,8};
            String[] champ = {"référence","description","qualifiant","prix","poids","quantité restante","quantite"};
            affichage_less(co.produitPlusVendu(),champ,taille,"Voir les produits les plus vendus :");

        }
        else if(choix == 3){
            int [] taille = {10,16,34,16,11,10,18,13};
            String[] champ = {"login","nom","adresse","ville","code postal","pays","téléphone","Total dépensé"}; 
            affichage_less(co.listeClientsPlusDepensies(),champ,taille,"Voir les clients les plus dépensiés :");

        }
        else if(choix == 4){
            System.out.print("Pas Implementer"); 
            Pause(1000);
        }

    }

    public static void choix_douane(int choix){
        System.out.println("Pas implémenter");
        Pause(2000);
    }

    public static boolean produitsExiste(String ref,int quantite){
        HashMap<String,Object> hb = co.infosProduit(ref);
        if(hb == null){
            System.out.println("Le produit n'existe pas");
            return false;
        }
        int quantite_max = ((Integer) hb.get("quantité restante")).intValue();
        if(quantite > quantite_max){
            System.out.println("Il ne reste "+quantite_max+" au produit "+ref);
            return false;
        }
        return true;

    }

    public static void affichage_less (LinkedList<HashMap<String,Object>> liste, String[] champ,int[] taille, String req){
        int choix = -1;
        int ligne = -1;
        int total = 0;
        while(choix != 1){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println(req);
            System.out.println("-------------------------------------------------------------");
            //affiche les nom de colonne
            for(int i = 0; i < champ.length; i++){
                String mot = champ[i];
                for ( int j = mot.length(); j < taille[i] ;j++){
                    mot += " ";
                }
                System.out.print(mot+"| ");
            }
            System.out.println();
            //affiche le contenu du LinkedListe
            while((total < liste.size()) && (!(ligne% 15 == 0 ))){
                if (ligne == -1) {
                    ligne = 0;
                }

                HashMap<String,Object> hb = liste.get(total);
                for(int i = 0; i < champ.length; i++){
                    String mot;
                    if(hb.get(champ[i]) != null)
                        mot = (String) hb.get(champ[i]).toString();
                    else
                        mot = "null";
                    for ( int j = mot.length(); j < taille[i] ;j++){
                        mot = " "+ mot;
                    }
                    System.out.print(mot+"| ");
                }
                System.out.println();

                total++;
                ligne++;

            }
            if(total < liste.size()){
                System.out.println("-------------------------------------------------------------");
                System.out.println("0 - Continuer");
                System.out.println("1 - Quitter"); 
                System.out.println("-------------------------------------------------------------");
                do{
                    choix = priseEntier("choix : "); 
                }while((choix != 0 )&&( choix != 1));
            }
            else{
                System.out.println("-------------------------------------------------------------");
                System.out.println("1 - Quitter"); 
                System.out.println("-------------------------------------------------------------");
                do{
                    choix = priseEntier("choix : "); 
                }while(( choix != 1));

            }
        }
    }

    public static void affichage_less (HashMap<String,Object> hb, String[] champ,int[] taille, String req){
        int choix = -1;

        while(choix != 0){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println();
            System.out.println(req);
            System.out.println("-------------------------------------------------------------");
            //affiche le nom de colonne
            for(int i = 0; i < champ.length; i++){
                String mot = champ[i];
                for ( int j = mot.length(); j < taille[i] ;j++){
                    mot+= " ";
                }
                System.out.print(mot+"| ");
            }
            System.out.println();
            //affiche le contenu du LinkedListe
            for(int i = 0; i < champ.length; i++){
                String mot;
                if(hb.get(champ[i]) != null)
                    mot = (String) hb.get(champ[i]).toString();
                else
                    mot = "null";
                for ( int j = mot.length(); j < taille[i] ;j++){
                    mot = " "+mot;
                }
                System.out.print(mot+"| ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------");
            System.out.println("0 - Quitter");
            System.out.println("-------------------------------------------------------------");
            do{
                choix = priseEntier("choix : "); 
            }while((choix != 0 ));
        }
    }

    public static void main(String[]args)throws SQLException , ClassNotFoundException {
        //prend en argument le nom bdd et utilisateur et mdp_bdd
        if(args.length == 3){
            String nom_bdd = args[0];
            String user = args[1];
            String mdp_bdd = args[2];
            //crée notre interface et la lance
            co = new ConnexionBDD(nom_bdd, user, mdp_bdd,true);
            menuConnexion();
        }
    }
}
