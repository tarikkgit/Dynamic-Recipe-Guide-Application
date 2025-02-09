public class Ingredient {
    private int malzemeID;
    private String malzemeAdi;
    private int toplamMiktar;
    private int usedAmount; // Bu tarifte kullanılacak miktar
    private String malzemeBirim;
    private double birimFiyat;

    // Constructor
    public Ingredient(int malzemeID, String malzemeAdi, int toplamMiktar, String malzemeBirim, double birimFiyat) {
        this.malzemeID = malzemeID;
        this.malzemeAdi = malzemeAdi;
        this.toplamMiktar = toplamMiktar;
        this.malzemeBirim = malzemeBirim;
        this.birimFiyat = birimFiyat;
    }

    // Yeni constructor
    public Ingredient(int malzemeID, String malzemeAdi, int toplamMiktar, int usedAmount, String malzemeBirim, double birimFiyat) {
        this.malzemeID = malzemeID;
        this.malzemeAdi = malzemeAdi;
        this.toplamMiktar = toplamMiktar;
        this.usedAmount = usedAmount;
        this.malzemeBirim = malzemeBirim;
        this.birimFiyat = birimFiyat;
    }

    // Getter methods
    public int getMalzemeID() {
        return malzemeID;
    }

    public String getMalzemeAdi() {
        return malzemeAdi;
    }

    public int getToplamMiktar() {
        return toplamMiktar;
    }

    public int getUsedAmount() {
        return usedAmount;
    }

    public String getMalzemeBirim() {
        return malzemeBirim;
    }

    public double getBirimFiyat() {
        return birimFiyat;
    }

    // Malzeme fiyatını hesaplayan metot
    public double getPrice() {
        return getUsedAmount() * birimFiyat;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "malzemeID=" + malzemeID +
                ", malzemeAdi='" + malzemeAdi + '\'' +
                ", toplamMiktar=" + toplamMiktar +
                ", malzemeBirim='" + malzemeBirim + '\'' +
                ", birimFiyat=" + birimFiyat +
                '}';
    }
}
