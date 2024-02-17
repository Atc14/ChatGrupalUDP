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
            MulticastSocket socket=new MulticastSocket(6005);
            InetAddress grupo= InetAddress.getByName("228.0.0.15");
            socket.joinGroup(grupo);
            System.out.println("Introduce el nombre de tu usuario: ");
            String userName = br.readLine();
            DatagramPacket dp;
            byte [] buffer;
            HiloCliente hc = new HiloCliente(socket, grupo);
            hc.start();
            while(true){
                Mensaje m = new Mensaje(userName, br.readLine());
                buffer = m.getMensaje().getBytes();
                dp = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("228.0.0.15"),6005);
                socket.send(dp);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
