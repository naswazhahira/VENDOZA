package Produk;

import java.util.ArrayList;
import java.util.List;

public class Keranjang {
    private List<ItemKeranjang> items = new ArrayList<>();

    public void tambah(ProdukVendoza produk, int jumlah) {
        if (jumlah > produk.getStok()) {
            System.out.println("Gagal: stok " + produk.getNama() + " tidak cukup.");
            return;
        }
        items.add(new ItemKeranjang(produk, jumlah));
        System.out.println("Ditambahkan: " + produk.getNama() + " x" + jumlah);
    }

    public double getTotal() {
        return items.stream().mapToDouble(ItemKeranjang::getSubtotal).sum();
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    public void tampilKeranjang() {
        System.out.println("\n===== ISI KERANJANG =====");
        items.forEach(System.out::println);
        System.out.printf("  Total : Rp %,.0f%n", getTotal());
    }
}
