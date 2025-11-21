package activeRecord;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void seulObjet(){
        DBConnection.setConfig("./config/config.ini");
        DBConnection instance = DBConnection.getInstance();

        Connection connection = instance.getConnect();
        assertNull(connection);

        instance.setNomDB("testpersonne");

        connection = instance.getConnect();

        assertNotNull(connection);

        Connection connection2 = instance.getConnect();

        assertEquals(connection, connection2);
    }

    @Test
    void changerDB(){
        DBConnection instance =  DBConnection.getInstance();
        Connection connection = instance.getConnect();

        assertNotNull(connection);

        instance.setNomDB("test");
        Connection connection2 = instance.getConnect();

        assertNotEquals(connection, connection2);
    }
}