package datos;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private final String userName;
    private final String texto;

    public Mensaje(String userName, String texto){
        this.userName = userName;
        this.texto = texto;
    }

    public synchronized String getMensaje(){
        return userName + ": " + texto + "\n";
    }
}
