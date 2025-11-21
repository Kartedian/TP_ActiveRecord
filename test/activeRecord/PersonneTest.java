package activeRecord;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonneTest {

    @BeforeEach
    void test() throws Exception {
        DBConnection.setConfig("./config/config.ini");
        DBConnection instance = DBConnection.getInstance();
        instance.setNomDB("testpersonne");

        Personne.createTable();

        Personne personne = new Personne("Rossi", "Andrea");
        Personne personne2 = new Personne("Bianchi", "Luca");

        personne.save();
        personne2.save();
    }

    @AfterEach
    void testDeleteTable() throws Exception {
        Personne.deleteTable();
    }

    @Test
    void testTable(){

        ArrayList<Personne> list = Personne.findAll();

        assertNotNull(list);

        assertEquals(2, list.size());

        ArrayList<Personne> pers = Personne.findByName("Rossi");

        assertNotNull(pers);
        assertEquals(1, pers.size());
        assertEquals("Andrea", pers.get(0).getPrenom());

        pers.get(0).delete();

        list = Personne.findAll();

        assertNotNull(list);

        assertEquals(1, list.size());
    }


}