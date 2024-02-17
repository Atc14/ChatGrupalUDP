package servidor;

import datos.Mensaje;
import datos.Usuario;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class HiloServer extends Thread {
    private final MulticastSocket socket;

    public HiloServer(MulticastSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        byte[] mensaje;
        DatagramPacket recibido, envio;
        try {
            while (true) {
                mensaje = new byte[4000];
                recibido = new DatagramPacket(mensaje, mensaje.length);
                socket.receive(recibido);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recibido.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Object object = objectInputStream.readObject();
                objectInputStream.close();
                if(object instanceof Usuario){
                    Servidor.agregarUsuario((Usuario) object);
                    System.out.println(Servidor.getUsuarios());
                }
                else {
                    envio = new DatagramPacket(mensaje, mensaje.length, InetAddress.getByName("228.0.0.15"), 6005);
                    socket.send(envio);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}