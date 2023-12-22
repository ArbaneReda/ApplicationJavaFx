import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;

/**
 * Cette classe représente une communauté d'agglomération.
 */
public class CommunauteAgglomeration {

    private final int nombreVilles;
    private Map<Ville, List<Ville>> mapVilles;
    private Set<String> nomsVilles;

    /**
     * Constructeur de la classe CommunauteAgglomeration
     * 
     * @param : nombreVilles
     */
    public CommunauteAgglomeration(int nombreVilles) {
        this.nombreVilles = nombreVilles;
        this.mapVilles = new HashMap<Ville, List<Ville>>();
        this.nomsVilles = new HashSet<>();
    }

    /**
     * Constructeur de la classe CommunauteAgglomeration
     * 
     * @param : void
     */
    public CommunauteAgglomeration() {
        this.nombreVilles = 0;
        this.mapVilles = new HashMap<Ville, List<Ville>>();
        this.nomsVilles = new HashSet<>();
    }

    /**
     * Méthode pour effacer toutes les zones de recharge
     * 
     * @param : void
     * @return : void
     */
    public void effacerZonesRecharge() {
        for (Ville ville : this.mapVilles.keySet()) {
            ville.setZoneRecharge(false);
        }
    }

    /**
     * Méthode pour obtenir une copie de la map des villes
     * 
     * @param : void
     * @return : Map<Ville, List<Ville>>
     */
    public Map<Ville, List<Ville>> getMapVilles() {
        return new HashMap<>(this.mapVilles);
    }

    /**
     * Méthode pour ajouter une ville
     * 
     * @param : ville de type Ville
     * @return : void
     */
    public void ajouterVille(Ville ville) {
        System.out.println();
        String nomVille = ville.getNom();
        if (!nomsVilles.contains(nomVille)) {
            this.mapVilles.put(ville, new ArrayList<Ville>());
            nomsVilles.add(nomVille);
            System.out.println(" * Ville créée : " + nomVille);
        } else {
            System.out.println(" /!/ Une ville avec le nom '" + nomVille + "' existe déjà.");
        }
    }

    /**
     * Méthode pour ajouter uyne route entre deux villes
     * 
     * @param : ville1 de type Ville
     * @param : ville2 de type Ville
     * @return : boolean
     */
    public boolean ajouterRoute(Ville ville1, Ville ville2) {
        if (this.mapVilles.containsKey(ville1) && this.mapVilles.containsKey(ville2)) {
            if (!ville1.equals(ville2)) {
                if (!this.mapVilles.get(ville1).contains(ville2) && !this.mapVilles.get(ville2).contains(ville1)) {
                    this.mapVilles.get(ville1).add(ville2);
                    this.mapVilles.get(ville2).add(ville1);
                    System.out.println();
                    System.out.println(" * Route ajoutée entre " + ville1.getNom() + " et " + ville2.getNom() + ".");
                    return true;
                } else {
                    System.out.println(" /!/ Une route existe déjà entre ces deux villes.");
                    return false;
                }
            } else {
                System.out.println(" /!/ Vous ne pouvez pas ajouter de route entre une ville et elle-même.");
                return false;
            }
        } else {
            System.out.println(" /!/ Au moins l'une des deux villes n'existe pas. Impossible d'ajouter une route.");
            System.out.println(
                    " /!/ Il est possible que vous ayez mal écrit les majuscules ou les minuscules dans le nom de la ville.");
            return false;
        }
    }

    /**
     * Méthode pour supprimer une route entre deux villes
     * 
     * @param : ville1 de type Ville
     * @param : ville2 de type Ville
     * @return : boolean
     */
    public boolean supprimerRoute(Ville ville1, Ville ville2) {
        if (this.mapVilles.containsKey(ville1) && this.mapVilles.containsKey(ville2)) {
            if (!ville1.equals(ville2)) {
                if (this.mapVilles.get(ville1).contains(ville2) && this.mapVilles.get(ville2).contains(ville1)) {
                    this.mapVilles.get(ville1).remove(ville2);
                    this.mapVilles.get(ville2).remove(ville1);
                    System.out.println(" * Route supprimée entre " + ville1.getNom() + " et " + ville2.getNom() + ".");
                    return true;
                } else {
                    System.out.println(" /!/ Impossible de supprimer une route inexistante entre ces deux villes.");
                    return false;
                }
            } else {
                System.out.println(" /!/ Vous ne pouvez pas supprimer une route entre une ville et elle-même.");
                return false;
            }
        } else {
            System.out.println(" /!/ Au moins l'une des deux villes n'existe pas. Impossible de supprimer une route.");
            System.out.println(
                    " /!/ Il est possible que vous ayez mal écrit les majuscules ou les minuscules dans le nom de la ville.");
            return false;
        }
    }

    /**
     * Méthode pour obtenir les villes adjacentes à une ville
     * 
     * @param : ville de type Ville
     * @return : List<Ville>
     */
    public List<Ville> getVillesAdjacentes(Ville ville) {
        return this.mapVilles.get(ville);
    }

    /**
     * Méthode pour obtenir une ville à partir de son nom
     * 
     * @param nomVille de type String : le nom de la ville à obtenir
     * @return Ville la ville correspondante
     */
    public Ville getVille(String nomVille) {
        for (Ville ville : this.mapVilles.keySet()) {
            if (ville.getNom().equals(nomVille)) {
                return ville;
            }
        }
        return null;
    }

    /**
     * Méthode pour obtenir la liste des villes
     * 
     * @param : void
     * @return : List<Ville>
     */
    public List<Ville> getVilles() {
        return new ArrayList<Ville>(this.mapVilles.keySet());
    }

    /**
     * Méthode pour obtenir le nombre de villes
     * 
     * @param : void
     * @return : int
     */
    public int getNombreVilles() {
        return this.nombreVilles;
    }

    /**
     * Méthode pour obtenir les villes avec une zone de recharge
     * 
     * @param : void
     * @return : List<Ville>
     */
    public List<Ville> getZonesRecharge() { // retourne les villes avec une zone de recharge
        List<Ville> villesAvecZoneRecharge = new ArrayList<Ville>();
        for (Ville ville : this.mapVilles.keySet()) {
            if (ville.aZoneRecharge()) {
                villesAvecZoneRecharge.add(ville);
            }
        }
        return villesAvecZoneRecharge;
    }

    /**
     * Méthode pour obtenir les routes
     * 
     * @param : void
     * @return : Map<Ville, List<Ville>>
     */
    public Map<Ville, List<Ville>> getRoutes() {
        return this.mapVilles;
    }

    /**
     * Méthode pour obtenir le nombre de routes
     * 
     * @param : void
     * @return : int
     */
    public boolean respecteContrainteAccessibiliteCommunaute(Set<Ville> villesAvecZone) {
        for (Ville ville : this.mapVilles.keySet()) {
            if (!ville.aZoneRecharge() && !estAccessible(ville, villesAvecZone)) {
                return false; // Si une ville sans zone de recharge n'est pas accessible, la contrainte n'est
                              // pas respectée
            }
        }
        return true; // Toutes les villes respectent la contrainte d'accessibilité
    }

    /**
     * Méthode pour vérifier si une ville est accessible
     * 
     * @param : ville de type Ville
     * @param : villesAvecZone de type Set<Ville>
     * @return : boolean
     */
    private boolean estAccessible(Ville ville, Set<Ville> villesAvecZone) {
        if (villesAvecZone.contains(ville)) {
            return true; // La ville a une zone de recharge
        }

        for (Ville villeAdjacente : getVillesAdjacentes(ville)) {
            if (villesAvecZone.contains(villeAdjacente)) {
                return true; // Une ville adjacente a une zone de recharge
            }
        }

        return false; // Aucune des conditions ci-dessus n'a été satisfaite, la ville n'est pas
                      // accessible
    }

    /**
     * Méthode pour ajouter une zone de recharge
     * 
     * @param : ville de type Ville
     * @return : boolean
     */
    public boolean ajouterZoneRecharge(Ville ville) {
        System.out.println();
        if (!ville.aZoneRecharge()) {
            ville.setZoneRecharge(true);
            System.out.println(" * Zone de recharge ajoutée à la ville " + ville.getNom());
            return true;
        } else {
            System.out.println(" /!/ La ville " + ville.getNom() + " possède déjà une zone de recharge.");
            return false;
        }
    }

    /**
     * Méthode pour verifier si une communauté respecte la contrainte
     * d'accessibilité
     * 
     * @param : void
     * @return : boolean
     */
    public boolean respecteContrainteAccessibiliteCommunaute() {
        for (Ville ville : mapVilles.keySet()) {
            if (!respecteContrainteAccessibilite(ville)) {
                return false; // Si au moins une ville ne respecte pas la contrainte, la communauté ne la
                              // respecte pas non plus
            }
        }
        return true; // Toutes les villes respectent la contrainte d'accessibilité
    }

    /**
     * Méthode pour vérifier si une ville respecte la contrainte d'accessibilité
     * 
     * @param : ville de type Ville
     * @return : boolean
     */
    public boolean respecteContrainteAccessibilite(Ville ville) {
        if (ville.aZoneRecharge()) {
            return true; // Une ville avec une zone de recharge respecte la contrainte
        }

        for (Ville villeAdjacente : getVillesAdjacentes(ville)) {
            if (villeAdjacente.aZoneRecharge()) {
                return true; // Si une ville adjacente a une zone de recharge, la contrainte est respectée
            }
        }

        return false; // Aucune des conditions ci-dessus n'a été satisfaite, la contrainte n'est pas
                      // respectée
    }

    /**
     * Méthode pour obtenir le nombre de zones de recharge adjacents à une ville
     * 
     * @param : ville de type Ville
     * @return : int
     */
    public int nombreZonesRechargeAdjacents(Ville ville) {
        int nombreZonesRechargeAdjacents = 0;
        for (Ville villeAdjacente : getVillesAdjacentes(ville)) {
            if (villeAdjacente.aZoneRecharge()) {
                nombreZonesRechargeAdjacents++;
            }
        }
        return nombreZonesRechargeAdjacents;
    }

    /*
     * Methodes pour ajouter ou supprimer une zone de recharge
     * @param ville de type Ville : la ville à laquelle on veut ajouter ou supprimer une zone de recharge
     * @return : void
     */
    public void supprimerZoneRecharge(Ville ville) {
        boolean condition1Check = false;
        boolean condition2Check = false;
        boolean condition3Check = false;
        boolean condition3FinalCheck = false;
        boolean apresSuppression = false;

        if (!ville.aZoneRecharge()) {
            System.out.println(" /!/ Vous ne pouvez pas supprimer une zone de recharge qui n'existe pas !");
            return;
        }

        // si une ville est isolée et qu'elle a une zone de recharge, on ne peut pas la
        // supprimer
        if (getVillesAdjacentes(ville).isEmpty()) {
            System.out.println(" /!/ Vous ne pouvez pas supprimer une zone de recharge d'une ville isolée !");
            return;
        }

        // si tous les voisins de la ville en question ont une zone de recharge, on peut
        // supprimer la zone de recharge
        // et condition1Check devient true
        for (Ville v : getVillesAdjacentes(ville)) {
            if (v.aZoneRecharge()) {
                condition1Check = true;
                System.out.println(
                        " /!/ Vous pouvez supprimer cette zone de recharge car tous les voisins de la ville en question ont une zone de recharge.");
            } else {
                condition1Check = false;
                break;
            }
        }

        // maintenant les cas ou au moins un voisin de la ville en question n'a pas de
        // zone de recharge

        // si un voisin de la ville en question n'a pas une zone de recharge et n'a
        // aucun voisin, donc il dépend
        // forcément de la ville en question, donc on ne peut pas supprimer la zone de
        // recharge de la ville en question
        // cela est pour tous les voisins de la ville en question
        // et dès que cette vérification est faite pour tous les voisins,
        // condition2Check devient true
        for (Ville v : getVillesAdjacentes(ville)) {
            if (!v.aZoneRecharge() && getVillesAdjacentes(v).isEmpty()) {
                condition2Check = false;
                System.out.println(
                        " /!/ Vous ne pouvez pas supprimer cette zone de recharge car un des voisins de la ville en question est isolé et dépend de cette ville.");
                break;
            } else {
                condition2Check = true;
            }
        }

        // si au moins un voisins de la ville en question n'a pas de zone de recharge,
        // on parcourt les voisins de ce voisin pour
        // trouver une ville qui a une zone de recharge. Si on en trouve une, on peut
        // supprimer la zone de recharge de la ville en question
        // et condition2Check devient true dès qu'on vérifie cela pour tous les voisins
        // sans zone de recharge
        // on ne prend pas en compte la ville en question quand on vérifie les voisins
        // des voisins
        for (Ville v : getVillesAdjacentes(ville)) {
            if (!v.aZoneRecharge()) {
                for (Ville v2 : getVillesAdjacentes(v)) {
                    if (v2.aZoneRecharge() && (v2.getNom()).equals(ville.getNom()) == false) {
                        condition3Check = true;
                        break;
                    }
                }
            }
            if (condition3Check) {
                condition3FinalCheck = true;
            } else { // ce voisin n'a pas de voisins avec une zone de recharge, donc on ne peut pas
                     // supprimer la zone de recharge de la ville en question
                condition3FinalCheck = false;
                System.out.println(
                        " /!/ Vous ne pouvez pas supprimer cette zone de recharge car un des voisins de la ville en question n'a pas de voisins avec une zone de recharge.");
                break;
            }

        }

        if (condition1Check || (condition2Check && condition3FinalCheck)) {
            ville.setZoneRecharge(false);
            System.out.println(" * La zone de recharge a été supprimée de la ville " + ville.getNom());
        } else {
            System.out.println(
                    " /!/ Vous ne pouvez pas supprimer cette zone de recharge car la ville n'est pas connectée à une autre ville avec une zone de recharge.");
        }

        // Enfin, si après supprésion de la zone de recharge, si la ville en question
        // n'est connéctée à aucune autre ville avec une zone de recharge,
        // la zone de recharge est rétablie
        if (condition1Check || (condition2Check && condition3FinalCheck)) {
            for (Ville v : getVillesAdjacentes(ville)) {
                if (v.aZoneRecharge()) {
                    apresSuppression = true;
                    break;
                }
            }
            if (!apresSuppression) {
                ville.setZoneRecharge(true);
                System.out.println(" /!/ La zone de recharge a été rétablie pour la ville " + ville.getNom()
                        + " car elle devenue isolée après la suppression. Donc ce n'est pas possible finalement");
            }
        }
    }

}
