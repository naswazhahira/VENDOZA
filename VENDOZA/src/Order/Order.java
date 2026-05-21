package Order;

import Method.Payment;
import Model.Customer;
import Produk.ItemKeranjang;
import Produk.Keranjang;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private static int counter = 1;
    private int orderId;
    private Customer customer;
    private List<ItemKeranjang> items;
    private double total;
    private String status;
    private Payment metodePembayaran;

    public Order(Customer customer, Keranjang keranjang, Payment metodePembayaran) {
        this.orderId          = counter++;
        this.customer         = customer;
        this.items            = new ArrayList<>(keranjang.getItems());
        this.total            = keranjang.getTotal();
        this.metodePembayaran = metodePembayaran;
        this.status           = "MENUNGGU_PEMBAYARAN";

        // Kurangi stok setiap produk yang dibeli
        for (ItemKeranjang item : items) {
            item.getProduk().kurangiStok(item.getJumlah());
        }
    }

    public void prosesPembayaran() {
        System.out.println("\n===== PROSES CHECKOUT — Order #" + orderId + " =====");
        System.out.println("Customer : " + customer.getNama());
        items.forEach(System.out::println);
        System.out.printf("Total    : Rp %,.0f%n%n", total);

        // Polymorphism: bayar() & konfirmasi() dipanggil seragam,
        // tapi perilakunya beda tergantung jenis Payment (Transfer / COD)
        metodePembayaran.bayar(total);
        metodePembayaran.konfirmasi();

        this.status = "DIPROSES";
        System.out.println("\nStatus Order: " + status);
    }

    public void updateStatus(String status) {
        this.status = status;
        System.out.println("Order #" + orderId + " — Status diperbarui: " + status);
    }

    public void cekTracking() {
        System.out.println("\n===== TRACKING ORDER #" + orderId + " =====");
        System.out.println("Customer : " + customer.getNama());
        System.out.println("Status   : " + status);
    }

    public int    getOrderId() { return orderId; }
    public String getStatus()  { return status; }
}
