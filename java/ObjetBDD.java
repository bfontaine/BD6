class ObjetBDD {

  private String table;
  private ConnexionBDD co;

  public ObjectBdd(ConnexionBDD co, String table) {
    this.table = table;
    this.co = co;
  }
}
