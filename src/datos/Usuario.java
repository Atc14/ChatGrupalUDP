package datos;

import java.io.Serializable;

public class Usuario implements Serializable {
    private final String userName;  

    public Usuario(String userName) {
        this.userName = userName;
    }

    public synchronized String getUserName() {
        return userName;
    }
}
