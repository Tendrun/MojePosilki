package Project.mojeposilki;

public class HealthGoal {
    private double weightGoal;
    private double calorieGoal;
    private double proteinGoal;
    private double fatsGoal;
    private double carbsGoal;

    public HealthGoal(double weightGoal, double calorieGoal, double proteinGoal, double fatsGoal, double carbsGoal) {
        this.weightGoal = weightGoal;
        this.calorieGoal = calorieGoal;
        this.proteinGoal = proteinGoal;
        this.fatsGoal = fatsGoal;
        this.carbsGoal = carbsGoal;
    }

    // Getters and Setters
    public double getWeightGoal() { return weightGoal; }
    public void setWeightGoal(double weightGoal) { this.weightGoal = weightGoal; }

    public double getCalorieGoal() { return calorieGoal; }
    public void setCalorieGoal(double calorieGoal) { this.calorieGoal = calorieGoal; }

    public double getProteinGoal() { return proteinGoal; }
    public void setProteinGoal(double proteinGoal) { this.proteinGoal = proteinGoal; }

    public double getFatsGoal() { return fatsGoal; }
    public void setFatsGoal(double fatsGoal) { this.fatsGoal = fatsGoal; }

    public double getCarbsGoal() { return carbsGoal; }
    public void setCarbsGoal(double carbsGoal) { this.carbsGoal = carbsGoal; }
}
