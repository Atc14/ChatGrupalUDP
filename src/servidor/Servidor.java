package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Servidor {
    public static void main(String[] args) {
        try {
            MulticastSocket socket = new MulticastSocket();
            byte [] mensaje;
            DatagramPacket recibido;
            DatagramPacket envio;
            while (true){
                mensaje = new byte[4000];
                recibido = new DatagramPacket(mensaje, mensaje.length);
                socket.receive(recibido);
                envio = new DatagramPacket(mensaje, mensaje.length, InetAddress.getByName("228.0.0.15"), 6004);
                socket.send(envio);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
