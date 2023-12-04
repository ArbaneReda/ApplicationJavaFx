
// Classe représentant une ville
public class Ville {
  // Nom de la ville
  private String nom;
  // Indicateur si la ville a une zone de recharge ou non
  private boolean aZoneRecharge;

  // Constructeur de la classe
  public Ville(String nom) {
    this.nom = nom;
    this.aZoneRecharge = false;
  }

  // Méthode pour obtenir le nom de la ville
  public String getNom() {
    return nom;
  }

  // Méthode pour vérifier si la ville a une zone de recharge
  public boolean aZoneRecharge() {
    return this.aZoneRecharge;
  }

  // Méthode pour définir le nom de la ville
  public void setNom(String nom) {
    this.nom = nom;
  }

  // Méthode pour définir si la ville a une zone de recharge
  public void setZoneRecharge(boolean aZoneRecharge) {
    this.aZoneRecharge = aZoneRecharge;
  }

  @Override
  public String toString() {
    return nom;
  }
}
