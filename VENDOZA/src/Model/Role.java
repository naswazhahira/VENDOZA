package Model;

public enum Role {
    ADMIN,
    CUSTOMER;

    public String getDisplayName() {
        return this == ADMIN ? "Administrator" : "Pelanggan";
    }
}