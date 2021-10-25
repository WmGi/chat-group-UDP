package threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class etud_receive extends  Thread {
    DatagramSocket s;

    public etud_receive(DatagramSocket s) {
        this.s = s;
    }
    public void run(){
        while (true){

            DatagramPacket datagramPacket=new DatagramPacket(new byte[1024],1024);
            try {
                s.receive(datagramPacket);
                String msg=new String(datagramPacket.getData()).trim();
                //System.out.println("this is receive thread");

                System.out.println(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
