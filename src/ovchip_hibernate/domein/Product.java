package ovchip_hibernate.domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "product", schema = "public", catalog = "ovchip")
public class Product {

    @Id private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    @ManyToMany(mappedBy = "producten")
    private List<OVChipKaart> ovChipKaarten;

    public Product() {
        this.ovChipKaarten = new ArrayList<OVChipKaart>();
    }

    public Product(int product_nummer, String name, String beschrijving, double price) {
        this.product_nummer = product_nummer;
        this.naam = name;
        this.beschrijving = beschrijving;
        this.prijs = price;
    }

    public List<OVChipKaart> getOvChipKaarten() {
        return ovChipKaarten;
    }

    public void setOvChipKaarten(ArrayList<OVChipKaart> ovChipKaarten) {
        this.ovChipKaarten = ovChipKaarten;
    }

    public void addOVKaart(OVChipKaart ovChipKaart){
        this.ovChipKaarten.add(ovChipKaart);
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String name) {
        this.naam = name;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", name='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", price=" + prijs +
                ", kaarten=" + ovChipKaarten.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return product_nummer == product.product_nummer &&
                Double.compare(product.prijs, prijs) == 0 &&
                naam.equals(product.naam) &&
                beschrijving.equals(product.beschrijving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_nummer, naam, beschrijving, prijs);
    }
}
