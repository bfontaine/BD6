import java.io.*;
import java.util.Scanner;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;


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
                    choix_client(choix);
                }
            }
        }
        else if (type.equals("emballeur")) {
            while(choix != 3 ){
                choix = menuEmballeur();
                if((choix < 2) || (choix > -1)){
                    choix_client(choix);
                }
            }
        }
        else if (type.equals("gerant")) {
            while(choix != 5 ){
                choix = menuGerant();
                if((choix < 4) || (choix > -1)){
                    choix_client(choix);
                }
            }
        }
        else if (type.equals("douane")) {
            while(choix != 5 ){
                choix = menuDouane();
                if((choix < 4) || (choix > -1)){
                    choix_client(choix);
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
        System.out.println("1 - Situation commande"); 
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

    public static int  menuTransporteur(){
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

    public static int menuEmballeur(){
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

        }
        else if(choix == 1){

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
                    String mot = (String) hb.get(champ[i]).toString();
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
