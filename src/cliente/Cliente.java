package cliente;

import datos.Mensaje;
import datos.Usuario;

import javax.swing.*;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.List;

public class Cliente {
    public static void main(String[] args) throws RuntimeException {
        try {
            MulticastSocket socket = new MulticastSocket(6005);
            InetAddress grupo = InetAddress.getByName("228.0.0.15");
            socket.joinGroup(grupo);
            String userName = JOptionPane.showInputDialog("Debes introducir un nombre");
            if (userName == null || userName.isBlank()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un nombre. La aplicación se cerrará.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            byte[] buffer;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(new Usuario(userName));
            objectOutputStream.close();

            buffer = byteArrayOutputStream.toByteArray();
            DatagramPacket envio = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("228.0.0.15"), 6005);
            socket.send(envio);

            HiloCliente hc = new HiloCliente(socket, grupo, userName);
            hc.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
