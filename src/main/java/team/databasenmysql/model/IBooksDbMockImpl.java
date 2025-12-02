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

    public IBooksDbMockImpl() {
        books = Arrays.asList(DATA);
    }


    /*



public static void executeQuery(Connection con, String queryStr)
        throws SQLException {

    // try-with-resources
    try (Statement statement = con.createStatement()) {
        // execute the query
        ResultSet rs = statement.executeQuery(queryStr);
        // get the attribute names
        ResultSetMetaData metaData = rs.getMetaData();
        int colCount = metaData.getColumnCount();
        for (int c = 1; c <= colCount; c++) {
            System.out.print(metaData.getColumnName(c) + "\t");
        }
        System.out.println();

        // for each tuple, get the attribute values
        while (rs.next()) {
            int eno = rs.getInt(1);
            String name = rs.getString(2);
            Date dob = rs.getDate(3);
            float salary = rs.getFloat(4);
            int dno = rs.getInt(5);
            // NB! In a "real" application this data (each tupel) would be converted into an object
            System.out.println("" + eno + ' ' + name + '\t' + dob + '\t' + salary + '\t' + dno);
        }
    } // at this point, the statement will automatically be closed (try-with-resources)
}
}*/





@Override
    public boolean connect(String database) throws ConnectionException,SQLException {
        String user ="root"; // username (or use hardcoded values)
        String pwd = "1234"; // password
        System.out.println(user + pwd);
        String serverUrl = "jdbc:mysql://localhost:3306/" + database
                + "?UseClientEnc=UTF8";
        Connection conn = null;
        try {
        Statement sst = conn.createStatement();

        sst.executeQuery("select * from ");







        } finally {
            if (conn != null) conn.close();
            System.out.println("Connection closed.");
        }

        // mock implementation
        return true;
    }



    @Override
    public void disconnect() throws ConnectionException {
        // mock implementation
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
