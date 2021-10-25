package model;

import java.net.Inet4Address;

public class etudiant {
    String nom;
    Inet4Address addr;
    int port;
    String niveau;
    public etudiant(String nom, Inet4Address addr, int port) {
        this.nom = nom;
        this.addr = addr;
        this.port = port;
    }

    public etudiant(String nom, Inet4Address addr, int port, String niveau) {
        this.nom = nom;
        this.addr = addr;
        this.port = port;
        this.niveau = niveau;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Inet4Address getAddr() {
        return addr;
    }

    public void setAddr(Inet4Address addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "etudiant{" +
                "nom='" + nom + '\'' +
                ", addr=" + addr +
                ", port=" + port +
                ", niveau='" + niveau + '\'' +
                '}';
    }
}
