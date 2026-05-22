package Model;

public class Admin extends User {
    // Atribut khusus Admin
    private String divisi;
    private String levelAkses;
    private static final String ROLE = "ADMIN";

    // Constructor
    public Admin(String nama, String email, String password, String divisi) {
        super(nama, email, password);
        this.divisi = divisi;
        this.levelAkses = "STANDARD";
    }

    // Overloaded constructor dengan level akses
    public Admin(String nama, String email, String password, String divisi, String levelAkses) {
        super(nama, email, password);
        this.divisi = divisi;
        this.levelAkses = levelAkses;
    }

    // GETTERS & SETTERS
    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        if (divisi == null || divisi.trim().isEmpty()) {
            throw new IllegalArgumentException("Divisi tidak boleh kosong!");
        }
        this.divisi = divisi;
    }

    public String getLevelAkses() {
        return levelAkses;
    }

    public void setLevelAkses(String levelAkses) {
        this.levelAkses = levelAkses;
    }

    public String getRole() {
        return ROLE;
    }

    // Method khusus Admin
    public void kelolaProduk() {
        System.out.println("[Admin] " + getNama() + " sedang mengelola produk di divisi " + divisi);
    }

    public void lihatLaporan() {
        System.out.println("[Admin] " + getNama() + " membuka laporan penjualan");
    }

    // IMPLEMENTASI ABSTRACT METHOD
    @Override
    public void tampilRole() {
        System.out.println(" [ADMIN] " + getNama());
        System.out.println(" Divisi    : " + divisi);
        System.out.println(" Level     : " + levelAkses);
        System.out.println(" Email     : " + getEmail());
    }

    // Override method tampilkanInfo dari User
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Role      : ADMIN");
        System.out.println("Divisi    : " + divisi);
        System.out.println("Level Akses: " + levelAkses);
    }

    @Override
    public String toString() {
        return "[ADMIN] " + super.toString() + " | Divisi: " + divisi;
    }
}
