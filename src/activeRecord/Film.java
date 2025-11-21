package activeRecord;

public class Film {

    private String titre;
    private int id;
    private int id_real;

    public Film(String titre, Personne personne){
        this.titre = titre;
        this.id_real = personne.getId();
        this.id = -1;
    }

    private Film(String titre, int id, int id_real){
        this.titre = titre;
        this.id = id;
        this.id_real = id_real;
    }

}
