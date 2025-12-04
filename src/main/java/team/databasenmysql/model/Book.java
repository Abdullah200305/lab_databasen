package team.databasenmysql.model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se
 */
public class Book {

    private final int bookId;
    private final String isbn; // should check format
    private final String title;
    private final Date published;
    private final String storyLine = "";
    private final Authors author;
    private ArrayList<Authors> authors = new ArrayList<>(); /// by Chefen
    private ArrayList<String>genres = new ArrayList<>();
    private final String genre;
    private final Grade grade;

    public Book(int bookId, String isbn, String title, Authors author, Date published,Grade grade,String genre) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.published = published;
        this.grade = grade;
        this.genre = genre;
    }



    

    public Book(String isbn, String title, Date published) {
        this(-1, isbn, title, null, published,null,null);
    }
    public Book(String isbn, String title,Authors author, Date published, Grade grade,String genre) {
        this(-1, isbn, title, author, published,grade,genre);
    }

    /// abody

    public int getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }
    /// by Chefen
    public void addAuthor(Authors author){
        authors.add(author);
    }

    public ArrayList<Authors> getAuthors() {
        ArrayList <Authors> temp  = authors;
        return temp;
    }

    public Authors getAuthor() {
        return author;
    }

    public Date getPublished() {
        return published;
    }

    public String getStoryLine() {
        return storyLine;
    }


    public Grade getGrade(){ return grade;}


    public String getGenre() {
        return genre;
    }

    public void addGenre(String genre){
        genres.add(genre);
    }
    public ArrayList<String> getGenres(){
        ArrayList <String> temp  = genres;
        return temp;
    }

    @Override
    public String toString() {
        return title + ", " + isbn + ", Author:" + getAuthors() + "," + published.toString()+", Grade: " + grade + "," + getGenres();
    }
}
