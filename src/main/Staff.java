package main;

public class Staff extends Employee {

    Staff() {

    }

    public Staff createStaffAccount (String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;

        return this;
    }

    public Staff changeStaffAccountData (String name, String address, Integer age) {
        this.name = name;
        this.address = address;
        this.age = age;

        return this;
    }

    public Staff setSalary (Double salary) {
        this.salary = salary;

        return this;
    }

    public Staff setContract (Integer contract) {
        this.contract = contract;

        return this;
    }

    public Staff setIsActive (Boolean isActive) {
        this.isActive = isActive;

        return this;
    }
}
