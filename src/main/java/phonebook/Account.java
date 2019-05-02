package phonebook;

import java.util.Objects;

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

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account1 = (Account) o;
        return contactId == account1.contactId &&
                Objects.equals(type, account1.type) &&
                Objects.equals(protocol, account1.protocol) &&
                Objects.equals(address, account1.address) &&
                Objects.equals(account, account1.account);
    }

    @Override
    public String toString() {
        return account;
    }

}
