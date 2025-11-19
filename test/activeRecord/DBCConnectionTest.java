package activeRecord;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBCConnectionTest {

    @Test
    void seulObjet(){
        DBCConnection.setConfig("./config/config.ini");
        DBCConnection instance = DBCConnection.getInstance();

    }
}