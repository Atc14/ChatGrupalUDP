package cliente;

import datos.Chat;
import datos.Mensaje;
import datos.Usuario;

import javax.swing.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class HiloCliente extends Thread {
    private final MulticastSocket sCliente;
    private final InetAddress grupo;
    private final Chat chat;

    public HiloCliente(MulticastSocket sCliente, InetAddress grupo, String userName) {
        this.sCliente = sCliente;
        this.grupo = grupo;
        this.chat = new Chat(userName, sCliente);
        chat.setVisible(true);
        chat.setSize(800, 600);
        chat.setTitle("Chat Web : " + userName);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.setLocationRelativeTo(null);
        chat.setContentPane(chat.getTexto());
    }

    @Override
    public void run() {
        byte[] buffer;
        DatagramPacket entrada;

        try {
            while (true) {
                buffer = new byte[4000];
                entrada = new DatagramPacket(buffer, buffer.length);
                if (!sCliente.isClosed()) {
                    sCliente.receive(entrada);

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(entrada.getData());
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Object object = objectInputStream.readObject();
                    objectInputStream.close();
                    if (object instanceof Mensaje) {
                        Mensaje mensaje = (Mensaje) object;
                        chat.agregarMensaje(mensaje.getMensaje());
                    }
                    else if(object instanceof List<?>){
                        chat.actualizarLista((ArrayList) object);
                    }

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                sCliente.leaveGroup(grupo);
                sCliente.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
