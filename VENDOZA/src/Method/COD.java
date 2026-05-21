package Method;

public class COD implements Payment {
    private String alamatPengiriman;

    public COD(String alamatPengiriman) {
        this.alamatPengiriman = alamatPengiriman;
    }

    @Override
    public void bayar(double total) {
        System.out.println(">>> Pembayaran via COD (Cash on Delivery) <<<");
        System.out.printf("    Alamat  : %s%n", alamatPengiriman);
        System.out.printf("    Nominal : Rp %,.0f%n", total);
        System.out.println("    Bayar saat barang tiba di tangan Anda.");
    }

    @Override
    public void konfirmasi() {
        System.out.println("    [COD] Pesanan dikonfirmasi. Kurir akan segera dikirim.");
    }
}
