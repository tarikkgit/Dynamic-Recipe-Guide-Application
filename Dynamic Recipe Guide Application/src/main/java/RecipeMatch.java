public class RecipeMatch {
    private int tarifID;
    private String tarifAdi;
    private double matchPercentage;
    private boolean isComplete; // Tarifteki tüm malzemeler yeterli mi
    private double missingIngredientsCost; // Eksik malzemelerin toplam maliyeti

    public RecipeMatch(int tarifID, String tarifAdi, double matchPercentage, boolean isComplete, double missingIngredientsCost) {
        this.tarifID = tarifID;
        this.tarifAdi = tarifAdi;
        this.matchPercentage = matchPercentage;
        this.isComplete = isComplete;
        this.missingIngredientsCost = missingIngredientsCost;
    }

    public int getTarifID() {
        return tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public double getMatchPercentage() {
        return matchPercentage;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public double getMissingIngredientsCost() {
        return missingIngredientsCost;
    }

    @Override
    public String toString() {
        return "RecipeMatch{" +
                "tarifID=" + tarifID +
                ", tarifAdi='" + tarifAdi + '\'' +
                ", matchPercentage=" + matchPercentage +
                ", isComplete=" + isComplete +
                ", missingIngredientsCost=" + missingIngredientsCost +
                '}';
    }
}
