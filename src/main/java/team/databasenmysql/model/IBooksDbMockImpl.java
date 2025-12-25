/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team.databasenmysql.model;

import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;
import team.databasenmysql.model.exceptions.ConnectionException;
import team.databasenmysql.model.exceptions.InsertException;
import team.databasenmysql.model.exceptions.SelectException;
import team.databasenmysql.view.UpdateChoice;


import java.sql.*;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Mock implementation of the IBooksDb interface.
 *
 * This class demonstrates how to interact with a database for book-related operations.
 * It provides methods to connect/disconnect from a MySQL database, search books by
 * title, ISBN, author, grade, or genre, insert/update/delete books, and manage reviews.
 *
 * Author: abhasan@kth.se
 *
 */
public class IBooksDbMockImpl implements IBooksDb {

    /* private final List<Book> books; // the mock "database"*/
    private List<Book> books;
    /* private Connection conn;*/
    private MongoClient mongoClient;
    private MongoDatabase db;
    private User user;
    /// by abody
    /**
     * Connects to a MySQL database using JDBC.
     *
     *
     * @return true if connection succeeds
     * @throws ConnectionException if connection fails
     */
    public IBooksDbMockImpl() {
        /* books = Arrays.asList(DATA);*/
        books = new ArrayList<>();
        mongoClient = null;
        db  = null;
        /*  conn = null;*/
        user = null;
    }

    /**
     * Connects to a MySQL database using JDBC.
     *
     * @throws ConnectionException if connection fails
     */
    @Override
    public boolean connect(String database) throws ConnectionException {
       /* MongoDatabase a = mongoClient.getDatabase("lab2");
        MongoCollection<Document> collection = a.getCollection("book");
        Document doc = collection.find(eq("title", "Dune")).first();
        System.out.println(doc.get("title"));
*/
        String urlServer = "mongodb://localhost:27017/";
        try {
            mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyConnectionString(new ConnectionString(urlServer))
                            .build());
            db = mongoClient.getDatabase(database);
        } catch (Exception e) {
            throw new ConnectionException("Database connection failed!", e);
        }
        return true;



    // old version
 /*       String user = "app_user"; // username (or use hardcoded values)

        String pwd = "Batman@2003"; // password

        String serverUrl = "jdbc:mysql://localhost:3306/" + database
                + "?UseClientEnc=UTF8";
        try {
            conn = DriverManager.getConnection(serverUrl, user, pwd);

        } catch (SQLException e) {
            throw new ConnectionException("Database connection failed!");
        }
        return true;*/
    }
    /**
     * Disconnects from the database.
     *
     * @throws ConnectionException if closing connection fails
     */
    @Override
    public void disconnect() throws ConnectionException {
        try {
            if(mongoClient != null){
                mongoClient.close();
            }
        }
        catch (Exception e) {
            throw new ConnectionException("Error closing MongoDB connection", e);
        }


        // old version
       /* try {

            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            throw new ConnectionException("Error closing database connection", e);
        }*/
    }


    /**
     * Finds books whose title contains the specified string (case-insensitive).
     *
     * @param title the title or part of it to search for
     * @return a list of books matching the title
     * @throws SelectException if a database error occurs
     */
    @Override
    public List<Book> findBooksByTitle(String title) throws SelectException {
        List<Book> result = new ArrayList<>();
        books.clear();
        MongoCollection<Document> collection = db.getCollection("book");
        FindIterable find = collection.find(regex("title",title,"i"));
       /* SQLrun(find);*/

        for (MongoCursor<Document> cursor = find.iterator(); cursor.hasNext();) {
            Document doc = cursor.next();
            Book b = new Book(
                    doc.getString("_id"),
                    doc.getString("title"),
                    doc.getDate("published")
            );
            List<Document> authorsDocs = doc.getList("authors", Document.class);
            List<Authors> authors = new ArrayList<>();

            if (authorsDocs != null) {
                for (Document authorDoc : authorsDocs) {
                    // Map each subdocument to your Authors class
                   b.addAuthor(new Authors(0,  authorDoc.getString("name"),authorDoc.getDate("birthDate")));

                }
            }

            books.add(b);
        }

        // old version
/*        String sql = "SELECT TITLE, ISBN, PUBLISHED FROM T_BOOK WHERE TITLE LIKE ? ";
        try {
            SQLrun(sql,"%"+title+"%");
            title = title.trim().toLowerCase();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(title)) {
                    result.add(book);
                }
            }
        }
        catch (SQLException e) {
            throw new SelectException("Could not search for books by title", e);
        }*/



        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title)) {
                result.add(book);
            }
        }
        System.out.println(books);
        return result;
    }

    /**
     * Finds books by exact ISBN match.
     *
     * @param isbn the ISBN to search for
     * @return a list containing the book with the given ISBN, or empty if none
     * @throws SelectException if a database error occurs
     */
    @Override
    public List<Book> findBooksByIsbn(String isbn) throws SelectException {
        List<Book> result = new ArrayList<>();
        /*String sql = "SELECT TITLE, ISBN, PUBLISHED FROM T_BOOK WHERE ISBN = ?";
        try {
            *//*SQLrun(sql,isbn);*//*
            isbn = isbn.trim().toLowerCase();
            for (Book book : books) {
                if (book.getIsbn().toLowerCase().equals(isbn)) { // exact match
                    result.add(book);
                }
            }
        }
        catch (SQLException e) {
            throw new SelectException("Could not search for books by title", e);
        }*/
        return result;
    }

    /**
     * Finds books written by the given author (partial match allowed).
     *
     * @param author_name the author name or part of it
     * @return a list of books written by the author
     * @throws SelectException if a database error occurs
     */
    @Override
    public List<Book> findBooksByAuthor(String author_name) throws SelectException {
        List<Book> result = new ArrayList<>();
     /*   String sql = " SELECT b.TITLE, b.ISBN, b.PUBLISHED \n" +
                "    FROM T_BOOK AS b \n" +
                "    JOIN T_BOOK_AUTHOR AS a ON a.ISBN = b.ISBN \n" +
                "    WHERE a.AUTHOR LIKE ?;";
        try{
       *//*     SQLrun(sql,"%"+author_name+"%");*//*

            author_name = author_name.trim().toLowerCase();
            for (Book book : books) {
                for (Authors auth : book.getAuthors()) {
                    if (auth.getAuthorName().toLowerCase().contains(author_name)) {
                        result.add(book);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new SelectException("Could not search for books by title", e);
        }*/
        return result;
    }



    /**
     * Finds books that have a review with the specified grade.
     *
     * @param grade the grade to search for (e.g., "AA", "BB")
     * @return a list of books with at least one review of the given grade
     * @throws SelectException if a database error occurs
     */
    @Override
    public List<Book> findBooksByGrade(String grade) throws SelectException {
        List<Book> result = new ArrayList<>();
       /* String sql = "SELECT b.TITLE, b.ISBN,b.PUBLISHED\n" +
                "FROM T_BOOK b\n" +
                "JOIN T_REVIEW r ON r.ISBN = b.ISBN\n" +
                "WHERE r.GRADE = ?;";
        try {
        *//*    SQLrun(sql,grade);*//*
            for (Book book : books) {
                for (Review review : book.getReviews()){
                    if (review.getGrade() == Grade.valueOf(grade.toUpperCase())) {
                        result.add(book);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new SelectException("Could not search for books by title", e);
        }*/
        return result;
    }


    /**
     * Finds books by genre (partial match allowed).
     *
     * @param genre the genre to search for
     * @return a list of books matching the genre
     * @throws SelectException if a database error occurs
     */
    @Override
    public List<Book> findBooksByGenre(String genre) throws SelectException {
        List<Book> result = new ArrayList<>();
      /*  books.clear();
        String sql ="SELECT b.TITLE, b.ISBN, b.PUBLISHED\n" +
                "FROM T_BOOK AS b\n" +
                "JOIN T_BOOK_GENRE AS g ON g.ISBN = b.ISBN\n"+
                "WHERE g.GENRE LIKE ?;";
        try {
         *//*   SQLrun(sql,"%"+genre+"%");*//*
            genre = genre.trim().toLowerCase();
            for (Book book : books) {
                for (String s : book.getGenres()) {
                    if (s.toLowerCase().contains(genre)) {
                        result.add(book);
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new SelectException("Could not search for books by title", e);
        }*/
        return result;
    }



    private void SQLrun(FindIterable find) throws SelectException{
        Book b = null;

        for (MongoCursor<Document> cursor = find.iterator(); cursor.hasNext();) {
            Document doc = cursor.next();
                b = new Book(
                    doc.getString("ISBN"),
                    doc.getString("TITLE"),
                    doc.getDate("PUBLISHED")
            );
            List<Authors> authors = (List<Authors>) doc.get("authors");
            for (Authors author : authors){
                b.addAuthor(author);
            }
            System.out.println(b);
            books.add(b);



            /*System.out.println(doc.get(”shoesize"));*/
        }

    }


/*
    private void SQLrun(String sql,String Target) throws SelectException,SQLException{
       *//* PreparedStatement st = null;
        ResultSet rs = null;
        books.clear();

        try{
            st = conn.prepareStatement(sql);
            st.setString(1, Target);
            rs = st.executeQuery();
            while (rs.next()) {
                Book b = new Book(
                        rs.getString("ISBN"),
                        rs.getString("TITLE"),
                        rs.getDate("PUBLISHED")
                );
                books.add(b);
            }
            for (Book book : books) {
                loadBookDetails(book);
            }
        }
        catch (SQLException e) {
            throw new SelectException("Could not search for book", e);
        }
        finally {
                if(st!=null)st.close();
                if(rs!=null)rs.close();
        }*//*
    }*/
    private void loadBookDetails(Book book){
        loadAuthors(book);
        loadGenres(book);
        loadReview(book);
    }
    private void loadAuthors(Book book){
       /* String sql = "SELECT AUTHOR, birthDate, AUTHORID FROM T_BOOK_AUTHOR WHERE ISBN = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(sql);
            st.setString(1, book.getIsbn());
            rs = st.executeQuery();
            while (rs.next()) {
                book.addAuthor(
                        new Authors(
                                rs.getInt("AUTHORID"),
                                rs.getString("AUTHOR"),
                                rs.getDate("birthDate")
                        )
                );
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(st!=null){st.close();}
            if(rs!=null){rs.close();}
        }*/

    }
    private void loadGenres(Book book){
      /*  String sql = "SELECT GENRE FROM T_BOOK_GENRE WHERE ISBN = ?";
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(sql);
            st.setString(1, book.getIsbn());
            rs = st.executeQuery();
            while (rs.next()) {
                book.addGenre(rs.getString("GENRE"));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(st!=null){st.close();}
            if(rs!=null){rs.close();}
        }*/
    }
    private void loadReview(Book book){
/*
        String sql;
        PreparedStatement st = null;
        ResultSet rs = null;
        if (user == null) {
            sql = "SELECT GRADE FROM T_REVIEW WHERE ISBN = ?";
        } else {
            sql = "SELECT Grade, SUMMARY, REVIEWDATE FROM T_REVIEW WHERE ISBN = ? AND SSN = ?";
        }
        try{
            st = conn.prepareStatement(sql);
            st.setString(1, book.getIsbn());
            if (user != null) st.setString(2, user.getSSN());
             rs = st.executeQuery();
            while (rs.next()) {
                Grade grade = Grade.valueOf(rs.getString("Grade"));
                String summary = (user != null) ? rs.getString("SUMMARY") : null;
                Date date = (user != null) ? rs.getDate("REVIEWDATE") : null;
                book.addReviews(new Review(grade, summary, date));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(st!=null){st.close();}
            if(rs!=null){rs.close();}
        }*/
    }

    /**
     * Checks if a user exists in the database with the given username and password.
     *
     * @param User     the username of the user
     * @param password the password of the user
     * @return a User object if login succeeds, otherwise null
     */
    @Override
    public User CheckUser(String User,String password){
        User result = null;
      /*  User result = null;
        System.out.println(password);
        String sql = String.format("SELECT FULL_NAME,PASSKODE,SSN\n" +
                "FROM t_customer\n" +
                "WHERE FULL_NAME = '%s'\n" +
                "  AND PASSKODE = '%s';\n",User,password);
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()){
                result = new User(rs.getString(3),rs.getString(2),rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(result);

        user = result;
        return result;*/
        return result;
    };

    /**
     * Retrieves a list of all distinct authors in the database.
     *
     * @return a list of Authors objects
     */
    @Override
    public List<Authors> bringAuthors(){
        List<Authors> result = new ArrayList<>();
       /* String sql = "SELECT DISTINCT AUTHOR FROM T_BOOK_AUTHOR;";

        try {
            Statement st1 = conn.createStatement();
            ResultSet rsAuthors = st1.executeQuery(sql);
            while (rsAuthors.next()) {
                String Author = rsAuthors.getString(1);
                result.add(new Authors(Author));
            }
        } catch (SQLException e) {
        }*/
        return result;
    }


    /**
     * Inserts a new book into the database, including its authors and genres.
     * Uses batch inserts for authors and genres.
     *
     * @param book the book to insert
     * @throws InsertException if there is a database error or transaction fails
     */
    @Override
    public Book InsertBook(Book book) throws InsertException {
      /*  String sqlBook = "INSERT INTO T_BOOK (ISBN, Title, Published) VALUES (?,?,?)";
        String sqlGenre = "INSERT INTO T_BOOK_GENRE (ISBN, Genre) VALUES (?, ?)";
        String sqlAuthor = "INSERT INTO T_BOOK_AUTHOR (ISBN, Author) VALUES (?, ?)";
        try {
            conn.setAutoCommit(false);
            try (
                    PreparedStatement psBook = conn.prepareStatement(sqlBook);
                    PreparedStatement psAuthor = conn.prepareStatement(sqlAuthor);
                    PreparedStatement psGenre = conn.prepareStatement(sqlGenre)
            ) {

                psBook.setString(1, book.getIsbn());
                psBook.setString(2, book.getTitle());
                psBook.setDate(3, new java.sql.Date(book.getPublished().getTime()));
                psBook.executeUpdate();

                for (Authors author : book.getAuthors()) {
                    psAuthor.setString(1, book.getIsbn());
                    psAuthor.setString(2, author.getAuthorName());
                    psAuthor.addBatch();
                }
                psAuthor.executeBatch();

                for (String genre : book.getGenres()) {
                    psGenre.setString(1, book.getIsbn());
                    psGenre.setString(2, genre);
                    psGenre.addBatch();
                }
                psGenre.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new InsertException("Error in transaction!", e);
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new InsertException("Error inserting book! Try again.", e);
        }*/
        return new Book(book.getIsbn(),book.getTitle(),book.getPublished());
    }



    /**
     * Deletes a book (and its authors/genres) from the database.
     *
     * @param isbn the ISBN of the book to delete
     * @return the deleted Book object, or null if the book did not exist
     */
    @Override
    public Book DeleteBook(String isbn) {
        Book book = null;
      /*  String sql = String.format(
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
        }*/
        return book;
    }




    /**
     * Updates a book's title, author, or genre based on the choice mode.
     *
     * @param choiceValue the choice of what to update
     * @param newValue    the new value to set
     * @param oldValue    the old value (needed for author or genre updates)
     * @return the updated Book object
     * @throws SelectException if a database error occurs
     */
    @Override
    public Book UppdateBook(UpdateChoice choiceValue, String newValue,String oldValue) throws SelectException {

       /* String isbn = choiceValue.getIsbn();
        String sql = null;
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
            return null;
        }*/
        /* return  this.findBooksByIsbn(isbn).getFirst();*/
        return  null;
    }

    /**
     * Inserts a review for a book by the currently logged-in user.
     *
     * @param review the review to insert
     * @param isbn   the ISBN of the book
     * @throws InsertException if the insert fails
     */
    @Override
    public Review insertReview(Review review, String isbn) throws InsertException {
       /* String sql = "INSERT INTO T_REVIEW (SSN, ISBN, GRADE, REVIEWDATE, SUMMARY) VALUES (?, ?, ?, ?, ?)";
        System.out.println(user.getSSN()+"  "+isbn+"  "+review.getGrade().toString()+"  "+review.getDate()+"  "+review.getSummary());
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getSSN());
            ps.setString(2, isbn);
            ps.setString(3,  review.getGrade().toString());
            ps.setDate(4, review.getDate()); // java.sql.Date
            ps.setString(5, review.getSummary());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new InsertException("Insert failed: " + e.getMessage());
        }*/
        return new Review(review.getGrade(),review.getSummary(),review.getDate());
    }};






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

