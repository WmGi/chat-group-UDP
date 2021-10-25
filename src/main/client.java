package main;

import threads.etud_receive;
import threads.etud_send;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class client {
    public static void main(String[] args) throws IOException {
        DatagramSocket ets = new DatagramSocket();
        String msg = null;
        Boolean login = false;
        while (!login) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             msg = br.readLine();
            if (msg.startsWith("##"))
                login=true;

        }
        DatagramPacket datagramPacket=new DatagramPacket(msg.getBytes(StandardCharsets.UTF_8)
                , msg.length(), Inet4Address.getLocalHost(),4000);
        ets.send(datagramPacket);
        etud_send etud_send=new etud_send(ets,msg.substring(2));
        etud_receive etud_receive=new etud_receive(ets);
        etud_send.start();
        etud_receive.start();



    }
}
