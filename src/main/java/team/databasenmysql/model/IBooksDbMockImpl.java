/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.databasenmysql.model;


import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.InsertException;
import team.databasenmysql.model.exceptions.SelectException;
import team.databasenmysql.view.UpdateChoice;

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
    private Connection conn;

    /// by abody
    public IBooksDbMockImpl() {
        /* books = Arrays.asList(DATA);*/
        books = new ArrayList<>();
        conn = null;
    }


    /// by abody
    @Override
    public boolean connect(String database) throws ConnectionException {
        String user = "root"; // username (or use hardcoded values)
        String pwd = "1234"; // password
        System.out.println(user + pwd);
        String serverUrl = "jdbc:mysql://localhost:3306/" + database
                + "?UseClientEnc=UTF8";
        try {
            conn = DriverManager.getConnection(serverUrl, user, pwd);
            /*  Db_book(conn);*/
        } catch (SQLException e) {
            throw new ConnectionException("Connection went wrong pls try again!");
        }
        return true;
    }

    @Override
    public void disconnect() throws ConnectionException {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new ConnectionException("Something wrong when database go down", e);
        }
    }


    /// by abody
    @Override
    public List<Book> findBooksByTitle(String title) throws SelectException {
        List<Book> result = new ArrayList<>();
        books.clear();
        String sqlBook = String.format("SELECT TITLE, ISBN, published,Grade\n" + "FROM T_BOOK\n" + "WHERE TITLE LIKE '%s%%'", title);
        Db_data(sqlBook);
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
        books = new ArrayList<>();
        String sql = String.format("SELECT TITLE, ISBN, published,Grade\n" + "FROM T_BOOK\n" + "WHERE ISBN = '%s'", isbn);
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
        books.clear();
        String sql = String.format("SELECT TITLE,b.ISBN, published,Grade\n" +
                "FROM T_BOOK AS b\n" +
                "JOIN T_BOOK_AUTHOR AS a ON a.ISBN = b.ISBN\n" +
                "WHERE a.AUTHOR LIKE '%s%%'", author_name);
        Db_data(sql);
        author_name = author_name.trim().toLowerCase();
        for (Book book : books) {
            for (Authors auth : book.getAuthors()) {
                if (auth.getAuthorName().toLowerCase().contains(author_name)) {
                    result.add(book);
                }
            }
        }
        return result;
    }


    @Override
    public User CheckUser(String User,String password){
        User result = null;
        String sql = String.format("SELECT FULL_NAME,PASSKODE,SSN\n" +
                "FROM t_customer\n" +
                "WHERE FULL_NAME = '%s'\n" +
                "  AND PASSKODE = '%s';\n",User,password);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                result = new User(rs.getString(3),rs.getString(2),rs.getString(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return null;
    };





    @Override
    public List<Book> findBooksByGrade(String grade,String user) throws SelectException {
        List<Book> result = new ArrayList<>();
        books.clear();
        String sql = String.format(
                "SELECT b.TITLE, b.ISBN,b.PUBLISHED\n" +
                "FROM T_BOOK b\n" +
                "JOIN T_REVIEW r ON r.ISBN = b.ISBN\n" +
                "JOIN T_CUSTOMER c ON c.SSN = r.SSN\n" +
                "WHERE c.FULL_NAME = '%s'\n" +
                "  AND r.GRADE = '%s';\n",user ,grade);
        Db_data(sql);
        System.out.println(books.size());
        for (Book book : books) {
          /*  if (book.getGrade() == Grade.valueOf(grade.toUpperCase())) {
                result.add(book);
            }*/
        }
        return result;
    }









    @Override
    public List<Book> findBooksByGenre(String genre) throws SelectException {
        List<Book> result = new ArrayList<>();
        books.clear();
        String sql = String.format("SELECT TITLE,b.ISBN,published,Grade\n" +
                "FROM T_BOOK AS b\n" +
                "JOIN T_BOOK_GENRE AS g ON g.ISBN = b.ISBN\n" +
                "WHERE g.GENRE LIKE '%s%%'", genre);
        Db_data(sql);
        genre = genre.trim().toLowerCase();
        for (Book book : books) {
            for (String s : book.getGenres()) {
                if (s.toLowerCase().contains(genre)) {
                    result.add(book);
                }
            }
        }
        return result;
    }








    @Override
    public List<Authors> bringAuthors(){
        List<Authors> result = new ArrayList<>();
        String sql = "SELECT DISTINCT AUTHOR FROM T_BOOK_AUTHOR;";

        try {
            Statement st1 = conn.createStatement();
            ResultSet rsAuthors = st1.executeQuery(sql);
            while (rsAuthors.next()) {
                String Author = rsAuthors.getString(1);
                result.add(new Authors(Author));
            }
        } catch (SQLException e) {
        }
        return result;
    }







    @Override
    public void InsertBook(Book book) throws InsertException {
        String sqlBook = "INSERT INTO T_BOOK (ISBN, Title, Published) VALUES (?,?,?)";
        String sqlGenre = "INSERT INTO T_BOOK_GENRE (ISBN, Genre) VALUES (?, ?)";
        String sqlAuthor = "INSERT INTO T_BOOK_AUTHOR (ISBN, Author) VALUES (?, ?)";
        try {
            conn.setAutoCommit(false);
            try {
                PreparedStatement psBook = conn.prepareStatement(sqlBook);
                PreparedStatement psAuthor = conn.prepareStatement(sqlAuthor);
                PreparedStatement psGenre = conn.prepareStatement(sqlGenre);
                psBook.setString(1, book.getIsbn());
                psBook.setString(2, book.getTitle());
                psBook.setDate(3, book.getPublished());
                psBook.executeUpdate();

                for (Authors author : book.getAuthors()) {
                    psAuthor.setString(1, book.getIsbn());
                    psAuthor.setString(2, author.getAuthorName());
                    psAuthor.executeUpdate();
                }

                for (String genre : book.getGenres()) {
                    System.out.println(genre);
                    psGenre.setString(1, book.getIsbn());
                    psGenre.setString(2, genre);
                    psGenre.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw new InsertException("Error in Transation!!!");
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InsertException("Error in insert book try again!!!");
        }
    }





    @Override
    public Book DeleteBook(String isbn) {
        String sql = String.format(
                "select ISBN,TITLE\n" +
                        "FROM t_BOOK \n" +
                        "WHERE ISBN = '%s';"
                , isbn);
        String delauthor = String.format("DELETE FROM T_BOOK_AUTHOR WHERE ISBN= '%s';\n", isbn);
        String delGenre = String.format("DELETE FROM t_BOOK_GENRE WHERE ISBN= '%s';\n", isbn);
        String delBook = String.format("DELETE FROM t_BOOK WHERE ISBN= '%s';\n", isbn);
        Book book = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String ISBN = rs.getString(1);
                String TITLE = rs.getString(2);
                book = new Book(ISBN, TITLE, null);

            }
            if (book == null) {
                return null;
            }

            conn.setAutoCommit(false);
            try {
                PreparedStatement prsAuthor = conn.prepareStatement(delauthor);
                PreparedStatement pesGenre = conn.prepareStatement(delGenre);
                PreparedStatement prsBook = conn.prepareStatement(delBook);
                prsAuthor.executeUpdate();
                pesGenre.executeUpdate();
                prsBook.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            System.out.println("done");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return book;
    }





    private void Db_data(String sqlBook) throws SelectException {
        Book book = null;
        try {
            Statement st1 = conn.createStatement();
            Statement st2 = conn.createStatement();
            Statement st3 = conn.createStatement();
            ResultSet rsBook = st1.executeQuery(sqlBook);
            while (rsBook.next()) {
                String TITLE = rsBook.getString(1);
                String ISBN = rsBook.getString(2);
                Date published = rsBook.getDate(3);
                /*Grade grade = Grade.valueOf(rsBook.getString(4));*/

                book = new Book(ISBN, TITLE, published);
                ResultSet rsAuthors_book = st2.executeQuery("SELECT AUTHOR, birthDate, AUTHORID FROM T_BOOK_AUTHOR WHERE ISBN ='" + ISBN + "';");
                while (rsAuthors_book.next()) {
                    String author = rsAuthors_book.getString(1);
                    Date birthdate = rsAuthors_book.getDate(2);
                    int authorid = rsAuthors_book.getInt(3);

                    book.addAuthor(new Authors(authorid, author, birthdate));
                }
                ResultSet rsGenre = st3.executeQuery("SELECT g.GENRE FROM T_BOOK_GENRE AS g JOIN T_BOOK AS b ON b.ISBN = g.ISBN WHERE b.ISBN = '" + ISBN + "';");
                while (rsGenre.next()) {
                    String genre = rsGenre.getString(1);
                    book.addGenre(genre);
                }
                books.add(book);
            }
        } catch (SQLException e) {
            throw new SelectException("Bad select" + e.getSQLState());
        }
    }

    public boolean UppdateBook(UpdateChoice choiceValue, String newValue,String oldValue) {
        String isbn = choiceValue.getIsbn();

        String sql = null;
        System.out.println(oldValue+" "+newValue);
        switch (choiceValue.getMode()) {
            case Title -> sql = "UPDATE T_BOOK SET TITLE = ? WHERE ISBN = '"+isbn+"'";
            case Author -> sql = "UPDATE T_BOOK_AUTHOR SET AUTHOR = ? WHERE ISBN = '"+isbn+"' AND AUTHOR = '"+oldValue+"'";
            case Genera -> sql = "UPDATE T_BOOK_GENRE SET GENRE = ? WHERE ISBN = '"+isbn+"' AND GENRE = '"+oldValue+"'";
            default -> throw new IllegalArgumentException("Unknown update mode: " + choiceValue.getMode());
        }

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newValue);
                pstmt.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            } finally {
                assert conn != null;
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    };
}








   /* private static final Book[] DATA = {
            new Book("123456789", "Databases Illuminated", new Date(2018, 1, 1),Grade.AA),
            new Book("234567891", "Dark Databases", new Date(1990, 1, 1),Grade.AA),
            new Book("456789012", "The buried giant", new Date(2000, 1, 1),Grade.AA),
            new Book("567890123", "Never let me go", new Date(2000, 1, 1),Grade.AA),
            new Book("678901234", "The remains of the day", new Date(2000, 1, 1),Grade.AA),
            new Book("234567890", "Alias Grace", new Date(2000, 1, 1),Grade.AA),
            new Book("345678911", "The handmaids tale", new Date(2010, 1, 1),Grade.AA),
            new Book("345678901", "Shuggie Bain", new Date(2020, 1, 1),Grade.AA),
            new Book("345678912", "Microserfs", new Date(2000, 1, 1),Grade.AA),
    };*/

