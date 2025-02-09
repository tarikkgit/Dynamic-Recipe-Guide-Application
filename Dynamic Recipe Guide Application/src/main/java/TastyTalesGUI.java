import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TastyTalesGUI extends JFrame {
    private ArrayList<Administrator> adminList = new ArrayList<>();

    public TastyTalesGUI() {
        // Pencere özellikleri
        setTitle("Tasty Tales GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Demo admin ekle
        adminList.add(new Administrator("Admin", "123", "Master", "555", "h@h.com"));

        // Ana paneli oluştur
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        // Kullanıcı tipi seçim düğmeleri
        JButton adminButton = createStyledButton("Admin");
        JButton customerButton = createStyledButton("Tüketici");
        JButton bmiButton = createStyledButton("BMI Hesapla");
        JButton guestButton = createStyledButton("Ziyaretçi");

        // GridBagLayout için kısıtlamalar
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Düğmeleri panele ekle
        panel.add(adminButton, gbc);
        gbc.gridy++;
        panel.add(customerButton, gbc);
        gbc.gridy++;
        panel.add(bmiButton, gbc);
        gbc.gridy++;
        panel.add(guestButton, gbc);

        // Düğme tıklama olayları
        adminButton.addActionListener(e -> openAdminMenu());
        customerButton.addActionListener(e -> openCustomerMenu());
        bmiButton.addActionListener(e -> openBMICalculator());
        guestButton.addActionListener(e -> showRecipesForGuest());

        // Ana panele ekle ve pencereyi görünür hale getir
        add(panel);
        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    private void openAdminMenu() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "Kullanıcı Adı:", usernameField,
            "Şifre:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Admin Girişi", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Admin listesini kontrol et
            boolean loginSuccess = false;
            for (Administrator admin : adminList) {
                if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                    loginSuccess = true;
                    break;
                }
            }

            if (loginSuccess) {
                JOptionPane.showMessageDialog(this, "Giriş başarılı! Admin menüsü açılıyor...");
                // Burada admin menüsünü açacak kodu ekle
            } else {
                JOptionPane.showMessageDialog(this, "Hatalı kullanıcı adı veya şifre!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openCustomerMenu() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
            "Kullanıcı Adı:", usernameField,
            "Şifre:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Tüketici Girişi", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            JOptionPane.showMessageDialog(this, "Giriş başarılı! Tüketici menüsü açılıyor...");
            // Burada tüketici menüsünü açacak kodu ekle
        }
    }

    private void openBMICalculator() {
        JTextField heightField = new JTextField();
        JTextField weightField = new JTextField();

        Object[] message = {
            "Boy (cm):", heightField,
            "Kilo (kg):", weightField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "BMI Hesapla", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double height = Double.parseDouble(heightField.getText()) / 100; // metre cinsine çevir
                double weight = Double.parseDouble(weightField.getText());
                double bmi = weight / (height * height);

                JOptionPane.showMessageDialog(this, "BMI Değeriniz: " + String.format("%.2f", bmi));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Geçerli bir sayı giriniz.", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRecipesForGuest() {
        JTextArea textArea = new JTextArea(10, 30);
        textArea.setText("Ziyaretçi olarak tarifler:\n- Tarif 1\n- Tarif 2\n- Tarif 3");
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(this, scrollPane, "Tarifler", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TastyTalesGUI::new);
    }

    // Basit bir Administrator sınıfı
    class Administrator {
        private String username;
        private String password;
        private String fullName;
        private String phone;
        private String email;

        public Administrator(String username, String password, String fullName, String phone, String email) {
            this.username = username;
            this.password = password;
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
