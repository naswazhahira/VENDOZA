package Produk;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Keranjang {
    // ENCAPSULATION
    // List item belanja dibatasi aksesnya agar tidak bisa dimanipulasi
    private List<ItemKeranjang> items = new ArrayList<>();

    public void tambah(ProdukVendoza produk, int jumlah) {
        if (jumlah <= 0) {
            System.out.println("Gagal: Jumlah yang ditambahkan harus lebih dari 0.");
            return;
        }

        // 1. Cek apakah produk sudah ada di dalam keranjang
        // ABSTRACTION
        Optional<ItemKeranjang> itemAda = items.stream()
                .filter(item -> item.getProduk().getNama().equalsIgnoreCase(produk.getNama()))
                .findFirst();

        if (itemAda.isPresent()) {
            // Validasi total jika jumlah baru ditambahkan
            int totalJumlahBaru = itemAda.get().getJumlah() + jumlah;
            if (totalJumlahBaru > produk.getStok()) {
                System.out.println("Gagal: Total jumlah di keranjang (" + totalJumlahBaru + ") melebihi stok " + produk.getNama() + ".");
                return;
            }
            // Jika aman, akumulasikan jumlahnya
            itemAda.get().tambahJumlah(jumlah);
            System.out.println("Diperbarui di keranjang: " + produk.getNama() + " sekarang x" + itemAda.get().getJumlah());
        } else {
            // Jika produk baru benar-benar masuk keranjang, cek stok awal
            if (jumlah > produk.getStok()) {
                System.out.println("Gagal: stok " + produk.getNama() + " tidak cukup.");
                return;
            }
            items.add(new ItemKeranjang(produk, jumlah));
            System.out.println("Ditambahkan: " + produk.getNama() + " x" + jumlah);
        }
    }

    // 2. Fitur Hapus Item Tertentu
    public void hapus(String namaProduk) {
        boolean terhapus = items.removeIf(item -> item.getProduk().getNama().equalsIgnoreCase(namaProduk));
        if (terhapus) {
            System.out.println("Berhasil menghapus " + namaProduk + " dari keranjang.");
        } else {
            System.out.println("Gagal menghapus: " + namaProduk + " tidak ditemukan di keranjang.");
        }
    }

    // 3. Fitur Mengosongkan Isi Keranjang setelah checkout
    public void kosongkan() {
        items.clear();
        System.out.println("Keranjang telah dikosongkan.");
    }

    public double getTotal() {
        return items.stream().mapToDouble(ItemKeranjang::getSubtotal).sum();
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    // Akan dihapus setelah masuk ke bagian backend
    public void tampilKeranjang() {
        System.out.println("\nISI KERANJANG");
        if (items.isEmpty()) {
            System.out.println("  (Keranjang kosong)");
        } else {
            // Abstraksi & Polimorfisme: Memanggil toString() secara otomatis untuk tiap item
            items.forEach(System.out::println);
        }
        System.out.printf("  Total : Rp %,.0f%n", getTotal());
    }
}
