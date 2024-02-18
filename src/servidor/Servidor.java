package servidor;

import datos.Desconectado;
import datos.Usuario;

import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
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

    public static void eliminarUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }

    public static boolean usuarioValido(String userName) {
        for (Usuario u : usuarios) {
            if (u.getUserName().equals(userName)) return false;
        }
        return true;
    }

    public static synchronized void enviarUsuarios() {
        try {
            MulticastSocket socket = new MulticastSocket(6005);
            InetAddress grupo = InetAddress.getByName("228.0.0.15");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(getUsuarios());
            objectOutputStream.close();

            byte[] mensaje = byteArrayOutputStream.toByteArray();
            DatagramPacket envio = new DatagramPacket(mensaje, mensaje.length, grupo, 6005);
            socket.send(envio);
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        byte[] mensaje;
        DatagramPacket recibido, envio;
        MulticastSocket socket;
        try {
            socket = new MulticastSocket(6005);
            InetAddress grupo = InetAddress.getByName("228.0.0.15");
            socket.joinGroup(grupo);

            while (true) {
                mensaje = new byte[4000];
                recibido = new DatagramPacket(mensaje, mensaje.length);
                socket.receive(recibido);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recibido.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                if (object instanceof Usuario) {
                    if(usuarioValido(((Usuario) object).getUserName())) {
                        agregarUsuario((Usuario) object);
                        System.out.println("lista de usuarios: " + getUsuarios());
                        enviarUsuarios();
                    }
                } else if (object instanceof Desconectado) {
                    Usuario usuarioEliminar = null;
                    for (Usuario usuario : getUsuarios()) {
                        if (usuario.getUserName().equals(((Desconectado) object).getUserName())) {
                            usuarioEliminar = usuario;
                        }
                    }
                    if (usuarioEliminar != null) {
                        eliminarUsuario(usuarioEliminar);
                        enviarUsuarios();
                    }
                }

            }

        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

    }
}