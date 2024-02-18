package datos;

import java.io.Serializable;

public class Desconectado implements Serializable {
    private final String userName;

    public Desconectado(String userName){
        this.userName = userName;
    }

    public synchronized String getUserName() {
        return userName;
    }
}
