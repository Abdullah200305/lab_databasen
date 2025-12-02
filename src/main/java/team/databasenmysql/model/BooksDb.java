package team.databasenmysql.model;

import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.SelectException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class BooksDb implements IBooksDb {
    @Override
    public boolean connect(String database) throws ConnectionException {
        String user ="root"; // username (or use hardcoded values)
        String pwd = "1234"; // password
        System.out.println(user + pwd);
        String serverUrl = "jdbc:mysql://localhost:3306/" + database
                + "?UseClientEnc=UTF8";
        Connection conn = null;
        try {
            // open a connection
            conn = DriverManager.getConnection(serverUrl, user, pwd);
            System.out.println("Connected!");
            // execute a query
            executeQuery(conn, "SELECT * FROM T_Employee");
        } finally {
            if (conn != null) conn.close();
            System.out.println("Connection closed.");
        }

        return false;
    }

    @Override
    public void disconnect() throws ConnectionException {

    }

    @Override
    public List<Book> findBooksByTitle(String title) throws SelectException {
        return List.of();
    }

    @Override
    public List<Book> findBooksByIsbn(String isbn) throws SelectException {
        return List.of();
    }
}
