package activeRecord;

public class RealisateurAbsentException extends Exception {
    public RealisateurAbsentException() throws Exception {
        throw new Exception("La personne il n'est pas sauvegarde dans la BD.");
    }
}
