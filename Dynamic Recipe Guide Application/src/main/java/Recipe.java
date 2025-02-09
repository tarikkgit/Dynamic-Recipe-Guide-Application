import java.util.List;

public class Recipe {
    private int tarifID;
    private String tarifAdi;
    private String kategori;
    private int hazirlamaSuresi;
    private String talimatlar;
    private List<Ingredient> ingredients;

    // Constructor
    public Recipe(int tarifID, String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar, List<Ingredient> ingredients) {
        this.tarifID = tarifID;
        this.tarifAdi = tarifAdi;
        this.kategori = kategori;
        this.hazirlamaSuresi = hazirlamaSuresi;
        this.talimatlar = talimatlar;
        this.ingredients = ingredients;
    }

    // Getter methods
    public int getTarifID() {
        return tarifID;
    }

    public String getTarifAdi() {
        return tarifAdi;
    }

    public String getKategori() {
        return kategori;
    }

    public int getHazirlamaSuresi() {
        return hazirlamaSuresi;
    }

    public String getTalimatlar() {
        return talimatlar;
    }
    
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    // New method to get the ingredient count
    public int getIngredientCount() {
        return ingredients.size();
    }

    // Method to calculate the total cost of the recipe
    public double getTotalCost() {
        double totalCost = 0;
        for (Ingredient ingredient : ingredients) {
            totalCost += ingredient.getPrice();
        }
        return totalCost;
    }
    
    private int getUsedAmountForIngredient(Ingredient ingredient) {
        return ConnectDB.getInstance().getUsedAmount(tarifID, ingredient.getMalzemeID());
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "tarifID=" + tarifID +
                ", tarifAdi='" + tarifAdi + '\'' +
                ", kategori='" + kategori + '\'' +
                ", hazirlamaSuresi=" + hazirlamaSuresi +
                ", talimatlar='" + talimatlar + '\'' +
                ", ingredients=" + ingredients +
                '}';
    }
}
