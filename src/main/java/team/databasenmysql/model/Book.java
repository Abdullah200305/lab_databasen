package team.databasenmysql.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a book.
 *
 * @author anderslm@kth.se
 */
public class Book {

  /*  private final int bookId;*/
    private final String isbn; // should check format
    private final String title;
    private final Date published;
    private final String storyLine = "";

    private ArrayList<Authors> authors = new ArrayList<>(); /// by Chefen
    private ArrayList<String>genres = new ArrayList<>();

    public Book(String isbn, String title, Date published) {
        this.isbn = isbn;
        this.title = title;
        this.published = published;
    }

    public String getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;
    }
    /// by Chefen
    public Date getPublished() {
        return published;
    }

    public String getStoryLine() {
        return storyLine;
    }

    public void addGenre(String genre){
        genres.add(genre);
    }
    public ArrayList<String> getGenres(){
        ArrayList <String> temp  = genres;
        return temp;
    }
    public ArrayList<Authors> getAuthors() {
        ArrayList <Authors> temp  = authors;
        return temp;
    }
    public void addAuthor(Authors author){
        authors.add(author);
    }
    @Override
    public String toString() {
        return title + ", " + isbn + ", Author:" + getAuthors() + "," + published.toString()+", "+ getGenres();
    }
}
