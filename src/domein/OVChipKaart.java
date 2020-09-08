package domein;

import java.util.Date;

public class OVChipKaart {
    private int kaartnummer, klasse, reiziger_id;
    private Date geldig_tot;
    private double saldo;
    private Reiziger reiziger;

    public OVChipKaart(int kaartnummer, int klasse, int reiziger_id, Date geldig_tot, double saldo) {
        this.kaartnummer = kaartnummer;
        this.klasse = klasse;
        this.reiziger_id = reiziger_id;
        this.geldig_tot = geldig_tot;
        this.saldo = saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaartnummer = kaartnummer;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public Date getGeldig_tot() {
        return geldig_tot;
    }

    public void setGeldig_tot(Date geldig_tot) {
        this.geldig_tot = geldig_tot;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "OVChipKaart{" +
                "kaartnummer=" + kaartnummer +
                ", klasse=" + klasse +
                ", reiziger_id=" + reiziger_id +
                ", geldig_tot=" + geldig_tot +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger +
                '}';
    }
}
