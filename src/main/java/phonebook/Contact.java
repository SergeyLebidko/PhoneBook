package phonebook;

import java.util.LinkedList;

public class Contact {

    private int id;
    private String name;
    private LinkedList<Account> accounts;

    public Contact(int id, String name) {
        this.id = id;
        this.name = name;
        accounts = new LinkedList<>();
    }

    public void addAccount(Account account){
        accounts.add(account);
    }

    public void removeAccount(Account account){
        accounts.remove(account);
    }

    @Override
    public String toString() {
        return "[ id=" + id + " name=" + name + " ]";
    }

}
