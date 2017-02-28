package Applicatie;

public class TestCustomerBuilder {
    private String name;
    private String address;
    private String postal;

    public TestCustomerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TestCustomerBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public TestCustomerBuilder setPostal(String postal) {
        this.postal = postal;
        return this;
    }

    public Auto.TestCustomer createTestCustomer() {
        return new Auto.TestCustomer(name, address, postal);
    }
}