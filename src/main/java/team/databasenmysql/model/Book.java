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
    private ArrayList<String>genres = new ArrayList<>();

    private final Grade grade;

    public Book(int bookId, String isbn, String title, Date published,Grade grade,ArrayList<String> genres) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.published = published;
        this.grade = grade;
        this.genres = genres;
    }



    

    public Book(String isbn, String title, Date published) {
        this(-1, isbn, title, published,null,null);
    }



    public Book(String isbn, String title, Date published, Grade grade,ArrayList<String> genres) {
        this(-1, isbn, title, published,grade,genres);
    }

    /// abody
    public Grade getGrade(){ return grade;}




    public ArrayList<String> getGenres(){
        ArrayList <String> temp  = genres;
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
        return "Book{" +
                "authors='" + getAuthors() + '\'' +
                ", bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", published=" + published +
                ", storyLine='" + storyLine + '\''+
                ", genres=" + genres +
                ", grade=" + grade +
                '}';
    }
}
