package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class HiloCliente extends Thread {
    private final MulticastSocket sCliente;
    private final InetAddress grupo;

    public HiloCliente(MulticastSocket sCliente, InetAddress grupo) {
        this.sCliente = sCliente;
        this.grupo = grupo;
    }

    @Override
    public void run() {
        byte[] buffer;
        DatagramPacket entrada;

        try {
            while (true) {
                buffer = new byte[4000];
                entrada = new DatagramPacket(buffer, buffer.length);
                sCliente.receive(entrada);
                System.out.println(new String(entrada.getData()).trim());
            }
        } catch (IOException e) {
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
