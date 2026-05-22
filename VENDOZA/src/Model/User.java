package Model;

public abstract class User {
    // ENCAPSULATION: private fields
    private String nama;
    private String email;
    private String password;
    private String nomorTelepon;
    private String idUser;

    // Constructor
    public User(String nama, String email, String password) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.nomorTelepon = "";
        this.idUser = generateId();
    }

    // Overloaded constructor dengan nomor telepon
    public User(String nama, String email, String password, String nomorTelepon) {
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.nomorTelepon = nomorTelepon;
        this.idUser = generateId();
    }

    // Method untuk generate ID unik
    private String generateId() {
        return "USR" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }

    // GETTERS & SETTERS (Encapsulation)
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email tidak valid!");
        }
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password minimal 4 karakter!");
        }
        this.password = password;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomorTelepon = nomorTelepon;
    }

    public String getIdUser() {
        return idUser;
    }

    // ABSTRACT METHOD (wajib diimplementasikan oleh subclass)
    public abstract void tampilRole();

    // Method konkrit yang bisa di-override
    public void tampilkanInfo() {
        System.out.println("ID User   : " + idUser);
        System.out.println("Nama      : " + nama);
        System.out.println("Email     : " + email);
        System.out.println("No. Telepon: " + (nomorTelepon.isEmpty() ? "-" : nomorTelepon));
    }

    // POLYMORPHISM: Override toString
    @Override
    public String toString() {
        return String.format("%s | %s | %s", idUser, nama, email);
    }
}
