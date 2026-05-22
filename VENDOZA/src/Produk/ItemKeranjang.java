package Produk;

public class ItemKeranjang {
    // ENCAPSULATION
    private ProdukVendoza produk;
    private int jumlah;

    public ItemKeranjang(ProdukVendoza produk, int jumlah) {
        this.produk = produk;
        this.jumlah = jumlah;
    }

    public ProdukVendoza getProduk() {
        return produk;
    }

    public int getJumlah() {
        return jumlah;
    }

    // 1. Method untuk menambahkan kuantitas jika user memilih produk yang sama lagi
    public void tambahJumlah(int jumlah) {
        this.jumlah += jumlah;
    }

    // 2. Method untuk mengubah jumlah secara langsung
    public void setJumlah(int jumlah) {
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah item harus lebih dari 0!");
        this.jumlah = jumlah;
    }

    public double getSubtotal() {
        return produk.getHarga() * jumlah;
    }

    // POLYMORPHISM (OVERRIDING)
    // Melakukan override method toString() dari kelas Object Java.
    @Override
    public String toString() {
        return String.format("  - %-22s x%d = Rp %,.0f",
                produk.getNama(), jumlah, getSubtotal());
    }
}
