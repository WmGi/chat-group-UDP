package main;

import model.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class serverm {
        static List<etudiant> etudiants=new ArrayList<>();
         static List<groupe> groupes=new ArrayList<>();
        static List<message> messages=new ArrayList<>();

   public  boolean Search(String name){
      Boolean exist=false;
      for (int i=0;i<etudiants.size();i++){
         if (etudiants.get(i).getNom().equals(name)){
            exist=true;
         }
      }
      return exist;
   }
   public  boolean SearchGroupe(String name){
      Boolean exist=false;
      for (int i=0;i<groupes.size();i++){
         if (groupes.get(i).getName().equals(name)){
            exist=true;
         }
      }
      return exist;
   }
   public  etudiant GetEtudiant(Inet4Address address,int port){
      etudiant e=null;
      for (int i=0;i<etudiants.size();i++){
         if (etudiants.get(i).getAddr().equals(address) && etudiants.get(i).getPort()==port){
            e=new etudiant(etudiants.get(i).getNom(),etudiants.get(i).getAddr(),etudiants.get(i).getPort());
         }
      }
      return e;
   }
   public  etudiant getEtudiantByName(String name){
      etudiant e=null;
      for (int i=0;i<etudiants.size();i++){
         if (etudiants.get(i).getNom().equals(name)){
            e=new etudiant(etudiants.get(i).getNom(),etudiants.get(i).getAddr(),etudiants.get(i).getPort());
         }
      }
      return e;
   }
   public  groupe getGroupeByName(String name){
      groupe e=null;
      for (int i=0;i<groupes.size();i++){
         if (groupes.get(i).getName().equals(name)){
            e=new groupe(groupes.get(i).getName(),groupes.get(i).getList());
            return e;
         }

      }
      return e;
   }
   public  int GetIndexOfGroupe(String name){
      int index=-1;
      for (int i=0;i<groupes.size();i++){
         if (groupes.get(i).getName().equals(name)){
            return i ;
         }

   } return index;
   }
   public static void main(String[] args) throws IOException {
        DatagramSocket s=new DatagramSocket(4000);
      while (true){
         try {
            byte[] dataSnd = new byte[1024];
            DatagramPacket pkRcv = new DatagramPacket(new byte[1024], 1024);
            s.receive(pkRcv);
            String msg = new String(pkRcv.getData()).trim();
            String msgsend = "";
            if (msg.startsWith("##")) {
               if (!new serverm().Search(msg)) {
                  etudiant etudiant = new etudiant(msg.substring(2), (Inet4Address) pkRcv.getAddress(), pkRcv.getPort());
                  etudiants.add(etudiant);
                  msgsend="welcome "+etudiant.getNom()+"\n";
               }
            } else if (msg.startsWith("#List")) {
               for (int i = 0; i < etudiants.size(); i++) {
                  msgsend += "\n" + etudiants.get(i).getNom();
               }

            } else if (msg.startsWith("#HISTo")) {
               for (int i = 0; i < messages.size(); i++) {
                  System.out.println(messages.get(i));
                  if ((messages.get(i).getSrc().getAddr().equals(pkRcv.getAddress()) && messages.get(i).getSrc().getPort() == pkRcv.getPort()) || (messages.get(i).getDest().getAddr().equals(pkRcv.getAddress() ) && messages.get(i).getDest().getPort() == pkRcv.getPort()))
                     msgsend += "\nFrom: " + messages.get(i).getSrc().getNom()+" : "+messages.get(i).getMsg();
               }
            } else if (msg.startsWith("@#")) {
               etudiant e = new serverm().GetEtudiant((Inet4Address) pkRcv.getAddress(),pkRcv.getPort());
               if (new serverm().getEtudiantByName(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@#", 2))) != null) {
                  etudiant e2 = new serverm().getEtudiantByName(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@#", 2)));
                  message message = new message(e, e2, new String(new String(pkRcv.getData()).trim().substring(new String(pkRcv.getData()).trim().indexOf("@#", 2) +2)));
                  System.out.println("message from function @# "+message);
                  messages.add(message);
                  InetAddress IPAddress = e2.getAddr();
                  int portClt = e2.getPort();
                  System.out.println(msgsend);
                  dataSnd =(e.getNom()+" > "+ message.getMsg()).getBytes();
                  DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
                  s.send(pkSend);
                  continue;
               } else {
                  msgsend = "client doesnt exist";
               }
            } else if (msg.startsWith("#GRPS")) {
               for (int i = 0; i < groupes.size(); i++) {
                  msgsend += "\n" + groupes.get(i).getName();
               }
            } else if (msg.startsWith("#GRP#")) {
               if (!new serverm().SearchGroupe(msg.substring(5))) {
                  List<etudiant> list = new ArrayList<>();
                  List<message> listmessages = new ArrayList<>();
                  list.add(new serverm().GetEtudiant((Inet4Address) pkRcv.getAddress(),pkRcv.getPort()));
                  groupe groupe = new groupe(msg.substring(5), list,listmessages);
                  groupes.add(groupe);
               } else msgsend = "groupe exist";
            } else if (msg.startsWith("#>")) {
               System.out.println("looking for groupe "+msg.substring(2));
               if (new serverm().SearchGroupe(msg.substring(2))) {
                  etudiant e = new serverm().GetEtudiant((Inet4Address) pkRcv.getAddress(),pkRcv.getPort());
                  int index = new serverm().GetIndexOfGroupe(msg.substring(2));
                  groupes.get(index).getList().add(e);
                  msgsend="you joined succesfully";
               } else
                  msgsend = "groupe doesnt exist";
            } else if (msg.startsWith("#ETDS#")) {
               if (new serverm().SearchGroupe(msg.substring(6))) {
                  groupe groupe = groupes.get(new serverm().GetIndexOfGroupe(msg.substring(6)));
                  for (etudiant e : groupe.getList()) {
                     msgsend += "\n" + e.getNom();
                  }
               }
            } else if (msg.startsWith("@>")) {
               if (new serverm().SearchGroupe(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@>", 2)))) {
                  groupe groupe = new serverm().getGroupeByName(new String(pkRcv.getData()).trim().substring(2, new String(pkRcv.getData()).trim().indexOf("@>", 2)));
                  etudiant e = new serverm().GetEtudiant((Inet4Address) pkRcv.getAddress(),pkRcv.getPort());

                  for (int i = 0; i < groupe.getList().size(); i++) {
                     etudiant e2 = new serverm().getEtudiantByName(groupe.getList().get(i).getNom());
                     message message = new message(e, e2, new String(new String(pkRcv.getData()).trim().substring(new String(pkRcv.getData()).trim().indexOf("@>", 2) + 2)));
                     messages.add(message);
                   //  System.out.println(groupes.get(new serverm().GetIndexOfGroupe(groupe.getName())));
                     groupes.get(new serverm().GetIndexOfGroupe(groupe.getName())).getMessages().add(message);
                     InetAddress IPAddress = groupe.getList().get(i).getAddr();
                     int portClt = groupe.getList().get(i).getPort();
                     System.out.println(msgsend);
                     dataSnd = ("Group "+groupe.getName()+"::"+e.getNom()+"::"+message.getMsg()).getBytes();
                     DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
                     s.send(pkSend);
                  }
               }
               continue;
            }
            InetAddress IPAddress = pkRcv.getAddress();
            int portClt = pkRcv.getPort();
            System.out.println(msgsend);
            dataSnd = msgsend.getBytes();
            DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
            s.send(pkSend);
//         System.out.println(pkRcv.getData());
//         InetAddress IPAddress = pkRcv.getAddress();
//         int portClt = pkRcv.getPort();
//         System.out.println(portClt);
//         String msgrep = msg.toUpperCase()+"wassim";
//         System.out.println(msgrep);
//         dataSnd = msgrep.getBytes();
//         DatagramPacket pkSend = new DatagramPacket(dataSnd, dataSnd.length, IPAddress, portClt);
//         s.send(pkSend);
         }catch (Exception e){
            e.printStackTrace();
         }}
   }

}
