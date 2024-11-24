package Project.mojeposilki;

public class Product {
    private String skladnik;
    private String ilosc;
    private String jednostka;

    private String Kategoria;

    private String protein;
    private String carbohydrates;
    private String fats;

    public Product(String skladnik, String ilosc, String jednostka, String Kategoria,
                   String protein, String carbohydrates, String fats) {
        this.skladnik = skladnik;
        this.ilosc = ilosc;
        this.jednostka = jednostka;
        this.fats = fats;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
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
        return Kategoria;
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

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getcarbohydrates() {
        return carbohydrates;
    }

    public void setcarbohydrates(String carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public String getfats() {
        return fats;
    }

    public void setfats(String fats) {
        this.fats = fats;
    }
}

