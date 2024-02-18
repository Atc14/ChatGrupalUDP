package datos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class Chat extends JFrame {
    private JPanel Texto;
    private JList listaUsuarios;
    private DefaultListModel<String> modelo = new DefaultListModel<>();
    private JTextArea mensajesChat;
    private JPanel panelInferior;
    private JTextField textField_endrada;
    private JButton button_enviar;
    private JScrollPane panelMensajes;
    private JScrollPane panelListaUsuarios;
    private Mensaje mensaje;
    private byte[] buffer;
    private DatagramPacket envio;

    public Chat(String userName, MulticastSocket socket) {
        Texto.setBorder(new EmptyBorder(30, 10, 30, 10));
        mensajesChat.setEditable(false);
        mensajesChat.setMinimumSize(new Dimension(100, 0));
        panelMensajes.setBorder(new EmptyBorder(0, 0, 0, 10));
        panelListaUsuarios.setPreferredSize(new Dimension(175, 0));
        panelInferior.setBorder(new EmptyBorder(10, 0, 0, 0));

        button_enviar.addActionListener(e -> {
            if (!textField_endrada.getText().isBlank()) {
                mensaje = new Mensaje(userName, textField_endrada.getText());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(mensaje);
                    objectOutputStream.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                buffer = byteArrayOutputStream.toByteArray();
                try {
                    envio = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("228.0.0.15"), 6005);
                } catch (UnknownHostException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    socket.send(envio);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                textField_endrada.setText("");
            }
        });
        textField_endrada.addActionListener(e -> button_enviar.doClick());
    }

    public synchronized void agregarMensaje(String mensaje) {
        mensajesChat.append(mensaje);
    }

    public synchronized JPanel getTexto() {
        return Texto;
    }

    public synchronized void actualizarLista(ArrayList usuarios) {
        modelo.removeAllElements();
        for (Object usuario: usuarios){
            modelo.addElement(((Usuario)usuario).getUserName());
        }
        listaUsuarios.setModel(modelo);
    }
}