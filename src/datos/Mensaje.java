package datos;

public class Mensaje {
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
