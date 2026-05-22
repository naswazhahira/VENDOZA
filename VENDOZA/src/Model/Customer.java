package Model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    // Atribut khusus Customer
    private String alamat;
    private double saldo;
    private int poinReward;
    private List<String> riwayatPembelian;
    private static final String ROLE = "CUSTOMER";

    // Constructor
    public Customer(String nama, String email, String password, String alamat) {
        super(nama, email, password);
        this.alamat = alamat;
        this.saldo = 0.0;
        this.poinReward = 0;
        this.riwayatPembelian = new ArrayList<>();
    }

    // Overloaded constructor dengan saldo awal
    public Customer(String nama, String email, String password, String alamat, double saldoAwal) {
        super(nama, email, password);
        this.alamat = alamat;
        this.saldo = saldoAwal;
        this.poinReward = 0;
        this.riwayatPembelian = new ArrayList<>();
    }

    // GETTERS & SETTERS
    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        if (alamat == null || alamat.trim().isEmpty()) {
            throw new IllegalArgumentException("Alamat tidak boleh kosong!");
        }
        this.alamat = alamat;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        if (saldo < 0) {
            throw new IllegalArgumentException("Saldo tidak boleh negatif!");
        }
        this.saldo = saldo;
    }

    public int getPoinReward() {
        return poinReward;
    }

    public void setPoinReward(int poinReward) {
        this.poinReward = Math.max(poinReward, 0);
    }

    public String getRole() {
        return ROLE;
    }

    public List<String> getRiwayatPembelian() {
        return new ArrayList<>(riwayatPembelian);
    }

    // Method khusus Customer
    public void topUpSaldo(double jumlah) {
        if (jumlah <= 0) {
            System.out.println("Jumlah top up harus lebih dari 0!");
            return;
        }
        this.saldo += jumlah;
        System.out.println("Top up berhasil! Saldo Anda sekarang: Rp " + String.format("%,.0f", saldo));
    }

    public boolean kurangiSaldo(double jumlah) {
        if (jumlah <= 0) {
            System.out.println("Jumlah tidak valid!");
            return false;
        }
        if (saldo >= jumlah) {
            saldo -= jumlah;
            tambahPoin((int) (jumlah / 10000)); // Setiap Rp 10.000 dapat 1 poin
            return true;
        }
        System.out.println("Saldo tidak mencukupi!");
        return false;
    }

    private void tambahPoin(int poin) {
        this.poinReward += poin;
        System.out.println("+" + poin + " poin reward! Total poin: " + poinReward);
    }

    public void gunakanPoin(int poin) {
        if (poin <= 0) {
            System.out.println("Poin harus lebih dari 0!");
            return;
        }
        if (poinReward >= poin) {
            double diskon = poin * 100; // 1 poin = Rp 100 diskon
            poinReward -= poin;
            System.out.println("Berhasil menggunakan " + poin + " poin. Diskon Rp " + String.format("%,.0f", diskon));
        } else {
            System.out.println("Poin tidak mencukupi! Poin Anda: " + poinReward);
        }
    }

    public void tambahRiwayatPembelian(String orderInfo) {
        riwayatPembelian.add(orderInfo);
    }

    public void tampilkanRiwayat() {
        System.out.println("\n RIWAYAT PEMBELIAN ");
        if (riwayatPembelian.isEmpty()) {
            System.out.println("Belum ada riwayat pembelian.");
        } else {
            for (int i = 0; i < riwayatPembelian.size(); i++) {
                System.out.println((i + 1) + ". " + riwayatPembelian.get(i));
            }
        }
    }

    // IMPLEMENTASI ABSTRACT METHOD
    @Override
    public void tampilRole() {
        System.out.println(" [CUSTOMER] " + getNama());
        System.out.println(" Email     : " + getEmail());
        System.out.println(" Alamat    : " + alamat);
        System.out.println(" Saldo     : Rp " + String.format("%,.0f", saldo));
        System.out.println(" Poin      : " + poinReward);
    }

    // Override method tampilkanInfo dari User
    @Override
    public void tampilkanInfo() {
        super.tampilkanInfo();
        System.out.println("Role      : CUSTOMER");
        System.out.println("Alamat    : " + alamat);
        System.out.println("Saldo     : Rp " + String.format("%,.0f", saldo));
        System.out.println("Poin Reward: " + poinReward);
    }

    @Override
    public String toString() {
        return "[CUSTOMER] " + super.toString() + " | Saldo: Rp " + String.format("%,.0f", saldo);
    }
}
