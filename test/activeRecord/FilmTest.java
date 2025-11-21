package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    @BeforeEach
    void createTable() {
        DBConnection.setConfig("./config/config.ini");
        DBConnection instance = DBConnection.getInstance();
        instance.setNomDB("testpersonne");

        Personne.createTable();
        Film.createTable();

        Personne pers1 = new Personne("Smith", "Will");
        pers1.save();
        Personne pers2 = new Personne("Holland", "Tom");
        pers2.save();

        Film film1 = new Film("Intestellar", pers1);
        film1.save();
        Film film2 = new Film("Jurassic Park", pers2);
        film2.save();
        Film film3 = new Film("Tron", pers1);
        film3.save();
    }

    @AfterEach
    void deleteTable() {
        Film.deleteTable();
        Personne.deleteTable();
    }

    @Test
    void testTable(){
        ArrayList<Personne> list = Personne.findAll();

        assertNotNull(list);

        assertEquals(2, list.size());

        Film film = Film.findById(1);

        assertEquals(list.get(0).getId(), film.getId_real());

        film = Film.findById(2);

        assertEquals(list.get(1).getId(), film.getId_real());

        film.delete();

        film = Film.findById(2);

        assertNull(film);

        ArrayList<Film> films = Film.findByRealisateur(list.getFirst());

        assertNotNull(films);

        assertEquals(2, films.size());
    }
}