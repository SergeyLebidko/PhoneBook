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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account1 = (Account) o;
        return contactId == account1.contactId &&
                type.equals(account1.type) &&
                protocol.equals(account1.protocol) &&
                address.equals(account1.address) &&
                account.equals(account1.account);
    }

    @Override
    public String toString() {
        return "[ contactId=" + contactId + " type=" + type + " protocol=" + protocol + " address=" + address + " account=" + account + " ]";
    }

}
