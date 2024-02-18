package servidor;

import datos.Mensaje;
import datos.Usuario;

import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
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
                    agregarUsuario((Usuario) object);
                    System.out.println("lista de usuarios: " + getUsuarios());
                    enviarUsuarios();
                }

            }

        } catch (IOException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }

    }
}