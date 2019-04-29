package phonebook;

public class Contact {

    private int id;
    private String name;
    private Account[] accounts;

    public Contact(int id, String name, Account[] accounts) {
        this.id = id;
        this.name = name;
        this.accounts = accounts;
    }

}
