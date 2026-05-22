package Method;

public class COD implements Payment {
    private String alamatPengiriman;
    private boolean sudahDibayar; //untuk mengecek status pembayaran
    
    public COD(String alamatPengiriman) {
        this.alamatPengiriman = alamatPengiriman;
        this.sudahDibayar = false;
    }

    @Override
    public void bayar(double total) {
        System.out.println(">>> METODE PEMABAYARAN COD (Cash on Delivery) <<<");
        System.out.printf("    Alamat  : %s%n", alamatPengiriman);
        System.out.printf("    Nominal : Rp %,.0f%n", total);
        System.out.println("    Bayar saat barang tiba di tangan Anda.");
        this.sudahDibayar = false; 
    }

    @Override
    public void konfirmasi() {
        System.out.println("  Pesanan COD dikonfirmasi. Kurir menuju ke alamatmu.");
    }

    @Override
    public String getStatus() {
        return sudahDibayar ? "PEMBAYARAN BERHASIL!" : "MENUNGGU PEMBAYARAN!"; //jika sudah melakukan pembayaran(true) akan berhasil dan jika blm membayar(false) akan muncul menunggu pembayaran.
    }
    }
