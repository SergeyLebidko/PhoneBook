package phonebook.dataaccesscomponents;

import phonebook.Contact;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

public class DataBaseConnector {

    static final String database = "jdbc:sqlite:database\\phonebook.db";

    private Statement statement;

    DataBaseConnector() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection(database);
        statement = connection.createStatement();
    }

    public Contact[] loadContacts() throws Exception {
        return loadContacts("");
    }

    public Contact[] loadContacts(String filter) throws Exception {
        //Сперва получаем список имен
        ResultSet resultSet = executeQuery("SELECT * FROM CONTACTS");

        LinkedList<Contact> contacts = new LinkedList<>();
        while (resultSet.next()) {
            contacts.add(new Contact(resultSet.getInt(1), resultSet.getString(2)));
        }

        return contacts.toArray(new Contact[contacts.size()]);
    }

    private ResultSet executeQuery(String SQLQuery) throws Exception {
        return statement.executeQuery(SQLQuery);
    }

    private int updateQuery(String SQLQuery) throws Exception {
        return statement.executeUpdate(SQLQuery);
    }

}
