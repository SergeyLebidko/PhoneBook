package phonebook.data_access_components;

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
        ResultSet resultSet = executeQuery("SELECT * FROM CONTACTS");

        //Формируем список
        LinkedList<Contact> contacts = new LinkedList<>();
        int id;
        String name;
        while (resultSet.next()) {
            id = resultSet.getInt(1);
            name = resultSet.getString(2);
            contacts.add(new Contact(id, name));
        }

        //ВСТАВИТЬ КОД ПОЛУЧЕНИЯ АККАУНТОВ ДЛЯ ЗАГРУЖЕННЫХ КОНТАКТОВ

        return contacts.toArray(new Contact[contacts.size()]);
    }

    public void addContact(String contact) throws Exception {
        String query = "INSERT INTO CONTACTS (NAME) VALUES (\""+contact+"\")";
        updateQuery(query);
    }

    public void deleteContact(Contact contact) throws Exception {
        String query = "DELETE FROM CONTACTS WHERE ID="+contact.getId();
        updateQuery(query);

        //ВСТАВИТЬ КОД УДАЛЕНИЯ АККАУНТОВ, СВЯЗАННЫХ С ДАННЫМ КОНТАКТОМ
    }

    private ResultSet executeQuery(String SQLQuery) throws Exception {
        return statement.executeQuery(SQLQuery);
    }

    private int updateQuery(String SQLQuery) throws Exception {
        return statement.executeUpdate(SQLQuery);
    }

}
