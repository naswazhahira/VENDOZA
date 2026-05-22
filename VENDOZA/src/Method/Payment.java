package Method;

public interface Payment {
    void bayar(double total);
    void konfirmasi();
    String getStatus(); //mengecek status pembayaran
}
