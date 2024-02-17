package cliente;

import datos.Mensaje;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Cliente {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            MulticastSocket socket = new MulticastSocket(6005);
            InetAddress grupo = InetAddress.getByName("228.0.0.15");
            socket.joinGroup(grupo);
            System.out.println("Introduce el nombre de tu usuario: ");
            String userName = br.readLine();

            HiloCliente hc = new HiloCliente(socket, grupo, userName);
            hc.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
