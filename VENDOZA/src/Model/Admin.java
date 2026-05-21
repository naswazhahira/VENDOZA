package Model;

public class Admin extends User {
    private String divisi;

    public Admin(String nama, String email, String password, String divisi) {
        super (nama, email, password);
        this.divisi = divisi;
    }

    public String getDivisi() {
        return divisi;
    }

    public void setDivisi(String divisi) {
        this.divisi = divisi;
    }

    @Override
    public void tampilRole() {
        System.out.println("[Admin] " + getNama() + " | Divisi: " + divisi);
    }
}
