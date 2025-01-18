package Project.mojeposilki;

public class NutritionalInfo {
    public double calories;
    public double protein;
    public double fats;
    public double carbohydrates;

    // Optional: Constructor for initialization
    public NutritionalInfo() {
        this.calories = 0;
        this.protein = 0;
        this.fats = 0;
        this.carbohydrates = 0;
    }

    // Optional: Constructor with parameters
    public NutritionalInfo(double calories, double protein, double fats, double carbohydrates) {
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
    }
}
