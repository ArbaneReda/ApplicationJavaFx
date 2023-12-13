
// Classe représentant une ville
public class Ville {

  // ----------------   Attributs   ----------------

  private String nom;  // Nom de la ville
  private boolean aZoneRecharge;  // Indicateur si la ville a une zone de recharge ou non

  /*
   * ----------------   Constructeur   ----------------
   * 
   * @param nom Nom de la ville
   */
  public Ville(String nom) {
    this.nom = nom;
    this.aZoneRecharge = false;
  }

  // ----------------   Méthodes   ----------------

  /*
   * Méthode pour obtenir le nom de la ville
   */
  public String getNom() {
    return nom;
  }

  /*
   * Méthode pour obtenir si la ville a une zone de recharge
   */
  public boolean aZoneRecharge() {
    return this.aZoneRecharge;
  }

  /*
   * Méthode pour définir le nom de la ville
   * 
   * @param nom Nom de la ville
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  
  /*
   * Méthode pour déterminer si la ville a une zone de recharge
   * 
   * @param aZoneRecharge Indicateur si la ville a une zone de recharge ou non
   */
  public void setZoneRecharge(boolean aZoneRecharge) {
    this.aZoneRecharge = aZoneRecharge;
  }

  /*
   * Méthode pour obtenir une représentation textuelle de la ville sous la forme :
   * Ville [nom=nom, aZoneRecharge=aZoneRecharge]
   */
  @Override
  public String toString() {
    return "Ville [nom=" + nom + ", aZoneRecharge=" + aZoneRecharge + "]";
  }
}
