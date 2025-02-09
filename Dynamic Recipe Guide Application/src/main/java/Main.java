import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableCellRenderer;

public class Main {
private static ConnectDB dbConnection;
private static JTextArea recipeDisplayArea; // Class-level variable
private static JTable recipeTable; // JTable nesnesi



private static void handleIngredientSearch(JTextField ingredientInputField) {
    String ingredientsInput = ingredientInputField.getText();
    List<String> inputIngredients = Arrays.asList(ingredientsInput.split(","));
    List<RecipeMatch> results = dbConnection.searchRecipesByIngredients(inputIngredients);

    DefaultTableModel tableModel = (DefaultTableModel) recipeTable.getModel();
    tableModel.setRowCount(0); // Tablonun önceki içeriklerini temizleyin

    for (RecipeMatch match : results) {
        Object[] rowData = {
            match.getTarifAdi(),
            match.getMatchPercentage(),
            match.getMissingIngredientsCost() > 0 ? match.getMissingIngredientsCost() + " ₺" : "0 ₺"
        };
        tableModel.addRow(rowData);
    }

    recipeTable.setDefaultRenderer(Object.class, new CustomTableCellRenderer(results));
}

static class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private final List<RecipeMatch> matches;

    public CustomTableCellRenderer(List<RecipeMatch> matches) {
        this.matches = matches;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        RecipeMatch match = matches.get(row);
        if (match.getMatchPercentage() == 100) {
            cell.setBackground(Color.GREEN);
        } else   {
            cell.setBackground(Color.RED);
        } 
        return cell;
    }
}



private static JTextArea resultsArea;
    public static void main(String[] args) {
        // Establish a connection to the database
        dbConnection = ConnectDB.getInstance();

        // Set up the GUI
       SwingUtilities.invokeLater(() -> {
    JFrame frame = new JFrame("Tasty Tales");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1000, 800); 
    frame.setLayout(new BorderLayout());
frame.setVisible(true);
    handleViewRecipes(frame); // Program açılışında tarifleri gösterir
    
    
 
    
    
    
    // Set up recipeDisplayArea
recipeDisplayArea = new JTextArea();
recipeDisplayArea.setEditable(false);
JScrollPane displayScrollPane = new JScrollPane(recipeDisplayArea);
frame.add(displayScrollPane, BorderLayout.CENTER); // Adds the JTextArea to the center



   // JTable ve modelini oluştur
    String[] columnNames = { "Tarif Adı", "Hazır olma durumu","Maliyet Bilgisi"};
    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
    recipeTable = new JTable(tableModel);
    JScrollPane recipeScrollPane = new JScrollPane(recipeTable);

    // JTable'i orta alana ekleyin
    frame.add(recipeScrollPane, BorderLayout.CENTER);

    // Program açıldığında tarifleri görüntüle
    displayAllRecipes();
    
   
    
    
//jtable mouse listener ekle
recipeTable.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            int row = recipeTable.getSelectedRow();
            if (row != -1) {
                showRecipeDetails(row);
            }
        }
    }
});

    JPanel searchPanel = new JPanel(new GridLayout(8, 2));
            JTextField ingredientInputField = new JTextField();
            JTextField searchField = new JTextField();
            JTextField ingredientCountField = new JTextField();
            JTextField minCostField = new JTextField();
            JTextField maxCostField = new JTextField();
            JComboBox<String> categoryFilter = new JComboBox<>(new String[]{"Hepsi", "Ana Yemek", "Tatlı", "Aperatif", "Diğer"});
            JComboBox<String> sortBy = new JComboBox<>(new String[]{"Hazırlama Süresi (Artan)", "Hazırlama Süresi (Azalan)", "Maliyet (Artan)", "Maliyet (Azalan)"});
            JButton searchButton = new JButton("Ara");
            JButton filterButton = new JButton("Filtrele");
            JButton ingredientSearchButton = new JButton("Malzemeye Göre Ara");
            searchPanel.add(new JLabel("Malzeme Gir:"));
            searchPanel.add(ingredientInputField);


searchPanel.add(new JLabel("Malzeme Sayısı:"));
searchPanel.add(ingredientCountField);
searchPanel.add(new JLabel("Min Maliyet:"));
searchPanel.add(minCostField);
searchPanel.add(new JLabel("Max Maliyet:"));
searchPanel.add(maxCostField);
searchPanel.add(new JLabel("Tarif Ara:"));
searchPanel.add(searchField);
searchPanel.add(new JLabel("Kategori:"));
searchPanel.add(categoryFilter);
searchPanel.add(new JLabel("Sırala:"));
searchPanel.add(sortBy);

searchPanel.add(searchButton);

searchPanel.add(ingredientSearchButton);
// Ana çerçeveye arama panelini ekle
frame.add(searchPanel, BorderLayout.NORTH);

  searchButton.addActionListener(e -> handleSearch(
                frame,
                searchField.getText(),
                (String) categoryFilter.getSelectedItem(),
                (String) sortBy.getSelectedItem(),
                ingredientCountField.getText(),
                minCostField.getText(),
                maxCostField.getText()
                 // Yeni eklenen malzeme girdisi
  ));
  ingredientSearchButton.addActionListener(e -> handleIngredientSearch(ingredientInputField));

filterButton.addActionListener(e -> handleSearch(frame, searchField.getText(), (String) categoryFilter.getSelectedItem(), (String) sortBy.getSelectedItem(),    ingredientCountField.getText(),    minCostField.getText(),   maxCostField.getText()));

    // Create a panel for recipe inputs
    JPanel recipePanel = new JPanel(new GridLayout(0, 2));
    JTextField tarifIDField = new JTextField();
    JTextField tarifAdiField = new JTextField();
    JTextField kategoriField = new JTextField();
    JTextField hazirlamaSuresiField = new JTextField();
    JTextArea talimatlarArea = new JTextArea(5, 20);
    JScrollPane scrollPane = new JScrollPane(talimatlarArea);
    
    
    // Adding components to the recipe panel
    recipePanel.add(new JLabel("Tarif ID:"));
    recipePanel.add(tarifIDField);
    recipePanel.add(new JLabel("Tarif Adı:"));
    recipePanel.add(tarifAdiField);
    recipePanel.add(new JLabel("Kategori:"));
    recipePanel.add(kategoriField);
    recipePanel.add(new JLabel("Hazırlama Süresi:"));
    recipePanel.add(hazirlamaSuresiField);
    recipePanel.add(new JLabel("Talimatlar:"));
    recipePanel.add(scrollPane);

    // Create buttons for recipes
    JButton addRecipeButton = new JButton("Add Recipe");
    JButton updateRecipeButton = new JButton("Update Recipe");
    JButton deleteRecipeButton = new JButton("Delete Recipe");
    

    // Action listeners for recipe buttons
    addRecipeButton.addActionListener(e -> handleAddRecipe(tarifIDField, tarifAdiField, kategoriField, hazirlamaSuresiField, talimatlarArea));
    updateRecipeButton.addActionListener(e -> handleUpdateRecipe(tarifIDField, tarifAdiField, kategoriField, hazirlamaSuresiField, talimatlarArea));
    deleteRecipeButton.addActionListener(e -> handleDeleteRecipe(tarifIDField));
   
    
    
    // Create a panel for recipe ingredient inputs
    JPanel recipeIngredientPanel = new JPanel(new GridLayout(0, 2));
    JTextField tarifIDMalzemeField = new JTextField();
    JComboBox<String> malzemeSecimiBox = new JComboBox<>();
    JTextField malzemeMiktarField = new JTextField();
    JButton addRecipeIngredientButton = new JButton("Tarife Malzeme Ekle");
    // Panel bileşenlerini ekle
    recipeIngredientPanel.add(new JLabel("Tarif ID:"));
    recipeIngredientPanel.add(tarifIDMalzemeField);
    recipeIngredientPanel.add(new JLabel("Malzeme Seç:"));
    recipeIngredientPanel.add(malzemeSecimiBox);
    
    recipeIngredientPanel.add(malzemeMiktarField);
    recipeIngredientPanel.add(new JLabel(""));
    recipeIngredientPanel.add(addRecipeIngredientButton);

    // Mevcut malzemeleri ComboBox'a ekle
    List<Ingredient> ingredients = dbConnection.getAllIngredients();
    for (Ingredient ingredient : ingredients) {
        malzemeSecimiBox.addItem(ingredient.getMalzemeAdi());
    }

    // Tarife malzeme ekleme işlemi
    addRecipeIngredientButton.addActionListener(e -> handleAddRecipeIngredient(tarifIDMalzemeField, malzemeSecimiBox, malzemeMiktarField));


    // Frame'e ekle (tarif ve malzeme panellerinin ardından)
    frame.add(recipeIngredientPanel, BorderLayout.EAST);


JTextArea resultsArea = new JTextArea();

  searchButton.addActionListener(e -> {
    String ingredientsInput = ingredientInputField.getText();
    List<String> inputIngredients = Arrays.asList(ingredientsInput.split(","));
    
    // Veritabanı sorgusunu çağır
    List<RecipeMatch> results = ConnectDB.getInstance().searchRecipesByIngredients(inputIngredients);
    
    // Sonuçları JTextArea veya JTable'de görüntüle
    resultsArea.setText("");
    for (RecipeMatch match : results) {
        resultsArea.append(match.toString() + "\n");
    }
});

    
    
    // View paneline bileşenler ekle
JPanel buttonPanel = new JPanel();
buttonPanel.add(searchPanel); // "Ara" panelini ekle
buttonPanel.add(addRecipeButton);
buttonPanel.add(updateRecipeButton);
buttonPanel.add(deleteRecipeButton);

frame.add(buttonPanel, BorderLayout.SOUTH);

// Arama düğmesi olayını ekle
// Malzeme arama butonu için ActionListener ekleyin
ingredientSearchButton.addActionListener(e -> handleIngredientSearch(ingredientInputField));

searchButton.addActionListener(e -> handleSearch(
    frame,
    searchField.getText(),
    (String) categoryFilter.getSelectedItem(),
    (String) sortBy.getSelectedItem(),
    ingredientCountField.getText(),
    minCostField.getText(),
    maxCostField.getText()
));

filterButton.addActionListener(e -> handleSearch(
    frame,
    searchField.getText(),
    (String) categoryFilter.getSelectedItem(),
    (String) sortBy.getSelectedItem(),
    ingredientCountField.getText(),
    minCostField.getText(),
    maxCostField.getText()
));



    
    // Add components to the recipe frame
    frame.add(recipePanel, BorderLayout.WEST);
    
   
    buttonPanel.add(addRecipeButton);
    buttonPanel.add(updateRecipeButton);
    buttonPanel.add(deleteRecipeButton);
    
   

    // Create a panel for ingredient inputs
    JPanel ingredientPanel = new JPanel(new GridLayout(0, 2));
    JTextField malzemeIDField = new JTextField();
    JTextField malzemeAdiField = new JTextField();
    
    JTextField malzemeBirimField = new JTextField();
    JTextField malzemeFiyatField = new JTextField();
    JButton addIngredientButton = new JButton("Add Ingredient");
    JButton selectIngredientButton = new JButton("Select Existing Ingredient");

    // Adding components to the ingredient panel
    ingredientPanel.add(new JLabel("Malzeme ID:"));
    ingredientPanel.add(malzemeIDField);
    ingredientPanel.add(new JLabel("Malzeme Adı:"));
    ingredientPanel.add(malzemeAdiField);
    ingredientPanel.add(new JLabel("Malzeme Miktarı:"));
    ingredientPanel.add(malzemeMiktarField); 
    ingredientPanel.add(new JLabel("Malzeme Birimi:"));
    ingredientPanel.add(malzemeBirimField);
    ingredientPanel.add(new JLabel("Birim Fiyat:"));
    ingredientPanel.add(malzemeFiyatField);
    ingredientPanel.add(addIngredientButton);
    ingredientPanel.add(selectIngredientButton);

    // Action listeners for ingredient buttons
    addIngredientButton.addActionListener(e -> handleAddIngredient(malzemeIDField, malzemeAdiField, malzemeMiktarField, malzemeBirimField, malzemeFiyatField));
    selectIngredientButton.addActionListener(e -> handleSelectIngredient(frame));

    // Add ingredient panel to the frame
    frame.add(ingredientPanel, BorderLayout.NORTH);
    frame.setVisible(true);
    // Call handleViewRecipes to display recipes at startup
            handleViewRecipes(frame);
});

       
       
       
    }
    private static void showRecipeDetails(int row) {
        DefaultTableModel model = (DefaultTableModel) recipeTable.getModel();
        String tarifAdi = (String) model.getValueAt(row, 0);
        double maliyetBilgisi = (double) model.getValueAt(row, 2);

        Recipe selectedRecipe = dbConnection.getRecipeByName(tarifAdi);

        if (selectedRecipe != null) {
            JOptionPane.showMessageDialog(null,
                "Tarif ID: " + selectedRecipe.getTarifID() + "\n" +
                "Tarif Adı: " + selectedRecipe.getTarifAdi() + "\n" +
                "Kategori: " + selectedRecipe.getKategori() + "\n" +
                "Hazırlama Süresi: " + selectedRecipe.getHazirlamaSuresi() + "\n" +
                "Talimatlar: " + selectedRecipe.getTalimatlar() + "\n" +
                "Maliyet Bilgisi: " + maliyetBilgisi,
                "Tarif Detayları",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                "Tarif bulunamadı.",
                "Hata",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Program açıldığında tarifleri tabloya eklemek için
private static void displayAllRecipes() {
    List<Recipe> recipes = dbConnection.getAllRecipes();
    DefaultTableModel tableModel = (DefaultTableModel) recipeTable.getModel();
    tableModel.setRowCount(0); // Tablonun önceki içeriklerini temizleyin
    
    for (Recipe recipe : recipes) {
        Object[] rowData = {
             recipe.getTarifAdi(),
            recipe.getHazirlamaSuresi(),
            recipe.getTotalCost() // Tarife ait toplam maliyeti hesaplar
                     
        };
        tableModel.addRow(rowData);
    }
}
 private static void handleSearch(JFrame frame, String searchText, String category, String sortOption, String ingredientCountText, String minCostText, String maxCostText) {
    List<Recipe> recipes = dbConnection.getAllRecipes();

    // Filtreleme ve sıralama işlemi
    recipes = filterRecipes(recipes, searchText, category, ingredientCountText, minCostText, maxCostText);
    recipes = sortRecipes(recipes, sortOption);

    // JTable'deki verileri güncelleyin
    DefaultTableModel tableModel = (DefaultTableModel) recipeTable.getModel();
    tableModel.setRowCount(0); // Tablonun önceki içeriklerini temizleyin

    for (Recipe recipe : recipes) {
        Object[] rowData = {
            recipe.getTarifAdi(),
            recipe.getHazirlamaSuresi(),
            recipe.getTotalCost()
        };
        tableModel.addRow(rowData);
    }
}

private static List<Recipe> filterRecipes(List<Recipe> recipes, String searchText, String category, String ingredientCountText, String minCostText, String maxCostText) {
    List<Recipe> filteredRecipes = new ArrayList<>();

    int ingredientCount = ingredientCountText.isEmpty() ? -1 : Integer.parseInt(ingredientCountText);
    double minCost = minCostText.isEmpty() ? Double.MIN_VALUE : Double.parseDouble(minCostText);
    double maxCost = maxCostText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxCostText);

    for (Recipe recipe : recipes) {
        if ((category.equals("Hepsi") || recipe.getKategori().equals(category)) &&
            (searchText.isEmpty() || recipe.getTarifAdi().toLowerCase().contains(searchText.toLowerCase())) &&
            (ingredientCount == -1 || recipe.getIngredientCount() == ingredientCount) &&
            (recipe.getTotalCost() >= minCost && recipe.getTotalCost() <= maxCost)) {
            
            filteredRecipes.add(recipe);
        }
    }

    return filteredRecipes;
}


// Sıralama metodu
private static List<Recipe> sortRecipes(List<Recipe> recipes, String sortOption) {
    recipes.sort((r1, r2) -> {
        switch (sortOption) {
            case "Hazırlama Süresi (Artan)":
                return Integer.compare(r1.getHazirlamaSuresi(), r2.getHazirlamaSuresi());
            case "Hazırlama Süresi (Azalan)":
                return Integer.compare(r2.getHazirlamaSuresi(), r1.getHazirlamaSuresi());
            case "Maliyet (Artan)":
                return Double.compare(r1.getTotalCost(), r2.getTotalCost());
            case "Maliyet (Azalan)":
                return Double.compare(r2.getTotalCost(), r1.getTotalCost());
            default:
                return 0;
        }
    });

    return recipes;
}

    // Handle adding a recipe
    private static void handleAddRecipe(JTextField tarifIDField, JTextField tarifAdiField, JTextField kategoriField,
                                         JTextField hazirlamaSuresiField, JTextArea talimatlarArea) {
        try {
            int tarifID = Integer.parseInt(tarifIDField.getText());
            String tarifAdi = tarifAdiField.getText();
            String kategori = kategoriField.getText();
            int hazirlamaSuresi = Integer.parseInt(hazirlamaSuresiField.getText());
            String talimatlar = talimatlarArea.getText();

            dbConnection.addRecipe(tarifID, tarifAdi, kategori, hazirlamaSuresi, talimatlar);
            clearRecipeFields(tarifIDField, tarifAdiField, kategoriField, hazirlamaSuresiField, talimatlarArea);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void handleAddRecipeIngredient(JTextField tarifIDField, JComboBox<String> malzemeSecimiBox, JTextField malzemeMiktarField) {
    try {
        int tarifID = Integer.parseInt(tarifIDField.getText());
        String malzemeAdi = (String) malzemeSecimiBox.getSelectedItem();
        float malzemeMiktar = Float.parseFloat(malzemeMiktarField.getText());

        // Malzeme ID'sini almak için malzeme adını kullanarak arama yap
        int malzemeID = -1;
        List<Ingredient> ingredients = dbConnection.getAllIngredients();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getMalzemeAdi().equals(malzemeAdi)) {
                malzemeID = ingredient.getMalzemeID();
                break;
            }
        }

        if (malzemeID != -1) {
            dbConnection.addRecipeIngredient(tarifID, malzemeID, malzemeMiktar);
            clearRecipeIngredientFields(tarifIDField, malzemeMiktarField);
        } else {
            JOptionPane.showMessageDialog(null, "Seçilen malzeme bulunamadı.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
    }
}

private static void clearRecipeIngredientFields(JTextField tarifIDField, JTextField malzemeMiktarField) {
    tarifIDField.setText("");
    malzemeMiktarField.setText("");
}

    // Handle adding an ingredient
    private static void handleAddIngredient(JTextField malzemeIDField, JTextField malzemeAdiField,
                                             JTextField malzemeMiktarField, JTextField malzemeBirimField,
                                             JTextField malzemeFiyatField) {
        try {
            int malzemeID = Integer.parseInt(malzemeIDField.getText());
            String malzemeAdi = malzemeAdiField.getText();
            int toplamMiktar = Integer.parseInt(malzemeMiktarField.getText()); // Eklenen alan
            String malzemeBirim = malzemeBirimField.getText();
            float birimFiyat = Float.parseFloat(malzemeFiyatField.getText()); // float türünde olmalı

            dbConnection.addIngredient(malzemeID, malzemeAdi, toplamMiktar, malzemeBirim, birimFiyat);
            clearIngredientFields(malzemeIDField, malzemeAdiField, malzemeMiktarField, malzemeBirimField, malzemeFiyatField);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle updating a recipe
    private static void handleUpdateRecipe(JTextField tarifIDField, JTextField tarifAdiField, JTextField kategoriField,
                                            JTextField hazirlamaSuresiField, JTextArea talimatlarArea) {
        try {
            int tarifID = Integer.parseInt(tarifIDField.getText());
            String newTarifAdi = tarifAdiField.getText();
            String newKategori = kategoriField.getText();
            int newHazirlamaSuresi = Integer.parseInt(hazirlamaSuresiField.getText());
            String newTalimatlar = talimatlarArea.getText();

            dbConnection.updateRecipe(tarifID, newTarifAdi, newKategori, newHazirlamaSuresi, newTalimatlar);
            clearRecipeFields(tarifIDField, tarifAdiField, kategoriField, hazirlamaSuresiField, talimatlarArea);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir sayı girin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle deleting a recipe
    private static void handleDeleteRecipe(JTextField tarifIDField) {
        try {
            int tarifID = Integer.parseInt(tarifIDField.getText());
            dbConnection.deleteRecipe(tarifID);
            clearRecipeFields(tarifIDField, new JTextField(), new JTextField(), new JTextField(), new JTextArea());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Lütfen geçerli bir Tarif ID'si girin.", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle viewing all recipes
    private static void handleViewRecipes(JFrame frame) {
    List<Recipe> recipes = dbConnection.getAllRecipes();
    StringBuilder recipesList = new StringBuilder();
    for (Recipe recipe : recipes) {
        recipesList.append(recipe.toString()).append("\n");
    }
    if (recipeDisplayArea != null) {
        recipeDisplayArea.setText(recipesList.toString());
    }
}


    // Method to clear input fields for recipes
    private static void clearRecipeFields(JTextField tarifIDField, JTextField tarifAdiField, JTextField kategoriField,
                                          JTextField hazirlamaSuresiField, JTextArea talimatlarArea) {
        tarifIDField.setText("");
        tarifAdiField.setText("");
        kategoriField.setText("");
        hazirlamaSuresiField.setText("");
        talimatlarArea.setText("");
    }

        // Method to clear input fields for ingredients
    private static void clearIngredientFields(JTextField malzemeIDField, JTextField malzemeAdiField,
                                               JTextField malzemeMiktarField, JTextField malzemeBirimField,
                                               JTextField malzemeFiyatField) {
        malzemeIDField.setText("");
        malzemeAdiField.setText("");
        malzemeMiktarField.setText("");
        malzemeBirimField.setText("");
        malzemeFiyatField.setText("");
    }

    // Handle selecting an existing ingredient
    private static void handleSelectIngredient(JFrame frame) {
        List<Ingredient> ingredients = dbConnection.getAllIngredients();
        StringBuilder ingredientList = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            ingredientList.append(ingredient.toString()).append("\n");
        }
        JOptionPane.showMessageDialog(frame, ingredientList.toString(), "Ingredients", JOptionPane.INFORMATION_MESSAGE);
    }
}