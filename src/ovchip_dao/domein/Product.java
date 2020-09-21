package ovchip_dao.domein;

import java.util.ArrayList;
import java.util.Objects;

public class Product {
    private int product_nummer;
    private String name;
    private String beschrijving;
    private double price;
    private ArrayList<OVChipKaart> ovChipKaarten = new ArrayList<>();

    public ArrayList<OVChipKaart> getOvChipKaarten() {
        return ovChipKaarten;
    }

    public void setOvChipKaarten(ArrayList<OVChipKaart> ovChipKaarten) {
        this.ovChipKaarten = ovChipKaarten;
    }

    public void addOVKaart(OVChipKaart ovChipKaart){
        this.ovChipKaarten.add(ovChipKaart);
    }

    public Product(int product_nummer, String name, String beschrijving, double price) {
        this.product_nummer = product_nummer;
        this.name = name;
        this.beschrijving = beschrijving;
        this.price = price;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", name='" + name + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return product_nummer == product.product_nummer &&
                Double.compare(product.price, price) == 0 &&
                name.equals(product.name) &&
                beschrijving.equals(product.beschrijving);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_nummer, name, beschrijving, price);
    }
}
