package Main;

import Method.COD;
import Method.Payment;
import Method.Transfer;
import Model.Admin;
import Model.AuthService;
import Model.Customer;
import Model.User;
import Order.Order;
import Produk.Keranjang;
import Produk.ProdukVendoza;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainClass {
    static Scanner scanner = new Scanner(System.in);
    static List<ProdukVendoza> daftarProduk = new ArrayList<>();

    public static void main(String[] args) {
        // Data produk awal (bisa kamu tambah)
        daftarProduk.add(new ProdukVendoza("Laptop ASUS VivoBook", 8_500_000, 10));
        daftarProduk.add(new ProdukVendoza("Mouse Logitech M235", 150_000, 50));
        daftarProduk.add(new ProdukVendoza("Tas Ransel Pria", 275_000, 20));


        // Daftarkan user ke sistem
        AuthService auth = new AuthService();
        auth.register(new Admin("Sari Admin", "sari@toko.com", "admin123", "Operasional"));
        auth.register(new Customer("Budi", "budi@email.com", "pass123", "Jl. Merdeka No.5"));

        // Login
        System.out.println("===== LOGIN =====");
        System.out.print("Email    : ");
        String email = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();

        User userLogin = auth.login(email, password);
        if (userLogin == null) return; // stop kalau login gagal

        // Cek role lalu arahkan ke menu yang sesuai
        if (userLogin instanceof Admin) {
            menuAdmin((Admin) userLogin);
        } else if (userLogin instanceof Customer) {
            menuCustomer((Customer) userLogin);
        }
    }

    // ── MENU ADMIN ───────────────────────────────────────────
    static void menuAdmin(Admin admin) {
        boolean aktif = true;
        while (aktif) {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1. Lihat semua produk");
            System.out.println("2. Tambah produk baru");
            System.out.println("0. Keluar");
            int pilih = inputAngka("Pilih: ");

            switch (pilih) {
                case 1:
                    daftarProduk.forEach(p -> System.out.println("  " + p));
                    break;
                case 2:
                    System.out.print("Nama produk : "); String nama = scanner.nextLine();
                    double harga = inputAngka("Harga       : ");
                    int stok     = inputAngka("Stok        : ");
                    daftarProduk.add(new ProdukVendoza(nama, harga, stok));
                    System.out.println("Produk berhasil ditambahkan!");
                    break;
                case 0:
                    aktif = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    // ── MENU CUSTOMER ────────────────────────────────────────
    static void menuCustomer(Customer customer) {
        Keranjang keranjang = new Keranjang();
        boolean belanja = true;

        while (belanja) {
            System.out.println("\n===== DAFTAR PRODUK =====");
            for (int i = 0; i < daftarProduk.size(); i++) {
                System.out.println((i + 1) + ". " + daftarProduk.get(i));
            }
            int pilih = inputAngka("Pilih produk (0 = checkout): ");

            if (pilih == 0) {
                belanja = false;
            } else if (pilih >= 1 && pilih <= daftarProduk.size()) {
                int jumlah = inputAngka("Jumlah: ");
                keranjang.tambah(daftarProduk.get(pilih - 1), jumlah);
            } else {
                System.out.println("Pilihan tidak valid.");
            }
        }

        keranjang.tampilKeranjang();

        System.out.println("\nMetode Pembayaran:");
        System.out.println("1. Transfer Bank");
        System.out.println("2. COD");
        int metodePilih = inputAngka("Pilih (1/2): ");

        Payment payment;
        if (metodePilih == 1) {
            System.out.print("Nama Bank    : "); String bank  = scanner.nextLine();
            System.out.print("No. Rekening : "); String noRek = scanner.nextLine();
            payment = new Transfer(bank, noRek);
        } else {
            payment = new COD(customer.getAlamat());
        }

        Order order = new Order(customer, keranjang, payment);
        order.prosesPembayaran();
        order.cekTracking();
    }

    // ── HELPER INPUT ─────────────────────────────────────────
    static int inputAngka(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input harus angka, coba lagi.");
            }
        }
    }
}
