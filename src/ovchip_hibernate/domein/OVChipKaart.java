package ovchip_hibernate.domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ov_chipkaart", schema = "public", catalog = "ovchip")
public class OVChipKaart {

    @Id @GeneratedValue private int kaart_nummer;
    private int reiziger_id;
    private int klasse;
    private Date geldig_tot;
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "reiziger_id", insertable = false, updatable = false)
    private Reiziger reiziger;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "ov_chipkaart_product",
            joinColumns = {@JoinColumn(name = "kaart_nummer")},
            inverseJoinColumns = {@JoinColumn(name = "product_nummer")})
    private List<Product> producten;

    public OVChipKaart() {
        this.producten = new ArrayList<Product>();
    }

    public OVChipKaart(int kaartnummer, int klasse, int reiziger_id, Date geldig_tot, double saldo) {
        this.kaart_nummer = kaartnummer;
        this.klasse = klasse;
        this.reiziger_id = reiziger_id;
        this.geldig_tot = geldig_tot;
        this.saldo = saldo;
    }

    public List<Product> getProducten() { return producten; }

    public void addProduct(Product p){
        this.producten.add(p);
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public int getKaartnummer() {
        return kaart_nummer;
    }

    public void setKaartnummer(int kaartnummer) {
        this.kaart_nummer = kaartnummer;
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
                "kaartnummer=" + kaart_nummer +
                ", klasse=" + klasse +
                ", reiziger_id=" + reiziger_id +
                ", geldig_tot=" + geldig_tot +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger +
                ", producten=" + producten +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OVChipKaart that = (OVChipKaart) o;
        return kaart_nummer == that.kaart_nummer &&
                klasse == that.klasse &&
                reiziger_id == that.reiziger_id &&
                geldig_tot.equals(that.geldig_tot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kaart_nummer, klasse, reiziger_id, geldig_tot);
    }
}
