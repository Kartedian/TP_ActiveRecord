package activeRecord;

/**
 * Exception levée lorsqu'on tente de sauvegarder un film dont le réalisateur n'est pas encore présent en base (id_real == -1).
 */
public class RealisateurAbsentException extends Exception {
    public RealisateurAbsentException() throws Exception {
        // Le code original lance une nouvelle exception à l'intérieur du constructeur
        throw new Exception("La personne il n'est pas sauvegarde dans la BD.");
    }
}