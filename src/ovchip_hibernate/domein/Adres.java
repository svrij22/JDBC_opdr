package ovchip_hibernate.domein;

import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;

import javax.persistence.*;

@Entity
@Table(name = "adres", schema = "public", catalog = "ovchip")
public class Adres {

    @Id @GeneratedValue
    private int adres_id;
    private int reiziger_id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;

    @OneToOne(mappedBy = "adres", cascade = CascadeType.ALL)
    private Reiziger reiziger;

    public Adres() {
    }

    public Adres(int adres_id, int reiziger_id, String postcode, String huisnummer, String straat, String woonplaats) {
        this.adres_id = adres_id;
        this.reiziger_id = reiziger_id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public int getAdres_id() {
        return adres_id;
    }

    public void setAdres_id(int adres_id) {
        this.adres_id = adres_id;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }

    @Override
    public String toString() {
        return String.format("Adres (#%s %s-%s)", adres_id, postcode, huisnummer);
    }
}