package phonebook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DataBaseConnector {

    static final String database = "jdbc:sqlite:database\\phonebook.db";

    private Statement statement;

    DataBaseConnector() throws Exception{
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection(database);
        statement = connection.createStatement();
    }

    private ResultSet executeQuery(String SQLQuery) throws Exception{
        return statement.executeQuery(SQLQuery);
    }

    private int updateQuery(String SQLQuery) throws  Exception{
        return statement.executeUpdate(SQLQuery);
    }

}
