package Project.mojeposilki;

public class Product {
    private String skladnik;
    private String ilosc;
    private String jednostka;

    public Product(String skladnik, String ilosc, String jednostka) {
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

