package domein;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Reiziger {
    private int reiziger_id;
    private String voorletters, tussenvoegsel, achternaam;
    private Date date;
    private Adres adres;
    private ArrayList<OVChipKaart> chipkaarten = new ArrayList<>();

    public Reiziger(int reiziger_id, String voorletters, String tussenvoegsel, String achternaam, Date date) {
        this.reiziger_id = reiziger_id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        String str;
        if (tussenvoegsel != null){
            str = String.format("#%s: %s. %s %s (%s)", reiziger_id, voorletters, tussenvoegsel, achternaam, date.toString());
        }else{
            str =  String.format("#%s: %s. %s (%s)", reiziger_id, voorletters, achternaam, date.toString());
        }
        if (this.adres != null) str += "; " + this.adres.toString();
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
                Objects.equals(date, reiziger.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reiziger_id, voorletters, tussenvoegsel, achternaam, date);
    }

    public void addKaart(OVChipKaart chipkaart) {
        this.chipkaarten.add(chipkaart);
    }

    public ArrayList<OVChipKaart> getChipkaarten() {
        return chipkaarten;
    }

    public void setChipkaarten(ArrayList<OVChipKaart> chipkaarten) {
        this.chipkaarten = chipkaarten;
    }
}
