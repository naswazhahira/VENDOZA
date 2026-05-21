package Produk;

public class ProdukVendoza {
    private String nama;
    private double harga;
    private int stok;

    public ProdukVendoza(String nama, double harga, int stok) {
        this.nama = nama;
        setHarga(harga);
        setStok(stok);
    }

    public String getNama(){
        return nama;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public double getHarga() {
        return harga;
    }

    public void   setHarga(double harga) {
        if (harga < 0) throw new IllegalArgumentException("Harga tidak boleh negatif!");
        this.harga = harga;
    }

    public int  getStok() {
        return stok;
    }

    public void setStok(int stok) {
        if (stok < 0) throw new IllegalArgumentException("Stok tidak boleh negatif!");
        this.stok = stok;
    }

    public void kurangiStok(int jumlah) {
        if (jumlah > stok) throw new IllegalStateException("Stok tidak mencukupi!");
        this.stok -= jumlah;
    }

    @Override
    public String toString() {
        return String.format("%-22s | Rp %,.0f | Stok: %d", nama, harga, stok);
    }
}
