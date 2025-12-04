/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.databasenmysql.model;


import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.SelectException;

import javax.net.ssl.SSLException;
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

   /* private final List<Book> books; // the mock "database"*/
    private List<Book> books;
    private Connection conn; /// by abody
    public IBooksDbMockImpl() {
       /* books = Arrays.asList(DATA);*/
        books = new ArrayList<>();
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
      /*  Db_book(conn);*/
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




    /// by abody
    @Override
    public List<Book> findBooksByTitle(String title)
            throws SelectException {
        List<Book> result = new ArrayList<>();
        books = new ArrayList<>();
        String sql = String.format(
                "SELECT TITLE, ISBN, published\n" +
                        "FROM T_BOOK\n" +
                        "WHERE TITLE LIKE '%s%%'",
                title);
        Db_data(sql);
            title = title.trim().toLowerCase();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(title)) {
                    result.add(book);
                }
            }
            return result;

    }




    ///  by abody
    @Override
    public List<Book> findBooksByIsbn(String isbn) throws SelectException {
        List<Book> result = new ArrayList<>();
        books = new ArrayList<>();
        String sql = String.format(
                "SELECT TITLE, ISBN, published\n" +
                        "FROM T_BOOK\n" +
                        "WHERE ISBN = '%s'",
                isbn);
        Db_data(sql);
        isbn = isbn.trim().toLowerCase();
        for (Book book : books) {
            if (book.getIsbn().toLowerCase().equals(isbn)) { // exact match
                result.add(book);
            }
        }
        return result;
    }

    @Override
    public List<Book> findBooksByAuthor(String author_name) throws SelectException {
        List<Book> result = new ArrayList<>();
        books = new ArrayList<>();
        String sql = String.format("SELECT TITLE, b.ISBN, published, author, authorid, birthdate\n" +
                                "FROM T_BOOK AS b\n" +
                                "JOIN T_BOOK_AUTHOR AS a ON a.ISBN = b.ISBN\n" +
                                "WHERE a.AUTHOR LIKE '%s%%'",author_name);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String TITLE = rs.getString(1);
                String ISBN = rs.getString(2);
                Date published = rs.getDate(3);
                String author = rs.getString(4);
                int authorid = rs.getInt(5);
                Date birthdate = rs.getDate(6);

                Book bok = new Book(ISBN,TITLE,published);
                bok.addAuthor(new Authors(authorid,author,birthdate));
                books.add(bok);
            }

            author_name = author_name.trim().toLowerCase();
            for (Book book : books) {
                for (Authors auth : book.getAuthors()){
                    System.out.println(auth.getAuthorName().toLowerCase().contains(author_name));
                    if (auth.getAuthorName().toLowerCase().contains(author_name)) {
                        result.add(book);

                    }
                }
            }
            return result;

        } catch (SQLException e) {
            throw new SelectException("Bad select"+e.getSQLState());
        }
    }




    @Override
    public List<Book> findBooksByGrade(String betyg) throws SelectException{
        List<Book> result = new ArrayList<>();
        books.clear();

        String sql= String.format("");
       /* Db_data(sql);
        isbn = isbn.trim().toLowerCase();
        for (Book book : books) {
            if (book.getIsbn().toLowerCase().equals(isbn)) { // exact match
                result.add(book);
            }
        }*/
        return result;
    }


    @Override
    public List<Book> findBooksByGenre(String genre) throws SelectException{
        List<Book> result = new ArrayList<>();
        books.clear();
        String sql = String.format("SELECT TITLE,b.ISBN,published\n" +
                "FROM T_BOOK AS b\n" +
                "JOIN T_BOOK_GENRE AS g ON g.ISBN = b.ISBN\n" +
                "WHERE g.GENRE LIKE '%s%%'",
                genre);
        Db_data(sql);
        genre = genre.trim().toLowerCase();
        for (Book book : books) {
            if (book.getIsbn().toLowerCase().contains(genre)) {
                result.add(book);
            }}
        return result;
    }









    ///  by abody
    private void Db_data(String sql) throws SelectException {
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String TITLE = rs.getString(1);
                String ISBN = rs.getString(2);
                Date published = rs.getDate(3);
                books.add(new Book(ISBN,TITLE,published));
            }
        } catch (SQLException e) {
            throw new SelectException("Bad select"+e.getSQLState());
        }
    }

    private static final Book[] DATA = {
            new Book(1, "123456789", "Databases Illuminated", new Date(2018, 1, 1),1,null),
            new Book(2, "234567891", "Dark Databases", new Date(1990, 1, 1),1,null),
            new Book(3, "456789012", "The buried giant", new Date(2000, 1, 1),1,null),
            new Book(4, "567890123", "Never let me go", new Date(2000, 1, 1),1,null),
            new Book(5, "678901234", "The remains of the day", new Date(2000, 1, 1),1,null),
            new Book(6, "234567890", "Alias Grace", new Date(2000, 1, 1),1,null),
            new Book(7, "345678911", "The handmaids tale", new Date(2010, 1, 1),1,null),
            new Book(8, "345678901", "Shuggie Bain", new Date(2020, 1, 1),1,null),
            new Book(9, "345678912", "Microserfs", new Date(2000, 1, 1),1,null),
    };
}
