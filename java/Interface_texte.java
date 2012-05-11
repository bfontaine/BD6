import java.io.*;
import java.util.Scanner;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Calendar;


public class Interface_texte{
    static ConnexionBDD co;
    static Scanner in = new Scanner(System.in);
    static String Login;

    /**
     * Imprime le menu de connection
     **/
    public static void menuConnexion(){

        System.out.print("\033c"); //nettoyage de l'ecran
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

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Passer une commande");
        System.out.println("1 - Situation colis"); 
        System.out.println("2 - Lister les produits disponibles");
        System.out.println("3 - Changer son login/mdp");
        System.out.println("4 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static int menuGerant(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - A accès à la liste des employés/clients");
        System.out.println("1 - Changer le prix des produits"); 
        System.out.println("2 - Voir les produits les plus vendus");
        System.out.println("3 - Voir les clients les plus depensiés");
        System.out.println("4 - Voir les employés les moins actifs");
        System.out.println("5 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static int  menuEmballeur(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Information sur un colis");
        System.out.println("1 - Entrer les colis emballés"); 
        System.out.println("2 - Entrer les palettes préparées");
        System.out.println("3 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static int menuTransporteur(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Connaître la date limite de livraison souhaitée par le client");
        System.out.println("1 - Savoir si un colis est fragile ou dangereux"); 
        System.out.println("2 - Changer situation du colis");
        System.out.println("3 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static int menuDouane(){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - ");
        System.out.println("1 - "); 
        System.out.println("2 - ");
        System.out.println("3 - ");
        System.out.println("4 - ");
        System.out.println("5 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static void choix_client(int choix){
        if(choix == 0){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Insertion d'un commande :");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Nombre de produit :");
            int total_colis = in.nextInt();
            HashMap<String,Integer> hb = new HashMap<String,Integer>();
            for (int i = 0; i < total_colis; i++){;
                System.out.println("Réference du produit "+i+" :");
                String produit = in.next();
                System.out.println("Quantite: ");
                int quantite = in.nextInt();
                hb.put(produit,quantite);
            }
            System.out.println("-------------------------------------------------------------");
            System.out.println("Date de Livraison:");
            System.out.println("-------------------------------------------------------------");
            System.out.println("Annéé:");
            int year = in.nextInt();
            System.out.println("Mois:");
            int mois = in.nextInt();
            System.out.println("Jour:");
            int jour = in.nextInt();
            Calendar date = Calendar.getInstance();
            date.set(year,mois,jour);
            if(co.nouvelleCommande(Login,date,hb) > 0)
                System.out.println("Commande valide");
            else
                System.out.println("Commande echoué");
        }
        else if(choix == 1){
            int [] taille = {5,20,21,10,22,22,22};
            String[] champ = {"id","id_commande","produits","état","date d'emballage","date d'expédition","date de livraison"};
            System.out.print("Identifiant du colis :"); 
            int colis = in.nextInt();
            affichage_less(co.infosColis(colis),champ,taille,"Information sur le colis :");

        }
        else if(choix == 2){
            int [] taille = {16,60,10,7,5,5};
            String[] champ = {"référence","description","qualifiant","prix","poids","quantité restante"};

            affichage_less(co.listeProduitsRestants(),champ,taille,"Liste des Produit disponible :");
        }
        else if(choix == 3){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Changement de password :");
            System.out.println("-------------------------------------------------------------");
            System.out.print("Nouveau login :");
            String new_login = in.next();
            String password = PasswordField.readPassword("Ancien Password : ");  
            String new_password = PasswordField.readPassword("Nouveaux Password : ");  
            if(co.changerLogin(Login,new_login)){
                System.out.print("Changement Login effectuer");
                Login = new_login;
            }
            else{
                System.out.print("Erreur lors du changement de login");

            }
            if(co.changerMdp(new_login,password,new_password))
                System.out.print("Changement Password effectuer:");
            else{
                System.out.print("Erreur lors du changement de password");
            }

        }
    }

    public static void choix_transporteur(int choix){
        if(choix == 2){
             System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Changer situation d'une paletter");
            System.out.println("-------------------------------------------------------------");
            System.out.print("Indentification de la palette:");
            int palette = in.nextInt();
            if(co.livrerPalette(palette))
                System.out.println("Modification effectuer");
            else{
                System.out.println("Modification echouer");
            }

        }
        else if(choix == 1){
            int [] taille = {5,20,21,10,22,22,22};
            String[] champ = {"id","id_commande","produits","état","date d'emballage","date d'expédition","date de livraison"};
            System.out.print("Identifiant du colis :"); 
            int colis = in.nextInt();
            affichage_less(co.infosColis(colis),champ,taille,"Information sur le colis :");

        }
        else if(choix == 0){
            int [] taille = {5,20,21,10,22,22,22};
            String[] champ = {"id","id_commande","produits","état","date d'emballage","date d'expédition","date de livraison"};
            System.out.print("Identifiant de la commande:"); 
            int commande = in.nextInt();
            affichage_less(co.infosCommande(commande),champ,taille,"Information de la commande :");
        }
    }

    public static void choix_emballeur(int choix){
        if(choix == 0){

        }
        else if(choix == 1){

        }
        else if(choix == 2){

        }

    }

    public static void choix_gerant(int choix){
        if(choix == 0){
            int pers = 1;
            do{
                System.out.println("Choix du type"); 
                System.out.println(" 0 - client"); 
                System.out.println(" 1 - employer"); 
                System.out.print("Choix :");
                pers= in.nextInt();
            }while((pers < 0)&&(pers > 1));
            if(pers == 1){

            }
            if(pers == 0){
                int [] taille = {20,20,20,20,22,22,22,6};
                String[] champ = {"prénom","nom","adresse","ville","code postal","pays","téléphone","Total depensiés"}; 
                affichage_less(co.listeClientsPlusDepensies(),champ,taille,"Voir les clients les plus dépensiés :");

            }
        }
        else if(choix == 1){
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Changer Un prix de produit");
            System.out.println("-------------------------------------------------------------");
            System.out.print("Identification du produit:");
            String prod = in.next();
            System.out.print("Nouveau prix du produit :");
            float new_prix = in.nextInt();
            if(co.changePrix(prod,new_prix))
                System.out.println("Modification effectuer");
            else{
                System.out.println("Modification echouer");
            }

        }
        else if(choix == 2){
            int [] taille = {16,60,10,7,5,5,7};
            String[] champ = {"référence","description","qualifiant","prix","poids","quantité restante","quantite"};
            affichage_less(co.produitPlusVendu(),champ,taille,"Voir les produits les plus vendus :");

        }
        else if(choix == 3){
            int [] taille = {10,10,20,20,7,9,9,6};
            String[] champ = {"prénom","nom","adresse","ville","code postal","pays","téléphone","Total depensiés"};
            affichage_less(co.listeClientsPlusDepensies(),champ,taille,"Voir les clients les plus dépensiés :");

        }
        else if(choix == 4){
            System.out.print("Pas Implementer"); 
        }

    }

    public static void choix_douane(int choix){
        if(choix == 2){

        }
        else if(choix == 1){

        }

    }

    public static void affichage_less (LinkedList<HashMap<String,Object>> liste, String[] champ,int[] taille, String req){
        int choix = -1;
        int ligne = 0;
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
            do{
                HashMap<String,Object> hb = liste.get(total);
                for(int i = 0; i < champ.length; i++){
                    String mot;
                    if(hb.get(champ[i]) != null)
                        mot = (String) hb.get(champ[i]).toString();
                    else
                        mot = "null";
                    for ( int j = mot.length(); j < taille[i] ;j++){
                        mot += " ";
                    }
                    System.out.print(mot+"| ");
                }
                System.out.println();
                System.out.println(1%10+" "+ total+" "+(ligne % 10 == 0 )+" "+(total < liste.size()));

                ligne++;
                total++;

            }while((!(ligne% 10 == 0 )) || (total > liste.size()));
            System.out.println("-------------------------------------------------------------");
            System.out.println("0 - Continuer");
            System.out.println("1 - Quitter"); 
            System.out.println("-------------------------------------------------------------");
            System.out.print("choix :"); 
            choix = in.nextInt(); 
        }
    }

    public static void affichage_less (HashMap<String,Object> hb, String[] champ,int[] taille, String req){
        int choix = -1;
        while(choix != 0){
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
            for(int i = 0; i < champ.length; i++){
                String mot;
                if(hb.get(champ[i]) != null)
                    mot = (String) hb.get(champ[i]).toString();
                else
                    mot = "null";
                for ( int j = mot.length(); j < taille[i] ;j++){
                    mot += " ";
                }
                System.out.print(mot+"| ");
            }
            System.out.println();
            System.out.println("-------------------------------------------------------------");
            System.out.println("0 - Continuer");
            System.out.println("-------------------------------------------------------------");
            System.out.print("choix :"); 
            choix = in.nextInt(); 
        }
    }

    public static void main(String[]args)throws SQLException , ClassNotFoundException {
        //prend en argument le nom bdd et utilisateur et mdp_bdd
        if(args.length == 3){
            String nom_bdd = args[0];
            String user = args[1];
            String mdp_bdd = args[2];
            //crée notre interface et la lance
            co = new ConnexionBDD(nom_bdd, user, mdp_bdd);
            menuConnexion();
        }
    }
}
