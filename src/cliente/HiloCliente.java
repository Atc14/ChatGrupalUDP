package cliente;

import datos.Chat;
import datos.Desconectado;
import datos.Mensaje;
import datos.Usuario;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

public class HiloCliente extends Thread {
    private final MulticastSocket sCliente;
    private final InetAddress grupo;
    private final String userName;
    private final Chat chat;

    public HiloCliente(MulticastSocket sCliente, InetAddress grupo, String userName) {
        this.sCliente = sCliente;
        this.grupo = grupo;
        this.userName = userName;
        this.chat = new Chat(userName, sCliente);

    }


    @Override
    public void run() {
        byte[] buffer;
        DatagramPacket entrada;
        SwingUtilities.invokeLater(() -> {
                    chat.setVisible(true);
                    chat.setSize(800, 600);
                    chat.setTitle("Chat Web : " + userName);
                    chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    chat.setLocationRelativeTo(null);
                    chat.setContentPane(chat.getTexto());
                    chat.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            try {
                                Desconectado desconectado = new Desconectado(userName);
                                byte[] buffer;
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                                objectOutputStream.writeObject(desconectado);
                                objectOutputStream.close();

                                buffer = byteArrayOutputStream.toByteArray();
                                DatagramPacket envio = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("228.0.0.15"), 6005);
                                sCliente.send(envio);
                                sCliente.leaveGroup(grupo);
                                sCliente.close();
                                chat.dispose();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    });
                }
        );
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
                    } else if (object instanceof List<?>) {
                        chat.actualizarLista((ArrayList) object);
                    }

                }
            }
        } catch (IOException | ClassNotFoundException ignored) {

        } finally {
            sCliente.close();
        }

    }
}
