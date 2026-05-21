package Method;

public class Transfer implements Payment{
    private String namaBank;
    private String nomorRekening;

    public Transfer(String namaBank, String nomorRekening) {
        this.namaBank       = namaBank;
        this.nomorRekening  = nomorRekening;
    }

    @Override
    public void bayar(double total) {
        System.out.println(">>> Pembayaran via Transfer Bank <<<");
        System.out.printf("    Bank    : %s%n", namaBank);
        System.out.printf("    No. Rek : %s%n", nomorRekening);
        System.out.printf("    Nominal : Rp %,.0f%n", total);
        System.out.println("    Silakan transfer dalam 1x24 jam.");
    }

    @Override
    public void konfirmasi() {
        System.out.println("    [Transfer] Konfirmasi dikirim ke tim verifikasi.");
    }
}
