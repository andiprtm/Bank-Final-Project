package main;

import java.math.BigDecimal;

public class Staff extends Employee {

    public Staff(String username, String password) {
        super(username, password);
    }

    public Staff createStaffAccount (String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;

        return this;
    }

    public Staff changeStaffAccountData (String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;

        return this;
    }

    public Staff setIsActive (Boolean isActive) {
        this.isActive = isActive;

        return this;
    }
}
