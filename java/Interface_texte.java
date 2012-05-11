import java.io.*;
import java.util.Scanner;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;


public class Interface_texte{
    static ConnexionBDD co;
    static Scanner in = new Scanner(System.in);

    /**
     * Imprime le menu de connection
     **/
    public static void menuConnexion(){

        System.out.print("\033c"); //nettoyage de l'ecran
        String type;
        String login;
        boolean mdp_incorrect = false; 

        do{
            System.out.print("\033c"); //nettoyage de l'ecran
            System.out.println("Connexion Projet BDD :");
            System.out.println("-------------------------------------------------------------");
            if(mdp_incorrect)
                System.out.println("Login ou Password incorrecte");

            System.out.print("Utilisateur : ");
            login = in.next();
            String password = PasswordField.readPassword(" Password : ");  

            type = co.connecteUtilisateur(login,password);
            if(type == null)
                mdp_incorrect = true;

        }while(type == null);
        int choix = -1;
        if (type.equals("client")) {
            while(choix != 4 ){
                choix = menuClient(login);
                if((choix < 4) || (choix > -1)){
                    choix_client(choix);
                }
            }

        }
        else if (type.equals("transporteur")) {
            while(choix != 3 ){
                choix = menuTransporteur(login);
                if((choix < 2) || (choix > -1)){
                    choix_client(choix);
                }
            }
        }
        else if (type.equals("emballeur")) {
            while(choix != 3 ){
                choix = menuEmballeur(login);
                if((choix < 2) || (choix > -1)){
                    choix_client(choix);
                }
            }
        }
        else if (type.equals("gerant")) {
            while(choix != 5 ){
                choix = menuGerant(login);
                if((choix < 4) || (choix > -1)){
                    choix_client(choix);
                }
            }
        }
        else if (type.equals("douane")) {
            while(choix != 5 ){
                choix = menuDouane(login);
                if((choix < 4) || (choix > -1)){
                    choix_client(choix);
                }
            }
        }
    }

    public static int menuClient(String login){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Passer une commande");
        System.out.println("1 - Situation commande"); 
        System.out.println("2 - Lister les produits disponibles");
        System.out.println("3 - Changer son login/mdp");
        System.out.println("4 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static int menuGerant(String login){
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

    public static int  menuTransporteur(String login){
        System.out.print("\033c"); //nettoyage de l'ecran

        // -------------------
        // Impression du menu   
        // -------------------

        System.out.println("Veuillez entrer votre choix :");
        System.out.println("-------------------------------------------------------------");
        System.out.println("0 - Connaître la liste des commandes d'un client");
        System.out.println("1 - Entrer les colis emballés"); 
        System.out.println("2 - Entrer les palettes préparées");
        System.out.println("3 - quitter");
        System.out.println("-------------------------------------------------------------");
        System.out.print("choix :");

        return in.nextInt();
    }

    public static int menuEmballeur(String login){
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

    public static int menuDouane(String login){
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

        }
        else if(choix == 1){

        }
        else if(choix == 2){
            int [] taille = {16,50,10,7,4,5};
            String[] champ = {"référence","description","qualifiant","prix","poids","quantité restante"};

            affichage_less(co.listeProduitsRestants(),champ,taille,"Liste des Produit disponible :");
        }
        else if(choix == 3){

        }
    }


    public static void affichage_less (LinkedList<HashMap<String,Object>> liste, String[] champ,int[] taille, String nom req){
        int choix = -1;
        int ligne = 0;
        int total = 0;
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
            //affiche le contenu du LinkedListe
            do{
                HashMap<String,Object> hb = liste.get(total);
                for(int i = 0; i < champ.length; i++){
                    String mot = (String)hb.get(champ[i]);
                    for ( int j = mot.length(); j < taille[i] ;j++){
                        mot += " ";
                    }
                    System.out.print(mot+"| ");
                }
                System.out.println();
                ligne++;
                total++;

            }while((ligne % 10 == 0 )||(total < liste.size()));
            System.out.println("-------------------------------------------------------------");
            System.out.println("0 - Continuer");
            System.out.println("1 - Quitter"); 
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