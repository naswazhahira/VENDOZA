package Model;

public class Customer extends User {
    private String alamat;

    public Customer(String nama, String email, String password, String alamat) {
        super(nama, email, password);
        this.alamat = alamat;
    }

    public String getAlamat()               { return alamat; }
    public void   setAlamat(String alamat)  { this.alamat = alamat; }

    @Override
    public void tampilRole() {
        System.out.println("[Customer] " + getNama() + " | " + getEmail());
    }
}