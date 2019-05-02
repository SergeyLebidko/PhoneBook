package phonebook;

public class Account {

    private int id;
    private int contactId;
    private String type;
    private String protocol;
    private String address;
    private String account;

    public Account(int id, int contactId, String type, String protocol, String address, String account) {
        this.id = id;
        this.contactId = contactId;
        this.type = type;
        this.protocol = protocol;
        this.address = address;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public int getContactId() {
        return contactId;
    }

    public String getType() {
        return type;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAddress() {
        return address;
    }

    public String getAccount() {
        return account;
    }


    @Override
    public String toString() {
        return account;
    }

}
