package main;

public class Manager extends Employee {

    public Manager createManagerAccount (String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;

        return this;
    }

    public Manager changeManagerAccountData (String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;

        return this;
    }

    public Manager setSalary (Double salary) {
        this.salary = salary;

        return this;
    }

    public Manager setContract (Integer contract) {
        this.contract = contract;

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
