package Project.mojeposilki;

public class Product {
    private String skladnik;
    private String ilosc;
    private String jednostka;

    private String Kategoria;

    public Product(String skladnik, String ilosc, String jednostka, String Kategoria) {
        this.skladnik = skladnik;
        this.ilosc = ilosc;
        this.jednostka = jednostka;
    }

    public String getSkladnik() {
        return skladnik;
    }

    public void setSkladnik(String skladnik) {
        this.skladnik = skladnik;
    }

    public String getIlosc() {
        return ilosc;
    }

    public String getKategoria() {
        return ilosc;
    }

    public void setKategoria(String Kategoria) {
        this.Kategoria = Kategoria;
    }

    public void setIlosc(String ilosc) {
        this.ilosc = ilosc;
    }

    public String getJednostka() {
        return jednostka;
    }

    public void setJednostka(String jednostka) {
        this.jednostka = jednostka;
    }
}

