import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Cette classe contient les algorithmes pour optimiser les zones de recharge
 * dans une communauté d'agglomération.
 */
public class Algorithmes {

    private static List<Ville> solutionOptimale = null;
    private static int nombreMinZonesRecharge = Integer.MAX_VALUE;

    /**
     * Applique l'algorithme naïf pour ajouter une zone de recharge à chaque ville
     * qui n'en a pas.
     * 
     * @param communaute La communauté d'agglomération.
     * @return La communauté d'agglomération avec les zones de recharge ajoutées.
     */
    public static CommunauteAgglomeration algorithmeNaif(CommunauteAgglomeration communaute) {
        System.out.println(" ▓ Utilisation de l'algorithme naïf. ▓");
        for (Ville ville : communaute.getVilles()) {
            if (!ville.aZoneRecharge()) {
                communaute.ajouterZoneRecharge(ville);
            }
        }
        return communaute;
    }

    /**
     * Applique l'algorithme d'optimisation pour trouver la solution optimale de
     * zones de recharge.
     * 
     * @param communaute La communauté d'agglomération.
     */
    public static void algorithmeOptimiser(CommunauteAgglomeration communaute) {

        communaute.effacerZonesRecharge(); // Efface toutes les zones de recharge

        List<Ville> villes = communaute.getVilles();
        solutionOptimale = new ArrayList<>();
        nombreMinZonesRecharge = Integer.MAX_VALUE;

        List<Ville> etatInitial = new ArrayList<>();
        for (Ville ville : villes) {
            if (!ville.aZoneRecharge()) {
                etatInitial.add(ville);
            }
        }

        optimiserRecursivement(communaute, 0, etatInitial, new HashSet<>());
        communaute.effacerZonesRecharge();

        for (Ville ville : solutionOptimale) {
            communaute.ajouterZoneRecharge(ville);
        }
    }

    /**
     * Méthode récursive pour trouver la solution optimale de zones de recharge.
     * 
     * @param communaute     La communauté d'agglomération.
     * @param index          L'index de la ville actuelle.
     * @param villesSansZone La liste des villes sans zone de recharge.
     * @param villesAvecZone La liste des villes avec zone de recharge.
     */
    private static void optimiserRecursivement(CommunauteAgglomeration communaute, int index,
            List<Ville> villesSansZone, Set<Ville> villesAvecZone) {

        if (index == villesSansZone.size()) {
            if (villesAvecZone.size() < nombreMinZonesRecharge
                    && communaute.respecteContrainteAccessibiliteCommunaute(villesAvecZone)) {
                nombreMinZonesRecharge = villesAvecZone.size();
                solutionOptimale = new ArrayList<>(villesAvecZone);
            }
            return;
        }

        Ville villeActuelle = villesSansZone.get(index);

        // Cas sans ajouter de zone de recharge à la ville actuelle
        optimiserRecursivement(communaute, index + 1, villesSansZone, villesAvecZone);

        // Cas avec l'ajout d'une zone de recharge
        villesAvecZone.add(villeActuelle);
        optimiserRecursivement(communaute, index + 1, villesSansZone, villesAvecZone);
        villesAvecZone.remove(villeActuelle);
    }
}
