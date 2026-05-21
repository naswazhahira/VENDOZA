package Produk;

public class ItemKeranjang {
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

    public double getSubtotal() {
        return produk.getHarga() * jumlah;
    }

    @Override
    public String toString() {
        return String.format("  - %-22s x%d = Rp %,.0f",
                produk.getNama(), jumlah, getSubtotal());
    }
}