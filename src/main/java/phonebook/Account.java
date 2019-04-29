package phonebook;

public class Account {

    private int contactId;
    private String type;
    private String protocol;
    private String address;
    private String account;

    public Account(int contactId, String type, String protocol, String address, String account) {
        this.contactId = contactId;
        this.type = type;
        this.protocol = protocol;
        this.address = address;
        this.account = account;
    }

}
