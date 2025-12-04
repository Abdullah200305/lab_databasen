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
    private ArrayList<Authors> authors = new ArrayList<>(); /// by Chefen
    private ArrayList<String>genre = new ArrayList<>();
    private final int beyteg;

    public Book(int bookId, String isbn, String title, Date published,int beyteg,ArrayList<String> genre) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.published = published;
        this.beyteg = beyteg;
        this.genre = genre;
    }



    

    public Book(String isbn, String title, Date published) {
        this(-1, isbn, title, published,-1,null);
    }
    public Book(String isbn, String title, Date published,int beyteg,ArrayList<String> genre) {
        this(-1, isbn, title, published,beyteg,genre);
    }

    /// abody
    public int getBeyteg(){ return beyteg;}
    public ArrayList<String> getGenre(){
        ArrayList <String> temp  = genre;
        return temp;
    }
    public int getBookId() {
        return bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Date getPublished() {
        return published;
    }

    public String getStoryLine() {
        return storyLine;
    }
    /// by Chefen
    public void addAuthor(Authors author){
        authors.add(author);
    }

    public ArrayList<Authors> getAuthors() {
        ArrayList <Authors> temp  = authors;
        return temp;}

    @Override
    public String toString() {
        return title + ", " + isbn + ", " + published.toString()+getAuthors();
    }
}
