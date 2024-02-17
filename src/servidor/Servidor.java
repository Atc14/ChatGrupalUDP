package servidor;

import datos.Usuario;

import java.io.IOException;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private static final List<Usuario> usuarios = new ArrayList<>();

    public static synchronized List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public static boolean usuarioValido(String userName) {
        for (Usuario u : usuarios) {
            if (u.getUserName().equals(userName)) return false;
        }
        return true;
    }

    public static void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public static void main(String[] args) {
        try {
            MulticastSocket socket = new MulticastSocket();

            HiloServer hs = new HiloServer(socket);
            hs.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}