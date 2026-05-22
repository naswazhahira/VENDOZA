package Method;

public class Transfer implements Payment{
    private String namaBank;
    private String nomorRekening;
    private boolean sudahDibayar;

    public Transfer(String namaBank, String nomorRekening) {
        this.namaBank       = namaBank;
        this.nomorRekening  = nomorRekening;
        this.sudahDibayar = false;
    }

    @Override
    public void bayar(double total) {
        System.out.println(">>> METODE PEMBAYARAN TRANSFER BANK <<<");
        System.out.printf("    Bank    : %s%n", namaBank);
        System.out.printf("    No. Rek : %s%n", nomorRekening);
        System.out.printf("    Nominal : Rp %,.0f%n", total);
        System.out.println("   Silahkan melakukan pembayaran dalam 1x24 jam.");
        this.sudahDibayar = false;
    }

    @Override
    public void konfirmasi() {
        this.sudahDibayar = true; //status diubah menjadi lunas
        System.out.println("    Transfer diKonfirmasi pembayaran diterima. Pesanan anda sedang diproses");
    }

    @Override
    public String getStatus() {
        return sudahDibayar ? "PEMBAYARAN BERHASIL!" : "MENUNGGU PEMBAYARAN!";  //jika sudah melakukan pembayaran(true) akan berhasil dan jika blm membayar(false) akan muncul menunggu pembayaran.
        }
}
