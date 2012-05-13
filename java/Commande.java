class Commande extends ObjetBDD {
  private int id = 0;
  private LinkedList<Produit> produits = null;
  private Calendar date_commande = null;
  private Calendar date_prevue = null;
  private Calendar date_livree = null;
  private Client client = null;
  private int frais = 100;
  private int prix = 0;
}
