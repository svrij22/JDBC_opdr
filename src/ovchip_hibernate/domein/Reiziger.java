package ovchip_hibernate.domein;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "reiziger", schema = "public", catalog = "ovchip")
public class Reiziger implements Serializable {

    @Id
    private int reiziger_id;
    private String voorletters, tussenvoegsel, achternaam;
    private Date geboortedatum;

    @OneToOne
    @JoinColumn(name = "reiziger_id")
    private Adres adres;

    @OneToMany(
        mappedBy = "reiziger",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<OVChipKaart> chipkaarten = new ArrayList<>();

    public Reiziger() {
    }

    public Reiziger(int reiziger_id, String voorletters, String tussenvoegsel, String achternaam, Date date) {
        this.reiziger_id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = date;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public int getReiziger_id() {
        return reiziger_id;
    }

    public void setReiziger_id(int reiziger_id) {
        this.reiziger_id = reiziger_id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date date) {
        this.geboortedatum = date;
    }

    @Override
    public String toString() {
        String str;
        if (tussenvoegsel != null){
            str = String.format("#%s: %s. %s %s (%s)", reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum.toString());
        }else{
            str =  String.format("#%s: %s. %s (%s)", reiziger_id, voorletters, achternaam, geboortedatum.toString());
        }
        if (this.adres != null) str += "; " + this.adres.toString();
        str += " ovchipkaarten: " + this.chipkaarten.size();
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reiziger reiziger = (Reiziger) o;
        return reiziger_id == reiziger.reiziger_id &&
                Objects.equals(voorletters, reiziger.voorletters) &&
                Objects.equals(tussenvoegsel, reiziger.tussenvoegsel) &&
                Objects.equals(achternaam, reiziger.achternaam) &&
                Objects.equals(geboortedatum, reiziger.geboortedatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum);
    }

    public void addKaart(OVChipKaart chipkaart) {
        this.chipkaarten.add(chipkaart);
    }

    public void setKaarten(List<OVChipKaart> kaarten){
        this.chipkaarten = (ArrayList<OVChipKaart>) kaarten;
    }

    public List<OVChipKaart> getChipkaarten() {
        return chipkaarten;
    }

    public void setChipkaarten(ArrayList<OVChipKaart> chipkaarten) {
        this.chipkaarten = chipkaarten;
    }
}
