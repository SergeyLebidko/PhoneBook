package phonebook.data_access_components;

import phonebook.Account;
import phonebook.Contact;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

public class DataBaseConnector {

    static final String database = "jdbc:sqlite:database\\phonebook.db";

    private Statement statement;

    public DataBaseConnector() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection(database);
        statement = connection.createStatement();
    }

    public Contact[] loadContacts() throws Exception {
        //Сперва получаем список имен
        ResultSet resultSet;
        resultSet = executeQuery("SELECT * FROM CONTACTS");

        //Формируем список
        LinkedList<Contact> contacts = new LinkedList<>();
        int contactId;
        String name;
        while (resultSet.next()) {
            contactId = resultSet.getInt(1);
            name = resultSet.getString(2);
            contacts.add(new Contact(contactId, name));
        }

        //Заполняем списки аккаунтов каждого контакта
        int accountId;
        String type;
        String protocol;
        String address;
        String account;
        for (Contact contact: contacts){
            contactId = contact.getId();
            resultSet = executeQuery("SELECT * FROM ACCOUNTS WHERE CONTACT_ID="+contactId);
            while (resultSet.next()){
                accountId = resultSet.getInt(1);
                type = resultSet.getString(3);
                protocol = resultSet.getString(4);
                address = resultSet.getString(5);
                account = resultSet.getString(6);
                contact.addAccount(new Account(accountId, contactId, type, protocol, address, account));
            }
        }

        return contacts.toArray(new Contact[contacts.size()]);
    }

    public void addContact(String contact) throws Exception {
        String query = "INSERT INTO CONTACTS (NAME) VALUES (\""+contact+"\")";
        updateQuery(query);
    }

    public void addAccount(Account account) throws Exception {
        String query = "INSERT INTO ACCOUNTS (CONTACT_ID, TYPE, PROTOCOL, ADDRESS, ACCOUNT) VALUES (";
        query+=account.getContactId()+", \""+account.getType()+"\", \""+account.getProtocol()+"\", \""+account.getAddress()+"\", \""+account.getAccount()+"\")";
        updateQuery(query);
    }

    public void deleteContact(Contact contact) throws Exception {
        String query = "DELETE FROM CONTACTS WHERE ID="+contact.getId();
        updateQuery(query);

        //ВСТАВИТЬ КОД УДАЛЕНИЯ АККАУНТОВ, СВЯЗАННЫХ С ДАННЫМ КОНТАКТОМ
    }

    public void changeContact(Contact contact) throws Exception {
        String query = "UPDATE CONTACTS SET NAME = \""+contact.getName()+"\" WHERE ID="+contact.getId();
        updateQuery(query);
    }

    private ResultSet executeQuery(String SQLQuery) throws Exception {
        return statement.executeQuery(SQLQuery);
    }

    private int updateQuery(String SQLQuery) throws Exception {
        return statement.executeUpdate(SQLQuery);
    }

}
