/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.databasenmysql.model;


import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.InsertException;
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
    public List<Book> findBooksByTitle(String title) throws SelectException {
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
    public List<Book> findBooksByGrade(String grade) throws SelectException{
        List<Book> result = new ArrayList<>();
        books.clear();
        Grade betyg = null;
        String sql= String.format("SELECT TITLE,ISBN,published,Betyg \n" +
                "FROM T_BOOK \n" +
                "WHERE Betyg LIKE '%s%%';",grade);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String TITLE = rs.getString(1);
                String ISBN = rs.getString(2);
                Date published = rs.getDate(3);
                betyg = Grade.valueOf(rs.getString(4));
                books.add(new Book(ISBN,TITLE,null,published,betyg,null));
            }
            for (Book book : books) {
                if (book.getGrade().equals(betyg)) {
                    result.add(book);
                }
            }
            return result;

        } catch (SQLException e) {
            throw new SelectException("Bad select"+e.getSQLState());
        }
    }
    @Override
    public List<Book> findBooksByGenre(String genre) throws SelectException{
        List<Book> result = new ArrayList<>();
        books.clear();
        String sql = String.format("SELECT TITLE,b.ISBN,published,GENRE\n" +
                "FROM T_BOOK AS b\n" +
                "JOIN T_BOOK_GENRE AS g ON g.ISBN = b.ISBN\n" +
                "WHERE g.GENRE LIKE '%s%%'",
                genre);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                String TITLE = rs.getString(1);
                String ISBN = rs.getString(2);
                Date published = rs.getDate(3);
                String Genre = rs.getString(4);
                System.out.println(Genre);
                books.add(new Book(ISBN,TITLE,null,published,null,Genre));
            }
            genre = genre.trim().toLowerCase();
            for (Book book : books) {
                if (book.getGenre().toLowerCase().contains(genre)) {
                    result.add(book);
                }}
            return result;
        } catch (SQLException e) {
            throw new SelectException("Bad select"+e.getSQLState());
        }


    }


    @Override
    public void InsertBook(Book book) throws InsertException {
        String sqlBook = "INSERT INTO T_BOOK (ISBN, Title, Published) VALUES (?, ?, ?)";
        String sqlGenre = "INSERT INTO T_BOOK_GENRE (ISBN, Genre) VALUES (?, ?)";
        String sqlAuthor = "INSERT INTO T_BOOK_AUTHOR (ISBN, Author) VALUES (?, ?)";
        try{
            conn.setAutoCommit(false);
            try{
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
            }
            catch (SQLException e){
                if (conn != null) conn.rollback();
                throw new InsertException("Error in Transation!!!");
            }
            finally {
                conn.setAutoCommit(true);
            }
    }
        catch (SQLException e) {
            throw new InsertException("Error in insert book try again!!!");
        }
    }
    @Override
    public boolean DeleteBook(String isbn){
        String sql = String.format(
                "select ISBN,TITLE\n" +
                "FROM t_BOOK \n" +
                "WHERE ISBN = '%s';"
                ,isbn);
        String delauthor = String.format("DELETE FROM T_BOOK_AUTHOR WHERE ISBN= '%s';\n",isbn);
        String delGenre = String.format("DELETE FROM t_BOOK_GENRE WHERE ISBN= '%s';\n",isbn);
        String delCopy = String.format("DELETE FROM t_COPY WHERE ISBN= '%s';\n",isbn);
        String delBook = String.format("DELETE FROM t_BOOK WHERE ISBN= '%s';\n",isbn);
        Book book = null;
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {

                    String ISBN=rs.getString(1);
                    String TITLE=rs.getString(2);
                    book = new Book(ISBN,TITLE,null);
            }
            if(!rs.next()){ return false;};
            System.out.println(book.getIsbn() +" "+book.getTitle());
            conn.setAutoCommit(false);
            try {
                PreparedStatement prsAuthor = conn.prepareStatement(delauthor);
                PreparedStatement pesGenre = conn.prepareStatement(delGenre);
                PreparedStatement prsCopy = conn.prepareStatement(delCopy);
                PreparedStatement prsBook = conn.prepareStatement(delBook);
                prsAuthor.executeUpdate();
                pesGenre.executeUpdate();
                prsCopy.executeUpdate();
                prsBook.executeUpdate();
                conn.commit();
            }
            catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            }
            finally {
                conn.setAutoCommit(true);
            }
            System.out.println("done");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
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


    @Override
    public boolean UppdateBook(String isbn){

        return true;
    };







    private static final Book[] DATA = {
            new Book(1, "123456789", "Databases Illuminated",null, new Date(2018, 1, 1),null,null),
            new Book(2, "234567891", "Dark Databases",null, new Date(1990, 1, 1),null,null),
            new Book(3, "456789012", "The buried giant",null, new Date(2000, 1, 1),null,null),
            new Book(4, "567890123", "Never let me go",null, new Date(2000, 1, 1),null,null),
            new Book(5, "678901234", "The remains of the day",null, new Date(2000, 1, 1),null,null),
            new Book(6, "234567890", "Alias Grace",null, new Date(2000, 1, 1),null,null),
            new Book(7, "345678911", "The handmaids tale",null, new Date(2010, 1, 1),null,null),
            new Book(8, "345678901", "Shuggie Bain",null, new Date(2020, 1, 1),null,null),
            new Book(9, "345678912", "Microserfs",null, new Date(2000, 1, 1),null,null),
    };
}
