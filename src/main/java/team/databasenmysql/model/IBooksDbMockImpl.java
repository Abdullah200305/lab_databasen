/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.databasenmysql.model;


import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.SelectException;

import java.sql.*;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A mock implementation of the IBooksDB interface to demonstrate how to
 * use it together with the user interface.
 * <p>
 * Your implementation must access a real database.
 * @author anderslm@kth.se
 */
public class IBooksDbMockImpl implements IBooksDb {

    private final List<Book> books; // the mock "database"
    private Connection conn; /// by abody
    public IBooksDbMockImpl() {
        books = Arrays.asList(DATA);
        conn = null;
    }



/// by abody
@Override
    public boolean connect(String database) throws ConnectionException{
        String user ="root"; // username (or use hardcoded values)
        String pwd = "1234"; // password
        System.out.println(user + pwd);
        String serverUrl = "jdbc:mysql://localhost:3306/" + database
                + "?UseClientEnc=UTF8";
        try {
        conn = DriverManager.getConnection(serverUrl,user,pwd);
        Db_book(conn);
        } catch (SQLException e) {
           throw new ConnectionException("Connection went wrong pls try again!");
        }
        return true;
    }
    @Override
    public void disconnect() throws ConnectionException {
      try {
          if(conn != null) conn.close();
      }
      catch (SQLException e){
       throw new ConnectionException("Something wrong when database go down",e);
      }
    }


    private void Db_book(Connection conn) throws SQLException{
    try (Statement statement = conn.createStatement()){
        String sql = "SELECT c.EID, b.ISBN, b.published\n" +
                     "FROM T_BOOK AS b\n" +
                     "JOIN T_COPY AS c ON b.ISBN = c.ISBN;\n";

        ResultSet rs = statement.executeQuery(sql);
        while (rs.next()){
            int EID = rs.getInt(1);
            String ISBN = rs.getString(2);
            Date published = rs.getDate(3);
            System.out.println("" + EID + ' ' + ISBN+ '\t'+ + '\t'+'\t' + published + '\t');
        }
    }
    }











    @Override
    public List<Book> findBooksByTitle(String title)
            throws SelectException {
        List<Book> result = new ArrayList<>();
        title = title.trim().toLowerCase();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title)) {
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> findBooksByIsbn(String isbn) throws SelectException {
        List<Book> result = new ArrayList<>();
        // add check for valid isbn ...
        isbn = isbn.trim().toLowerCase();
        for (Book book : books) {
            if (book.getIsbn().toLowerCase().equals(isbn)) { // exact match
                result.add(book);
            }
        }
        return result;
    }

    private static final Book[] DATA = {
            new Book(1, "123456789", "Databases Illuminated", new Date(2018, 1, 1)),
            new Book(2, "234567891", "Dark Databases", new Date(1990, 1, 1)),
            new Book(3, "456789012", "The buried giant", new Date(2000, 1, 1)),
            new Book(4, "567890123", "Never let me go", new Date(2000, 1, 1)),
            new Book(5, "678901234", "The remains of the day", new Date(2000, 1, 1)),
            new Book(6, "234567890", "Alias Grace", new Date(2000, 1, 1)),
            new Book(7, "345678911", "The handmaids tale", new Date(2010, 1, 1)),
            new Book(8, "345678901", "Shuggie Bain", new Date(2020, 1, 1)),
            new Book(9, "345678912", "Microserfs", new Date(2000, 1, 1)),
    };
}
