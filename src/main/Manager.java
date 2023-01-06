package main;

public class Manager extends Employee {

    public Manager(String username, String password) {
        super(username, password);
    }

    public Manager createManagerAccount (String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;

        return this;
    }

    public Manager changeManagerAccountData (String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;

        return this;
    }


    public Manager setIsActive (Boolean isActive) {
        this.isActive = isActive;

        return this;
    }

    public Manager setManagerAuthentication (String username, String password) {
        this.username = username;
        this.password = password;

        return this;
    }
}
