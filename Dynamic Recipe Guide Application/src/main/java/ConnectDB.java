import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class ConnectDB {
    private static ConnectDB instance;
    private Connection conn;

    // Database information
    private static final String HOSTNAME = "localhost";
    private static final String SQL_INSTANCE_NAME = "Tarik\\SQLEXPRESS";
    private static final String SQL_DATABASE = "Database";
    private static final String SQL_USER = "sa";
    private static final String SQL_PASSWORD = "1234";

    private ConnectDB() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectURL = "jdbc:sqlserver://" + HOSTNAME + ":1433;instance=" + SQL_INSTANCE_NAME +
                                ";databaseName=" + SQL_DATABASE + ";encrypt=true;trustServerCertificate=true";
            conn = DriverManager.getConnection(connectURL, SQL_USER, SQL_PASSWORD);
            System.out.println("Veritabanına başarıyla bağlandı!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Veritabanı bağlantı hatası: " + e.getMessage());
        }
    }

    // Singleton instance retrieval method
    public static ConnectDB getInstance() {
        if (instance == null) {
            instance = new ConnectDB();
        }
        return instance;
    }
    
   public boolean isDuplicateRecipe(String tarifAdi) {
    String query = "SELECT COUNT(*) FROM Tarifler WHERE TarifAdi = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, tarifAdi);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.out.println("İkiz tarif kontrolü hatası: " + e.getMessage());
    }
    return false;
}


  public List<RecipeMatch> searchRecipesByIngredients(List<String> inputIngredients) {
    List<RecipeMatch> matchingRecipes = new ArrayList<>();

    String query = "SELECT t.TarifID, t.TarifAdi, COUNT(CASE WHEN m.MalzemeAdi IN (" + 
                   String.join(",", Collections.nCopies(inputIngredients.size(), "?")) + 
                   ") THEN 1 ELSE NULL END) AS matchCount, " +
                   "(SELECT COUNT(*) FROM TarifMalzeme WHERE TarifID = t.TarifID) AS totalIngredients, " +
                   "SUM(CASE WHEN m.MalzemeAdi NOT IN (" + 
                   String.join(",", Collections.nCopies(inputIngredients.size(), "?")) + 
                   ") THEN m.BirimFiyat * tm.MalzemeMiktar ELSE 0 END) AS missingIngredientsCost " +
                   "FROM Tarifler t " +
                   "JOIN TarifMalzeme tm ON t.TarifID = tm.TarifID " +
                   "JOIN Malzemeler m ON tm.MalzemeID = m.MalzemeID " +
                   "GROUP BY t.TarifID, t.TarifAdi";

    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        int index = 1;
        for (String ingredient : inputIngredients) {
            stmt.setString(index++, ingredient);
        }
        for (String ingredient : inputIngredients) {
            stmt.setString(index++, ingredient);
        }

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int tarifID = rs.getInt("TarifID");
                String tarifAdi = rs.getString("TarifAdi");
                int matchCount = rs.getInt("matchCount");
                int totalIngredients = rs.getInt("totalIngredients");
                double missingIngredientsCost = rs.getDouble("missingIngredientsCost");

                double matchPercentage = (double) matchCount / totalIngredients * 100;
                boolean isComplete = missingIngredientsCost == 0;

                matchingRecipes.add(new RecipeMatch(tarifID, tarifAdi, matchPercentage, isComplete, missingIngredientsCost));
            }
        }
    } catch (SQLException e) {
        System.out.println("Malzemeye göre tarif arama hatası: " + e.getMessage());
    }

    matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
    return matchingRecipes;
}




    // Returns the connection
    public Connection getConnection() {
        return conn;
    }

    // Method to get used amount of an ingredient for a specific recipe
    public int getUsedAmount(int tarifID, int malzemeID) {
        String query = "SELECT MalzemeMiktar FROM TarifMalzeme WHERE TarifID = ? AND MalzemeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tarifID);
            stmt.setInt(2, malzemeID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("MalzemeMiktar");
                }
            }
        } catch (SQLException e) {
            System.out.println("Malzeme miktarı alma hatası: " + e.getMessage());
        }
        return 0; // Eğer malzeme bulunamazsa veya hata oluşursa, 0 döndür
    }
    
    // Method to retrieve a recipe by its name
public Recipe getRecipeByName(String tarifAdi) {
    String query = "SELECT * FROM Tarifler WHERE TarifAdi = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, tarifAdi);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int tarifID = rs.getInt("TarifID");
                String kategori = rs.getString("Kategori");
                int hazirlamaSuresi = rs.getInt("HazirlamaSuresi");
                String talimatlar = rs.getString("Talimatlar");

                // Retrieve the ingredients for this recipe
                List<Ingredient> ingredients = getIngredientsForRecipe(tarifID);

                return new Recipe(tarifID, tarifAdi, kategori, hazirlamaSuresi, talimatlar, ingredients);
            }
        }
    } catch (SQLException e) {
        System.out.println("Tarif getirme hatası: " + e.getMessage());
    }
    return null; // Return null if no recipe is found
}

   public void addRecipe(int tarifID, String tarifAdi, String kategori, int hazirlamaSuresi, String talimatlar) {
    // Tarifin halihazırda var olup olmadığını kontrol et
    if (!isDuplicateRecipe(tarifAdi)) {
        String query = "INSERT INTO Tarifler (TarifID, TarifAdi, Kategori, HazirlamaSuresi, Talimatlar) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tarifID);
            stmt.setString(2, tarifAdi);
            stmt.setString(3, kategori);
            stmt.setInt(4, hazirlamaSuresi);
            stmt.setString(5, talimatlar);
            stmt.executeUpdate();
            System.out.println("Tarif başarıyla eklendi!");
        } catch (SQLException e) {
            System.out.println("Tarif ekleme hatası: " + e.getMessage());
        }
    } else {
        System.out.println("Bu tarif zaten mevcut!");
    }
}

    // Method to add an ingredient
    public void addIngredient(int malzemeID, String malzemeAdi,int toplamMiktar, String malzemeBirim, float birimFiyat) {
        String query = "INSERT INTO Malzemeler (MalzemeID, MalzemeAdi, ToplamMiktar, MalzemeBirim, BirimFiyat) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, malzemeID);
            stmt.setString(2, malzemeAdi);
            stmt.setInt(3, toplamMiktar); // Updated to include toplamMiktar
            stmt.setString(4, malzemeBirim);
            stmt.setFloat(5, birimFiyat);
            stmt.executeUpdate();
            System.out.println("Malzeme başarıyla eklendi!");
        } catch (SQLException e) {
            System.out.println("Malzeme ekleme hatası: " + e.getMessage());
        }
    }

    // Method to add a recipe ingredient
    public void addRecipeIngredient(int tarifID, int malzemeID, float malzemeMiktar) {
        String query = "INSERT INTO TarifMalzeme (TarifID, MalzemeID, MalzemeMiktar) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tarifID);
            stmt.setInt(2, malzemeID);
            stmt.setFloat(3, malzemeMiktar);
            stmt.executeUpdate();
            System.out.println("Malzeme tarifine başarıyla eklendi!");
        } catch (SQLException e) {
            System.out.println("Malzeme ekleme hatası: " + e.getMessage());
        }
    }

    // Method to update a recipe
    public void updateRecipe(int tarifID, String newTarifAdi, String newKategori, int newHazirlamaSuresi, String newTalimatlar) {
        String query = "UPDATE Tarifler SET TarifAdi = ?, Kategori = ?, HazirlamaSuresi = ?, Talimatlar = ? WHERE TarifID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newTarifAdi);
            stmt.setString(2, newKategori);
            stmt.setInt(3, newHazirlamaSuresi);
            stmt.setString(4, newTalimatlar);
            stmt.setInt(5, tarifID);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Tarif başarıyla güncellendi!");
            } else {
                System.out.println("Güncellenecek tarif bulunamadı!");
            }
        } catch (SQLException e) {
            System.out.println("Tarif güncelleme hatası: " + e.getMessage());
        }
    }

    // Method to delete a recipe
    public void deleteRecipe(int tarifID) {
        String query = "DELETE FROM Tarifler WHERE TarifID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, tarifID);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Tarif başarıyla silindi!");
            } else {
                System.out.println("Silinecek tarif bulunamadı!");
            }
        } catch (SQLException e) {
            System.out.println("Tarif silme hatası: " + e.getMessage());
        }
    }
public List<Recipe> getAllRecipes() {
    List<Recipe> recipes = new ArrayList<>();
    String query = "SELECT * FROM Tarifler";
    try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            int tarifID = rs.getInt("TarifID");
            String tarifAdi = rs.getString("TarifAdi");
            String kategori = rs.getString("Kategori");
            int hazirlamaSuresi = rs.getInt("HazirlamaSuresi");
            String talimatlar = rs.getString("Talimatlar");

            // Tarifin malzemelerini çekmek için ayrı bir sorgu
            List<Ingredient> ingredients = getIngredientsForRecipe(tarifID);

            // Create and add a Recipe object to the list
            recipes.add(new Recipe(tarifID, tarifAdi, kategori, hazirlamaSuresi, talimatlar, ingredients));
        }
    } catch (SQLException e) {
        System.out.println("Tarifleri alma hatası: " + e.getMessage());
    }
    return recipes;
}

private List<Ingredient> getIngredientsForRecipe(int tarifID) {
    List<Ingredient> ingredients = new ArrayList<>();
    String query = "SELECT m.MalzemeID, m.MalzemeAdi, m.ToplamMiktar, m.MalzemeBirim, m.BirimFiyat, tm.MalzemeMiktar " +
                   "FROM Malzemeler m " +
                   "JOIN TarifMalzeme tm ON m.MalzemeID = tm.MalzemeID " +
                   "WHERE tm.TarifID = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, tarifID);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int malzemeID = rs.getInt("MalzemeID");
                String malzemeAdi = rs.getString("MalzemeAdi");
                int toplamMiktar = rs.getInt("ToplamMiktar");
                String malzemeBirim = rs.getString("MalzemeBirim");
                float birimFiyat = rs.getFloat("BirimFiyat");
                int usedAmount = rs.getInt("MalzemeMiktar");  // Kullanılan miktar

                ingredients.add(new Ingredient(malzemeID, malzemeAdi, toplamMiktar, usedAmount, malzemeBirim,birimFiyat));
            }
        }
    } catch (SQLException e) {
        System.out.println("Tarife malzemeleri alma hatası: " + e.getMessage());
    }
    return ingredients;
}



    // Method to get all ingredients
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM Malzemeler";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int malzemeID = rs.getInt("MalzemeID");
                String malzemeAdi = rs.getString("MalzemeAdi");
                int toplamMiktar = rs.getInt("ToplamMiktar"); // Added to fetch total amount
                String malzemeBirim = rs.getString("MalzemeBirim");
                float birimFiyat = rs.getFloat("BirimFiyat");

                // Create and add an Ingredient object to the list
                ingredients.add(new Ingredient(malzemeID, malzemeAdi,toplamMiktar, malzemeBirim, birimFiyat));
            }
        } catch (SQLException e) {
            System.out.println("Malzemeleri alma hatası: " + e.getMessage());
        }

        return ingredients;
    }

    // Method to clear the database
    public void clearDatabase() {
        String deleteRecipes = "DELETE FROM Tarifler";
        String deleteIngredients = "DELETE FROM Malzemeler";
        String deleteRecipeIngredients = "DELETE FROM TarifMalzeme";

        try {
            // Clear the Recipes table
            try (PreparedStatement stmt1 = conn.prepareStatement(deleteRecipes)) {
                stmt1.executeUpdate();
            }

            // Clear the Ingredients table
            try (PreparedStatement stmt2 = conn.prepareStatement(deleteIngredients)) {
                stmt2.executeUpdate();
            }

            // Clear the Recipe-Material table
            try (PreparedStatement stmt3 = conn.prepareStatement(deleteRecipeIngredients)) {
                stmt3.executeUpdate();
            }

            System.out.println("Tüm tablolar başarıyla temizlendi.");
        } catch (SQLException e) {
            System.out.println("Veritabanı temizleme işlemi sırasında hata oluştu: " + e.getMessage());
        }
    }

    // Method to close the connection
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Veritabanı bağlantısı kapatıldı.");
            }
        } catch (SQLException e) {
            System.out.println("Bağlantı kapatma hatası: " + e.getMessage());
        }
    }
}
